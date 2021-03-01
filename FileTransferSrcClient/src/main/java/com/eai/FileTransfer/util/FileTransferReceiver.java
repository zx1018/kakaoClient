package com.eai.FileTransfer.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.eai.FileTransfer.sendReceive.Message;
import com.eai.FileTransfer.sendReceive.Sender;
import ch.qos.logback.classic.Logger;
 
@Component
public class FileTransferReceiver {
	
	private static Logger log = (Logger) LoggerFactory.getLogger(FileTransferReceiver.class);
	
    public static final int DEFAULT_BUFFER_SIZE = 10000;
    
	@Autowired(required=true)
    private Sender sender;

  
	/**
     * 파일 수신을 위한 Port를 오픈한다. 
     * @param Message msg ActiveMq에서 수신한 메세지 객체
     * @throws Exception
     */
    public int open(Message msg) {
        try {
        	// 수신 측 Port 임의로 10344로 정한다. 
        	int port = 10344;
            ServerSocket server = new ServerSocket(port);
            log.info("This server is listening... (Port: " + port  + ")");            
            Socket socket = server.accept();  													//새로운 연결 소켓 생성 및 accept대기
                       
            // 접속이 정상적으로 진행되면 
            InetSocketAddress isaClient = (InetSocketAddress) socket.getRemoteSocketAddress();
            log.info("A client("+isaClient.getAddress().getHostAddress()+" is connected. (Port: " +isaClient.getPort() + ")");
             
            // 파일 수신 시작 
            FileOutputStream fos = new FileOutputStream(msg.getDstPath()+"\\"+msg.getDstFileName()+".enc");
            InputStream is = socket.getInputStream();
             
            double startTime = System.currentTimeMillis(); 
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int readBytes;
            while ((readBytes = is.read(buffer)) != -1) {
                fos.write(buffer, 0, readBytes);
 
            }      
            double endTime = System.currentTimeMillis();
            double diffTime = (endTime - startTime)/ 1000;;
 
            log.info("time: " + diffTime+ " second(s)");
             
            is.close();
            fos.close();
            socket.close();
            server.close();
            // 파일 수신 완료 
            
            return 0;
        } catch (Exception e) {
            e.printStackTrace();
            return -1; 
        }
    }
}