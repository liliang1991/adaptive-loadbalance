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


    public static final String POOL_CORE_COUNT = "active_thread";


    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) {

        int index = SmoothWeight.getServer(SmoothWeight.sumWeight());
        //   System.out.println("index===="+index+"\t"+SmoothWeight.sumWeight());

        return invokers.get(index);
   /*     try {
          //  return invokers.get(ThreadLocalRandom.current().nextInt(invokers.size()));

        }catch (Exception e){
            return null;
        }
*/
    }

    public static void add(Result result, Invoker<?> invoker, Invocation invocation, long time) {
        completableFuture = CompletableFuture.supplyAsync(() ->
        {
            getResult(result, invoker, invocation, time);
            return result;
        });
    }

    private static final String TIMEOUT_FILTER_START_TIME = "timeout_filter_start_time";

    public static final String START_TIME = "start_time";


    public static void getResult(Result result, Invoker<?> invoker, Invocation invocation, long time) {
        Lock lock = new ReentrantLock();
        try {
            lock.lock();
            //  ExecutorService executor = (ExecutorService) ExtensionLoader.getExtensionLoader(ThreadPool.class).getAdaptiveExtension().getExecutor(invoker.getUrl());
            //     ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
            //   int timeout = invoker.getUrl().getMethodParameter(invocation.getMethodName(), Constants.TIMEOUT_KEY, Constants.DEFAULT_TIMEOUT);
            String host = invoker.getUrl().getHost();
            String params = result.getAttachment(POOL_CORE_COUNT);
      /*      if(time>=950){
                SmoothServer smoothServer = new SmoothServer(host, 0, 0);
                map.put(host, smoothServer);
                return;
            }*/
            if (params != null) {

                int activeThread = Integer.parseInt(params.split("\t")[0]);


                //            int thread = ((ThreadPoolExecutor) executor).getCorePoolSize();
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
            } else {
                System.out.println("result======" + result.getException());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
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
        for (int i = 0; i < 10; i++) {
            // System.out.println("w===="+SmoothWeight.sumWeight());
            System.out.println(SmoothWeight.getServer(SmoothWeight.sumWeight()));
        }

    }

}
