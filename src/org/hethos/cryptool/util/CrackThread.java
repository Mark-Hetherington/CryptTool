package org.hethos.cryptool.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.SwingUtilities;
import javax.xml.bind.DatatypeConverter;

import org.hethos.cryptool.ui.SwingUi;

public class CrackThread implements Runnable {
   
	private SwingUi Ui;
	byte[] Cipher;
	Integer Threads;
    private boolean run=true;
    private long keyCount;
    private long blockCount;
    private long start;
    private long count;
    private short keybytes;
    private short blocksizebytes;

    public CrackThread(SwingUi Ui, String Cipher, Integer Threads, short keybytes, short blocksizebytes)
    {        
        this.Ui = Ui;
        this.Cipher = DatatypeConverter.parseHexBinary(Cipher);
        this.Threads = Threads;
        start = new Date().getTime();
        this.keybytes=keybytes;
        this.blocksizebytes = blocksizebytes;
        this.keyCount=(long) Math.pow(2,(keybytes*8));
        this.blockCount=(long) Math.pow(2,(keybytes-blocksizebytes)*8);
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
        MonitorThread monitor = new MonitorThread(executorPool, 3, Ui, blockCount,this);
        Thread monitorThread = new Thread(monitor);
        monitorThread.start();
        count =0;
        genKeys(new byte[]{},executorPool);
		//ExecutorService executor = Executors.newFixedThreadPool((Integer)sThreads.getValue());
        
        try {
        	while(executorPool.getActiveCount()>0)
				Thread.sleep(1000);
	        //shut down the pool
	        executorPool.shutdown();
	        //shut down the monitor thread
	        Thread.sleep(5000);
	        monitor.shutdown();
	        Ui.Log("Finished all threads");
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}            
    }
    public void genKeys(byte[] key,ThreadPoolExecutor executorPool){
    	if (key.length == keybytes-blocksizebytes)
    	{
    		  Runnable worker = new WorkerThread(Cipher,key, this,keybytes);
		      executorPool.execute(worker);
		      count++;
		} else {
    		byte[] tempKey= Arrays.copyOf(key, key.length +1);
    		for (int i=0;i<256 && this.run;i++){
    			tempKey[key.length]=(byte)i;
    			genKeys(tempKey, executorPool);
    		}
    	}
    	
    }
    public void foundKey(final byte[] Key) {
		SwingUtilities.invokeLater(new Runnable() {
   		 public void run() {
   		      // Here, we can safely update the GUI
   		      // because we'll be called from the
   		      // event dispatch thread
   			 
   			 Ui.Log("key found:"+ DatatypeConverter.printHexBinary(Key));
   		 }
	   	});
    }
	public void logProgress(){
		final double percentProgress =count /blockCount;
		long seconds = ((new Date().getTime()-start)/1000);
		final long keysPerSecond;
		if (seconds > 0) {keysPerSecond = (long) (count*Math.pow(2, blocksizebytes*8)/seconds);} else {keysPerSecond =0;}
		long eta;
		if (keysPerSecond > 0) {eta = (long) (((blockCount-count)*Math.pow(2, blocksizebytes*8))/keysPerSecond);} else {eta=0;}
		System.out.println(String.format("%f, %d Keys/s, runtime: %ds, ETA %ds",percentProgress,keysPerSecond,seconds,eta));
		SwingUtilities.invokeLater(new Runnable() {
   		 public void run() {
   		      // Here, we can safely update the GUI
   		      // because we'll be called from the
   		      // event dispatch thread
   			 Ui.CrackProgress(percentProgress, keysPerSecond);
   		    }
   	});
	}
}
