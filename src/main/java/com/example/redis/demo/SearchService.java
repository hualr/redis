package com.example.redis.demo;

import com.example.redis.util.ZsetUtil;
import java.util.Set;
import javax.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Function: 热搜服务的实现<br>
 * Creating Time: 2021/3/17 <br>
 * Version: 1.0.0
 *
 * @author 宗旗
 */
@Service
public class SearchService {
    private static final String ZSET="weibo";

    @Resource
    private RedisTemplate<String, Object> redisTemplate;


    public void addItems(String arrSet,String key){
        final Long rank = ZsetUtil.rank(arrSet, key);
        if (rank == null){
            ZsetUtil.add(arrSet, key, 1.0);
        }else{
            final double score = ZsetUtil.score(arrSet, key);
            ZsetUtil.add(arrSet, key,score+1);
        }
    }

    public Set<Object> hotSearch(){
        return ZsetUtil.reverseRange(ZSET,0,3);
    }
}
