import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server{
	private static ServerSocket serverSocket;

	public static void main(String[]args) throws IOException {
		//1.创建一个serversocket对象
		serverSocket = new ServerSocket(1024);
		
		//2.调用accept（）方法来接受客户端的请求
		Socket socket = serverSocket.accept();
		System.out.println(socket.getInetAddress().getHostAddress()+"成功！");
		
		//3.获取socket对象的输入输出流
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		
		//PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);
		String line = null;
		
		//读取客户端传过来的数据
		while((line = br.readLine())!=null) {
			if(line.equals("over")) {
				break;
			}
			System.out.println(line);
			bw.write(line.toUpperCase());//把转换成大写的字符串传给客户端
			bw.newLine();
			bw.flush();
			//pw.println(line.toUpperCase());
		}
		br.close();
		bw.close();
		socket.close();
		System.out.println(socket.getInetAddress().getHostAddress()+"已断开！");
	}
	
}
