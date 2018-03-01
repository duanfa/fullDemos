package com.socket;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MyServer {
	private static ServerSocket serverSocket;

	public static void main(String args[]) throws Exception {
		serverSocket = new ServerSocket(8080);
		System.out.println("server is ok.");
		while (true) {
			Socket socket = serverSocket.accept();
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			OutputStream os = socket.getOutputStream();

			boolean autoflush = true;
			PrintWriter out = new PrintWriter(socket.getOutputStream(), autoflush);

			int line = 0;
			char[] cbuf = new char[10240];
			while ((line = in.read(cbuf)) != -1) {
				System.out.println("post:" + new String(cbuf, 0, line));
				if (line < 10240) {

					Response response = new Response(os);
					response.sendStaticResource();

					in.close();
					socket.close();
					socket = serverSocket.accept();
					in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					os = socket.getOutputStream();
					out = new PrintWriter(socket.getOutputStream(), autoflush);

				}
			}
		}
	}
}

class Response {
	private int BUFFER_SIZE = 1024;
	private OutputStream output;

	public Response(OutputStream output) {
		this.output = output;
	}

	public void sendStaticResource() throws IOException {
		byte[] bytes = new byte[BUFFER_SIZE];
		FileInputStream fis = null;
		try {
			File file = new File("/home/socket/Desktop/postc.html");
			if (file.exists()) {
				fis = new FileInputStream(file);
				int ch = fis.read(bytes, 0, BUFFER_SIZE);

				while (ch != -1) {
					output.write(bytes, 0, ch);
					ch = fis.read(bytes, 0, BUFFER_SIZE);
				}
			} else {
				String errorMessage = "HTTP/1.1 404 File Not Found\r\n" + "Content-Type: text/html\r\n" + "Content-Length: 23\r\n" + "\r\n" + "<h1>File Not Found</h1>";
				output.write(errorMessage.getBytes());
			}
		} catch (Exception e) {
			System.out.println(e.toString());
		} finally {
			if (fis != null)
				fis.close();
		}
	}
}
