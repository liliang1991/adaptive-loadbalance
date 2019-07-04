package com.aliware.tianchi;

import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.context.ConfigManager;
import org.apache.dubbo.remoting.exchange.Request;
import org.apache.dubbo.remoting.transport.RequestLimiter;
import org.apache.dubbo.rpc.Invocation;

import java.util.Map;

/**
 * @author daofeng.xjf
 * <p>
 * 服务端限流
 * 可选接口
 * 在提交给后端线程池之前的扩展，可以用于服务端控制拒绝请求
 */
public class TestRequestLimiter implements RequestLimiter {

    /**
     * @param request         服务请求
     * @param activeTaskCount 服务端对应线程池的活跃线程数
     * @return false 不提交给服务端业务线程池直接返回，客户端可以在 Filter 中捕获 RpcException
     * true 不限流
     */
    public static final String POOL_CORE_COUNT = "active_thread";

    @Override
    public boolean tryAcquire(Request request, int activeTaskCount) {
        //System.out.println("quata======"+System.getProperty("quota")+activeTaskCount);
      //  System.out.println("v====="+request.getData());
        Invocation invocation = (Invocation) request.getData();

        invocation.getAttachments().put(POOL_CORE_COUNT, String.valueOf(activeTaskCount));
        return true;
    }

}
