package com.guo.spi;

import java.util.Iterator;
import java.util.ServiceLoader;

/**
 * @Date: 2021/2/4 16:42
 * @Author 郭乐建
 * @Since JDK 1.8
 * @Description:
 */
public class Main {
    public static void main(String[] args) {
        ServiceLoader<Log> load = ServiceLoader.load(Log.class);
        Iterator<Log> iterator = load.iterator();
        while (iterator.hasNext()) {
            Log log = iterator.next();
            log.log("JDK SPI");
        }

    }
}
