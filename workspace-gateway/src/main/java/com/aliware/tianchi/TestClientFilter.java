package com.aliware.tianchi;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;

import org.apache.dubbo.rpc.*;


import org.apache.dubbo.rpc.support.RpcUtils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * @author daofeng.xjf
 * <p>
 * 客户端过滤器
 * 可选接口
 * 用户可以在客户端拦截请求和响应,捕获 rpc 调用时产生、服务端返回的已知异常。
 */
@Activate(group = Constants.CONSUMER)
public class TestClientFilter implements Filter {
    private final Logger Logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        try {
            boolean isAsync = RpcUtils.isAsync(invoker.getUrl(), invocation);
            if (isAsync) {
                    AsyncRpcResult asyncRpcResult = (AsyncRpcResult) invoker.invoke(invocation);
                    asyncRpcResult.thenApplyWithContext(r -> doPostProcess(r, invoker));
                return asyncRpcResult;

            } else {
                return invoker.invoke(invocation);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;


    }


    public Result doPostProcess(Result result, Invoker<?> invoker) {
        try {

            UserLoadBalance.callBack(result, invoker);
        }catch (Exception e){
          e.printStackTrace();
        }
        return  result;
    }

    @Override
    public Result onResponse(Result result, Invoker<?> invoker, Invocation invocation) {

        return result;
    }


}
