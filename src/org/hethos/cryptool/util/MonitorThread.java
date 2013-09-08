package org.hethos.cryptool.util;

import java.util.Date;
import java.util.concurrent.ThreadPoolExecutor;

import javax.swing.SwingUtilities;

import org.hethos.cryptool.ui.SwingUi;

public class MonitorThread implements Runnable
{
    final ThreadPoolExecutor executor;
    
    private int seconds;
    private SwingUi Ui;
    private CrackThread CrackThread;
    
    private boolean run=true;

    public MonitorThread(ThreadPoolExecutor executor, int delay,SwingUi Ui,Long blockCount,CrackThread crackThread)
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
        	
        	SwingUtilities.invokeLater(new Runnable() {
        		 public void run() {
        		      // Here, we can safely update the GUI
        		      // because we'll be called from the
        		      // event dispatch thread
        			 /*Ui.Log(
    			            String.format("[monitor] [%d/%d] Active: %d, Completed: %d, Task: %d, isShutdown: %s, isTerminated: %s",
    			                executor.getPoolSize(),
    			                executor.getCorePoolSize(),
    			                executor.getActiveCount(),
    			                executor.getCompletedTaskCount(),
    			                executor.getTaskCount(),
    			                executor.isShutdown(),
    			                executor.isTerminated()));*/
            		 CrackThread.logProgress();
        		    }
        	});
	        
	        try {
	            Thread.sleep(seconds*1000);
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
        }
            
    }
}
