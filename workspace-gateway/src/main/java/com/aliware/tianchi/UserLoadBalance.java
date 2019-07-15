package com.aliware.tianchi;

import com.aliware.tianchi.smooth.SmoothServer;
import com.aliware.tianchi.status.ProviderStatus;
import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.URLBuilder;
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
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    static CompletableFuture completableFuture = null;
    static Map<String, SmoothServer> map = SmoothWeight.servers;
    public static final String POOL_CORE_COUNT = "active_thread";
    static DecimalFormat df = new DecimalFormat("######0.00");

    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        int index = SmoothWeight.getServer(SmoothWeight.sumWeight());
        Invoker invoker = invokers.get(index);
        url.addParameter(POOL_CORE_COUNT, "10");
        return invoker;
    }

    public static void add(ProviderStatus providerStatus) {

     /*   completableFuture = CompletableFuture.runAsync(() ->
        {
            //this.sleep(3000);

        });*/

        getResult(providerStatus);

    }

    private static final String TIMEOUT_FILTER_START_TIME = "timeout_filter_start_time";
    public static final String START_TIME = "start_time";

    public synchronized static void getResult(ProviderStatus providerStatus) {
        try {
        /*    double threadbl = 1 - ((double) activeThread / (double) providerThread);
            double w = 0;
            w = Double.parseDouble(df.format(((threadbl))));
            int res = new Double(w * 100).intValue();
                SmoothServer smoothServer = new SmoothServer(res, 0);
            map.put(host, smoothServer);*/
            if (1 == providerStatus.getEnabled()) {
                SmoothServer smoothServer = null;
                if ("small".equals(providerStatus.getHost())) {
                    smoothServer = new SmoothServer(1, 0);

                } else if ("medium".equals(providerStatus.getHost())) {
                    smoothServer = new SmoothServer(2, 0);

                } else {
                    smoothServer = new SmoothServer(3, 0);
                }
                map.put(providerStatus.getHost(), smoothServer);

            } else {
                SmoothServer smoothServer = new SmoothServer(0, 0);
                map.put(providerStatus.getHost(), smoothServer);
            }

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
        String host = "small";
        for (int i = 0; i < 5; i++) {
            // System.out.println("w===="+SmoothWeight.sumWeight());
        /*    if (i == 10) {
                SmoothServer smoothServer = new SmoothServer(host, 5, 0);
                map.put(host, smoothServer);
            }*/
            System.out.println(SmoothWeight.getServer(SmoothWeight.sumWeight()));
        }

    }

}
