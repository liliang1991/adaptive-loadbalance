package com.aliware.tianchi;


import com.aliware.tianchi.status.ProviderStatus;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.rpc.AsyncContext;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.listener.CallbackListener;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author daofeng.xjf
 * <p>
 * 客户端监听器
 * 可选接口
 * 用户可以基于获取获取服务端的推送信息，与 CallbackService 搭配使用
 */
public class CallbackListenerImpl implements CallbackListener {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Override
    public void receiveServerMsg(String jsonStr) {
    /*   int active_thread_count=Integer.parseInt(msg.split("\t")[0]);
       int thread_count=Integer.parseInt(msg.split("\t")[1]);
        UserLoadBalance.add(result, invoker, invocation, time);*/
       /* completableFuture = CompletableFuture.runAsync(() ->
        {
            //this.sleep(3000);
            ProviderStatus providerStatus = gson.fromJson(jsonStr, ProviderStatus.class);
   *//*     long startTime=providerStatus.getStartTime();
        long stopTime=System.currentTimeMillis();

        logger.info("provider 状态调用时间为"+(stopTime-startTime)+"\t"+active_thread_count+"\t"+host);*//*

        //    UserLoadBalance.add(providerStatus);

        });*/

    }
}
