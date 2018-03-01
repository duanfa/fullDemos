package com.tom.thrift.hello;

import org.apache.thrift.TException;

public class HelloWorldService implements IHelloWorldService.Iface {

	@Override
	public long addExp(long tagWeightId, int expKey, String expVal, String expOp)
			throws TException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int deleteExp(long expId) throws TException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int modifyExp(String jsonParam) throws TException {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String searchExps(long expId, long tagWeightId, int page, int size)
			throws TException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long addTag(long appkey, String tagname, String tagDescri,
			String tagclass, String tagSubclass, double defWeight,
			int calcType, int days) throws TException {
		// TODO Auto-generated method stub
		return 0;
	}
}