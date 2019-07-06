package com.aliware.tianchi;

import com.aliware.tianchi.smooth.SmoothServer;
import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;
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

    static CompletableFuture<Result> completableFuture = null;
    static Map<String, SmoothServer> map = SmoothWeight.servers;

    /*
        @Override
        public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        */
/*    completableFuture.supplyAsync( Result res,Executor executor) {
            return asyncSupplyStage(screenExecutor(executor), supplier);
        }*//*


     */
/*     completableFuture.whenComplete((res, e) ->
        {
            System.out.println("结果：" + res);
        });*//*

        Invoker invoker = null;
        try {
     */
/*     Map<String,String> map= url.getParameters();
            for (Map.Entry<String, String> entry : map.entrySet()) {

                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

            }*//*

            //System.out.println("url===="+ url.getParameters().get(WEIGHT));
            //    System.out.println("url===="+invoker.getUrl().getHost());
            invoker = invokers.get(SmoothWeight.getServer(SmoothWeight.sumWeight()));

        } catch (Exception e) {
            e.printStackTrace();
        }

     */
/*   RpcStatus status= RpcStatus.getStatus(url);
        System.out.println(status.getAverageTps());*//*

     */
/*  Map<String,String> map=invocation.getAttachments();
        try {
            Cf.completableFuture = CompletableFuture.supplyAsync(() -> {
                Invoker invoker=invokers.get(SmoothWeight.getServer(6));
                return invoker;

            });
            map.put("com",Cf.completableFuture.toString());

            System.out.println("========"+Cf.completableFuture);
            return  Cf.completableFuture.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;*//*


        return invoker;

    }
*/
    public static final String POOL_CORE_COUNT = "active_thread";


    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        // Number of invokers

      /*  int length = invokers.size();
        // The least active value of all invokers
        double leastActive = -1;
        // The number of invokers having the same least active value (leastActive)
        int leastCount = 0;
        // The index of invokers having the same least active value (leastActive)
        int[] leastIndexes = new int[length];
        // the weight of every invokers
        int[] weights = new int[length];
        // The sum of the warmup weights of all the least active invokes
        int totalWeight = 0;
        // The weight of the first least active invoke
        int firstWeight = 0;
        // Every least active invoker has the same weight value?
        boolean sameWeight = true;

        // Filter out all the least active invokers
        for (int i = 0; i < length; i++) {
            Invoker<T> invoker = invokers.get(i);


            // Get the active number of the invoke
          //  RpcStatus.getStatus(invoker.getUrl()).getActive()
            double active=0;
            try {
                if(RpcStatus.getStatus(invoker.getUrl(),invocation.getMethodName()).get(POOL_CORE_COUNT)!=null) {
                    active = Double.parseDouble(RpcStatus.getStatus(invoker.getUrl(), invocation.getMethodName()).get(POOL_CORE_COUNT).toString());



                    *//*    if(active==200){
                        continue;
                    }*//*
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            // Get the weight of the invoke configuration. The default value is 100.
            int afterWarmup = SmoothWeight.sumWeight();
            // save for later use
            weights[i] = afterWarmup;
            // If it is the first invoker or the active number of the invoker is less than the current least active number
            if (leastActive == -1 || active < leastActive) {
                // Reset the active number of the current invoker to the least active number
                leastActive = active;
                // Reset the number of least active invokers
                leastCount = 1;
                // Put the first least active invoker first in leastIndexes
                leastIndexes[0] = i;
                // Reset totalWeight
                totalWeight = afterWarmup;
                // Record the weight the first least active invoker
                firstWeight = afterWarmup;
                // Each invoke has the same weight (only one invoker here)
                sameWeight = true;
                // If current invoker's active value equals with leaseActive, then accumulating.
            } else if (active == leastActive) {
                // Record the index of the least active invoker in leastIndexes order
                leastIndexes[leastCount++] = i;
                // Accumulate the total weight of the least active invoker
                totalWeight += afterWarmup;
                // If every invoker has the same weight?
                if (sameWeight && i > 0
                        && afterWarmup != firstWeight) {
                    sameWeight = false;
                }
            }
        }
        // Choose an invoker from all the least active invokers
        if (leastCount == 1) {
            // If we got exactly one invoker having the least active value, return this invoker directly.

            return invokers.get(leastIndexes[0]);
        }
        if (!sameWeight && totalWeight > 0) {
            // If (not every invoker has the same weight & at least one invoker's weight>0), select randomly based on
            // totalWeight.
            int offsetWeight = ThreadLocalRandom.current().nextInt(totalWeight);
            // Return a invoker based on the random value.
            for (int i = 0; i < leastCount; i++) {
                int leastIndex = leastIndexes[i];
                offsetWeight -= weights[leastIndex];
                if (offsetWeight < 0) {
                    return invokers.get(leastIndex);
                }
            }
        }
        // If all invokers have the same weight value or totalWeight=0, return evenly.
        return invokers.get(leastIndexes[ThreadLocalRandom.current().nextInt(leastCount)]);*/
        return invokers.get(SmoothWeight.getServer(SmoothWeight.sumWeight()));
    }

    public static void add(Result result, Invoker<?> invoker, Invocation invocation) {
        completableFuture = CompletableFuture.supplyAsync(() ->
        {
            getResult(result, invoker, invocation);
            return result;
        });
        completableFuture.whenComplete((res, e) ->
        {
        });

    }

    private static final String TIMEOUT_FILTER_START_TIME = "timeout_filter_start_time";

    public static final String START_TIME = "start_time";

    public void getWeights(Result result, Invoker<?> invoker, Invocation invocation) {
     /*   int length = invokers.size(); // 总个数
        int leastActive = -1; // 最小的活跃数
        int leastCount = 0; // 相同最小活跃数的个数
        int[] leastIndexs = new int[length]; // 相同最小活跃数的下标
        int totalWeight = 0; // 总权重
        int firstWeight = 0; // 第一个权重，用于于计算是否相同
        boolean sameWeight = true; // 是否所有权重相同
*/

    }

    public static void getResult(Result result, Invoker<?> invoker, Invocation invocation) {

       try {
           ExecutorService executor = (ExecutorService) ExtensionLoader.getExtensionLoader(ThreadPool.class).getAdaptiveExtension().getExecutor(invoker.getUrl());
           //     ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
           //   int timeout = invoker.getUrl().getMethodParameter(invocation.getMethodName(), Constants.TIMEOUT_KEY, Constants.DEFAULT_TIMEOUT);
           String host = invoker.getUrl().getHost();
           String params = result.getAttachment(POOL_CORE_COUNT);
          if(params!=null) {
              int activeThread = Integer.parseInt(params.split("\t")[0]);


              int thread = ((ThreadPoolExecutor) executor).getCorePoolSize();
              int providerThread = Integer.parseInt(params.split("\t")[1]);
              DecimalFormat df = new DecimalFormat("######0.00");
              // double totalThread = 1-((double) activeThread / 200);
              double threadbl = 1 - ((double) activeThread / providerThread);
              double w = 0;
              // int w=1;
    /*   if(invoker.getUrl().getHost().equals("provider-small")){
           w=1;
       }else if(invoker.getUrl().getHost().equals("provider-medium")){
           w=2;
       }else {
           w=3;
       }*/
              long time = 0;
              if(threadbl>0.15) {
                  if (result.getAttachment(START_TIME) != null) {
                      long startTime = Long.parseLong(result.getAttachment(START_TIME));
                      long stopTime = System.currentTimeMillis();
                      time = stopTime - startTime;
                      w += Double.parseDouble(df.format(1 - (time / 1000)));
                  }
              }
              //   w += Double.parseDouble(df.format(((totalThread))));
              w += Double.parseDouble(df.format(((threadbl))));

              //  double  threadRes= Double.parseDouble(df.format(((1-threadbl))));
              int res = new Double(w * 100).intValue();
              //System.out.println("res======" + res + "\t" + host + "\t" + activeThread + "\t" + providerThread + "\t" + time);
              //RpcStatus.getStatus(invoker.getUrl(), invocation.getMethodName()).set(POOL_CORE_COUNT, res);
              SmoothServer smoothServer = new SmoothServer(host, res, 0);

              map.put(host, smoothServer);
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
          }
       }catch (Exception e){
           e.printStackTrace();
       }

    }

    public static boolean updateThreadWeight(Map<String, SmoothServer> map, String host, int activeThread, int thread) {

        return false;
    }

    public static void updateWeight(Map<String, SmoothServer> map, String host, long time, long timeout) {
        if (time < timeout) {
            if (time < 400) {
                SmoothServer smoothServer = new SmoothServer(host, 3, 0);
                map.put(host, smoothServer);


            } else if (time < 700 && time >= 400) {
                SmoothServer smoothServer = new SmoothServer(host, 2, 0);
                map.put(host, smoothServer);

            } else if (time < 900) {
                SmoothServer smoothServer = new SmoothServer(host, 1, 0);
                map.put(host, smoothServer);

            } else {
                SmoothServer smoothServer = new SmoothServer(host, 0, 0);
                map.put(host, smoothServer);
            }
        } else {
            SmoothServer smoothServer = new SmoothServer(host, 0, 0);
            map.put(host, smoothServer);
        }

    }

    public static void main(String[] args) {


     /*   for(SmoothServer smoothServer:list){
            System.out.println(smoothServer.getIp());
            System.out.println(smoothServer.getWeight());

        }*/
 /*       for (int i = 0; i < 10; i++) {
            System.out.println("w===="+SmoothWeight.sumWeight());
            System.out.println(SmoothWeight.getServer(SmoothWeight.sumWeight()));
        }*/
      double a=0.16;
      int a1=100;
        System.out.println(0.16*a1);
        int   i   =   (new   Double(a*a1)).intValue();
        System.out.println(i);

    }

}
