package com.guo.trace;

import com.guo.agent.Trace;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.Method;
import java.util.concurrent.Callable;

/**
 * @Date: 2021/2/8 11:51
 * @Author 郭乐建
 * @Since JDK 1.8
 * @Description:
 */
public class MethodInterceptor {
    @RuntimeType
    public static Object intercept(@This Object obj, @Origin Method method, @SuperCall Callable<?> callable) throws Exception {
        Object result = null;
        long startTime = System.currentTimeMillis();
        try {
            result = callable.call(); // 执行原函数
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("=============>"+method.getName()+" time:"+(System.currentTimeMillis() - startTime)+"ms");
        }
        return result;
    }
}
