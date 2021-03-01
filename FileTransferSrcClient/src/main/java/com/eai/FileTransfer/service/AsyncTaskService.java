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
		// 송신 완료가 되었으면 확인 전문 요청 
		if(ret == 0) {
			msg.setMessageType("3"); 					// messageType을 3로 변경 ( 파일 전송 완료 상태 )
			sender.sndMsg(msg);
		}
		else {
			// 파일 전송 실패에 따른 후속 조치
			log.info("File Transfer Failed");
		}
	}

	@Async
	public void jobRunningInBackgroundFileCheck(Message msg) {
		log.info("Thread FileCheck Start");
		int ret = 0;
		
		// 전송 타입 수정 
		FileTransferFileCheck check = new FileTransferFileCheck();
		ret = check.check(msg);
		if(ret == 0) {
			msg.setMessageType("4");                    // messageType을 3로 변경 ( 파일 전송 완료 상태 )
			sender.sndMsg(msg);
		}
		else {
			// 파일 전송 실패에 따른 후속 조치
			msg.setMessageType("4");
			msg.setSndRst("9");  // 파일 부정합 
			sender.sndMsg(msg);
			log.info("File Transfer Failed");
		}
	}
}
