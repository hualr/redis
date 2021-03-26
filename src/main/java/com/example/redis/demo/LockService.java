package com.example.redis.demo;

import com.example.redis.util.LockUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Function: 分布式锁的使用<br>
 * Creating Time: 2021/3/17 <br>
 * Version: 1.0.0
 *
 * @author 宗旗
 */
@Service
@Slf4j
public class LockService {
    static final String KEY = "LOCK_KEY";

    public void test(){
        //加锁
        LockUtil.lock(KEY);
        try {
            //在这里就能保证线程安全,而且是分布式架构下的线程安全
            log.info(" 处理业务。。。");
        } catch (Exception e) {
            //异常处理
        }finally{
            //释放锁
            LockUtil.unlock(KEY);
        }
    }

}
