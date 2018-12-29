package com.example.threadDemo.testLockSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.LockSupport;

public class TestLockSupport {

    private static SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss:SSS");

    static class TestThread implements Runnable {
        @Override
        public void run() {
            System.out.println("1) " + formatter.format(new Date()) + " : " + Thread.currentThread().getName() + " 进来了");
            System.out.println("2) " + formatter.format(new Date()) + " : " + "执行普通  park() 让线程挂起 ");
            LockSupport.park();

            System.out.println("4) " + formatter.format(new Date()) + " : " + "执行 park(blocker) 为主线程传递参数 ");
            String blocker = "我是 blocker";
            LockSupport.park(blocker);

            System.out.println("7) " + formatter.format(new Date()) + " : " + "执行 parkNanos(nanos) 让线程沉睡 2 秒 ");
            LockSupport.parkNanos(2000000000);

            String blocker1 = "我是 blocker1";
            System.out.println("8) " + formatter.format(new Date()) + " : " + "执行 parkNanos(blocker, nanos) 让线程沉睡 2 秒，并为主线程传递参数");
            LockSupport.parkNanos(blocker1, 2000000000);

            System.out.println("10) " + formatter.format(new Date()) + " : " + "执行 parkUntil(deadline) 让线程沉睡在未来是一个时间戳醒来");
            LockSupport.parkUntil(System.currentTimeMillis() + 2000);

            System.out.println("11) " + formatter.format(new Date()) + " : " + "执行 parkUntil(blocker, deadline) 让线程沉睡在未来是一个时间戳醒来，并为主线程传递参数");
            String blocker2 = "我是 blocker2";
            LockSupport.parkUntil(blocker2, System.currentTimeMillis() + 2000);

            System.out.println("13) " + formatter.format(new Date()) + " : " + "定时 10 秒后重新启动，测试锁中断，中断时会立刻往下执行");
            LockSupport.parkUntil(System.currentTimeMillis() + 10000);

            System.out.println("15) " + formatter.format(new Date()) + " ==== 所有步骤执行完毕 ==== ");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new TestThread(), "线程 1");
        t1.start();

        Thread.sleep(2000);
        System.out.println("3) " + formatter.format(new Date()) + " : " + "执行 unpark(t1)  t1 重新运行 ");
        LockSupport.unpark(t1);

        Thread.sleep(2000);
        String blocker = (String) LockSupport.getBlocker(t1);
        System.out.println("5) " + formatter.format(new Date()) + " : " + "执行 getBlocker(t1) 从 t1  中获取到 blocker = " + blocker);
        System.out.println("6) " + formatter.format(new Date()) + " : " + "执行 unpark(t1)  t1 重新运行");
        LockSupport.unpark(t1);
        Thread.sleep(3000);
        String blocker1 = (String) LockSupport.getBlocker(t1);
        System.out.println("9) " + formatter.format(new Date()) + " : " + "执行 getBlocker(t1) 从 t1  中获取到 blocker1 = " + blocker1);

        Thread.sleep(5000);
        String blocker2 = (String) LockSupport.getBlocker(t1);
        System.out.println("12) " + formatter.format(new Date()) + " : " + "执行 getBlocker(t1) 从 t1  中获取到 blocker2 = " + blocker2);

        Thread.sleep(1000);
        System.out.println("14) " + formatter.format(new Date()) + " : " + "开始中断锁");
        t1.interrupt();
    }
}
