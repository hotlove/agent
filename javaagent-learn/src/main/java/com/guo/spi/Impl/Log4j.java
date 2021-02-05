package com.guo.spi.Impl;

import com.guo.spi.Log;

/**
 * @Date: 2021/2/4 16:38
 * @Author 郭乐建
 * @Since JDK 1.8
 * @Description:
 */
public class Log4j implements Log {
    @Override
    public void log(String info) {
        System.out.println("log4j:"+info);
    }
}
