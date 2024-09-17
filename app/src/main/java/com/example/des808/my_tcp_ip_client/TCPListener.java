package com.example.des808.my_tcp_ip_client;

public interface TCPListener {
	public void onTCPMessageRecieved(String message);
	public void onTCPConnectionStatusChanged(boolean isConnectedNow);

  /* public void onTCPMessageRecievedInt(Integer inMsgInt);*/
   public void onTCPMessageRecievedChar(char inMsgChar);
}
