package com.aliware.tianchi;

import com.aliware.tianchi.smooth.SmoothServer;
import com.aliware.tianchi.status.ProviderStatus;
import com.google.gson.Gson;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.cluster.LoadBalance;
import java.util.*;
/**
 * @author daofeng.xjf
 * <p>
 * 负载均衡扩展接口
 * 必选接口，核心接口
 * 此类可以修改实现，不可以移动类或者修改包名
 * 选手需要基于此类实现自己的负载均衡算法
 */
public class UserLoadBalance implements LoadBalance {
    private static final Logger logger = LoggerFactory.getLogger(UserLoadBalance.class);
    static Map<String, SmoothServer> map = SmoothWeight.servers;
    public static final String PROVIDER_CORE_COUNT = "provider_thread";
     static   Gson gson=new Gson();
    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        try {
            Invoker invoker = invokers.get(SmoothWeight.getServer(SmoothWeight.sumWeight()));

      /*      logger.info("选中的机器为"+invoker.getUrl().getHost());
            for (Map.Entry<String, SmoothServer> entry : map.entrySet()) {
              logger.info("时间消耗"+entry.getKey()+"\t"+entry.getValue().getWeight()+"\t"+entry.getValue().getActiveCount()+"\t"+entry.getValue().getThreadCount());
            }*/
            return invoker;
        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public static void addCallBack(Result result, Invoker<?> invoker) {
        try {
            callBack(result, invoker);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void callBack(Result result, Invoker<?> invoker) {
        try {
            String host = invoker.getUrl().getHost();
            String params = result.getAttachment(PROVIDER_CORE_COUNT);
            if (params != null) {
                ProviderStatus providerStatus=gson.fromJson(params, ProviderStatus.class);
                int activeThread=providerStatus.getActiveCount();
                int thread=providerStatus.getThreadCount();
              //  long elapsed=providerStatus.getElapsed();
              // logger.info("elapsed===="+elapsed);
                //总次数
                long total=providerStatus.getTotal();
                //总调用时长
                long totalElapsed=providerStatus.getTotalElapsed();
                long avg=totalElapsed/total;
                int surplusThread=(thread-activeThread)-(new Long(avg).intValue());

        /*        int timeoutNum=map.get(host).getTimeoutCount();
                if(timeoutNum>=0) {
                    if (elapsed > avg) {
                        timeoutNum = timeoutNum + 1;
                    } else {
                        timeoutNum = timeoutNum - 1;
                    }
                }

                if(timeoutNum>=100){
                    surplusThread=SmoothWeight.minWeight();
                }*/
                SmoothServer smoothServer = new SmoothServer(surplusThread, 0);
                map.put(host, smoothServer);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {


     /*   for(SmoothServer smoothServer:list){
            System.out.println(smoothServer.getIp());
            System.out.println(smoothServer.getWeight());

        }*/
        String host = "small";
        for (int i = 0; i < 15; i++) {
            // System.out.println("w===="+SmoothWeight.sumWeight());
        /*    if (i == 10) {
                SmoothServer smoothServer = new SmoothServer(host, 5, 0);
                map.put(host, smoothServer);
            }*/
      /*      if (i == 5) {
                for (Map.Entry<String, SmoothServer> entry : map.entrySet()) {
                    System.out.println("key====" + entry.getKey());
                }
                SmoothServer smoothServer = new SmoothServer(0, 0);
                map.put("provider-small", smoothServer);
                for (Map.Entry<String, SmoothServer> entry : map.entrySet()) {
                    System.out.println("key====" + entry.getKey());
                }

            }*/

            System.out.println(SmoothWeight.getServer(SmoothWeight.sumWeight()));
        }

    }

}
