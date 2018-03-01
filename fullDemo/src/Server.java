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
		//1.����һ��serversocket����
		serverSocket = new ServerSocket(1024);
		
		//2.����accept�������������ܿͻ��˵�����
		Socket socket = serverSocket.accept();
		System.out.println(socket.getInetAddress().getHostAddress()+"�ɹ���");
		
		//3.��ȡsocket��������������
		BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		
		//PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);
		String line = null;
		
		//��ȡ�ͻ��˴�����������
		while((line = br.readLine())!=null) {
			if(line.equals("over")) {
				break;
			}
			System.out.println(line);
			bw.write(line.toUpperCase());//��ת���ɴ�д���ַ��������ͻ���
			bw.newLine();
			bw.flush();
			//pw.println(line.toUpperCase());
		}
		br.close();
		bw.close();
		socket.close();
		System.out.println(socket.getInetAddress().getHostAddress()+"�ѶϿ���");
	}
	
}
