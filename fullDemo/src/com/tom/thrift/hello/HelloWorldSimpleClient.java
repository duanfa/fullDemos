package com.tom.thrift.hello;

import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TNonblockingTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import com.tom.thrift.util.CdapiCall;
import com.tom.thrift.util.CdapiCall.AsyncClient.getResultData_call;

public class HelloWorldSimpleClient {

	public static final String SERVER_IP = "127.0.0.1";
//	public static final int SERVER_PORT = 10025;// Thrift server listening port
	public static final int SERVER_PORT = HelloWorldSimpleServer.SERVER_PORT;// Thrift server listening port
	public static final int TIMEOUT = 30000;

	/**
	 *
	 * @param userName
	 */
	public void startClient(String userName) {
		TTransport transport = null;
		try {
			transport = new TSocket(SERVER_IP, SERVER_PORT, TIMEOUT);
			// 协议要和服务端一致
			TProtocol protocol = new TBinaryProtocol(transport);
			// TProtocol protocol = new TCompactProtocol(transport);
			// TProtocol protocol = new TJSONProtocol(transport);
			TagManageCall.Client client = new TagManageCall.Client(protocol);
			transport.open();
			String resStr  = "";
			
//			 resStr = client.searchExps(190, 161402, 1, 5);
			
//			 resStr = client.addExp(66056, 222, "222", "=")+"";  
//			String jsonParam= "{\"expId\":1169,\"tagWeightId\":161402,\"expKey\":9,\"expValue\":\"abc\",\"expOp\":\">\"}";
//			resStr = client.modifyExp(jsonParam)+""; 
			
//			resStr = client.deleteExp(1169)+""; 
			
//			 resStr = client.searchTags(1, 0, null, null, null, 0.00, 7);  //未修改
			
//			 resStr = client.searchWgtConfigs(123, 6, 1303, null,null, null, 1, 1, 5);  //未修改
			
//			resStr = client.addTag(172139920, "tag_b", "tag测试b标签", "1", "tagbbb", 0, 7 , 3) +"";
			
//			resStr = client.modifyTag(0, "tagaaaa", "tagaaaa", "tagaaaa", 0.1, 8,91) +"";
			
//			resStr = client.searchTags(199957, 0, null, null, null, -1, -1, -1);
			
//			resStr = client.addWgtConfig(199958, 1, "itemid_c", "actionid_c", "itemtype_c", 1l, null)+""; //未修改
			 resStr = client.addExp(66059, 222, "222", "=")+"";
			System.out.println("Thrift client result:" + resStr);
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

	public String addSentiKeyWords() throws Exception {
		TSocket trans = new TSocket(SERVER_IP, SERVER_PORT, TIMEOUT);
		TProtocol protocol = new TBinaryProtocol(trans);
		TagManageCall.Client client = new TagManageCall.Client(protocol);
		try {
			trans.open();
			String resStr  = "";
//			 resStr = client.searchExps(190, 161402, 1, 5);
			 resStr = client.addExp(161402, 8, "9", "=")+"";
			System.out.println(resStr);
			return resStr;
		} catch (Exception e) {
			throw e;
		} finally {
			trans.close();
		}
	}

	public static String call2(String userId, int topN) throws Exception {
		long start = System.currentTimeMillis();
		TAsyncClientManager clientManager = new TAsyncClientManager();
		TNonblockingTransport transport = new TNonblockingSocket("127.0.0.1",
				SERVER_PORT, 1000);
		TProtocolFactory protocol = new TCompactProtocol.Factory();
		CdapiCall.AsyncClient asyncClient = new CdapiCall.AsyncClient(protocol,
				clientManager, transport);
		MyCallback callBack = new MyCallback(start);
		asyncClient.getResultData(
				"paramJson={\"appid\":\"20\",\"passwd\":\"123456\"}", callBack);

		Thread.sleep(2000);
		long end = System.currentTimeMillis();
		System.out.println("use:" + (end - start));
		return null;
	}

	private static class MyCallback implements
			AsyncMethodCallback<getResultData_call> {
		private long start;

		public MyCallback(long start) {
			this.start = start;
		}

		// 返回结果
		@Override
		public void onComplete(getResultData_call response) {
			try {
				long end = System.currentTimeMillis();
				System.out.println((end - start) + " use "
						+ response.getResult().toString());
				System.out
						.println(start + " " + Thread.currentThread().getId());
			} catch (TException e) {
				e.printStackTrace();
			}
		}

		// 返回异常
		@Override
		public void onError(Exception exception) {
			exception.printStackTrace();
			System.out.println("onError");
		}

	}

	/**
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		HelloWorldSimpleClient client = new HelloWorldSimpleClient();
		client.startClient("Tom");
		client.addSentiKeyWords();
	}
}