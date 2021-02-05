package com.guo.agent;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Date: 2021/2/4 14:08
 * @Author 郭乐建
 * @Since JDK 1.8
 * @Description:
 */
public class TimerInterceptor {

    static Map<String, Trace> traceMap = new ConcurrentHashMap<>();
    static Map<String, Trace> tracePointer = new ConcurrentHashMap<>();
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
    @RuntimeType
    public static Object intercept(@Origin Method method, @SuperCall Callable<?> callable) throws Exception {
        Object result = null;
        long threadId = Thread.currentThread().getId();
        String key = threadId+"_key";
        System.out.println("==========> thread_key:"+key+"------methodName:"+method.getName());
        try {
            Trace firstTrace = traceMap.get(key);
            if (firstTrace == null) {
                // 说明是第一个节点
                Trace trace = newTrace(method.getName(), "0", true);
                traceMap.put(key, trace);
                tracePointer.put(key, trace);
            } else {
                Trace currentPointerTrace = tracePointer.get(key);
                Trace trace = newTrace(method.getName(), currentPointerTrace.getSpanId(), false);
                currentPointerTrace.next = trace;
                trace.prev = currentPointerTrace;
                tracePointer.put(key, trace);
            }

            result = callable.call(); // 执行原函数
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Trace trace = tracePointer.get(key);
            if (!trace.isFirst()) {
                trace.setEndTime(System.currentTimeMillis());
                tracePointer.put(key, trace.prev);
            } else {
                trace.setEndTime(System.currentTimeMillis());
                printTrace(trace);
            }
//            System.out.println("==================>methodName:"+method.getName()+"---time:"+(System.currentTimeMillis() - startTime)+"ms");
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
