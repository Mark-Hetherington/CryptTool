package org.hethos.cryptool.util;

import java.util.Date;
import java.util.concurrent.ThreadPoolExecutor;

import org.hethos.cryptool.ui.SwingUi;

public class MonitorThread implements Runnable
{
    private ThreadPoolExecutor executor;
    
    private int seconds;
    private SwingUi Ui;
    private CrackThread CrackThread;
    
    private boolean run=true;

    public MonitorThread(ThreadPoolExecutor executor, int delay,SwingUi Ui,Long totalCount,CrackThread crackThread)
    {
        this.executor = executor;
        this.seconds=delay;
        this.Ui = Ui;
        this.CrackThread=crackThread;
    }
    
    public void shutdown(){
        this.run=false;
    }

    @Override
    public void run()
    {
        while(run){
	        this.Ui.Log(
	            String.format("[monitor] [%d/%d] Active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",
	                this.executor.getPoolSize(),
	                this.executor.getCorePoolSize(),
	                this.executor.getActiveCount(),
	                this.executor.getCompletedTaskCount(),
	                this.executor.getTaskCount(),
	                this.executor.isShutdown(),
	                this.executor.isTerminated()));
	        try {
	            Thread.sleep(seconds*1000);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
        }
            
    }
}
