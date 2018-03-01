package com.thriftHello.server;
import org.apache.thrift.TProcessor;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.THsHaServer;
import org.apache.thrift.server.THsHaServer.Args;
import org.apache.thrift.server.TServer;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TNonblockingServerSocket;

import com.thriftHello.service.HelloWorldImpl;
import com.thriftHello.service.IHelloWorldService;
import com.thriftHello.service.IHelloWorldService.Processor;
  
/** 
 * 半同步半异步的服务端模型，需要指定为： TFramedTransport 数据传输的方式  THsHaServer服务模型 
 * @author LK 
 * 
 */  
public class HelloTHsHaServerDemo {  
  
    public static final int SERVER_PORT = 8090;  
    public void startServer() {  
        try {  
            System.out.println("HelloWorld THsHaServer start ....");  
   
            TProcessor tprocessor = new IHelloWorldService.Processor(new HelloWorldImpl()); 
            TNonblockingServerSocket tnbSocketTransport = new TNonblockingServerSocket(SERVER_PORT);  
            Args args = new Args(tnbSocketTransport);  
            args.processor(tprocessor);  
            args.transportFactory(new TFramedTransport.Factory());  
            args.protocolFactory(new TBinaryProtocol.Factory());  
            TServer server = new THsHaServer(args);  
            server.serve();  
              
        } catch (Exception e) {  
            System.out.println("Server start error!!!");  
            e.printStackTrace();  
        }  
    }  
   
    /** 
     * @param args 
     */  
    public static void main(String[] args) {  
        HelloTHsHaServerDemo server = new HelloTHsHaServerDemo();  
        server.startServer();  
    }  
}  