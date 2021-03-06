package com.eai.FileTransfer.sendReceive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

/**********************************
 * 
 * @author iwj
 * Queue Sender Å¬·¡½º  
 *
 ***********************************/
@Component
public class Sender {
	
	@Autowired
	private JmsTemplate jmsTemplate;
	    
    public void sndMsg(Message msg) {
        jmsTemplate.convertAndSend("snd-queue-server1", msg);
    }
    
}
