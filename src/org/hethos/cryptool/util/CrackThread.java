package org.hethos.cryptool.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.hethos.cryptool.ui.SwingUi;

public class CrackThread implements Runnable {
   
	private SwingUi Ui;
	byte[] Cipher;
	Integer Threads;
    private boolean run=true;
    private long totalCount = 1099511627776L;// 1099511627776 = 2^40
    private long start;
    private long i;

    public CrackThread(SwingUi Ui, String Cipher, Integer Threads)
    {        
        this.Ui = Ui;
        try {
			this.Cipher = Base64.decode(Cipher);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        this.Threads = Threads;
        start = new Date().getTime();        
    }
    
    public void shutdown(){
        this.run=false;
    }

    @Override
    public void run()
    {
      //creating the ThreadPoolExecutor
        ThreadPoolExecutor executorPool = new ThreadPoolExecutor(Threads, Threads*10, 10, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2), Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
        //start the monitoring thread
        //MonitorThread monitor = new MonitorThread(executorPool, 3, Ui, 1099511627776L,this);
        //Thread monitorThread = new Thread(monitor);
        //monitorThread.start();
		//ExecutorService executor = Executors.newFixedThreadPool((Integer)sThreads.getValue());
        for (i = (long) 0; i < totalCount && this.run; i++) { 
        	
        	ByteArrayOutputStream bos = new ByteArrayOutputStream();  
        	DataOutputStream dos = new DataOutputStream(bos);  
        	try {
				dos.writeLong(i);
	        	dos.flush();
	        	byte[] data = bos.toByteArray();  
	            Runnable worker = new WorkerThread(Cipher,data, Ui);
		        executorPool.execute(worker);
	        	
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        	if (i % 200000 ==0)
        	logProgress();
          }
        try {
        	while(executorPool.getActiveCount()>0)
				Thread.sleep(1000);
	        //shut down the pool
	        executorPool.shutdown();
	        //shut down the monitor thread
	        Thread.sleep(5000);
	        //monitor.shutdown();
	        Ui.Log("Finished all threads");
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}            
    }
	public void logProgress(){
		double percentProgress = i/totalCount;
		long seconds = ((new Date().getTime()-start)/1000);
		long keysPerSecond;
		if (seconds > 0) {keysPerSecond = (long) (i/seconds);} else {keysPerSecond =0;}
		long eta;
		if (seconds > 0) {eta = (totalCount-i)/keysPerSecond;} else {eta=0;}
		System.out.println(String.format("%f, %d Keys/s, runtime: %ds, ETA %ds",percentProgress,keysPerSecond,seconds,eta));
	}
}
