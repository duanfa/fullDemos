import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client1{
	public static void main(String[]args) throws UnknownHostException, IOException {
		Socket socket = new Socket("localhost",1024);
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));
		//2.BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
		//通过socket获得输出流
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		
		//通过过socket获得输入流
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		//PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);
		//1.BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		while(true) {
			String line = br.readLine();//获取键盘输入的字符串
			bw.write(line);
			bw.newLine();//换行
			bw.flush();
			//pw.println(line);
			if(line.equals("over")) {
				break;
			}
			System.out.println(reader.readLine());//获取服务端传来的大写字符串
		}
		reader.close();
		br.close();
		bw.close();
		socket.close();
	}
}