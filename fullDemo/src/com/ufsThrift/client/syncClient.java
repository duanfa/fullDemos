package com.ufsThrift.client;
  
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.ufsThrift.service.UfsRecipeCall.Client;
  
/** 
 *  
 * @author LK 
 * 
 * HelloTHsHaServerDemo
 * 
 */  
public class syncClient {  
    public static final String SERVER_IP = "localhost";  
    public static final int SERVER_PORT = 10029;  
    public static final int TIMEOUT = 30000;  
  
    /** 
     * 
     * @param userName 
     */  
    public void startClient(String jsonParam) {  
        TTransport transport = null;  
        try {  
            //transport = new TSocket(SERVER_IP, SERVER_PORT, TIMEOUT);  
            transport = new TFramedTransport(new TSocket(SERVER_IP, SERVER_PORT, TIMEOUT));  
            // 协议要和服务端一致  
//            TProtocol protocol = new TBinaryProtocol(transport);  
            TProtocol protocol = new TCompactProtocol(transport);  
            // TProtocol protocol = new TJSONProtocol(transport);  
            Client client = new Client(protocol);  
            transport.open();  
            String result = client.getMayLikeRecipe(jsonParam);
            System.out.println("Thrify client result =: " + result);  
        } catch (TTransportException e) {  
            e.printStackTrace();  
        } catch (TException e) {  
            e.printStackTrace();  
        } finally {  
            if (null != transport) {  
                transport.close();  
            }  
        }  
    }  
  
    /** 
     * @param args 
     */  
    public static void main(String[] args) {  
        syncClient client = new syncClient();  
        String jsonParam= "{\"uid\":\"578f54c2905e8865228b5423\",\"topN\":\"5\",\"appkey\":\"172139920\",\"method\":\"getMayLikeRecipe\",\"remoteip\":\"54.222.180.77\"}";
        
        client.startClient(jsonParam);  
  
    }  
}  