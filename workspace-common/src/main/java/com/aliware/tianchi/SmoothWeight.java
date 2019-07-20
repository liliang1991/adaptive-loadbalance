package com.aliware.tianchi;

import com.aliware.tianchi.smooth.SmoothServer;

import java.util.*;

public class SmoothWeight {
    static  Map<String, SmoothServer> servers = null;

    static {
        servers = new LinkedHashMap<>();
        servers.put("provider-small", new SmoothServer(1, 0));
        servers.put("provider-medium", new SmoothServer(4, 0));
        servers.put("provider-large", new SmoothServer(5, 0));

    }

    public  static int getServer(int weightCount) {
/*
        int num = 0;
        SmoothServer tmpSv = null;
        for (int i = 0; i < servers.size(); i++) {
            SmoothServer sv = servers.get(i);
            sv.setCurWeight(sv.getWeight() + sv.getCurWeight());
            if (tmpSv == null || tmpSv.getCurWeight() < sv.getCurWeight()) {
                tmpSv = sv;
                num = i;
            }
            ;
        }

        tmpSv.setCurWeight(tmpSv.getCurWeight() - weightCount);
        return num;
*/


        int num = 0;
        int i = 0;
        SmoothServer tmpSv = null;

        for (Map.Entry<String, SmoothServer> entry : servers.entrySet()) {
            SmoothServer sv = entry.getValue();
            sv.setCurWeight(sv.getWeight() + sv.getCurWeight());
            if (tmpSv == null || tmpSv.getCurWeight() < sv.getCurWeight()) {
                tmpSv = sv;
                num = i;
            }
            i++;

        }
        int curWeight = tmpSv.getCurWeight() - weightCount;

     /*   if (curWeight < 0) {
            curWeight = 0;
        }*/
        tmpSv.setCurWeight(curWeight);
        return num;

    }
    public  static int sumWeight() {
        int value = 0;
        for (Map.Entry<String, SmoothServer> entry : servers.entrySet()) {
            value += entry.getValue().getWeight();
        }
       // System.out.println("sum===="+value);
        return value;
    }

    public  static int minWeight() {
        int min=servers.entrySet().stream().mapToInt(w->w.getValue().getWeight()).min().getAsInt();
       return min;
    }
    public  static int sumElapsed() {
        int value = 0;
        for (Map.Entry<String, SmoothServer> entry : servers.entrySet()) {
            value += entry.getValue().getElapsed();
        }
        // System.out.println("sum===="+value);
        return value;
    }
    public static void main(String[] args) {
        int min=servers.entrySet().stream().mapToInt(w->w.getValue().getWeight()).min().getAsInt();
        System.out.println(min);

    }
}
