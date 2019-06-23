package com.aliware.tianchi;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.Filter;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;
import org.apache.dubbo.rpc.RpcException;

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
            Result result = invoker.invoke(invocation);
  /*          if(result.getException()!=null)
            System.out.println(result.getException().getMessage());*/
            // System.out.println(result.getValue());
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Result onResponse(Result result, Invoker<?> invoker, Invocation invocation) {
        return result;
    }

}
