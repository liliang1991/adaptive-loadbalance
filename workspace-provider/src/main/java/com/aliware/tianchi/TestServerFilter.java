package com.aliware.tianchi;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.context.ConfigManager;
import org.apache.dubbo.rpc.*;
import org.apache.dubbo.rpc.filter.tps.DefaultTPSLimiter;
import org.apache.dubbo.rpc.filter.tps.TPSLimiter;
import org.apache.dubbo.rpc.model.ApplicationModel;
import org.apache.dubbo.rpc.model.ConsumerMethodModel;
import org.apache.dubbo.rpc.model.ConsumerModel;
import org.springframework.cache.Cache;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    Map<String, ProtocolConfig> map = ConfigManager.getInstance().getProtocols();


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
            //      fireInvokeCallback(invoker, invocation);
            //    invocation.setAttachment(TIMEOUT_FILTER_START_TIME, String.valueOf(System.currentTimeMillis()));
            //     invocation.getAttachments().put(TIMEOUT_FILTER_START_TIME, String.valueOf(System.currentTimeMillis()));
     /*       URL url = invoker.getUrl();

            String methodName = invocation.getMethodName();
            int maxThread = map.get("dubbo").getThreads();
            System.out.println("====="+url.getMethodParameter(methodName,POOL_CORE_COUNT));*/
      /*      if (!RpcStatus.beginCount(url, methodName, maxThread)) {
                throw new RpcException("Failed to invoke method " + invocation.getMethodName() + " in provider " +
                        url + ", cause: The service using threads greater than <dubbo:service executes=\"" + maxThread +
                        "\" /> limited.");
            }*/
            //   result.setAttachment(START_TIME, String.valueOf(startTime));
  /*          if(result.getException()!=null)
            System.out.println(result.getException().getMessage());*/
            // System.out.println(result.getValue());

      /*      long startTime = System.currentTimeMillis();
            RpcInvocation ivc = (RpcInvocation) invocation;
            ivc.setAttachment(START_TIME, String.valueOf(startTime));*/
            return invoker.invoke(invocation);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }

    public static final String START_TIME = "start_time";

    public static final String PROVIDER_CORE_COUNT = "provider_thread";
    private static final String ELAPSE_TIME = "elapsed_time";
    public static final String POOL_CORE_COUNT = "active_thread";

    @Override
    public Result onResponse(Result result, Invoker<?> invoker, Invocation invocation) {
        String provider_core_count = invocation.getAttachment(PROVIDER_CORE_COUNT);
        if (provider_core_count != null) {
            result.setAttachment(PROVIDER_CORE_COUNT, invocation.getAttachment(PROVIDER_CORE_COUNT) + "\t" + map.get("dubbo").getThreads());
        }
     /*   String startAttach = invocation.getAttachment(TIMEOUT_FILTER_START_TIME);
        if (startAttach != null) {
            long elapsed = System.currentTimeMillis() - Long.valueOf(startAttach);
            result.setAttachment(ELAPSE_TIME, String.valueOf(elapsed));
        }
        String provider_core_count = invocation.getAttachment(PROVIDER_CORE_COUNT);
        RpcContext.getContext().setAttachment(POOL_CORE_COUNT,String.valueOf(provider_core_count));

        if (provider_core_count != null) {

            result.setAttachment(PROVIDER_CORE_COUNT, invocation.getAttachment(PROVIDER_CORE_COUNT) + "\t" + map.get("dubbo").getThreads());
        }*/
        // System.out.println("value===="+new Date(result.getValue().toString()).getTime());
        // map.get("dubbo").setDispatcher("message");
        //  System.out.println("path===="+map.get("dubbo").getDispatcher());
        try {

        /*    int coreCount=Integer.parseInt( invocation.getAttachment(POOL_CORE_COUNT));
            if(coreCount>=map.get("dubbo").getThreads()){
                System.out.println(">>>>>");
            }*/

        /*    long startTime = Long.parseLong(invocation.getAttachment(START_TIME));
            long stopTime = System.currentTimeMillis();
            long time = stopTime - startTime;
            System.out.println("time===="+time);
            if(time>=1000) {
                logger.info("机器响应时间为，" + time);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }


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
