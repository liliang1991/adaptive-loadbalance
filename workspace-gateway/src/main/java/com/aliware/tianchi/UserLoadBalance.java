package com.aliware.tianchi;

import com.aliware.tianchi.smooth.SmoothServer;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.RpcException;
import org.apache.dubbo.rpc.RpcStatus;
import org.apache.dubbo.rpc.cluster.LoadBalance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author daofeng.xjf
 * <p>
 * 负载均衡扩展接口
 * 必选接口，核心接口
 * 此类可以修改实现，不可以移动类或者修改包名
 * 选手需要基于此类实现自己的负载均衡算法
 */
public class UserLoadBalance implements LoadBalance {
    public static final String WEIGHT="weight";

    boolean ispass=false;
    @Override
    public <T> Invoker<T> select(List<Invoker<T>> invokers, URL url, Invocation invocation)  {

        Invoker invoker=null;
        try {
     /*     Map<String,String> map= url.getParameters();
            for (Map.Entry<String, String> entry : map.entrySet()) {

                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

            }*/
            //System.out.println("url===="+ url.getParameters().get(WEIGHT));
        //    System.out.println("url===="+invoker.getUrl().getHost());
            invoker=invokers.get(SmoothWeight.getServer(6));

        }catch (Exception e){
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

         return  invoker;

    }
    public  void sleeps(int timeout,List<Invoker> invokers, Invocation invocation) throws RpcException {
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
       try {
           wait(timeout);

       }catch (Exception e){

       }

    }
    public static void main(String[] args) {

    }

}
