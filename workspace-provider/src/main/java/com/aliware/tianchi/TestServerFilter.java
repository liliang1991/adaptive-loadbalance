package com.aliware.tianchi;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.context.ConfigManager;
import org.apache.dubbo.rpc.*;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author daofeng.xjf
 * <p>
 * 服务端过滤器
 * 可选接口
 * 用户可以在服务端拦截请求和响应,捕获 rpc 调用时产生、服务端返回的已知异常。
 */
@Activate(group = Constants.PROVIDER)
public class TestServerFilter implements Filter {
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        try {

            /*Map<String,String> map=invocation.getAttachments();
            System.out.println("aaaaaaaaaa"+map.get("aa"));

            System.out.println(Cf.completableFuture+"==");
            CompletableFuture<Invoker> completableFuture = Cf.completableFuture.thenCompose(i -> {
                return CompletableFuture.supplyAsync(() -> {
                    SmoothWeight.getServer(6);
                    return i;
                });
            });
            completableFuture= completableFuture.whenComplete((res, e) ->
            {
            });

            invoker = completableFuture.get();*/

      /*      Map<String, ProtocolConfig> map = ConfigManager.getInstance().getProtocols();
            System.out.println("最大可接受连接数======"+map.get("dubbo").getAccepts());
            System.out.println("最大线程数======"+map.get("dubbo").getThreads());
            System.out.println("线程池类型为======"+map.get("dubbo").getThreadpool());
            System.out.println("核心线程池为======"+map.get("dubbo").getCorethreads());*/
            long startTime = System.currentTimeMillis();

            Result result = invoker.invoke(invocation);
            result.setAttachment(START_TIME, String.valueOf(startTime));

  /*          if(result.getException()!=null)
            System.out.println(result.getException().getMessage());*/
            // System.out.println(result.getValue());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static final String POOL_CORE_COUNT = "active_thread";
    public static final String START_TIME = "start_time";
    Map<String, ProtocolConfig> map = ConfigManager.getInstance().getProtocols();

    @Override
    public Result onResponse(Result result, Invoker<?> invoker, Invocation invocation) {
       // System.out.println("value===="+new Date(result.getValue().toString()).getTime());
        result.setAttachment(POOL_CORE_COUNT, invocation.getAttachment(POOL_CORE_COUNT) + "\t" + map.get("dubbo").getThreads());

        if(result.hasException()){
            System.out.println("exception====="+result.getException());
        }
        int coreCount=Integer.parseInt( invocation.getAttachment(POOL_CORE_COUNT));
     //   RpcStatus.getStatus(invoker.getUrl(),invocation.getMethodName()).set(POOL_CORE_COUNT,String.valueOf(coreCount));

      /*  int threadcount=map.get("dubbo").getThreads();

     */
     /*   if (result.hasException()) {
            System.out.println(">>>>>>>");
        }*/
        // System.out.println( "corecount======"+invocation.getAttachment(POOL_CORE_COUNT));
/*        System.out.println("quota======"+System.getProperty("quota"));
        result.setAttachment("quota",System.getProperty("quota"));*/
        //   Map<String, ProtocolConfig> map = ConfigManager.getInstance().getProtocols();
        return result;
    }

}
