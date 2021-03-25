package com.guo.agent;

import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import net.bytebuddy.implementation.bind.annotation.This;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;

public class TimerInterceptor {

    static ThreadLocal<Trace> traceLocal = new ThreadLocal<>();

    static Map<String, Trace> tracePointer = new ConcurrentHashMap<>();

    @RuntimeType
    public static Object intercept(@Origin Method method, @SuperCall Callable<?> callable) throws Exception {
        long start = System.currentTimeMillis();
        try {
            return callable.call();
        } finally {
            System.out.println(method.getName() +":"+(System.currentTimeMillis() - start)+"ms");
        }
    }
//    @RuntimeType
//    public static Object intercept(@This Object obj, @Origin Method method, @SuperCall Callable<?> callable) throws Exception {
//        Object result = null;
//        long threadId = Thread.currentThread().getId();
//        String key = null;
//        System.out.println("=============>method:"+method.getName());
//
//        Trace firstTrace = traceLocal.get();
//        if (firstTrace == null) {
//            Trace trace = newTrace(method.getName(), "0", null,true);
//            key = trace.getTraceId();
//            tracePointer.put(trace.getTraceId(), trace);
//            traceLocal.set(trace);
//        } else {
//            Trace rootTrace = traceLocal.get();
//            Trace currentPointerTrace = tracePointer.get(rootTrace.getTraceId());
//            Trace trace = newTrace(method.getName(), currentPointerTrace.getSpanId(), rootTrace.getTraceId(),false);
//            tracePointer.put(rootTrace.getTraceId(), trace);
//
//            currentPointerTrace.next = trace;
//            trace.prev = currentPointerTrace;
//
//            key = trace.getTraceId();
//        }
//
//        try {
//            result = callable.call();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            Trace trace = tracePointer.get(key);
//            if (trace.isFirst()) {
//                trace.setEndTime(System.currentTimeMillis());
//                printTrace(trace);
//
//                trace.next = null;
//                tracePointer.put(trace.getTraceId(), trace);
//            } else {
//                trace.setEndTime(System.currentTimeMillis());
//                tracePointer.put(trace.getTraceId(), trace.prev);
//            }
//        }
//        return result;
//    }

    private static void printTrace(Trace trace) {
        while (trace.next != null) {
            System.out.println("=====>"+trace.toString());
            trace = trace.next;
        }
        if (trace.next == null) {
            System.out.println("=====>"+trace.toString());
        }
    }

    private static Trace newTrace(String methodName, String parentId, String traceId, Boolean isFirst) {
        long threadId = Thread.currentThread().getId();
        String spanId = UUID.randomUUID().toString();
        Trace t = new Trace();
        t.setFirst(isFirst);
        t.setThreadId(threadId+"");
        t.setTraceId(traceId == null ? threadId+"_"+spanId : traceId);
        t.setParentId(parentId);
        t.setSpanId(spanId);
        t.setMethodName(methodName);
        t.setStartTime(System.currentTimeMillis());

        return t;
    }
}
