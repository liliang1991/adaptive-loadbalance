package com.aliware.tianchi;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.context.ConfigManager;
import org.apache.dubbo.rpc.*;

import java.util.Map;

/**
 * @author daofeng.xjf
 * <p>
 * 客户端过滤器
 * 可选接口
 * 用户可以在客户端拦截请求和响应,捕获 rpc 调用时产生、服务端返回的已知异常。
 */
@Activate(group = Constants.CONSUMER)
public class TestClientFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {

            Result result = invoker.invoke(invocation);
/*            URL url=invocation.getInvoker().getUrl();
            RpcStatus status= RpcStatus.getStatus(url);
            System.out.println(status.getActive());*/

            return result;
        } catch (RpcException e) {
        }
        return null;

    }

    private static final String TIMEOUT_FILTER_START_TIME = "timeout_filter_start_time";

    public static final String POOL_CORE_COUNT = "active_thread";
    public static final String WEIGHT = "weight";

    @Override
    public Result onResponse(Result result, Invoker<?> invoker, Invocation invocation) {
        try {

          /*  Map<String, String> map = invoker.getUrl().getParameters();
            map.put(WEIGHT, "1");
            for (Map.Entry<String, String> entry : map.entrySet()) {

                System.out.println("Key1 = " + entry.getKey() + ", Value1 = " + entry.getValue());

            }*/
            // System.out.println(result.getAttachment("quota"));
/*            String params = result.getAttachment(POOL_CORE_COUNT);
            int activeThread = Integer.parseInt(params.split("\t")[0]);
            int thread = Integer.parseInt(params.split("\t")[1]);
            System.out.println("activethread=====" + activeThread);
            System.out.println("thereads=======" + thread);
            System.out.println("Available======="+invoker.isAvailable());*/
            if (result.hasException()) {
                synchronized(invoker) {
               //     System.out.println(result.getException());
                    invoker.wait(1000);
                }
                //  System.out.println("exception====="+result.getAttachment("quota")+result.getException());
                return result;
            } else {
                return result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
        //System.out.println("key-------"+result.getAttachment(key));
     /*   Map<String, String> map = result.getAttachments();
        System.out.println("mapsize========" + map.size());
        for (Map.Entry<String, String> entry : map.entrySet()) {

            System.out.println("key=======" + entry.getKey() + "====value======" + entry.getValue());


        }*/
    }
}
