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
        URL url = invoker.getUrl();
        String methodName = invocation.getMethodName();
        long begin = System.currentTimeMillis();
        boolean isException = false;
        try {

            RpcStatus count = RpcStatus.getStatus(url, methodName);
            int maxActive = count.getActive();
            if (maxActive >= map.get("dubbo").getThreads()) {
                throw new RpcException("provider Thread pool is EXHAUSTED " + maxActive);
            }
            RpcStatus.beginCount(url,methodName);
            Result result= invoker.invoke(invocation);
             result.setAttachment(PROVIDER_CORE_COUNT, maxActive + "\t" + map.get("dubbo").getThreads());
            return result;
        } catch (Exception e) {
            isException = true;
            e.printStackTrace();
        } finally {
            RpcStatus.endCount(url, methodName, System.currentTimeMillis() - begin, isException);
        }
        return null;
    }
    public static final String PROVIDER_CORE_COUNT = "provider_thread";



}
