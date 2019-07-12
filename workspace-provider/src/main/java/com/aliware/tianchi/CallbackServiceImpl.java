package com.aliware.tianchi;
import com.google.gson.Gson;
import org.apache.dubbo.config.ProtocolConfig;
import org.apache.dubbo.config.context.ConfigManager;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.listener.CallbackListener;
import org.apache.dubbo.rpc.service.CallbackService;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

import com.aliware.tianchi.TestRequestLimiter.ProviderStatus;

/**
 * @author daofeng.xjf
 * <p>
 * 服务端回调服务
 * 可选接口
 * 用户可以基于此服务，实现服务端向客户端动态推送的功能
 */
public class CallbackServiceImpl implements CallbackService {
    Map<String, ProtocolConfig> map = ConfigManager.getInstance().getProtocols();
    Invocation invocation = RpcContext.getContext().getInvocation();
    public static Gson gson = new Gson();

    public CallbackServiceImpl() {

        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (!listeners.isEmpty()) {
                    for (Map.Entry<String, CallbackListener> entry : listeners.entrySet()) {
                        try {
                            entry.getValue().receiveServerMsg(getProvoderStatus());
                        } catch (Throwable t1) {
                            System.out.println(t1.getMessage() + "exceipnt=====");
                            listeners.remove(entry.getKey());
                        }
                    }
                }
            }
        }, 0, 0);

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
            listeners.put(key, listener);
            listener.receiveServerMsg(getProvoderStatus());

            getProvoderStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public String getProvoderStatus(){
        ProviderStatus providerStatus = TestRequestLimiter.providerStatus;
        String jsonString = gson.toJson(providerStatus);
        return jsonString; // send notification for change
    }
}
