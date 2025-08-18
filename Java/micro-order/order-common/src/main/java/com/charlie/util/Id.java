package com.charlie.util;


import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @Classname IdUtil
 * @Description id生成工具类
 * @Date 2021/3/29 11:50
 * @Created xxx
 */
@Slf4j
public class Id {

    private volatile static Snowflake snowflake;

    public static Snowflake getSingleton() {
        if(snowflake == null){
            synchronized (Id.class){
                if(snowflake == null){
                    snowflake = new Snowflake();
                }
            }
        }
        return snowflake;
    }

    public static Long getId(){
        Snowflake snowflake = getSingleton();
        return snowflake.nextId();
    }

    public static String getIdStr(){
        return Long.toString(getId());
    }

    public static String getUUID(){
        return IdUtil.simpleUUID();
    }

    public static void main(String[] args) {
        System.out.println("getUUID() = " + getUUID());
    }
}