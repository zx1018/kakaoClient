package com.eai.FileTransfer.web.controller;

import java.io.File;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import com.eai.FileTransfer.sendReceive.Message;
import com.eai.FileTransfer.sendReceive.Sender;
import com.eai.FileTransfer.util.Utils;

import ch.qos.logback.classic.Logger;
 

/**********************************
 * 
 * @author IWJ
 * Demo 전송 View Controller
 *
 ***********************************/
@Controller
public class CommonController {
	
	private static Logger log = (Logger) LoggerFactory.getLogger(CommonController.class);
	
	@Autowired(required=true)
    private Sender sender;
    
    @RequestMapping("/sendFile")
    public String sndFile(HttpServletRequest request) throws Exception{
    	
    	log.info("파일 전송 요청 !! ");
    	
    	String reqsrcPath = request.getParameter("srcPath");
    	String reqFileName = request.getParameter("fileName");
    	String reqSecretKey = request.getParameter("secretKey");
    	String reqSndType = request.getParameter("sndType");
    	String reqDstPath = request.getParameter("dstPath");
    	String reqDstFileName = request.getParameter("dstFileName");
    	String reqTgtSvrName= request.getParameter("tgtSvrName");
    	
    	// 메세지 객체를 만들어서 전송 
    	Message msg = new Message();
    
    	// 먼저 파일이 있는지 없는지 확인한다.  
    	File file = new File(reqsrcPath+reqFileName);
    	if (!file.exists()) {
    		// 예외 처리는 여기서 고려하지 않는다. 
    		log.info("File Not Exist");
    		return "filecomplete";
        }
    	else{
    		
    		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMddHHmmss");
    		String format_time1 = format1.format (System.currentTimeMillis());
    		
    		// 파일이 있다면 Message 객체 세팅 
	    	msg.setFileName(file.getName());                     // 보내는 파일명 
	    	msg.setSrcPath(file.getPath());                      // 보내는 팡리읠 경로 
	    	msg.setDstPath(reqDstPath);                          // 도착 서버 파일 경로 
	    	msg.setDstFileName(reqDstFileName);                  // 도착 서버 파일 명 
	    	msg.setFileHash(Utils.getFileHsash(file));           // 보내는 파일의 해시 값
	    	msg.setFileSize(String.valueOf(file.length()));      // 보내는 파일의 파일 사이즈 
	    	msg.setSecretKey(reqSecretKey);                      // 암복화화 대칭키 
	    	msg.setSndType(reqSndType);                          // 파일 전송 타입 
	    	msg.setTransactionKey(format_time1);      // 파일 전송 고유값 
	    	msg.setMessageType("1");                             // 파일 전송 요청은 1로 시작 
	    	msg.setTgtSverName(reqTgtSvrName);                   // 타겟 서버명 
	    	msg.setSrcSverName(Utils.getServerName());           // 보내는 파일의 서버명  ( 호스트 명 )
	    	
	    	log.info(msg.toString());	
	        sender.sndMsg(msg);               // miniEAI에 전송 요청 하고 종료  (결과는 고려하지 않는다.)
	        
	        return "filecomplete";
    	}
    }
 
    @RequestMapping("/demo")
    public String demo_test(Model model) throws Exception{
    	
    	log.info("demo 페이지 인입 ! ");
		
		return "demo";    
    } 
}

