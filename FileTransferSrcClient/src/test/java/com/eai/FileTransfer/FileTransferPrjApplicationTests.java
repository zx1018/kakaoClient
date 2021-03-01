package com.eai.FileTransfer;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.eai.FileTransfer.sendReceive.Sender;

@RunWith(SpringRunner.class)
@SpringBootTest
class FileTransferPrjApplicationTests {
	
	@Autowired
    private Sender sender;

    @Test
    public void testSendMsg() {
    	System.out.println("Àü¼Û ");
    	
//        sender.sendMsg("Hello");
    }
}