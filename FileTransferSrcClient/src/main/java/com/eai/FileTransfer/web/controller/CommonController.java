package com.eai.FileTransfer.web.controller;

import java.io.File;

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
 
@Controller
public class CommonController {
	
	private static Logger log = (Logger) LoggerFactory.getLogger(CommonController.class);
	
	@Autowired(required=true)
    private Sender sender;
    
    @RequestMapping("/sendFile")
    public String sndFile(HttpServletRequest request) throws Exception{
    	
    	log.info("파일 전송 요청 !! ");
    	
    	File dstfile = new File("2.txt");
    	
    	
    	String reqsrcPath = request.getParameter("srcPath");
    	String reqFileName = request.getParameter("fileName");
    	String reqSecretKey = request.getParameter("secretKey");
    	String reqSndType = request.getParameter("sndType");
    	String reqDstPath = request.getParameter("dstPath");
    	String reqDstFileName = request.getParameter("dstFileName");
    	
    	log.info("Input Data : "  +reqFileName + " : " + reqSecretKey + " : " +reqSndType+  " : " +reqDstPath + "  : " +reqDstFileName+ " : " +dstfile.getPath());
    	
    	// 메세지 객체를 만들어서 전송 
    	Message msg = new Message();
    
    	File file = new File(reqsrcPath+reqFileName);
    	if (!file.exists()) {
    		log.info("File Not Exist");
    		return "filecomplete";
        }
    	else{
	    	msg.setFileName(file.getName());
	    	msg.setSrcPath(file.getPath());
	    	msg.setDstPath(reqDstPath);
	    	msg.setDstFileName(reqDstFileName);
	    	msg.setFileHash(Utils.getFileHsash(file));
	    	msg.setFileSize(String.valueOf(file.length()));
	    	msg.setSecretKey(reqSecretKey);
	    	msg.setSndType(reqSndType);
	    	msg.setTransactionKey(String.valueOf(System.currentTimeMillis()));
	    	msg.setMessageType("1");
	    	
	    	log.info(msg.toString());	
	        sender.sndMsg(msg);
	        
	        return "filecomplete";
    	}
    }
 
    @RequestMapping("/demo")
    public String demo_test(Model model) throws Exception{
    	
    	log.info("초기 페이지 인입 ! ");
		model.addAttribute("name", "hello springBoot1234");
		
		return "hello";    
    } 
}

