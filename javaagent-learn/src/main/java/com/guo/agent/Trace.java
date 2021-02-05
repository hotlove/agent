package com.guo.agent;

/**
 * @Date: 2021/2/5 16:56
 * @Author 郭乐建
 * @Since JDK 1.8
 * @Description:
 */
public class Trace {

    private String traceId;

    private String spanId;

    private String threadId;

    private String parentId;

    public Trace next;

    public Trace prev;

    private long startTime;

    private long endTime;

    private boolean first = false;

    private String methodName;

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getSpanId() {
        return spanId;
    }

    public void setSpanId(String spanId) {
        this.spanId = spanId;
    }

    public String getThreadId() {
        return threadId;
    }

    public void setThreadId(String threadId) {
        this.threadId = threadId;
    }

    @Override
    public String toString() {
        return "threadId:"+threadId+"--traceId:"+traceId+"--parentId:"+parentId
                +"--spanId:"+spanId+"--methodName:"+methodName+"--time:"
                +(endTime - startTime) + "ms";
    }
}
