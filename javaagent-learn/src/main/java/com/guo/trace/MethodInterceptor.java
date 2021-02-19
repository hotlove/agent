package com.guo.trace;

import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date: 2021/2/8 11:51
 * @Author 郭乐建
 * @Since JDK 1.8
 * @Description:
 */
public class MethodInterceptor {

    static ThreadLocal<List<Map<String, Long>>> methodStack = new ThreadLocal<>();

    static ThreadLocal<Integer> countPointer = new ThreadLocal<>();

    @RuntimeType
    public static Object intercept(@This Object obj, @Origin Method method, @SuperCall Callable<?> callable) throws Exception {
        if (method.getName().equals("toString") || method.getName().equals("hashCode")) {
            return callable.call();
        }
        Object result = null;
        long startTime = System.currentTimeMillis();
        try {
            List<Map<String, Long>> stack = methodStack.get();
            if (stack != null && stack.size() > 0) {

                Map<String, Long> methodObj = new HashMap<>();
                methodObj.put(method.getName(), System.currentTimeMillis());
                stack.add(methodObj);

                methodStack.set(stack);

                int counter = countPointer.get();
                Integer i = counter + 1;
                countPointer.set(i);
            } else {
                Map<String, Long> methodObj = new HashMap<>();
                methodObj.put(method.getName(), System.currentTimeMillis());
                stack = new ArrayList();
                stack.add(methodObj);
                methodStack.set(stack);
                countPointer.set(0);
            }
            result = callable.call(); // 执行原函数
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            List<Map<String, Long>> stack = methodStack.get();
            if (stack != null && stack.size() > 0) {
                int index = countPointer.get();
                if (index == 0) {
                    Map<String, Long> methodObj = (Map<String, Long>) stack.get(index);
                    Long startt = methodObj.get(method.getName());
                    methodObj.put(method.getName(), System.currentTimeMillis() - startt);

                    for (Map<String, Long> map : stack) {
                        map.forEach((k, v) -> {
                            System.out.print("==> 【method:"+k+"耗时:"+v+"ms】 ");
                        });
                    }
                } else {
                    Map<String, Long> methodObj = (Map<String, Long>) stack.get(index);
                    Long startt = methodObj.get(method.getName());
                    if (startt != null) {
                        methodObj.put(method.getName(), System.currentTimeMillis() - startt);

                        methodStack.set(stack);

                        Integer i = index - 1;
                        countPointer.set(i);
                    }

                }
            }
            System.out.println("=============>"+method.getName()+" time:"+(System.currentTimeMillis() - startTime)+"ms");
        }
        return result;
    }

//    @RuntimeType
//    public Object intercept(@This Object obj, @Origin Method method, @SuperCall Callable<?> callable) throws Exception {
//        Object result = null;
//        long startTime = System.currentTimeMillis();
//        try {
//            result = callable.call(); // 执行原函数
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            System.out.println("=============>"+method.getName()+" time:"+(System.currentTimeMillis() - startTime)+"ms");
//        }
//        return result;
//    }
}
