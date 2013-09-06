package org.hethos.cryptool.util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import org.hethos.cryptool.ui.SwingUi;

public class WorkerThread implements Runnable {
    
    private byte[] Cipher;
    private byte[] Key;
    private SwingUi Ui;
    
    public WorkerThread(byte[] Cipher, byte[] Key, SwingUi Ui){
        this.Cipher=Cipher;
        this.Key=Key;
        this.Ui=Ui;
    }

    @Override
    public void run() {
        //System.out.println(Thread.currentThread().getName()+" Start. Command = "+command);
        processCommand();
        //System.out.println(Thread.currentThread().getName()+" End.");
    }

    private void processCommand() {
        try {
			byte[] result = crypto.Decrypt(this.Cipher, this.Key);
			//Ui.Log("Found key:"+Base64.encodeBytes(this.Key)+" Decoded to (base64):"+Base64.encodeBytes(result));
			//Ui.Log("Decoded to (base64):"+Base64.encodeBytes(result));
			//com.sun.org.apache.xml.internal.security.Init.init();
			//Ui.Log("Decoded to (plain):"+new String(result));
			// TODO if we are sure this is the key then stop processing thread pool.
			if (new String(result)=="Hello") {
				System.out.println("key found:"+Base64.encodeBytes(this.Key));
			}
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			// Do nothing, not a valid key
		}
    }

    @Override
    public String toString(){
        return Base64.encodeBytes(this.Key);
    }
}