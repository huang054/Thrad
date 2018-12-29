package com.example.threadDemo.CyclicBarrier;

import java.util.concurrent.*;
import java.util.concurrent.locks.LockSupport;

public class CyclicBarrierTest {

    private final int mCount = 5;
    private final CyclicBarrier mBarrier;
    ExecutorService mExecutor;

    class BarrierAction implements Runnable
    {
        @Override
        public void run()
        {
            // TODO Auto-generated method stub
            System.out.println("所有线程都已经完成任务,计数达到预设值");
            //mBarrier.reset();//恢复到初始化状态

        }
    }

    CyclicBarrierTest()
    {
        //初始化CyclicBarrier
        mBarrier = new CyclicBarrier(mCount, new BarrierAction());
        mExecutor = Executors.newFixedThreadPool(mCount);

        for (int i = 0; i < mCount; i++)
        {
            //启动工作线程
            mExecutor.execute(new Walker(mBarrier, i));
        }
    }
}

//工作线程
class Walker implements Runnable
{
    private final CyclicBarrier mBarrier;
    private final int mThreadIndex;

    Walker(final CyclicBarrier barrier, final int threadIndex)
    {
        mBarrier = barrier;
        mThreadIndex = threadIndex;
    }

    @Override
    public void run()
    {
        // TODO Auto-generated method stub
        System.out.println("Thread " + mThreadIndex + " is running...");
        // 执行任务
        try
        {
            TimeUnit.MILLISECONDS.sleep(5000);


            // do task
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        // 完成任务以后，等待其他线程完成任务
        try
        {
            mBarrier.await();
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        catch (BrokenBarrierException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 其他线程任务都完成以后，阻塞解除，可以继续接下来的任务
        System.out.println("Thread " + mThreadIndex + " do something else");
    }

}




