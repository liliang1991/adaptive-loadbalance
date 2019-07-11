package com.aliware.tianchi;

import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.context.ConfigManager;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.listener.CallbackListener;
import org.apache.dubbo.rpc.service.CallbackService;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author daofeng.xjf
 * <p>
 * 服务端回调服务
 * 可选接口
 * 用户可以基于此服务，实现服务端向客户端动态推送的功能
 */
public class CallbackServiceImpl implements CallbackService {
    Map<String, ProtocolConfig> map = ConfigManager.getInstance().getProtocols();
    Invocation invocation=RpcContext.getContext().getInvocation();

    public CallbackServiceImpl() {

        timer.schedule(new TimerTask() {

            @Override
            public void run() {

      /*          System.out.println("最大线程数===="+map.get("dubbo").getThreads());
                System.out.println("核心线程数====="+map.get("dubbo").getCorethreads());*/
                if (!listeners.isEmpty()) {
                    for (Map.Entry<String, CallbackListener> entry : listeners.entrySet()) {
                        try {
                            String pool_core_count = invocation.getAttachment(POOL_CORE_COUNT);
                            System.out.println("pool_core_count==="+pool_core_count);
                            entry.getValue().receiveServerMsg(pool_core_count);
                        } catch (Throwable t1) {
                            System.out.println(t1.getMessage() + "exceipnt=====");
                            listeners.remove(entry.getKey());
                        }
                    }
                }
            }
        }, 0, 5000);

    }

    private Timer timer = new Timer();

    /**
     * key: listener type
     * value: callback listener
     */
    private final Map<String, CallbackListener> listeners = new ConcurrentHashMap<>();
    public static final String POOL_CORE_COUNT = "active_thread";

    @Override
    public void addListener(String key, CallbackListener listener) {
      try {
          System.out.println("key======"+key);
          String pool_core_count = invocation.getAttachment(POOL_CORE_COUNT);
          listeners.put(key, listener);
          listener.receiveServerMsg(pool_core_count); // send notification for change
      }catch (Exception e){
          e.printStackTrace();
      }

    }
}
