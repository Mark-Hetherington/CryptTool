package org.hethos.cryptool.util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;


public class crypto {
	//Strings are base64 data
	public static byte[] Encrypt(byte[] ClearData, byte[] KeyData) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		SecretKeySpec KS = new SecretKeySpec(KeyData, "Blowfish");
		Cipher cipher = Cipher.getInstance("Blowfish");
		cipher.init(Cipher.ENCRYPT_MODE, KS);
		return cipher.doFinal(ClearData);
	}
	
	public static byte[] Decrypt(byte[] CipherData, byte[] KeyData) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		SecretKeySpec KS = new SecretKeySpec(KeyData, "Blowfish");
		Cipher cipher = Cipher.getInstance("Blowfish");
		cipher.init(Cipher.DECRYPT_MODE, KS);
		return cipher.doFinal(CipherData);
	}
	public static byte[] GenKey(String algorithm, int keySize) throws NoSuchAlgorithmException{
		 // create a key generator based upon the Blowfish cipher
	    KeyGenerator keygenerator;
			keygenerator = KeyGenerator.getInstance(algorithm);
			keygenerator.init(keySize);
			// create a key
		    SecretKey secretkey = keygenerator.generateKey();
			
		    return secretkey.getEncoded();

	}
}
