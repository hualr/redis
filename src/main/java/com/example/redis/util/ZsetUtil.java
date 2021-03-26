package com.example.redis.util;

import java.util.HashSet;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

/**
 * Function: zset工具类<br>
 * Creating Time: 2021/3/17 <br>
 * Version: 1.0.0
 *
 * @author 宗旗
 */

@Component
public class ZsetUtil {
    private static ZsetUtil zsetUtil;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static void setUtil(ZsetUtil newzSetUtil){
        zsetUtil=newzSetUtil;
    }

    /**
     * 获取一个名为arrSet数组中,key的索引
     *
     * @param arrSet
     * @param key
     * @return
     */
    public static Long rank(String arrSet, Object key) {
        ZSetOperations<String, Object> zSet = zsetUtil.redisTemplate.opsForZSet();
        return zSet.rank(arrSet, key);
    }

    /**
     * 有序集合添加
     *
     * @param arrSet
     * @param key
     * @param score
     */
    public static void add(String arrSet, Object key, double score) {
        ZSetOperations<String, Object> zSet = zsetUtil.redisTemplate.opsForZSet();
        zSet.add(arrSet, key, score);
    }

    /**
     * 获得key数组里面key2元素的排序值
     *
     * @param arrSet
     * @param key
     * @return
     */
    public static double score(String arrSet, Object key) {
        ZSetOperations<String, Object> zSet = zsetUtil.redisTemplate.opsForZSet();
        return zSet.score(arrSet, key);
    }

    /**
     * 从高到低的排序集中获取从头(start)到尾(end)内的元素。
     *
     * @param arrSet
     * @param start
     * @param end
     * @return
     */
    public static Set<Object> reverseRange(String arrSet, long start, long end) {
        ZSetOperations<String, Object> zset = zsetUtil.redisTemplate.opsForZSet();
        return zset.reverseRange(arrSet, start, end);
    }

    @PostConstruct
    void initialize() {
        setUtil(this);
    }

    /**
     * 根据分数保留指定个数，其余的元素删除
     *
     * @param key
     * @param number
     * @return
     */
    public Boolean remove(String key, Integer number) {
        ZSetOperations<String, Object> zSet = zsetUtil.redisTemplate.opsForZSet();

        long size = zSet.size(key) == null ? 0 : zSet.size(key);
        if (size > number) {
            //获取变量指定区间的元素
            Set<ZSetOperations.TypedTuple<Object>> typedTuples = zSet.rangeWithScores(key, 0, (size - 1) - number);
            Set set = new HashSet();
            for (ZSetOperations.TypedTuple<Object> o : typedTuples) {
                set.add(o.getValue());
            }
            for (Object o : set) {
                Long aLong = zSet.remove(key, o);
                if (aLong == null) {
                    return false;
                }
            }
            return true;
        } else {
            return true;
        }
    }
}
