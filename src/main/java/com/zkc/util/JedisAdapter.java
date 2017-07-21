package com.zkc.util;

import redis.clients.jedis.Jedis;

/**
 * Created by zkc on 17/7/21.
 */
public class JedisAdapter {
    public static void print(int index, Object obj){
        System.out.println(String.format("%d, %s", index, obj.toString()));
    }
    public static void main(String[] argv){
        Jedis jedis = new Jedis("redis://localhost:6379/9");
        //delete all in database
        jedis.flushDB();

        jedis.set("hello", "world");
        print(1,jedis.get("hello"));

    }
}
