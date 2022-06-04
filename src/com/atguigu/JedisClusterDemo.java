package com.atguigu;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;

public class JedisClusterDemo {
    public static void main(String[] args) throws IOException {
        HostAndPort hostAndPort = new HostAndPort("192.168.163.131",6379);
        int connectionTimeout = 3000;
        int soTimeout = 3000;
        int maxAttempts = 6;
        String password = "yu149286";
        GenericObjectPoolConfig<Jedis> poolConfig = new GenericObjectPoolConfig<>();
        poolConfig.setMaxTotal(200);
        poolConfig.setMaxIdle(32);
        poolConfig.setMaxWaitMillis(100*1000);
        poolConfig.setBlockWhenExhausted(true);
        poolConfig.setTestOnBorrow(true);
        JedisCluster jedisCluster = new JedisCluster(hostAndPort,connectionTimeout,soTimeout,maxAttempts,password,poolConfig);
        jedisCluster.set("k1","v1");
        String result = jedisCluster.get("k1");
        System.out.println(result);
        jedisCluster.close();
    }
}
