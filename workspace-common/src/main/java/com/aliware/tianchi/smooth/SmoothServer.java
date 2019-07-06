package com.aliware.tianchi.smooth;

public class SmoothServer {
    private String ip;

    private double weight;

    private double curWeight;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public double getCurWeight() {
        return curWeight;
    }

    public void setCurWeight(double curWeight) {
        this.curWeight = curWeight;
    }

    public SmoothServer(double weight, double curWeight) {
        this.weight = weight;
        this.curWeight = curWeight;
    }

    public SmoothServer(String ip, double weight, double curWeight) {
        this.ip = ip;
        this.weight = weight;
        this.curWeight = curWeight;
    }
}
