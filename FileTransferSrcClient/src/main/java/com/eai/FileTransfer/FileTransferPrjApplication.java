package com.eai.FileTransfer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.jms.annotation.EnableJms;

@SpringBootApplication
@EnableJms
@ComponentScan("com.eai.FileTransfer")  // 스캔 범위 지정
public class FileTransferPrjApplication {

	public static void main(String[] args) {
		
		SpringApplication.run(FileTransferPrjApplication.class, args);
	}

}
