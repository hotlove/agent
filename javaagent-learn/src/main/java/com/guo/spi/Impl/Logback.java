package com.guo.spi.Impl;

import com.guo.spi.Log;

/**
 * @Date: 2021/2/4 16:37
 * @Author 郭乐建
 * @Since JDK 1.8
 * @Description:
 */
public class Logback implements Log {
    @Override
    public void log(String info) {
        System.out.println("logback:"+info);
    }
}
