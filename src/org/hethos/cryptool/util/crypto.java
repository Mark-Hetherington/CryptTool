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
	public static String Encrypt(String ClearText, String Key) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException{
		return Base64.encodeBytes(Encrypt(Base64.decode(ClearText),Base64.decode(Key)));	
	}
	public static String Encrypt(byte[] ClearData, String Key) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException{
		return Base64.encodeBytes(Encrypt(ClearData,Base64.decode(Key)));
	}
	public static byte[] Encrypt(byte[] ClearData, byte[] KeyData) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		SecretKeySpec KS = new SecretKeySpec(KeyData, "Blowfish");
		Cipher cipher = Cipher.getInstance("Blowfish");
		cipher.init(Cipher.ENCRYPT_MODE, KS);
		return cipher.doFinal(ClearData);
	}
	
	//Strings are base64 data
	public static String Decrypt(String CipherText,String key) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException{
		return Base64.encodeBytes(Decrypt(Base64.decode(CipherText),Base64.decode(key)));	
	}

	public static String Decrypt(byte[] CipherData,String key) throws  InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, IOException{
		return Base64.encodeBytes(Decrypt(CipherData,Base64.decode(key)));
	}
	public static byte[] Decrypt(byte[] CipherData, byte[] KeyData) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		SecretKeySpec KS = new SecretKeySpec(KeyData, "Blowfish");
		Cipher cipher = Cipher.getInstance("Blowfish");
		cipher.init(Cipher.DECRYPT_MODE, KS);
		return cipher.doFinal(CipherData);
	}
	public static String GenKey() throws NoSuchAlgorithmException{
		 // create a key generator based upon the Blowfish cipher
	    KeyGenerator keygenerator;
			keygenerator = KeyGenerator.getInstance("Blowfish");
			keygenerator.init(40);
			// create a key
		    SecretKey secretkey = keygenerator.generateKey();
			
		    return Base64.encodeBytes(secretkey.getEncoded());

	}
	public static byte[] GenKeyRaw() throws NoSuchAlgorithmException{
		 // create a key generator based upon the Blowfish cipher
	    KeyGenerator keygenerator;
			keygenerator = KeyGenerator.getInstance("Blowfish");
			keygenerator.init(40);
			// create a key
		    SecretKey secretkey = keygenerator.generateKey();
			
		    return secretkey.getEncoded();

	}
}
