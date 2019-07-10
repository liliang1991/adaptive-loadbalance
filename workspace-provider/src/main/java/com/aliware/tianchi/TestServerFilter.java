package com.aliware.tianchi;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Activate;
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
    String $INVOKE = "$invoke";
    private static final String TIMEOUT_FILTER_START_TIME = "timeout_filter_start_time";

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
            Result result = invoker.invoke(invocation);
            //   result.setAttachment(START_TIME, String.valueOf(startTime));
            RpcContext.getContext().getFuture();
  /*          if(result.getException()!=null)
            System.out.println(result.getException().getMessage());*/
            // System.out.println(result.getValue());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static final String PROVIDER_CORE_COUNT = "active_thread";
    Map<String, ProtocolConfig> map = ConfigManager.getInstance().getProtocols();
    private static final String ELAPSE_TIME = "elapsed_time";

    @Override
    public Result onResponse(Result result, Invoker<?> invoker, Invocation invocation) {
        String startAttach = invocation.getAttachment(TIMEOUT_FILTER_START_TIME);
        if (startAttach != null) {
            long elapsed = System.currentTimeMillis() - Long.valueOf(startAttach);
            result.setAttachment(ELAPSE_TIME, String.valueOf(elapsed));
        }
        String provider_core_count = invocation.getAttachment(PROVIDER_CORE_COUNT);
        if (provider_core_count != null) {
            result.setAttachment(PROVIDER_CORE_COUNT, invocation.getAttachment(PROVIDER_CORE_COUNT) + "\t" + map.get("dubbo").getThreads());
        }
        // System.out.println("value===="+new Date(result.getValue().toString()).getTime());
        // map.get("dubbo").setDispatcher("message");
        //  System.out.println("path===="+map.get("dubbo").getDispatcher());
        try {

        /*    int coreCount=Integer.parseInt( invocation.getAttachment(POOL_CORE_COUNT));
            if(coreCount>=map.get("dubbo").getThreads()){
                System.out.println(">>>>>");
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

    private ConsumerMethodModel.AsyncMethodInfo getAsyncMethodInfo(Invoker<?> invoker, Invocation invocation) {
        final ConsumerModel consumerModel = ApplicationModel.getConsumerModel(invoker.getUrl().getServiceKey());
        if (consumerModel == null) {
            return null;
        }

        String methodName = invocation.getMethodName();
        if (methodName.equals($INVOKE)) {
            methodName = (String) invocation.getArguments()[0];
        }

        ConsumerMethodModel methodModel = consumerModel.getMethodModel(methodName);
        if (methodModel == null) {
            return null;
        }

        final ConsumerMethodModel.AsyncMethodInfo asyncMethodInfo = methodModel.getAsyncInfo();
        if (asyncMethodInfo == null) {
            return null;
        }

        return asyncMethodInfo;
    }

}
