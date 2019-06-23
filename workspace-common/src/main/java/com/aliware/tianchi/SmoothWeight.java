package com.aliware.tianchi;

import com.aliware.tianchi.smooth.SmoothServer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SmoothWeight {
    static List<SmoothServer> servers = new ArrayList<SmoothServer>(Arrays.asList(new SmoothServer("provider-small", 1, 0), new SmoothServer("provider-medium", 2, 0), new SmoothServer("provider-large", 3, 0)));

    public static int getServer(int weightCount) {
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

    }
}
