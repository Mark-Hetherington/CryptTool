package org.hethos.cryptool.util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.SwingUtilities;

import org.hethos.cryptool.ui.SwingUi;

public class WorkerThread implements Runnable {
    
    private byte[] Cipher;
    private byte[] Key;
    private CrackThread Ui;
    private short keyBytes;
    
    public WorkerThread(byte[] Cipher, byte[] Key, CrackThread Ui, short keyBytes){
        this.Cipher=Cipher;
        this.Key=Key;
        this.Ui=Ui;
        this.keyBytes = keyBytes;
    }

    @Override
    public void run() {
        //System.out.println(Thread.currentThread().getName()+" Start. Command = "+command);
        processCommand();
        //System.out.println(Thread.currentThread().getName()+" End.");
    }

    private void processCommand() {  	
    	byte[] tempKey= Arrays.copyOf(Key, Key.length +1);
    	if (tempKey.length >= keyBytes) {
    		for (int i = 0;i<256;i++){
    	        try {
    	        	tempKey[Key.length] = (byte) i;
    	        	
    				byte[] result = crypto.Decrypt(Cipher, tempKey);
    				if (IsPlainText(result)) {
    					Ui.foundKey( Arrays.copyOf(tempKey, tempKey.length));
    				}
    			} catch (InvalidKeyException | NoSuchAlgorithmException
    					| NoSuchPaddingException | IllegalBlockSizeException
    					| BadPaddingException e) {
    				// TODO Auto-generated catch block
    				//e.printStackTrace();
    				// Do nothing, not a valid key
    			}
        	}
    	} else {
    		for (int i = 0;i<256;i++){
	        	tempKey[Key.length] = (byte) i;
    			WorkerThread wThread = new WorkerThread(Cipher,tempKey,Ui,keyBytes);
    			wThread.run();
    		}
         	
     	}
    }
    public boolean IsPlainText(byte[] result){
    	//TODO this will be a lot more complex
    	return (new String(result)).equals("Hello");
    }
    @Override
    public String toString(){
        return Base64.encodeBytes(this.Key);
    }
}