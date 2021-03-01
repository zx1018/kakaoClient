package com.eai.FileTransfer.sendReceive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class Sender {
	
	@Autowired
	private JmsTemplate jmsTemplate;
	    
    public void sndMsg(Message msg) {
        jmsTemplate.convertAndSend("src_snd-queue", msg);
    }
    
}
