import java.io.*;
import java.io.IOException;
import java.net.*;  
import java.util.HashMap;  
import java.util.Map;  
import java.util.concurrent.ExecutorService;  
import java.util.concurrent.Executors;  
import java.util.concurrent.ThreadPoolExecutor;  
  
  
public class TCPServer {      
      
    private ServerSocket serverSocket;  
      
    /** 
    * �����̳߳�������ͻ��˵������߳� 
    * ����ϵͳ��Դ�����˷� 
    */  
    private ExecutorService exec;  
      
    // ��ſͻ���֮��˽�ĵ���Ϣ  
    private Map<String,PrintWriter> storeInfo;  
      
    public TCPServer() {  
        try {  
  
            serverSocket = new ServerSocket(6789);  
            storeInfo = new HashMap<String, PrintWriter>();  
            exec = Executors.newCachedThreadPool();  
  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
      
    // ���ͻ��˵���Ϣ��Map��ʽ���뼯����  
    private void putIn(String key,PrintWriter value) {  
        synchronized(this) {  
            storeInfo.put(key, value);  
        }  
    }  
      
    // ��������������ӹ�������ɾ��  
    private synchronized void remove(String  key) {  
        storeInfo.remove(key);  
        System.out.println("��ǰ��������Ϊ��"+ storeInfo.size());  
    }  
      
    // ����������Ϣת�������пͻ���  
    private synchronized void sendToAll(String message) {  
        for(PrintWriter out: storeInfo.values()) {  
            out.println(message);  
        }  
    }  
      
    // ����������Ϣת����˽�ĵĿͻ���  
    private synchronized void sendToSomeone(String name,String message) {  
        PrintWriter pw = storeInfo.get(name); //����Ӧ�ͻ��˵�������Ϣȡ����Ϊ˽�����ݷ��ͳ�ȥ  
        if(pw != null) pw.println(message);   
    }  
      
    public void start() {  
        try {  
            while(true) {  
            System.out.println("�ȴ��ͻ�������... ... ");  
            Socket socket = serverSocket.accept();  
  
            // ��ȡ�ͻ��˵�ip��ַ  
            InetAddress address = socket.getInetAddress();  
            System.out.println("�ͻ��ˣ���" + address.getHostAddress() + "�����ӳɹ��� ");  
            /* 
            * ����һ���̣߳����߳�������ͻ��˵��������������ٴμ��� 
            * ��һ���ͻ��˵����� 
            */  
            exec.execute(new ListenrClient(socket)); //ͨ���̳߳��������߳�  
            }  
        } catch(Exception e) {  
            e.printStackTrace();  
        }  
    }  
      
    /** 
    * ���߳����������������ĳһ���ͻ��˵���Ϣ��ѭ�����տͻ��˷��� 
    * ��ÿһ���ַ����������������̨ 
    */  
    class ListenrClient implements Runnable {  
  
        private Socket socket;  
        private String name;  
                  
        public ListenrClient(Socket socket) {  
            this.socket = socket;  
        }  
          
        // �����ڲ�������ȡ�ǳ�  
        private String getName() throws Exception {  
            try {  
                //����˵���������ȡ�ͻ��˷��������ǳ������  
                BufferedReader bReader = new BufferedReader(  
                    new InputStreamReader(socket.getInputStream(), "UTF-8"));  
                //����˽��ǳ���֤���ͨ���������������͸��ͻ���  
                PrintWriter ipw = new PrintWriter(  
                    new OutputStreamWriter(socket.getOutputStream(), "UTF-8"),true);  
  
                //��ȡ�ͻ��˷������ǳ�  
                while(true) {  
                    String nameString = bReader.readLine();  
                    if ((nameString.trim().length() == 0) || storeInfo.containsKey(nameString)) {  
                        ipw.println("FAIL");  
                    } else {  
                        ipw.println("OK");  
                        return nameString;  
                    }  
                }  
            } catch(Exception e) {  
                throw e;  
            }  
        }  
          
        @Override         
        public void run() {  
            try {  
                /* 
                * ͨ���ͻ��˵�Socket��ȡ�ͻ��˵������ 
                * ��������Ϣ���͸��ͻ��� 
                */  
                PrintWriter pw = new PrintWriter(  
                    new OutputStreamWriter(socket.getOutputStream(), "UTF-8"), true);  
                  
                /* 
                * ���ͻ��ǳƺ�����˵�����ݴ��빲����HashMap�� 
                */  
                name = getName();  
                putIn(name, pw);  
                Thread.sleep(100);  
                  
                // �����֪ͨ���пͻ��ˣ�ĳ�û�����  
                sendToAll("[ϵͳ֪ͨ] ��" + name + "��������");  
                  
                /* 
                * ͨ���ͻ��˵�Socket��ȡ������ 
                * ��ȡ�ͻ��˷���������Ϣ 
                */  
                BufferedReader bReader = new BufferedReader(  
                    new InputStreamReader(socket.getInputStream(), "UTF-8"));  
                String msgString = null;  
                  
                  
                while((msgString = bReader.readLine()) != null) {  
                    // �����Ƿ�Ϊ˽�ģ���ʽ��@�ǳƣ����ݣ�  
                    if(msgString.startsWith("@")) {  
                        int index = msgString.indexOf(":");  
                        if(index >= 0) {  
                            //��ȡ�ǳ�  
                            String theName = msgString.substring(1, index);  
                            String info = msgString.substring(index+1, msgString.length());  
                            info =  name + "��"+ info;  
                            //��˽����Ϣ���ͳ�ȥ  
                            sendToSomeone(theName, info);  
                            continue;  
                        }  
                    }  
                    // ������������������ÿͻ��˷��͵���Ϣת�������пͻ���  
                    System.out.println(name+"��"+ msgString);  
                    sendToAll(name+"��"+ msgString);  
                }     
            } catch (Exception e) {  
                // e.printStackTrace();  
            } finally {  
                remove(name);  
                // ֪ͨ���пͻ��ˣ�ĳĳ�ͻ��Ѿ�����  
                sendToAll("[ϵͳ֪ͨ] "+name + "�Ѿ������ˡ�");  
                  
                if(socket!=null) {  
                    try {  
                        socket.close();  
                    } catch(IOException e) {  
                        e.printStackTrace();  
                    }  
                }     
            }  
        }  
    }  
      
    public static void main(String[] args) {  
        TCPServer server = new TCPServer();  
        server.start();  
    }  
}  
