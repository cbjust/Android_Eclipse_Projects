package com.cb.structure.http.json;

public abstract class HttpEventHandler <T> {
	
	public abstract void HttpSucessHandler(T result);
	
	public abstract void HttpFailHandler();
	
}
