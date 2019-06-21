package com.aliware.tianchi;

import org.apache.dubbo.rpc.listener.CallbackListener;
import org.apache.dubbo.rpc.service.CallbackService;

import java.util.Date;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author daofeng.xjf
 * <p>
 * 服务端回调服务
 * 可选接口
 * 用户可以基于此服务，实现服务端向客户端动态推送的功能
 */
public class CallbackServiceImpl implements CallbackService {

    public CallbackServiceImpl() {
        try {
            listeners.take().receiveServerMsg(System.getProperty("quota") + " " + new Date().toString());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private Timer timer = new Timer();

    /**
     * key: listener type
     * value: callback listener
     */
    ArrayBlockingQueue<CallbackListener> listeners = new ArrayBlockingQueue<CallbackListener>(3);

    @Override
    public void addListener(String key, CallbackListener listener) {
        try {
            listeners.put(listener);

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        listener.receiveServerMsg(new Date().toString()); // send notification for change
    }
}
