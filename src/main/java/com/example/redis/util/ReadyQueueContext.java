package com.example.redis.util;

import static com.example.redis.common.constant.RedisQueueKey.CONSUMER_TOPIC_LOCK;
import static com.example.redis.common.constant.RedisQueueKey.JOB_POOL_KEY;
import static com.example.redis.common.constant.RedisQueueKey.LOCK_RELEASE_TIME;
import static com.example.redis.common.constant.RedisQueueKey.LOCK_WAIT_TIME;
import static com.example.redis.common.constant.RedisQueueKey.RD_LIST_TOPIC_PRE;

import com.alibaba.fastjson.JSON;
import com.example.redis.common.constant.RedisQueueKey;
import com.example.redis.demo.queue.api.ConsumerService;
import com.example.redis.model.Job;
import com.example.redis.task.TaskManager;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RBlockingQueue;
import org.redisson.api.RLock;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ReadyQueueContext {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private ConsumerService consumerService;

    public static void main(String[] args) {
        Job job = new Job();
        job.setJobId(UUID.randomUUID().toString());
        job.setTopic("NOTIFY");
        long delay = System.currentTimeMillis() + 10000;
        DateUtil.long2Str(delay);
        job.setDelay(delay);
        job.setBody("{\"amount\":0.01}");
        job.setUrl("http://localhost:8080/testCallBack");
        System.out.println(JSON.toJSONString(job));
    }

    /**
     * TOPIC消费线程
     */
    @PostConstruct
    public void startTopicConsumer() {
        TaskManager.doTask(this::runTopicThreads, "开启TOPIC消费线程");
    }

    /**
     * 开启TOPIC消费线程
     * 将所有可能出现的异常全部catch住，确保While(true)能够不中断
     */
    @SuppressWarnings("InfiniteLoopStatement")
    private void runTopicThreads() {
        while (true) {
            RLock lock = null;
            try {
                lock = redissonClient.getLock(CONSUMER_TOPIC_LOCK);
            } catch (Exception e) {
                log.error("runTopicThreads getLock error", e);
            }
            try {
                if (lock == null) {
                    continue;
                }
                // 分布式锁时间比Blpop阻塞时间多1S，避免出现释放锁的时候，锁已经超时释放，unlock报错
                boolean lockFlag = lock.tryLock(LOCK_WAIT_TIME, LOCK_RELEASE_TIME, TimeUnit.SECONDS);
                if (!lockFlag) {
                    continue;
                }

                // 1. 获取ReadyQueue中待消费的数据
                RBlockingQueue<String> queue = redissonClient.getBlockingQueue(RD_LIST_TOPIC_PRE);
                String topicId = queue.poll(60, TimeUnit.SECONDS);
                if (StringUtils.isEmpty(topicId)) {
                    continue;
                }

                // 2. 获取job元信息内容
                RMap<String, Job> jobPoolMap = redissonClient.getMap(JOB_POOL_KEY);
                Job job = jobPoolMap.get(topicId);

                // 3. 消费
                FutureTask<Boolean> taskResult = TaskManager.doFutureTask(() -> consumerService.consumerMessage(job.getUrl(), job.getBody()), job.getTopic() + "-->消费JobId-->" + job.getJobId());
                if (Objects.equals(taskResult.get(), true)) {
                    // 3.1 消费成功，删除JobPool和DelayBucket的job信息
                    jobPoolMap.remove(topicId);
                } else {
                    int retrySum = job.getRetry() + 1;
                    // 3.2 消费失败，则根据策略重新加入Bucket
                    // 如果重试次数大于5，则将jobPool中的数据删除，持久化到DB
                    if (retrySum > RetryStrategyEnum.RETRY_FIVE.getRetry()) {
                        jobPoolMap.remove(topicId);
                        continue;
                    }
                    job.setRetry(retrySum);
                    long nextTime = job.getDelay() + RetryStrategyEnum.getDelayTime(job.getRetry()) * 1000L;
                    log.info("next retryTime is [{}]", DateUtil.long2Str(nextTime));
                    RScoredSortedSet<Object> delayBucket = redissonClient.getScoredSortedSet(RedisQueueKey.RD_ZSET_BUCKET_PRE);
                    delayBucket.add(nextTime, topicId);
                    // 3.3 更新元信息失败次数
                    jobPoolMap.put(topicId, job);
                }
            } catch (Exception e) {
                log.error("runTopicThreads error", e);
            } finally {
                if (lock != null) {
                    try {
                        lock.unlock();
                    } catch (Exception e) {
                        log.error("runTopicThreads unlock error", e);
                    }
                }
            }
        }
    }
}
