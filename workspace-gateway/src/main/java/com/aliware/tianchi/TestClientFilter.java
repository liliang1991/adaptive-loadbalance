package com.aliware.tianchi;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import java.util.Map;

/**
 * @author daofeng.xjf
 *
 * 客户端过滤器
 * 可选接口
 * 用户可以在客户端拦截请求和响应,捕获 rpc 调用时产生、服务端返回的已知异常。
 */
@Activate(group = Constants.CONSUMER)
public class TestClientFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try{

            Result result = invoker.invoke(invocation);
/*            URL url=invocation.getInvoker().getUrl();
            RpcStatus status= RpcStatus.getStatus(url);
            System.out.println(status.getActive());*/

            return result;
        }catch (RpcException e){
        }
        return null;

    }
    private static final String TIMEOUT_FILTER_START_TIME = "timeout_filter_start_time";

    public static final String POOL_CORE_COUNT = "active_thread";
    @Override
    public Result onResponse(Result result, Invoker<?> invoker, Invocation invocation) {

       // System.out.println(result.getAttachment("quota"));
        invoker.getUrl().getHost();
        System.out.println(result.getAttachment(POOL_CORE_COUNT)+"poolcount========");
        if(result.hasException()){
          //  System.out.println("exception====="+result.getAttachment("quota")+result.getException());
            System.out.println("exception=========");
            return null;
        }else {
            return result;
        }
        //System.out.println("key-------"+result.getAttachment(key));
     /*   Map<String, String> map = result.getAttachments();
        System.out.println("mapsize========" + map.size());
        for (Map.Entry<String, String> entry : map.entrySet()) {

            System.out.println("key=======" + entry.getKey() + "====value======" + entry.getValue());


        }*/
    }
}
