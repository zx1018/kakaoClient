<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>Demo Send File Page !! </title>
</head>

<body>

    <form action="/sendFile" method="post">
    	srcPath : <input type="text" name="srcPath" value="F:\\" size="30"><br>
        fileName : <input type="file" name="fileName" size="50"> <br>
        secretKey : <input type="text" name="secretKey" value="696d697373796f7568616e6765656e61" size="30"><br>
        sndType : 
        <select name="sndType" id="sndType" >
			<option value="socket">socket</option>
			<option value="FTP">FTP</option>
			<option value="SFTP">SFTP</option>
		</select>
		<br>
        dstPath : <input type="text" name="dstPath" value="F:\\SOCKET" size="30"><br>
        dstFileName : <input type="text" name="dstFileName" value="2.txt" size="30"><br>
        
        <input type="submit" value="파일 전송 ">
    </form>
    
</body>

</html>