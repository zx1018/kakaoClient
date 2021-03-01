package com.eai.FileTransfer.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class FileCoder {

	private static final String algorithm = "AES";
	private static final String transformation = algorithm+"/OFB/PKCS5Padding";
	
	private Key key;
	private IvParameterSpec ivParameterSpec;
	
	public FileCoder(String keyDatas) {
		byte[] keyData = ByteUtils.toBytes(keyDatas, 16 ); //키 크기 128비트
		try {
			Key secretKey = generateKey(algorithm, keyData);
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SecretKeySpec keySpec = new SecretKeySpec(keyData, algorithm);
		key = keySpec; 
    	ivParameterSpec = new IvParameterSpec(keyData);   	
		
	}
	
	public Key generateKey(String algorithm, byte[] keyData) throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException{

        Key secretKey = null;
    	String upper = algorithm.toUpperCase();
        
        if ("DES".equals(upper)) {
        	KeySpec keySpec = new DESKeySpec(keyData);
        	SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithm);
        	SecretKey resultKey = secretKeyFactory.generateSecret(keySpec);
        	secretKey = resultKey;
        } else if ("DESede".equals(upper) || "TripleDES".equals(upper)) {
        	KeySpec keySpec = new DESedeKeySpec(keyData);
        	SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(algorithm);
        	SecretKey resultKey = secretKeyFactory.generateSecret(keySpec);
        	secretKey = resultKey;
       	} else {
       		SecretKeySpec keySpec = new SecretKeySpec(keyData, algorithm);
       		secretKey = keySpec;
       	}
		return secretKey;
    }    

	public FileCoder(Key key){
		this.key = key;
	}
	
	/**
     * 원본 파일을 암호화해서 대상 파일을 만든다.
     * @param source 원본 파일
     * @param dest 대상 파일
     * @throws Exception
     */
	public void encrypt(File source, File dest) throws Exception{
		crypt(Cipher.ENCRYPT_MODE, source, dest);
	}
	
	/**
     * 원본 파일을 복호화해서 대상 파일을 만든다.
     * @param source 원본 파일
     * @param dest 대상 파일
     * @throws Exception
     */
	public void decrypt(File source, File dest) throws Exception {
        crypt(Cipher.DECRYPT_MODE, source, dest);
    }
	
	/**
     * 원본 파일을 읽어와 암호화하여 대상 파일을 만든다.
     * @param source 원본 파일
     * @param dest 대상 파일
     * @throws Exception
     */
	public void encrypt(InputStream is, OutputStream os) throws Exception{
		crypt(Cipher.ENCRYPT_MODE, is, os);
	}
	
	/**
     * 원본 파일을 읽어와 복호화하여 대상 파일을 만든다.
     * @param source 원본 파일
     * @param dest 대상 파일
     * @throws Exception
     */
	public void decrypt(InputStream is, OutputStream os) throws Exception{
		crypt(Cipher.DECRYPT_MODE, is, os);
	}

	
	private void crypt(int mode, InputStream is, OutputStream os) throws Exception{
		Cipher cipher = Cipher.getInstance(transformation);
		cipher.init(mode, key , ivParameterSpec);
		
		InputStream input = is;
		OutputStream output = os;
		
		byte[] buffer = new byte[8];
		int read = -1;
		while((read = input.read(buffer)) != -1){
			output.write(cipher.update(buffer, 0, read));
		}
		output.write(cipher.doFinal());
	}
	
	
	private void crypt(int mode, File source, File dest) throws Exception{
		Cipher cipher = Cipher.getInstance(transformation);
		cipher.init(mode, key , ivParameterSpec);
		InputStream input = null;
		OutputStream output = null;
		
		try{
			input = new BufferedInputStream(new FileInputStream(source));
			output = new BufferedOutputStream(new FileOutputStream(dest));
			byte[] buffer = new byte[8];
			int read = -1;
			while((read = input.read(buffer)) != -1){
				output.write(cipher.update(buffer, 0, read));
			}
			output.write(cipher.doFinal());
		}finally{
			if(output != null){
				try{output.close();}catch(IOException e){}
			}
			if(input != null){
				try{input.close();}catch(IOException e){}
			}
		}
	}
}
