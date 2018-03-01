package com.thriftHello.service;

import org.apache.thrift.TException;

import com.thriftHello.service.IHelloWorldService.Iface;

/**
 * 
 * @author LK
 * 
 */
public class HelloWorldImpl implements Iface {

	public HelloWorldImpl() {

	}

	public String sayHello(String username) throws TException {
		return "Hi," + username + " welcome to my blog www.jmust.com";
	}

}