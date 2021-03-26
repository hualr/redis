package com.example.redis.config;

import com.example.redis.util.LockUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.SingleServerConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Function: redission配置<br>
 * Creating Time: 2021/3/18 <br>
 * Version: 1.0.0
 *
 * @author 宗旗
 */
@Configuration
/**
 * 运行流程
 * <li> 首先,configuration保证该类会被启动,其中的bean在spring中会自动生成</li>
 * <li> 生成两个Bean 第一个为redis实例,包含redis信息</li>
 * <li> 第二个主要组成工具类</li>
 * <li> 最后运行工具类即可</li>
 */
public class RedissonConfig {
    @Value("${spring.redis.database}")
    private int database;
    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private String port;
    @Value("${spring.redis.password}")
    private String password;
    @Value("${spring.redis.timeout}")
    private int timeout;

    /**
     * RedissonClient,单机模式
     *
     * @return
     * @throws IOException
     */
    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() {
        Config config = new Config();
        SingleServerConfig singleServerConfig = config.useSingleServer();
        singleServerConfig.setAddress("redis://" + host + ":" + port);
        singleServerConfig.setTimeout(timeout);
        singleServerConfig.setDatabase(database);
        //有密码
        if (password != null && !"".equals(password)) {
            singleServerConfig.setPassword(password);
        }
        return Redisson.create(config);
    }

    @Bean
    public RedissonLocker redissonLocker(RedissonClient redissonClient) {
        RedissonLocker locker = new RedissonLocker(redissonClient);
        //设置LockUtil的锁处理对象
        LockUtil.setLocker(locker);
        return locker;
    }
}
