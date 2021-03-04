package com.eai.FileTransfer.util;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;

public class Utils {

	public static String getFileHsash(File file) {
		
		MessageDigest md;
		try {
			md = MessageDigest.getInstance("SHA-256");
	        FileInputStream fis = new FileInputStream(file);
	        
	        byte[] dataBytes = new byte[1024];
	     
	        int nread = 0; 
	        while ((nread = fis.read(dataBytes)) != -1) {
	          md.update(dataBytes, 0, nread);
	        };
	        byte[] mdbytes = md.digest();
	     
	        StringBuffer sb = new StringBuffer();
	        for (int i = 0; i < mdbytes.length; i++) {
	          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
	        }
	        
	        return sb.toString();
	        
		} catch (Exception e) {
			e.printStackTrace();
			return "x";
		}
	}
	
	public static String getServerName() {
		return "server1";
	}

}
