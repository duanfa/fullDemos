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
		
		//ͨ��socket��������
		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		
		//ͨ����socket���������
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		//PrintWriter pw = new PrintWriter(socket.getOutputStream(),true);
		//1.BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		while(true) {
			String line = br.readLine();//��ȡ����������ַ���
			bw.write(line);
			bw.newLine();//����
			bw.flush();
			//pw.println(line);
			if(line.equals("over")) {
				break;
			}
			System.out.println(reader.readLine());//��ȡ����˴����Ĵ�д�ַ���
		}
		reader.close();
		br.close();
		bw.close();
		socket.close();
	}
}