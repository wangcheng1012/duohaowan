package com.wlj.base.web;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class EnAes implements Encrpt{
	
	 public static final String VIPARA = "0102030405060708";  
	 public static final String bm = "UTF-8";  
	 
	 private Cipher cipher = null;
	 
	 public EnAes(){
		 try {
//			cipher = Cipher.getInstance("AES");
			
//			cipher = Cipher.getInstance("AES/CBC/NoPadding");
//			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
//			cipher = Cipher.getInstance("AES/CBC/ISO10126Padding");
//			cipher = Cipher.getInstance("AES/CFB/NoPadding");
//			cipher = Cipher.getInstance("AES/CFB/PKCS5Padding");
//			cipher = Cipher.getInstance("AES/CFB/ISO10126Padding");
//			cipher = Cipher.getInstance("AES/ECB/NoPadding");
			cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
//			cipher = Cipher.getInstance("AES/ECB/ISO10126Padding");
//			cipher = Cipher.getInstance("AES/OFB/NoPadding");
//			cipher = Cipher.getInstance("AES/OFB/PKCS5Padding");
//			cipher = Cipher.getInstance("AES/OFB/ISO10126Padding");
//			cipher = Cipher.getInstance("AES/PCBC/NoPadding");
//			cipher = Cipher.getInstance("AES/PCBC/PKCS5Padding");
//			cipher = Cipher.getInstance("AES/PCBC/ISO10126Padding");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		}
	 }
	 
	 /**
	  * 
	  * @param cleartext
	  * @param key 16bytes 的key
	  * @return
	  * @throws Exception
	  */
	public String encrypt(String cleartext,String key)
			throws Exception {
		byte[] rawKey = null;
		if(key.length() >= 16){
			rawKey = key.substring(0, 16).getBytes();
		}else{
			rawKey = key.getBytes();
		}
		byte[] result = encrypt(rawKey, cleartext.getBytes());
		return toHex(result);
	}
	/**
	  * 
	  * @param cleartext
	  * @param key 16bytes 的key
	  * @return
	  * @throws Exception
	  */
	public String decrypt(String encrypted,String key)
			throws Exception {
		byte[] rawKey = null;
		if(key.length() > 16){
			rawKey = key.substring(0, 16).getBytes();
		}else{
			rawKey = key.getBytes();
		}
		byte[] enc = toByte(encrypted);
		byte[] result = decrypt(rawKey, enc);
		return new String(result);
	}

	private byte[] getRawKey(byte[] seed) throws Exception {
		KeyGenerator kgen = KeyGenerator.getInstance("AES");
		SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
		sr.setSeed(seed);
		kgen.init(128, sr); // 192 and 256 bits may not be available
		SecretKey skey = kgen.generateKey();
		byte[] raw = skey.getEncoded();
		return raw;
	}

	private byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
		byte[] encrypted = cipher.doFinal(clear);
		return encrypted;
	}

	private byte[] decrypt(byte[] raw, byte[] encrypted)
			throws Exception {
		SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
		cipher.init(Cipher.DECRYPT_MODE, skeySpec);
		byte[] decrypted = cipher.doFinal(encrypted);
		return decrypted;
	}

	public String toHex(String txt) {
		return toHex(txt.getBytes());
	}

	public String fromHex(String hex) {
		return new String(toByte(hex));
	}

	public byte[] toByte(String hexString) {
		int len = hexString.length() / 2;
		byte[] result = new byte[len];
		for (int i = 0; i < len; i++)
			result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
					16).byteValue();
		return result;
	}

	public String toHex(byte[] buf) {
		if (buf == null)
			return "";
		StringBuilder result = new StringBuilder(2 * buf.length);
		for (int i = 0; i < buf.length; i++) {
			appendHex(result, buf[i]);
		}
		return result.toString();
	}

	private final static String HEX = "0123456789ABCDEF";

	private void appendHex(StringBuilder sb, byte b) {
		sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
	}
	
	public static void main(String[] args) throws Exception { 
		
	}

}