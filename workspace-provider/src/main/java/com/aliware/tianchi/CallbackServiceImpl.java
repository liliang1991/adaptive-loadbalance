package com.aliware.tianchi;
import com.aliware.tianchi.status.ProviderStatus;
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

/**
 * @author daofeng.xjf
 * <p>
 * 服务端回调服务
 * 可选接口
 * 用户可以基于此服务，实现服务端向客户端动态推送的功能
 */
public class CallbackServiceImpl implements CallbackService {
    public static Gson gson = new Gson();

    public CallbackServiceImpl() {

        timer.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                if (!listeners.isEmpty()) {
                    for (Map.Entry<String, CallbackListener> entry : listeners.entrySet()) {
                        try {
                            if(getProvoderStatus()!=null) {
                                entry.getValue().receiveServerMsg(getProvoderStatus());
                            }
                        } catch (Throwable t1) {
                            listeners.remove(entry.getKey());
                        }
                    }
                }
            }
        }, 0, 100);

    }

    private Timer timer = new Timer();

    /**
     * key: listener type
     * value: callback listener
     */
    private final Map<String, CallbackListener> listeners = new ConcurrentHashMap<>();

    @Override
    public void addListener(String key, CallbackListener listener) {
        try {
            listeners.put(key, listener);
            listener.receiveServerMsg("");
          //  getProvoderStatus()
            //getProvoderStatus();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public String getProvoderStatus(){
        ProviderStatus providerStatus = TestRequestLimiter.providerStatus;
        if(providerStatus.getActiveCount()>=providerStatus.getThreadCount()*0.8){
            providerStatus.setEnabled(0);
        }else {
            providerStatus.setEnabled(1);
        }
        String jsonString = gson.toJson(providerStatus);

        return jsonString; // send notification for change
    }
}
