package com.aliware.tianchi;

import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.context.ConfigManager;
import org.apache.dubbo.rpc.*;

import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * @author daofeng.xjf
 * <p>
 * 服务端过滤器
 * 可选接口
 * 用户可以在服务端拦截请求和响应,捕获 rpc 调用时产生、服务端返回的已知异常。
 */
@Activate(group = Constants.PROVIDER)
public class TestServerFilter implements Filter {
    private static final Logger logger = LoggerFactory.getLogger(TestServerFilter.class);
    private static final int PROVIDER_THREADS= ConfigManager.getInstance().getProtocols().get("dubbo").getThreads();
    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {

        URL url = invoker.getUrl();
        String methodName = invocation.getMethodName();
        boolean isException = false;
        long begin = System.currentTimeMillis();

        try {
            RpcStatus count = RpcStatus.getStatus(url, methodName);
            int maxActive = count.getActive();
            if (maxActive >= PROVIDER_THREADS) {
                throw new RpcException("provider Thread pool is EXHAUSTED " + maxActive);
            }
            RpcStatus.beginCount(url,methodName);

            Result result= invoker.invoke(invocation);
            return result;
        } catch (Exception e) {
            isException = true;
            throw new RpcException("provider invoke exception ");

        } finally {
            RpcStatus.endCount(url, methodName, System.currentTimeMillis() - begin, isException);
        }
    }
    public static final String PROVIDER_CORE_COUNT = "provider_thread";
    @Override
    public Result onResponse(Result result, Invoker<?> invoker, Invocation invocation) {
        try {
            //  logger.info("result=="+result.toString());
            URL url = invoker.getUrl();
            String methodName = invocation.getMethodName();
            RpcStatus rpcStatus = RpcStatus.getStatus(url, methodName);
            long elapsed=rpcStatus.getMaxElapsed();

            result.setAttachment(PROVIDER_CORE_COUNT, rpcStatus.getActive()+"\t"+PROVIDER_THREADS+"\t"+elapsed);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


}
