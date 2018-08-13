import java.io.*;
import java.io.IOException;
import java.net.*;  
import java.util.Scanner;  
import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;  
import java.util.concurrent.ThreadPoolExecutor;  
  
public class TCPClient {  
      
    static private Socket clientSocket;  
      
    public TCPClient() {}  
  
    public static void main(String[] args) throws Exception {  
        Scanner scanner = new Scanner(System.in);  
        String serverIP;  
      
        System.out.println("�����÷�����IP��");  
        serverIP = scanner.next();  
        clientSocket = new Socket(serverIP, 6789);  
        TCPClient client = new TCPClient();  
        client.start();  
    }  
              
    public void start() throws IOException {  
        try {  
            Scanner scanner = new Scanner(System.in);  
            setName(scanner);  
              
            // ���շ������˷��͹�������Ϣ���߳�����  
            ExecutorService exec = Executors.newCachedThreadPool();  
            exec.execute(new ListenrServser());  
              
            // �����������������˷���Ϣ  
            PrintWriter pw = new PrintWriter(  
                new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"), true);  
              
            while(true) {  
                pw.println(scanner.nextLine());  
            }  
        } catch(Exception e) {  
            e.printStackTrace();  
        } finally {  
            if (clientSocket !=null) {  
                try {  
                    clientSocket.close();  
                } catch(IOException e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
    }  
  
    private void setName(Scanner scan) throws Exception {  
        String name;  
        //���������  
        PrintWriter pw = new PrintWriter(  
                new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"),true);  
        //����������  
        BufferedReader br = new BufferedReader(  
                new InputStreamReader(clientSocket.getInputStream(),"UTF-8"));  
          
        while(true) {     
            System.out.println("�봴�������ǳƣ�");  
            name = scan.nextLine();  
            if (name.trim().equals("")) {  
                System.out.println("�ǳƲ���Ϊ��");  
            } else {      
                pw.println(name);  
                String pass = br.readLine();  
                if (pass != null && (!pass.equals("OK"))) {  
                    System.out.println("�ǳ��Ѿ���ռ�ã����������룺");  
                } else {  
                    System.out.println("�ǳơ�"+name+"�������óɹ������Կ�ʼ������");  
                    break;  
                }  
            }  
        }  
    }  
      
    // ѭ����ȡ����˷��͹�������Ϣ��������ͻ��˵Ŀ���̨  
    class ListenrServser implements Runnable {  
      
    @Override  
        public void run() {  
            try {  
                BufferedReader br = new BufferedReader(  
                    new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));  
                String msgString;  
                while((msgString = br.readLine())!= null) {  
                    System.out.println(msgString);  
                }  
            } catch(Exception e) {  
                e.printStackTrace();  
            }  
        }  
    }  
  
}  