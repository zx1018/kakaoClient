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
 * Queue Receiver 클래스  
 *
 ***********************************/
@Component
public class Receiver {
	
	private static Logger log = (Logger) LoggerFactory.getLogger(Receiver.class);
	
	@Autowired(required=true)
    private Sender sender;

	@Autowired(required=true)
    private AsyncTaskService service;
	
	// 파일 전송 시작 큐 
	@JmsListener(destination = "dst-queue_server1", containerFactory="jsaFactory")
	public void receiveMessage(Message msg){
		log.info("Received : " +msg.getMessageType());
		log.info(msg.toString());
		
		processMessageType(msg);
	}
	
	
	// 수신한 MessageType에 맞게 처리하는 함수  
	private void processMessageType(Message msg) {
		// 처리 프로세스 
		int status = Integer.parseInt(msg.getMessageType());
		// 어싱크 프로세스 return 값 
		int ret; 
		// 파일 전송 오류 타입 ( 오류 타입 정의 하고 그것에 대한 대응 처리 필요.. 
		if("9".equals(msg.getSndRst())){
			
		}
		else {
			// 모든 메세지는 받아서 서버 타겟명을 변경해야 한다. 
//			String temp = "" ;
//			temp = msg.getSrcSverName() ;
//			temp = msg.getTgtSverName();
			msg.setTgtSverName(msg.getSrcSverName());
			msg.setSrcSverName(Utils.getServerName());
			
			switch(status) {
				// 파일 전송 요청을 받은 단계 ( 서버를 기동 할 수 있으면 기동하고 그 응답 값을 회신한다. 
				case 1 : 
					try {
						int port = 10344;                                // 일반 소켓 포트
						if("FTP".equals(msg.getSndType())) { 
							port = 21;                                   // ftp 포트 
						}
						else {
							(new ServerSocket(port)).close();
				            // 소켓 생성 오류가 없다면 응답을 주자
							service.jobRunningInBackgroundServer(msg);
						}
			            // 자신의 IP 조사 
			            InetAddress local = InetAddress.getLocalHost();
			            msg.setRcvPort(String.valueOf(port));
			            msg.setRcvip(local.getHostAddress());
			            msg.setMessageType("2");                         // messageType을 2로 변경 ( 파일 수신 대기 상태 )
			            sender.sndMsg(msg);
					}catch(Exception e) {
						e.printStackTrace();
						// 소켓 생성 오류가 나면 
						log.info("Socket Create Error");
			        	msg.setSndRst("9");
			        	sender.sndMsg(msg);
					}
					break;
				// 수신 서버가 준비되었다는 메세지를 받은 단계
				case 2 : 
					// 파일 전송 시작 
					service.jobRunningInBackgroundClient(msg);
					break;
				// 파일 전송이 완료되었고 파일 수신이 정상적인지 확인 요청하는 단계 
				case 3 : 
					// 파일 정상 유불 확인 및 결과에 따른 응답 처리 
					service.jobRunningInBackgroundFileCheck(msg);
					// 파일 정상 완료가 되었다면  
					break;
					
				// 파일 전송 정합성 확인 완료 단계  
				case 4 : 
					// 파일 전송 완료 처리 
					// 여기선 정의가 불필요
					break;
					
			}
		}
	}
}