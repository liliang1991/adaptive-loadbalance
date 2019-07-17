package com.aliware.tianchi;

import com.aliware.tianchi.smooth.SmoothServer;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.cluster.LoadBalance;
import java.text.DecimalFormat;
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
    public static final String WEIGHT = "weight";
    private static final Logger logger = LoggerFactory.getLogger(UserLoadBalance.class);
    static CompletableFuture completableFuture = null;
    static Map<String, SmoothServer> map = SmoothWeight.servers;
    public static final String PROVIDER_CORE_COUNT = "provider_thread";
    static DecimalFormat df = new DecimalFormat("######0.00");

    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation) {
        try {
            int index = SmoothWeight.getServer(SmoothWeight.sumWeight());
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

    private static final String TIMEOUT_FILTER_START_TIME = "timeout_filter_start_time";
    public static final String START_TIME = "start_time";



    public static void callBack(Result result, Invoker<?> invoker, Invocation invocation) {
        try {
            String host = invoker.getUrl().getHost();
                String params = result.getAttachment(PROVIDER_CORE_COUNT);
                if (params != null) {
              /*  URL url = invoker.getUrl();
                String methodName = invocation.getMethodName();*/
                    int activeThread = Integer.parseInt(params.split("\t")[0]);
                    int providerThread = Integer.parseInt(params.split("\t")[1]);
                    /*     RpcStatus count = RpcStatus.getStatus(url,methodName);*/
                    // logger.info("active==="+count.getActive()+"\t"+activeThread);
                    double threadbl = 1 - ((double) activeThread / (double) providerThread);
                    double w = Double.parseDouble(df.format(((threadbl))));
                    int res = new Double(w * 100).intValue();
                    SmoothServer     smoothServer = new SmoothServer(res, 0);
                    smoothServer.setActiveCount(activeThread);
                    smoothServer.setThreadCount(providerThread);
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
