package com.aliware.tianchi.smooth;

public class SmoothServer {
    private String ip;

    private int weight;

    private int curWeight;

    public SmoothServer(String ip, int weight, int curWeight) {
        this.ip = ip;
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

    public SmoothServer(int weight, int curWeight) {
        this.weight = weight;
        this.curWeight = curWeight;
    }
}
