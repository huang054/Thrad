package com.imooc.interview.questions.java.concurrency.barrier;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MyCountDownLatch {

    private int count ;

    private Lock lock = new ReentrantLock();
    private Condition condition = lock.newCondition();

    public MyCountDownLatch(int count){
        this.count=count;
    }

    public void await() throws InterruptedException {
        lock.lock();
       if (Thread.interrupted()){
           throw new InterruptedException();
       }
            try {
                while (count>0){
                condition.await();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }finally {

            lock.unlock();
        }
    }
    private static void action() {
        System.out.printf("线程[%s] 正在执行...\n", Thread.currentThread().getName());  // 2
    }
    public void  count(){
        lock.lock();
            try{
                if(count<1){
                    return;
                }
                count--;
                if(count<1){
                   condition.signalAll();
                }
            }finally {
                lock.unlock();
            }
    }

    public static void main(String[] args) throws InterruptedException {
  MyCountDownLatch latch = new MyCountDownLatch(5);

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 5; i++) {
            executorService.submit(() -> {
                action();
                latch.count(); // -1
            });
        }

        // 等待完成
        // 当计数 > 0，会被阻塞
        latch.await();

        System.out.println("Done");

        // 关闭线程池
        executorService.shutdown();
    }
}
