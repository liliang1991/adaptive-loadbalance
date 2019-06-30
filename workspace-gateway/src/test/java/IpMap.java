import java.util.HashMap;

public class IpMap {
    // 待路由的Ip列表，Key代表Ip，Value代表该Ip的权重
    public static HashMap<String, Integer> serverWeightMap =
            new HashMap<String, Integer>();

    static {
        serverWeightMap.put("192.168.1.101", 1);
        // 权重为4
        serverWeightMap.put("192.168.1.102", 2);
        serverWeightMap.put("192.168.1.103", 3);

    }
}
