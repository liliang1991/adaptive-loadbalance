package com.aliware.tianchi;

import com.aliware.tianchi.smooth.SmoothServer;
import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.threadpool.ThreadPool;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.cluster.LoadBalance;

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
    boolean ispass = false;
    static Map<String, SmoothServer> map = SmoothWeight.servers;

    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) {
    /*    completableFuture.supplyAsync( Result res,Executor executor) {
            return asyncSupplyStage(screenExecutor(executor), supplier);
        }*/

   /*     completableFuture.whenComplete((res, e) ->
        {
            System.out.println("结果：" + res);
        });*/
        Invoker invoker = null;
        try {
     /*     Map<String,String> map= url.getParameters();
            for (Map.Entry<String, String> entry : map.entrySet()) {

                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

            }*/
            //System.out.println("url===="+ url.getParameters().get(WEIGHT));
            //    System.out.println("url===="+invoker.getUrl().getHost());
            invoker = invokers.get(SmoothWeight.getServer(SmoothWeight.sumWeight()));

        } catch (Exception e) {
            e.printStackTrace();
        }

     /*   RpcStatus status= RpcStatus.getStatus(url);
        System.out.println(status.getAverageTps());*/
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
        return null;*/

        return invoker;

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

    public static final String POOL_CORE_COUNT = "active_thread";
    public static final String START_TIME = "start_time";

    public static void getResult(Result result, Invoker<?> invoker, Invocation invocation) {
        ExecutorService executor = (ExecutorService) ExtensionLoader.getExtensionLoader(ThreadPool.class).getAdaptiveExtension().getExecutor(invoker.getUrl());
        ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
        int timeout = invoker.getUrl().getMethodParameter(invocation.getMethodName(), Constants.TIMEOUT_KEY, Constants.DEFAULT_TIMEOUT);
        String host = invoker.getUrl().getHost();
        String params = result.getAttachment(POOL_CORE_COUNT);

        int activeThread = Integer.parseInt(params.split("\t")[0]);
        int thread = ((ThreadPoolExecutor) executor).getCorePoolSize();
        int providerThread = Integer.parseInt(params.split("\t")[1]);

        if (result.hasException()) {
            System.out.println("activeThead===" + activeThread);
            System.out.println("thread====" + thread);
            System.out.println("exception======" + result.getException());
        }
        if (result.getAttachment(POOL_CORE_COUNT) != null) {

            if (thread == activeThread) {

                SmoothServer smoothServer = new SmoothServer(host, 0, 0);
                map.put(host, smoothServer);
                return ;
            }

            if(activeThread<providerThread*0.8){
                    SmoothServer smoothServer = new SmoothServer(host, map.get(host).getWeight()+1, 0);
                    map.put(host, smoothServer);
                    return ;
            }else if(activeThread>=providerThread*0.9){
                SmoothServer smoothServer = new SmoothServer(host, 0, 0);
                map.put(host, smoothServer);
                return ;
            }


            if (result.getAttachment(START_TIME) != null) {
                long startTime = Long.parseLong(result.getAttachment(START_TIME));
                long stopTime = System.currentTimeMillis();

                long time = stopTime - startTime;
                updateWeight(map, host, time, timeout);
            }
        }



     /*   if (result.hasException()) {
            System.out.println("exception====" + result.getException());

        }*/
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

            } else {
                SmoothServer smoothServer = new SmoothServer(host, 1, 0);
                map.put(host, smoothServer);

            }
        }

    }

    public static void main(String[] args) {


     /*   for(SmoothServer smoothServer:list){
            System.out.println(smoothServer.getIp());
            System.out.println(smoothServer.getWeight());

        }*/
        for (int i = 0; i < 20; i++) {
 /*           if(i==5){
             map.put("provider-large",new SmoothServer(0,0));
            }
            map.put("provider-large",new SmoothServer(1,0));*/

            System.out.println(SmoothWeight.getServer(SmoothWeight.sumWeight()));
        }


    }

}
