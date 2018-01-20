package zookeeper;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.GetDataBuilder;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * Created by apple on 2017/11/15.
 */
public class Lock {
    static final InterProcessMutex lock;
    static long t1;

    static {
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString("localhost:2181")
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();
        lock = new InterProcessMutex(client, "/orderNo");
        System.out.println("开始");
        t1 = System.currentTimeMillis();
    }

    public static void main(String[] args) {
        for (int i = 0; i < 3000; i++) {
            getOrderNo();
        }
        System.out.println("耗时：" + (System.currentTimeMillis() - t1));
    }

    private static void getOrderNo() {
        try {
            lock.acquire();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
            String orderNo = sdf.format(new Date());
            System.out.println("订单号为：" + orderNo);
            lock.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
