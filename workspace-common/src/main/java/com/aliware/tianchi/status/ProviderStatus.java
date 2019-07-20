package com.aliware.tianchi.status;

public class ProviderStatus {
    private String host;
    private int activeCount;
    private int threadCount;
    private long startTime;
    //总次数
     private   long total;
    //总调用时长
    private long totalElapsed;
    private long elapsed;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getActiveCount() {
        return activeCount;
    }

    public void setActiveCount(int activeCount) {
        this.activeCount = activeCount;
    }

    public int getThreadCount() {
        return threadCount;
    }

    public void setThreadCount(int threadCount) {
        this.threadCount = threadCount;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTotalElapsed() {
        return totalElapsed;
    }

    public void setTotalElapsed(long totalElapsed) {
        this.totalElapsed = totalElapsed;
    }

    public long getElapsed() {
        return elapsed;
    }

    public void setElapsed(long elapsed) {
        this.elapsed = elapsed;
    }
}
