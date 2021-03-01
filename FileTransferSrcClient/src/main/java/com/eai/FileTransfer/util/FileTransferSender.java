package com.eai.FileTransfer.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.LoggerFactory;
import com.eai.FileTransfer.sendReceive.Message;
import ch.qos.logback.classic.Logger;

public class FileTransferSender {

	private static Logger log = (Logger) LoggerFactory.getLogger(FileTransferSender.class);

	public static final int DEFAULT_BUFFER_SIZE = 10000;

	public int start(Message msg) {
		
		int result = 0;

		File file = new File(msg.getSrcPath()); // 파일이 존재하는지 안하는지
		File fileEnc = new File(msg.getSrcPath() + ".enc"); // 암호화 파일 생성
		if (!file.exists()) {
			log.info("File not Exist.");
			return -1;
		}
		// 암호화 처리
		FileCoder coder = new FileCoder(msg.getSecretKey());
		try {
			coder.encrypt(file, fileEnc);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		if ("socket".equals(msg.getSndType())) {
			result = socketSnd(file, fileEnc, msg);
		} else {
			// 일단 기본 FTP로 전송 한다.
			result = ftpSnd(file, fileEnc, msg);
		}
		
		return result;
	}

	// FTP 전송 방식
	private int ftpSnd(File file, File fileEnc, Message msg) {
		// FTP를 접속하기 위한 클래스를 선언한다.
		FTPClient client = new FTPClient();
		try {
			// connection 환경에서 UTF-8의 인코딩 타입을 사용한다.
			client.setControlEncoding("UTF-8");
			// ftp://localhost에 접속한다.
			client.connect(msg.getRcvip(), Integer.parseInt(msg.getRcvPort()));
			// 접속을 확읺나다.
			int resultCode = client.getReplyCode();
			// 접속시 에러가 나오면 콘솔에 에러 메시지를 표시하고 프로그램을 종료한다.
			if (!FTPReply.isPositiveCompletion(resultCode)) {
				log.info("FTP server refused connection.!");
				return -1;
			} else {
				// 파일 전송간 접속 딜레이 설정 (1ms 단위기 때문에 1000이면 1초)
				client.setSoTimeout(1000);
				// 로그인을 한다.
				if (!client.login("ftp", "1234qwer")) { // 키 관리는 나중에 정리가 필요하다.
					// 로그인을 실패하면 프로그램을 종료한다.
					log.info("Login Error!");
					return -1;
				}
				client.setBufferSize(1000);
				client.enterLocalPassiveMode();
				client.setFileType(FTP.BINARY_FILE_TYPE);

				InputStream input = new FileInputStream(fileEnc);
//				String remoteFilePath = msg.getDstPath();
				String remoteFilePath = msg.getDstFileName()+".enc";

				if (!client.storeFile(remoteFilePath, input)) {   // 파일 전송 
					log.info("File Send Failed");
					throw new Exception("File store failed");
				}
			}
		} catch (Exception e) {
			return -1;
		}
		
		return 0;

	}

	// 소켓 전송 방식
	private int socketSnd(File file, File fileEnc, Message msg) {
		long fileSize = file.length();
		long totalReadBytes = 0;
		byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
		int readBytes;
		double startTime = 0;

		try {
			FileInputStream fis = new FileInputStream(fileEnc);
			Socket socket = new Socket(msg.getRcvip(), Integer.parseInt(msg.getRcvPort()));
			if (!socket.isConnected()) {
				System.out.println("Socket Connect Error.");
				System.exit(0);
			}

			startTime = System.currentTimeMillis();
			OutputStream os = socket.getOutputStream();
			while ((readBytes = fis.read(buffer)) > 0) {
				os.write(buffer, 0, readBytes);
				totalReadBytes += readBytes;
				log.info("In progress: " + totalReadBytes + "/" + fileSize + " Byte(s) ("
						+ (totalReadBytes * 100 / fileSize) + " %)");
			}
			log.info("File transfer completed.");
			fis.close();
			os.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}

		double endTime = System.currentTimeMillis();
		double diffTime = (endTime - startTime) / 1000;
		double transferSpeed = (fileSize / 1000) / diffTime;

		log.info("time: " + diffTime + " second(s)");
		log.info("Average transfer speed: " + transferSpeed + " KB/s");
		
		return 0;

	}
}
