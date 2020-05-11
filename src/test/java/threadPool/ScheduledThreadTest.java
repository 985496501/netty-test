package threadPool;

import org.junit.Test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 测试jdk的 java.util.concurrent.*
 *
 * @author: Liu Jinyun
 * @date: 2020/5/11/17:47
 */
public class ScheduledThreadTest {


    @Test
    public void test1() {
        ScheduledExecutorService scheduledThreadPool = Executors.newScheduledThreadPool(2);
        scheduledThreadPool.schedule(() -> System.out.println("我在定时任务里面执行....."), 5, TimeUnit.SECONDS);

        scheduledThreadPool.scheduleWithFixedDelay(this::printMessage,
                2, 5, TimeUnit.SECONDS);
        try {
            TimeUnit.SECONDS.sleep(60);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void printMessage() {
        System.out.println("我是周期性任务, 我会定期执行, 直到我被取消");
    }
}
