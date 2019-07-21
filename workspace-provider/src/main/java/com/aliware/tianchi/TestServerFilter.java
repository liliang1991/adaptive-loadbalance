package com.aliware.tianchi;

import com.aliware.tianchi.status.ProviderStatus;
import com.google.gson.Gson;
import org.apache.dubbo.common.Constants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.config.context.ConfigManager;
import org.apache.dubbo.rpc.*;


/**
 * @author daofeng.xjf
 * <p>
 * 服务端过滤器
 * 可选接口
 * 用户可以在服务端拦截请求和响应,捕获 rpc 调用时产生、服务端返回的已知异常。
 */
@Activate(group = Constants.PROVIDER)
public class TestServerFilter implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestServerFilter.class);
    private static final int PROVIDER_THREADS= ConfigManager.getInstance().getProtocols().get("dubbo").getThreads();
    public static final String  ELAPSED= "elapsed";
    Gson gson=new Gson();
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
            long elapsed=System.currentTimeMillis() - begin;
          //  ((RpcInvocation)invocation).setAttachment(ELAPSED,String.valueOf(elapsed));
            RpcStatus.endCount(url, methodName, elapsed, isException);
        }
    }
    public static final String PROVIDER_STATUS_PARAM = "provider_status_param";
    @Override
    public Result onResponse(Result result, Invoker<?> invoker, Invocation invocation) {
        try {
            //  logger.info("result=="+result.toString());
            URL url = invoker.getUrl();
            String methodName = invocation.getMethodName();
            RpcStatus rpcStatus = RpcStatus.getStatus(url, methodName);
            ProviderStatus providerStatus=new ProviderStatus();
            //总次数
            providerStatus.setTotal(rpcStatus.getTotal());
            //总调用时长
            providerStatus.setTotalElapsed(rpcStatus.getTotalElapsed());
            providerStatus.setActiveCount(rpcStatus.getActive());
            providerStatus.setThreadCount(PROVIDER_THREADS);
          //  providerStatus.setElapsed(Long.valueOf(invocation.getAttachment(ELAPSED)));
 /*           System.out.println("ELAPSED=="+invocation.getAttachment(ELAPSED));
            System.out.println("平均响应时间==="+totalElapsed/total);*/
            result.setAttachment(PROVIDER_STATUS_PARAM, gson.toJson(providerStatus));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


}
