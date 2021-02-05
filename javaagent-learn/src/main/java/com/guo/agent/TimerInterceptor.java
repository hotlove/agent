package com.guo.agent;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * @Date: 2021/2/4 14:08
 * @Author 郭乐建
 * @Since JDK 1.8
 * @Description:
 */
public class TimerInterceptor {

    static List<Trace> traceList = new CopyOnWriteArrayList<>();

    static Map<String, Trace> tracePointer = new ConcurrentHashMap<>();

    @RuntimeType
    public static Object intercept(@Origin Method method, @SuperCall Callable<?> callable) throws Exception {
        Object result = null;
        long threadId = Thread.currentThread().getId();
        String key = threadId+"_key";

        Trace firstTrace = tracePointer.get(key);
        String spanId = null;
        if (firstTrace == null) {
            // 说明是第一个节点
            Trace trace = newTrace(method.getName(), "0", true);
            spanId = trace.getSpanId();
            tracePointer.put(key, trace);
            traceList.add(trace);
        } else {
            Trace currentPointerTrace = tracePointer.get(key);
            Trace trace = newTrace(method.getName(), currentPointerTrace.getSpanId(), false);
            tracePointer.put(key, trace);
            traceList.add(trace);
            spanId = trace.getSpanId();
        }

        try {
            result = callable.call(); // 执行原函数
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            Trace trace = tracePointer.get(key);
            trace.setEndTime(System.currentTimeMillis());
            if (!trace.isFirst()) {
                List<Object> collect = traceList.stream().filter(e -> e.getSpanId().equals(spanId)).collect(Collectors.toList());
//                tracePointer.put()
            }
        }
        return result;
    }

    private static synchronized void printTrace(Trace trace) {
        while (trace.next != null) {
            System.out.println("=====>"+trace.toString());
            trace = trace.next;
        }
        if (trace.next == null) {
            System.out.println("=====>"+trace.toString());
        }
    }

    private static Trace newTrace(String methodName, String parentId, Boolean isFirst) {
        long threadId = Thread.currentThread().getId();
        Trace t = new Trace();
        t.setFirst(isFirst);
        t.setThreadId(threadId+"");
        t.setTraceId(threadId+"");
        t.setParentId(parentId);
        t.setSpanId(UUID.randomUUID().toString());
        t.setMethodName(methodName);
        t.setStartTime(System.currentTimeMillis());

        return t;
    }
}
