package com.aliware.tianchi.monitor.utils;

import javax.management.MBeanServerConnection;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;

public class ProviderUtils {


    public static void getMemory() {
        try {
            String ip = "provider-small";
            int port = 20880;
            String jmxURL = "service:jmx:rmi:///jndi/rmi://" + ip + ":" + port + "/jmxrmi";

            /*
             * host: 远程机器的ip地址
             * port: 远程java进程运行的jmxremote端口
             */

            JMXServiceURL serviceURL = new JMXServiceURL(jmxURL);
            JMXConnector conn = JMXConnectorFactory.connect(serviceURL);
            MBeanServerConnection mbs = conn.getMBeanServerConnection();

            //获取远程memorymxbean
            MemoryMXBean memBean = ManagementFactory.newPlatformMXBeanProxy
                    (mbs, ManagementFactory.MEMORY_MXBEAN_NAME, MemoryMXBean.class);
            //获取远程opretingsystemmxbean
          OperatingSystemMXBean opMXbean =
                    ManagementFactory.newPlatformMXBeanProxy(mbs,
                            ManagementFactory.OPERATING_SYSTEM_MXBEAN_NAME, OperatingSystemMXBean.class);
            MemoryUsage heap = memBean.getHeapMemoryUsage();
            MemoryUsage nonHeap = memBean.getNonHeapMemoryUsage();
            long heapSizeUsed = heap.getUsed();//堆使用的大小
            long nonHeapSizeUsed = nonHeap.getUsed();
            long heapCommitedSize = heap.getCommitted();
            long nonHeapCommitedSize = nonHeap.getCommitted();
            System.out.println("heapSizeUsed===="+heapSizeUsed);
        }catch (Exception e){
            e.printStackTrace();
        }
        }

    public static void main(String[] args) {
        System.out.println(">>>>>");
        getMemory();
    }

}
