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
 * Demo ���� View Controller
 *
 ***********************************/
@Controller
public class CommonController {
	
	private static Logger log = (Logger) LoggerFactory.getLogger(CommonController.class);
	
	@Autowired(required=true)
    private Sender sender;
    
    @RequestMapping("/sendFile")
    public String sndFile(HttpServletRequest request) throws Exception{
    	
    	log.info("���� ���� ��û !! ");
    	
    	String reqsrcPath = request.getParameter("srcPath");
    	String reqFileName = request.getParameter("fileName");
    	String reqSecretKey = request.getParameter("secretKey");
    	String reqSndType = request.getParameter("sndType");
    	String reqDstPath = request.getParameter("dstPath");
    	String reqDstFileName = request.getParameter("dstFileName");
    	String reqTgtSvrName= request.getParameter("tgtSvrName");
    	
    	// �޼��� ��ü�� ���� ���� 
    	Message msg = new Message();
    
    	// ���� ������ �ִ��� ������ Ȯ���Ѵ�.  
    	File file = new File(reqsrcPath+reqFileName);
    	if (!file.exists()) {
    		// ���� ó���� ���⼭ ������� �ʴ´�. 
    		log.info("File Not Exist");
    		return "filecomplete";
        }
    	else{
    		
    		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyyMMddHHmmss");
    		String format_time1 = format1.format (System.currentTimeMillis());
    		
    		// ������ �ִٸ� Message ��ü ���� 
	    	msg.setFileName(file.getName());                     // ������ ���ϸ� 
	    	msg.setSrcPath(file.getPath());                      // ������ �θ��� ��� 
	    	msg.setDstPath(reqDstPath);                          // ���� ���� ���� ��� 
	    	msg.setDstFileName(reqDstFileName);                  // ���� ���� ���� �� 
	    	msg.setFileHash(Utils.getFileHsash(file));           // ������ ������ �ؽ� ��
	    	msg.setFileSize(String.valueOf(file.length()));      // ������ ������ ���� ������ 
	    	msg.setSecretKey(reqSecretKey);                      // �Ϻ�ȭȭ ��ĪŰ 
	    	msg.setSndType(reqSndType);                          // ���� ���� Ÿ�� 
	    	msg.setTransactionKey(format_time1);      // ���� ���� ������ 
	    	msg.setMessageType("1");                             // ���� ���� ��û�� 1�� ���� 
	    	msg.setTgtSverName(reqTgtSvrName);                   // Ÿ�� ������ 
	    	msg.setSrcSverName(Utils.getServerName());           // ������ ������ ������  ( ȣ��Ʈ �� )
	    	
	    	log.info(msg.toString());	
	        sender.sndMsg(msg);               // miniEAI�� ���� ��û �ϰ� ����  (����� ������� �ʴ´�.)
	        
	        return "filecomplete";
    	}
    }
 
    @RequestMapping("/demo")
    public String demo_test(Model model) throws Exception{
    	
    	log.info("demo ������ ���� ! ");
		
		return "demo";    
    } 
}

