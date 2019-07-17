package com.aliware.tianchi;

import com.aliware.tianchi.status.ProviderStatus;
import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;

import org.apache.dubbo.config.ApplicationConfig;
import org.apache.dubbo.config.ServiceConfig;
import org.apache.dubbo.rpc.*;

import org.apache.dubbo.rpc.listener.CallbackListener;
import org.apache.dubbo.rpc.service.CallbackService;
import org.apache.dubbo.rpc.service.GenericException;
import org.apache.dubbo.rpc.service.GenericService;
import org.apache.dubbo.rpc.support.RpcUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;

/**
 * @author daofeng.xjf
 * <p>
 * 客户端过滤器
 * 可选接口
 * 用户可以在客户端拦截请求和响应,捕获 rpc 调用时产生、服务端返回的已知异常。
 */
@Activate(group = Constants.CONSUMER)
public class TestClientFilter implements Filter {
    CompletableFuture<Result> resultCompletableFuture = null;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    Map<String, Integer> map = new HashMap<>();

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        try {
            // RpcStatus.beginCount(invoker.getUrl(), invocation.getMethodName());
            boolean isAsync = RpcUtils.isAsync(invoker.getUrl(), invocation);
            if (isAsync) {
                AsyncRpcResult asyncRpcResult = (AsyncRpcResult) invoker.invoke(invocation);
                asyncRpcResult.thenApplyWithContext(r -> doPostProcess(r, invoker, invocation));
                resultCompletableFuture = asyncRpcResult.getResultFuture();
                return asyncRpcResult.getRpcResult();
            } else {
                return invoker.invoke(invocation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public Result doPostProcess(Result result, Invoker<?> invoker, Invocation invocation) {
        try {
            /*ServiceConfig<CallbackService> service = new ServiceConfig<CallbackService>();
            service.setApplication(new ApplicationConfig("bean-provider"));
            service.setRef(new CallbackService() {

                public void addListener(String jsonstr, CallbackListener listener) {
                    // ProviderStatus providerStatus = gson.fromJson(jsonStr, ProviderStatus.class);
                    logger.info("jsonstr======"+jsonstr);
                }
            });*/

            UserLoadBalance.addCallBack(result, invoker, invocation);
        }catch (Exception e){
          e.printStackTrace();
        }

        return result;
    }

    private static final String TIMEOUT_FILTER_START_TIME = "timeout_filter_start_time";

    public static final String POOL_CORE_COUNT = "active_thread";
    public static final String WEIGHT = "weight";
    public static final String START_TIME = "start_time";

    @Override
    public Result onResponse(Result result, Invoker<?> invoker, Invocation invocation) {
        try {
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;

    }
}
