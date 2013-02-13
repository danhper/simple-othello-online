package com.tuvistavie.othello.communication;

public class HostInfo {
	private String host;
	private int port;
	
	public HostInfo() {
        this.host = DefaultConnectionInfos.HOST;
        this.port = DefaultConnectionInfos.PORT;
    }
	
	public HostInfo(String ip) {
		this.host = ip;
		this.port = DefaultConnectionInfos.PORT;
	}
	
	public HostInfo(String host, int port) {
		this.host = host;
		this.port = port;
	}
	
	public String getHost() {
		return host;
	}
	
	public int getPort() {
		return port;
	}
}
