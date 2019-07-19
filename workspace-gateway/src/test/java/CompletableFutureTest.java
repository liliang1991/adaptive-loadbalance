import com.aliware.tianchi.UserLoadBalance;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.Invoker;
import org.apache.dubbo.rpc.Result;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;

public class CompletableFutureTest {
    static void timeConsumingOperation(){
        try
        {
            Thread.sleep(5000);
        }
        catch (Exception e){
            e.printStackTrace();
        }


    }
    public static void main(String[] args)throws Exception {
        long l = System.currentTimeMillis();

        CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() ->
        {

            System.out.println("执行耗时操作...");

            timeConsumingOperation();
            return 100;
        });


        completableFuture.whenComplete((result, e) ->
        {
            System.out.println("结果：" + result);
        });
        completableFuture.thenAcceptAsync(r -> doPostProcess(r));
      /*  if(completableFuture.isDone()) {
            completableFuture.get();
        }*/
        //CompletableFuture.allOf(completableFuture).get();

        System.out.println("主线程运算耗时:" + (System.currentTimeMillis() - l) + " ms");
        System.out.println(">>>>>>>>>");
        new CountDownLatch(1).await();
    }
    public static void doPostProcess(int num) {
        try {
            System.out.println("回调"+(num+10));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
