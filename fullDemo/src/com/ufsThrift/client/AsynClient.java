package com.ufsThrift.client;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.apache.thrift.TException;
import org.apache.thrift.async.AsyncMethodCallback;
import org.apache.thrift.async.TAsyncClientManager;
import org.apache.thrift.protocol.TCompactProtocol;
import org.apache.thrift.protocol.TProtocolFactory;
import org.apache.thrift.transport.TNonblockingSocket;
import org.apache.thrift.transport.TNonblockingTransport;

import com.thriftHello.service.IHelloWorldService.AsyncClient.sayHello_call;
import com.ufsThrift.service.UfsRecipeCall.AsyncClient;
import com.ufsThrift.service.UfsRecipeCall.AsyncClient.getMayLikeRecipe_call;
import com.ufsThrift.service.UfsRecipeCall.AsyncClient.getMergedRecipe_call;
import com.ufsThrift.service.UfsRecipeCall.AsyncClient.getRelatedRecipe_call;
import com.ufsThrift.service.UfsRecipeCall.AsyncClient.getTodayRecipe_call;

/**
 * 异步客户端
 * 
 * @author LK
 * 
 *         HelloTNonblockingServerDemo
 */
public class AsynClient {
	public static final String SERVER_IP = "localhost";
	public static final int SERVER_PORT = 10029;
	public static final int TIMEOUT = 30000;

	/**
	 * 
	 * @param userName
	 */
	public void getMayLikeRecipe(String jsonParam) {
		try {
			TAsyncClientManager clientManager = new TAsyncClientManager();
			TNonblockingTransport transport = new TNonblockingSocket(SERVER_IP, SERVER_PORT, TIMEOUT);
			TProtocolFactory tprotocol = new TCompactProtocol.Factory();
			AsyncClient asyncClient = new AsyncClient(tprotocol, clientManager, transport);
			

			CountDownLatch latch = new CountDownLatch(1);
			AsynMayLikeCallback callBack = new AsynMayLikeCallback(latch);
			
			asyncClient.getMayLikeRecipe(jsonParam, callBack);
			
			boolean wait = latch.await(30, TimeUnit.SECONDS);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void getTodayRecipe(String jsonParam) {
		try {
			TAsyncClientManager clientManager = new TAsyncClientManager();
			TNonblockingTransport transport = new TNonblockingSocket(SERVER_IP, SERVER_PORT, TIMEOUT);
			TProtocolFactory tprotocol = new TCompactProtocol.Factory();
			AsyncClient asyncClient = new AsyncClient(tprotocol, clientManager, transport);
			
			
			CountDownLatch latch = new CountDownLatch(1);
			AsynTodayCallback callBack = new AsynTodayCallback(latch);
			
			asyncClient.getTodayRecipe(jsonParam, callBack);
			
			boolean wait = latch.await(30, TimeUnit.SECONDS);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void getMergedRecipe(String jsonParam) {
		try {
			TAsyncClientManager clientManager = new TAsyncClientManager();
			TNonblockingTransport transport = new TNonblockingSocket(SERVER_IP, SERVER_PORT, TIMEOUT);
			TProtocolFactory tprotocol = new TCompactProtocol.Factory();
			AsyncClient asyncClient = new AsyncClient(tprotocol, clientManager, transport);
			
			
			CountDownLatch latch = new CountDownLatch(1);
			AsynMergedCallback callBack = new AsynMergedCallback(latch);
			
			asyncClient.getMergedRecipe(jsonParam, callBack);
			
			boolean wait = latch.await(30, TimeUnit.SECONDS);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public void getRelatedRecipe(String jsonParam) {
		try {
			TAsyncClientManager clientManager = new TAsyncClientManager();
			TNonblockingTransport transport = new TNonblockingSocket(SERVER_IP, SERVER_PORT, TIMEOUT);
			TProtocolFactory tprotocol = new TCompactProtocol.Factory();
			AsyncClient asyncClient = new AsyncClient(tprotocol, clientManager, transport);
			
			
			CountDownLatch latch = new CountDownLatch(1);
			AsynRelatedCallback callBack = new AsynRelatedCallback(latch);
			
			asyncClient.getRelatedRecipe(jsonParam, callBack);
			
			boolean wait = latch.await(30, TimeUnit.SECONDS);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public class AsynRelatedCallback implements AsyncMethodCallback<getRelatedRecipe_call> {
		private CountDownLatch latch;
		
		public AsynRelatedCallback(CountDownLatch latch) {
			this.latch = latch;
		}
		
		public void onError(Exception exception) {
			System.out.println("onError :" + exception.getMessage());
			latch.countDown();
		}
		
		@Override
		public void onComplete(getRelatedRecipe_call response) {
			
			try {
				System.out.println("AsynCall result =:" + response.getResult().toString());
			} catch (TException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				latch.countDown();
			}
		}
	}
	public class AsynMergedCallback implements AsyncMethodCallback<getMergedRecipe_call> {
		private CountDownLatch latch;
		
		public AsynMergedCallback(CountDownLatch latch) {
			this.latch = latch;
		}
		
		public void onError(Exception exception) {
			System.out.println("onError :" + exception.getMessage());
			latch.countDown();
		}
		
		@Override
		public void onComplete(getMergedRecipe_call response) {
			
			try {
				// Thread.sleep(1000L * 1);
				System.out.println("AsynCall result =:" + response.getResult().toString());
			} catch (TException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				latch.countDown();
			}
		}
	}
	public class AsynTodayCallback implements AsyncMethodCallback<getTodayRecipe_call> {
		private CountDownLatch latch;
		
		public AsynTodayCallback(CountDownLatch latch) {
			this.latch = latch;
		}
		
		public void onError(Exception exception) {
			System.out.println("onError :" + exception.getMessage());
			latch.countDown();
		}
		
		@Override
		public void onComplete(getTodayRecipe_call response) {
			
			try {
				// Thread.sleep(1000L * 1);
				System.out.println("AsynCall result =:" + response.getResult().toString());
			} catch (TException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				latch.countDown();
			}
		}
	}
	public class AsynMayLikeCallback implements AsyncMethodCallback<getMayLikeRecipe_call> {
		private CountDownLatch latch;

		public AsynMayLikeCallback(CountDownLatch latch) {
			this.latch = latch;
		}

		public void onError(Exception exception) {
			System.out.println("onError :" + exception.getMessage());
			latch.countDown();
		}

		@Override
		public void onComplete(getMayLikeRecipe_call response) {
			
			try {
				// Thread.sleep(1000L * 1);
				System.out.println("AsynCall result =:" + response.getResult().toString());
			} catch (TException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				latch.countDown();
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		gussYouLike();
//		todayRecipe();
		mergedRecipe();
//		relatedRecipe();
	}

	private static void gussYouLike() {
		AsynClient client = new AsynClient();
		String jsonParam = "{\"uid\":\"57902338fa2f94b3238b54b5\",\"topN\":\"5\",\"appkey\":\"172139920\",\"method\":\"getMayLikeRecipe\",\"clientIp\":\"222.87.72.81\"}";

		client.getMayLikeRecipe(jsonParam);
	}
	private static void todayRecipe() {
		AsynClient client = new AsynClient();
		String jsonParam = "{\"uid\":\"591ad4e6222a79-0012002f23\",\"topN\":\"5\",\"appkey\":\"172139920\",\"method\":\"getMayLikeRecipe\",\"clientIp\":\"54.222.180.77\"}";
		
		client.getTodayRecipe(jsonParam);
	}
	private static void mergedRecipe() {
		AsynClient client = new AsynClient();
		String jsonParam = "{\"uid\":\"aaaaaaaaaaaa222222222222\",\"topN\":\"4\",\"appkey\":\"172139920\",\"method\":\"getMayLikeRecipe\",\"clientIp\":\"1.180.209.196\"}";
		
		client.getMergedRecipe(jsonParam);
	}
	private static void relatedRecipe() {
		AsynClient client = new AsynClient();
		String jsonParam = "{\"uid\":\"aaaaaaaaaaaa222222222222\",\"recipeId\":\"2f636f6e74656e742f7566732f7a682f6262712f726563697065732f6a7968786a6b78792f6a63723a636f6e74656e74\",\"topN\":\"8\",\"appkey\":\"172139920\",\"method\":\"getRelatedRecipe\",\"clientIp\":\"54.222.141.4\"}";
//		String jsonParam2 = "{\"uid\":\"5a6ed0c8859e3e06826a2fb4\",\"recipeId\":\"2f636f6e74656e742f7566732f7a682f726563697065732f6a686a6273646c782f6a63723a636f6e74656e74\",\"topN\":\"8\",\"appkey\":\"172139920\",\"method\":\"getRelatedRecipe\",\"clientIp\":\"54.222.141.4\"}";
		
		client.getRelatedRecipe(jsonParam);
	}
}