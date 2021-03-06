package com.eai.FileTransfer.util;

import java.io.File;
import java.io.IOException;
import org.slf4j.LoggerFactory;
import com.eai.FileTransfer.sendReceive.Message;
import ch.qos.logback.classic.Logger;


/**********************************
 * 
 * @author IWJ
 * 파일 정상 수신을 확인 하기 위한 클래스 ( Async 서비스로 실행이 된다. )
 *
 ***********************************/
public class FileTransferFileCheck {

	private static Logger log = (Logger) LoggerFactory.getLogger(FileTransferFileCheck.class);

	public static final int DEFAULT_BUFFER_SIZE = 10000;

	public int check(Message msg) {
		
		// 복호화 처리 
        File src = new File(msg.getDstPath()+"\\"+msg.getDstFileName()+".enc");
        File dest = new File(msg.getDstPath()+"\\"+msg.getDstFileName());
        
        // 파일 복호화 
        FileCoder coder = new FileCoder(msg.getSecretKey());
        try {
			coder.decrypt(src, dest);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        // 파일 정합성 확인	
        if(String.valueOf(dest.length()).equals(msg.getFileSize())) {
            // 파일 FIN 파일생성
        	File fin = new File(msg.getDstPath()+"\\"+msg.getDstFileName()+".FIN");
        	try {
				fin.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }    
        
        // 해시 값 비교 한번 
        if(Utils.getFileHsash(dest).equals(msg.getFileHash())) {
        	log.info("Complete >> ?? " + Utils.getFileHsash(dest));	
        }
        
        log.info("File Integerity Complete");
        
        return 0;
        
	}
}
