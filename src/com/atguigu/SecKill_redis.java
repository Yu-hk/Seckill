package com.atguigu;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.util.List;

/**
 * @author Lenovo
 */
public class SecKill_redis {

    public static void main(String[] args) {
        Jedis jedis =new Jedis("192.168.163.131",6379);
        jedis.auth("yu149286");
        System.out.println(jedis.ping());
        jedis.close();
    }

    //秒杀过程
    public static boolean doSecKill(String uid,String prodid) {
        //1 uid和prodid非空判断
        if(uid == null || prodid == null){
            return false;
        }

        //2 连接redis
        JedisPool jedisPool = JedisPoolUtil.getJedisPool();
        Jedis jedis = jedisPool.getResource();

        //3 拼接key
        // 3.1 库存key
        String kcKey = "sk:"+prodid+":qt";
        // 3.2 秒杀成功用户key
        String userKey = "sk:"+prodid+":user";

        jedis.watch(kcKey);

        //4 获取库存，如果库存null，秒杀还没有开始
        String kc = jedis.get(kcKey);
        if(kc == null){
            System.out.println("秒杀还没开始，请稍等");
            jedis.close();
            return false;
        }

        // 5 判断用户是否重复秒杀操作

        if(jedis.sismember(userKey, uid)){
            System.out.println("每个用户只能秒杀成功一次，请下次再来");
            jedis.close();
            return false;
        }

        //6 判断如果商品数量，库存数量小于1，秒杀结束
        if(Integer.parseInt(kc) < 1){
            System.out.println("秒杀结束，请下次参与");
            jedis.close();
            return false;
        }

        //7 秒杀过程
        Transaction multi = jedis.multi();
        multi.decr(kcKey);
        multi.sadd(userKey,uid);
        List<Object> result = multi.exec();
        if (result.size() == 0) {
            System.out.println("秒杀失败");
            jedis.close();
            return false;
        }
        System.out.println("用户" + uid + "秒杀成功");
        jedis.close();
        return true;
    }
}

