package com.example.des808.my_tcp_ip_client.interfaces;

public interface TCPListener {
	public void onTCPMessageRecieved(String message);
	public void onTCPConnectionStatusChanged(boolean isConnectedNow);

    void onTCPMessageRecievedByteBuffer(byte[] inMsgByteBuffer, int count);
}
