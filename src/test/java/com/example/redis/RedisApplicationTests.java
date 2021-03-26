package com.example.redis;

import com.example.redis.demo.LockService;
import com.example.redis.demo.SearchService;
import com.example.redis.util.LockUtil;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
@Slf4j
class RedisApplicationTests {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private SearchService searchService;

    @Resource
    private LockService lockService;



    @Test
    void contextLoads() {
    }

    /**
     *
     */
    @Test
    void test(){
        redisTemplate.opsForValue().set("name", "Kiki");
        log.info(Objects.requireNonNull(redisTemplate.opsForValue().get("name")).toString());
    }

    /**
     * 排序功能/历史功能的使用 将信息加到一个zset表中,然后直接取zset表中的任意一个即可
     */
    @Test
    void addItems(){
        for (int i = 0; i < 1000; i++) {
            searchService.addItems("weibo", "mi");
        }
        for (int i = 0; i < 300; i++) {
            searchService.addItems("weibo", "shan");
        }
        for (int i = 0; i < 70; i++) {
            searchService.addItems("weibo", "kai");
        }
        for (int i = 0; i < 100; i++) {
            searchService.addItems("weibo", "du");
        }
        for (int i = 0; i < 120; i++) {
            searchService.addItems("weibo", "Li");
        }
        System.out.println("searchService.hotSearch() = " + searchService.hotSearch());
    }

    /**
     *
     */
    @Test
    public void test3(){
        lockService.test();
        try {
            while (LockUtil.isLocked("ID")) {
                TimeUnit.SECONDS.sleep(1);
            }
        } catch (InterruptedException e) {
            log.error("等待锁时间出现异常", e);
        }
        LockUtil.lock("ID", 100);
        try{
            //开始对ID对应的数据库进行对应的操作
            log.info("处理业务");
        }catch (Exception e){
            //异常处理
        }finally {
            LockUtil.unlock("ID");
        }

    }




}
