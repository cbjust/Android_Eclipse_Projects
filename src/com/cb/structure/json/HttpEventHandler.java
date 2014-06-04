package com.cb.structure.json;

public abstract class HttpEventHandler <T> {
	
	public abstract void HttpSucessHandler(T result);
	
	public abstract void HttpFailHandler();
	
}
