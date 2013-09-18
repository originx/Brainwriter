package foi.appchallenge.brainwriting.modules;


/*
 * Copyright (C) 2013 Surviving with Android (http://www.survivingwithandroid.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpPostClient {
	private String url;
    private HttpURLConnection connection;
    private OutputStream outputStream;
    
	private String delimiter = "--";
    private String boundary =  "xxxxxxxxx";

	public HttpPostClient(String url) {		
		this.url = url;
	}
	
	public byte[] downloadImage(String imgName) {
		ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
		try {
			System.out.println("URL ["+url+"] - Name ["+imgName+"]");
			
			HttpURLConnection con = (HttpURLConnection) ( new URL(url)).openConnection();
			con.setRequestMethod("POST");
			con.setDoInput(true);
			con.setDoOutput(true);
			con.connect();
			con.getOutputStream().write( ("name=" + imgName).getBytes());
			
			InputStream is = con.getInputStream();
			byte[] b = new byte[1024];
			
			while ( is.read(b) != -1)
				byteOutput.write(b);
			
			con.disconnect();
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
		
		return byteOutput.toByteArray();
	}

	public void connectForMultipart() throws Exception {
		connection = (HttpURLConnection) ( new URL(url)).openConnection();
		connection.setRequestMethod("POST");
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setRequestProperty("Connection", "Keep-Alive");
		connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
		connection.connect();
		outputStream = connection.getOutputStream();
	}
	
	public void addFormPart(String paramName, String value) throws Exception {
		writeParamData(paramName, value);
	}
	
	public void addFilePart(String paramName, String fileName, byte[] data) throws Exception {
		outputStream.write( (delimiter + boundary + "\r\n").getBytes());
		outputStream.write( ("Content-Disposition: form-data; name=\"" + paramName +  "\"; filename=\"" + fileName + "\"\r\n"  ).getBytes());
		outputStream.write( ("Content-Type: application/octet-stream\r\n"  ).getBytes());
		outputStream.write( ("Content-Transfer-Encoding: binary\r\n"  ).getBytes());
		outputStream.write("\r\n".getBytes());
   
		outputStream.write(data);
		
		outputStream.write("\r\n".getBytes());
	}
	
	public void finishMultipart() throws Exception {
		outputStream.write( (delimiter + boundary + delimiter + "\r\n").getBytes());
	}
	
	
	public String getResponse() throws Exception {
		InputStream is = connection.getInputStream();
		byte[] b1 = new byte[1024];
		StringBuffer buffer = new StringBuffer();
		
		while ( is.read(b1) != -1)
			buffer.append(new String(b1));
		
		connection.disconnect();
		
		return buffer.toString();
	}
	

	
	private void writeParamData(String paramName, String value) throws Exception {
		
		outputStream.write( (delimiter + boundary + "\r\n").getBytes());
		outputStream.write( "Content-Type: text/plain;encoding:utf-8\r\n".getBytes());
		outputStream.write( ("Content-Disposition: form-data; name=\"" + paramName + "\"\r\n").getBytes());;
		outputStream.write( ("\r\n" + value + "\r\n").getBytes());
			
		
	}
}
