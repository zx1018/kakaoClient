package com.eai.FileTransfer.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.LoggerFactory;
import com.eai.FileTransfer.sendReceive.Message;
import ch.qos.logback.classic.Logger;

public class FileTransferFileCheck {

	private static Logger log = (Logger) LoggerFactory.getLogger(FileTransferFileCheck.class);

	public static final int DEFAULT_BUFFER_SIZE = 10000;

	public int check(Message msg) {
		
		// ��ȣȭ ó�� 
        File src = new File(msg.getDstPath()+"\\"+msg.getDstFileName()+".enc");
        File dest = new File(msg.getDstPath()+"\\"+msg.getDstFileName());
        
        // �θ� ��ȣȭ 
        FileCoder coder = new FileCoder(msg.getSecretKey());
        try {
			coder.decrypt(src, dest);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        
        // ���� ���ռ� Ȯ��	
        if(String.valueOf(dest.length()).equals(msg.getFileSize())) {
            // ���� FIN ���ϻ���
        	File fin = new File(msg.getDstPath()+"\\"+msg.getDstFileName()+".FIN");
        	try {
				fin.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
        }    
        
        // �ؽ� �� �� �ѹ� 
        if(Utils.getFileHsash(dest).equals(msg.getFileHash())) {
        	log.info("Complete >> ?? " + Utils.getFileHsash(dest));	
        }
        
        log.info("File Integerity Complete");
        
        return 0;
        
	}
}