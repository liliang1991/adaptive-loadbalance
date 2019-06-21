package com.aliware.tianchi;

import com.aliware.tianchi.smooth.SmoothServer;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.cluster.LoadBalance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author daofeng.xjf
 *
 * 负载均衡扩展接口
 * 必选接口，核心接口
 * 此类可以修改实现，不可以移动类或者修改包名
 * 选手需要基于此类实现自己的负载均衡算法
 */
public class UserLoadBalance implements LoadBalance {
   static List<SmoothServer> servers = new ArrayList<SmoothServer>(Arrays.asList(new SmoothServer("provider-small", 1, 0),new SmoothServer("provider-medium", 2, 0),new SmoothServer("provider-large", 3, 0)));


    public static int getServer(int weightCount) {
        int num=0;
        SmoothServer tmpSv = null;
        for (int i=0;i<servers.size();i++) {
            SmoothServer sv=servers.get(i);
            sv.setCurWeight(sv.getWeight() + sv.getCurWeight());
            if (tmpSv == null || tmpSv.getCurWeight() < sv.getCurWeight()) {
                tmpSv = sv;
                num=i;
            } ;
        }

        tmpSv.setCurWeight(tmpSv.getCurWeight() - weightCount);
        return num;

    }
    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) throws RpcException {
        //System.out.println(getServer(6));
     /*   System.out.println(getServer(weightCount));

        System.out.println(invokers.get(0).getUrl().getHost());*/
     //   System.out.println(invokers.get(ThreadLocalRandom.current().nextInt(invokers.size())));
        return invokers.get(getServer(6));
    }
}
