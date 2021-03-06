package com.eai.FileTransfer.service;


import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.eai.FileTransfer.sendReceive.Message;
import com.eai.FileTransfer.sendReceive.Sender;
import com.eai.FileTransfer.util.FileTransferFileCheck;
import com.eai.FileTransfer.util.FileTransferReceiver;
import com.eai.FileTransfer.util.FileTransferSender;

import ch.qos.logback.classic.Logger;



/**********************************
 * 
 * @author iwj
 * Aync ó���� ���� ���� 
 *
 ***********************************/
@Component
@Service("asyncTaskService")
public class AsyncTaskService {
	
	private static Logger log = (Logger) LoggerFactory.getLogger(AsyncTaskService.class);
	
	@Autowired(required=true)
    private Sender sender;
	
	
	@Async
	public void jobRunningInBackgroundServer(Message msg) {
		log.info("Thread Server Start");
		FileTransferReceiver rcv = new FileTransferReceiver();
		rcv.open(msg);
	}
	
	@Async
	public void jobRunningInBackgroundClient(Message msg) {
		log.info("Thread Client Start");
		FileTransferSender snd = new FileTransferSender();
		int ret = snd.start(msg);
		// �۽� �Ϸᰡ �Ǿ����� Ȯ�� ���� ��û 
		if(ret == 0) {
			msg.setMessageType("3"); 					// messageType�� 3�� ���� ( ���� ���� �Ϸ� ���� )
			sender.sndMsg(msg);
		}
		else {
			// ���� ���� ���п� ���� �ļ� ��ġ
			log.info("File Transfer Failed");
		}
	}

	@Async
	public void jobRunningInBackgroundFileCheck(Message msg) {
		log.info("Thread FileCheck Start");
		int ret = 0;
		
		// ���� Ÿ�� ���� 
		FileTransferFileCheck check = new FileTransferFileCheck();
		ret = check.check(msg);
		if(ret == 0) {
			msg.setMessageType("4");                    // messageType�� 4�� ���� ( ���� Ȯ�� �Ϸ� ���� )
			sender.sndMsg(msg);
		}
		else {
			// ���� ���� ���п� ���� �ļ� ��ġ
			msg.setMessageType("4");
			msg.setSndRst("9");  // ���� ������ 
			sender.sndMsg(msg);
			log.info("File Transfer Failed");
		}
	}
}
