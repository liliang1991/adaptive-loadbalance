package com.aliware.tianchi.smooth;

public class SmoothServer {
    private String ip;

    private volatile int weight;

    private volatile int curWeight;
    private int activeCount;
    private int threadCount;
    public SmoothServer(String ip, int weight, int curWeight) {
        this.ip = ip;
        this.weight = weight;
        this.curWeight = curWeight;
    }

    public SmoothServer(int weight, int curWeight) {
        this.weight = weight;
        this.curWeight = curWeight;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getCurWeight() {
        return curWeight;
    }

    public void setCurWeight(int curWeight) {
        this.curWeight = curWeight;
    }

    @Override
    public String toString() {
        return  "{service: ip==}"+ip+", weight==="+weight+", curweight==="+curWeight;
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
}
