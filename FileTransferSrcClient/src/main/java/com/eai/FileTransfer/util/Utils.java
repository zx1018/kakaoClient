package com.eai.FileTransfer.util;

import java.io.File;
import java.io.FileInputStream;
import java.security.MessageDigest;


/**********************************
 * 
 * @author IWJ
 * Utils 함수 
 *
 ***********************************/
public class Utils {

	/* 파일의 해시 정보를 추출한다. */
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
	
	/* 파일의 서버명을 리턴한다. ( 원래대로라면 호스트명을 써야 하지만 지금은 단일 서버에서 테스트 하므로.. ) */
	public static String getServerName() {
		return "server1";
	}

}
