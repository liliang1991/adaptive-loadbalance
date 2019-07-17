package com.aliware.tianchi;

import com.aliware.tianchi.smooth.SmoothServer;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.cluster.LoadBalance;
import java.util.*;
import java.util.concurrent.CompletableFuture;
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
    static CompletableFuture completableFuture = null;
    static Map<String, SmoothServer> map = SmoothWeight.servers;
    public static final String PROVIDER_CORE_COUNT = "provider_thread";

    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        try {
            int index = SmoothWeight.getServer(SmoothWeight.sumWeight());
          //  map.entrySet().stream().forEach(elapsed-> System.out.println(index+"\t"+elapsed.getValue().getElapsed()));
         /*   if(index==0){
                SmoothServer smoothServer=map.get("provider-small");
                int activecount=smoothServer.getActiveCount();
                if(activecount>195){

                  logger.info("small 活跃线程为"+activecount);
                }

            }*/
            Invoker invoker = invokers.get(index);
            return invoker;

        } catch (Exception e) {
            e.printStackTrace();

        }
        return null;
    }

    public static void addCallBack(Result result, Invoker<?> invoker, Invocation invocation) {
        try {
            callBack(result, invoker, invocation);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void callBack(Result result, Invoker<?> invoker, Invocation invocation) {
        try {
            String host = invoker.getUrl().getHost();
                String params = result.getAttachment(PROVIDER_CORE_COUNT);
                if (params != null) {
                    int activeThread = Integer.parseInt(params.split("\t")[0]);
                    int providerThread = Integer.parseInt(params.split("\t")[1]);
                    long elapsed=Long.parseLong(params.split("\t")[2]);
                    int surplusThread=providerThread-activeThread;
                    SmoothServer     smoothServer = new SmoothServer(surplusThread, 0,elapsed);
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
            if (i == 5) {
                for (Map.Entry<String, SmoothServer> entry : map.entrySet()) {
                    System.out.println("key====" + entry.getKey());
                }
                SmoothServer smoothServer = new SmoothServer(0, 0);
                map.put("provider-small", smoothServer);
                for (Map.Entry<String, SmoothServer> entry : map.entrySet()) {
                    System.out.println("key====" + entry.getKey());
                }

            }

            System.out.println(SmoothWeight.getServer(SmoothWeight.sumWeight()));
        }

    }

}
