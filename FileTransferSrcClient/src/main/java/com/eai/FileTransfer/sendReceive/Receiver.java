package com.eai.FileTransfer.sendReceive;

import java.net.InetAddress;
import java.net.ServerSocket;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import com.eai.FileTransfer.service.AsyncTaskService;
import com.eai.FileTransfer.util.Utils;

import ch.qos.logback.classic.Logger;


/**********************************
 * 
 * @author iwj
 * Queue Receiver Ŭ����  
 *
 ***********************************/
@Component
public class Receiver {
	
	private static Logger log = (Logger) LoggerFactory.getLogger(Receiver.class);
	
	@Autowired(required=true)
    private Sender sender;

	@Autowired(required=true)
    private AsyncTaskService service;
	
	// ���� ���� ���� ť 
	@JmsListener(destination = "dst-queue_server1", containerFactory="jsaFactory")
	public void receiveMessage(Message msg){
		log.info("Received : " +msg.getMessageType());
		log.info(msg.toString());
		
		processMessageType(msg);
	}
	
	
	// ������ MessageType�� �°� ó���ϴ� �Լ�  
	private void processMessageType(Message msg) {
		// ó�� ���μ��� 
		int status = Integer.parseInt(msg.getMessageType());
		// ���ũ ���μ��� return �� 
		int ret; 
		// ���� ���� ���� Ÿ�� ( ���� Ÿ�� ���� �ϰ� �װͿ� ���� ���� ó�� �ʿ�.. 
		if("9".equals(msg.getSndRst())){
			
		}
		else {
			// ��� �޼����� �޾Ƽ� ���� Ÿ�ٸ��� �����ؾ� �Ѵ�. 
//			String temp = "" ;
//			temp = msg.getSrcSverName() ;
//			temp = msg.getTgtSverName();
			msg.setTgtSverName(msg.getSrcSverName());
			msg.setSrcSverName(Utils.getServerName());
			
			switch(status) {
				// ���� ���� ��û�� ���� �ܰ� ( ������ �⵿ �� �� ������ �⵿�ϰ� �� ���� ���� ȸ���Ѵ�. 
				case 1 : 
					try {
						int port = 10344;                                // �Ϲ� ���� ��Ʈ
						if("FTP".equals(msg.getSndType())) { 
							port = 21;                                   // ftp ��Ʈ 
						}
						else {
							(new ServerSocket(port)).close();
				            // ���� ���� ������ ���ٸ� ������ ����
							service.jobRunningInBackgroundServer(msg);
						}
			            // �ڽ��� IP ���� 
			            InetAddress local = InetAddress.getLocalHost();
			            msg.setRcvPort(String.valueOf(port));
			            msg.setRcvip(local.getHostAddress());
			            msg.setMessageType("2");                         // messageType�� 2�� ���� ( ���� ���� ��� ���� )
			            sender.sndMsg(msg);
					}catch(Exception e) {
						e.printStackTrace();
						// ���� ���� ������ ���� 
						log.info("Socket Create Error");
			        	msg.setSndRst("9");
			        	sender.sndMsg(msg);
					}
					break;
				// ���� ������ �غ�Ǿ��ٴ� �޼����� ���� �ܰ�
				case 2 : 
					// ���� ���� ���� 
					service.jobRunningInBackgroundClient(msg);
					break;
				// ���� ������ �Ϸ�Ǿ��� ���� ������ ���������� Ȯ�� ��û�ϴ� �ܰ� 
				case 3 : 
					// ���� ���� ���� Ȯ�� �� ����� ���� ���� ó�� 
					service.jobRunningInBackgroundFileCheck(msg);
					// ���� ���� �Ϸᰡ �Ǿ��ٸ�  
					break;
					
				// ���� ���� ���ռ� Ȯ�� �Ϸ� �ܰ�  
				case 4 : 
					// ���� ���� �Ϸ� ó�� 
					// ���⼱ ���ǰ� ���ʿ�
					break;
					
			}
		}
	}
}