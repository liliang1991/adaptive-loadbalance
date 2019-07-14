package com.aliware.tianchi;

import com.aliware.tianchi.smooth.SmoothServer;
import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.common.threadpool.ThreadPool;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.cluster.LoadBalance;
import org.apache.dubbo.rpc.cluster.loadbalance.AbstractLoadBalance;

import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author daofeng.xjf
 * <p>
 * 负载均衡扩展接口
 * 必选接口，核心接口
 * 此类可以修改实现，不可以移动类或者修改包名
 * 选手需要基于此类实现自己的负载均衡算法
 */
public class UserLoadBalance implements LoadBalance {
    public static final String WEIGHT = "weight";
    // static CompletableFuture<Result> completableFuture=new CompletableFuture<>();
    Map<String,Integer> mapProvider=new HashMap<>();

    static CompletableFuture completableFuture = null;
    static Map<String, SmoothServer> map = SmoothWeight.servers;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static final String POOL_CORE_COUNT = "active_thread";


    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        int index = SmoothWeight.getServer(SmoothWeight.sumWeight());
        String key=invokers.get(index).getUrl().getHost();
        mapProvider.put(key,mapProvider.getOrDefault(key,0)+1);

        return invokers.get(index);

    }

    public static void add(String host, int active_thread_count, int thread_count) {

        completableFuture = CompletableFuture.runAsync(() ->
        {
            //this.sleep(3000);
            getResult(host, active_thread_count, thread_count);

        });


    }

    private static final String TIMEOUT_FILTER_START_TIME = "timeout_filter_start_time";

    public static final String START_TIME = "start_time";


    public static void getResult(String host, int activeThread, int providerThread) {

        try {
            //  ExecutorService executor = (ExecutorService) ExtensionLoader.getExtensionLoader(ThreadPool.class).getAdaptiveExtension().getExecutor(invoker.getUrl());
            //     ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
            //   int timeout = invoker.getUrl().getMethodParameter(invocation.getMethodName(), Constants.TIMEOUT_KEY, Constants.DEFAULT_TIMEOUT);
      /*      if(time>=950){
                SmoothServer smoothServer = new SmoothServer(host, 0, 0);
                map.put(host, smoothServer);
                return;
            }*/


            //            int thread = ((ThreadPoolExecutor) executor).getCorePoolSize();
            DecimalFormat df = new DecimalFormat("######0.00");
            // double totalThread = 1-((double) activeThread / 200);

            double threadbl = 1 - (activeThread / providerThread);
            double w = 0;
            // int w=1;
    /*   if(invoker.getUrl().getHost().equals("provider-small")){
           w=1;
       }else if(invoker.getUrl().getHost().equals("provider-medium")){
           w=2;
       }else {
           w=3;
       }*/
              /*  long time = 0;
                if (threadbl > 0.15) {
                    if (result.getAttachment(START_TIME) != null) {
                        long startTime =Long.parseLong(invocation.getAttachment(START_TIME));
                        long stopTime = System.currentTimeMillis();
                        time = stopTime - startTime;
                        if(time>900){
                            System.out.println("time======"+time);
                        }
                    }
                }*/
            //   w += Double.parseDouble(df.format(((totalThread))));
            w += Double.parseDouble(df.format(((threadbl))));
           /*   if(w>0.2) {
                  w += Double.parseDouble(df.format(1 - (time / 1000)));
              }*/
            int res = new Double(w * 100).intValue();


            //  double  threadRes= Double.parseDouble(df.format(((1-threadbl))));
            // System.out.println("res======" + res + "\t" + host + "\t" + activeThread + "\t" + providerThread + "\t" + time);
            //RpcStatus.getStatus(invoker.getUrl(), invocation.getMethodName()).set(POOL_CORE_COUNT, res);
            SmoothServer smoothServer = new SmoothServer(host, res, 0);
            map.put(host, smoothServer);

         /*   for (Map.Entry<String, SmoothServer> entry : map.entrySet()) {
                System.out.println("weight===" + activeThread + "\t" + providerThread + "\t" + entry.getKey() + "\t" + entry.getValue().toString());

            }*/
            /*    int sumWeight=SmoothWeight.sumWeight();
                smoothServer.setCurWeight(smoothServer.getCurWeight()-sumWeight);
                map.put(host, smoothServer);*/

   /*     if (result.hasException()) {
            System.out.println("activeThead===" + activeThread);
            System.out.println("thread====" + thread);
            System.out.println("exception======" + result.getException());
        }*/

//        if (result.getAttachment(POOL_CORE_COUNT) != null) {
//
//            if (thread == activeThread) {
//
//                SmoothServer smoothServer = new SmoothServer(host, 0, 0);
//                map.put(host, smoothServer);
//                return;
//            }
//          /*  if (result.getAttachment(START_TIME) != null) {
//                long startTime = Long.parseLong(result.getAttachment(START_TIME));
//                long stopTime = System.currentTimeMillis();
//
//                long time = stopTime - startTime;
//                updateWeight(map, host, time, timeout);
//            }*/
//        }



     /*   if (result.hasException()) {
            System.out.println("exception====" + result.getException());

        }*/
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

    }

    public static boolean updateThreadWeight(Map<String, SmoothServer> map, String host, int activeThread, int thread) {

        return false;
    }


    public static void main(String[] args) {


     /*   for(SmoothServer smoothServer:list){
            System.out.println(smoothServer.getIp());
            System.out.println(smoothServer.getWeight());

        }*/
        for (int i = 0; i < 10; i++) {
            // System.out.println("w===="+SmoothWeight.sumWeight());
            System.out.println(SmoothWeight.getServer(SmoothWeight.sumWeight()));
        }

    }

}
