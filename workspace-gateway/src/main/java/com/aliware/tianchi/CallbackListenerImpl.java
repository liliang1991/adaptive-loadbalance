package com.aliware.tianchi;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.dubbo.rpc.AsyncContext;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.listener.CallbackListener;

import java.util.Map;

/**
 * @author daofeng.xjf
 *
 * 客户端监听器
 * 可选接口
 * 用户可以基于获取获取服务端的推送信息，与 CallbackService 搭配使用
 *
 */
public class CallbackListenerImpl implements CallbackListener {
    public static Gson gson = new Gson();

    @Override
     public void receiveServerMsg(String jsonStr) {
    /*   int active_thread_count=Integer.parseInt(msg.split("\t")[0]);
       int thread_count=Integer.parseInt(msg.split("\t")[1]);
        UserLoadBalance.add(result, invoker, invocation, time);*/
 /*       JsonObject json = new JsonParser().parse(jsonStr).getAsJsonObject();


        JsonObject obj= gson.fromJson(jsonStr, JsonObject.class);
         json.getas
        int active_thread_count=json.getAsString("activeCount");
        int thread_count=json.getInteger("threadCount");
        String host=json.getString("host");
        UserLoadBalance.add(host,active_thread_count,thread_count);*/
    }
}
