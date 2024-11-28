package com.example.des808.my_tcp_ip_client;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.des808.my_tcp_ip_client.interfaces.TCPListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class TCPCommunicator {
	private static TCPCommunicator uniqInstance;
	private static String serverHost;
	private static int serverPort;
	private static List<TCPListener> allListeners;
	private static BufferedWriter out;
	private static BufferedReader in;
	private static InputStream inSocketStream;
	private static Socket s;
	private static Handler UIHandler;
	private static Context appContext;
	private TCPCommunicator()
	{
		allListeners = new ArrayList<TCPListener>();
	}
	public static TCPCommunicator getInstance()
	{
		if(uniqInstance==null)
		{
			uniqInstance = new TCPCommunicator();
		}
		return uniqInstance;
	}
	public  TCPWriterErrors init(String host,int port)
	{
		setServerHost(host);
		setServerPort(port);
		InitTCPClientTask task = createInitTCPClientTask();
		task.execute(new Void[0]);
		return TCPWriterErrors.OK;
	}
	public static  TCPWriterErrors writeToSocket(final String i, Handler handle,Context context)
	{
		UIHandler=handle;
		appContext=context;
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				try
				{
			        String outMsg = i.toString() ;
			        out.write(  outMsg  );
			        out.flush();
					//Toast.makeText( appContext, i, Toast.LENGTH_LONG ).show();
			        Log.d("TcpClientOutputMessage", "sent: " + outMsg);
				}
				catch(Exception e)
				{
					UIHandler.post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(appContext ,"a problem has occured, the app might not be able to reach the server", Toast.LENGTH_SHORT).show();
						}
					});
				}
			}
		};
		Thread thread = new Thread(runnable);
		thread.start();
		return TCPWriterErrors.OK;
	}

	public static void addListener(TCPListener listener)
	{
		allListeners.clear();
		allListeners.add(listener);
	}
	public static void removeAllListeners()
    {
		try{allListeners.clear();}catch (Exception e) {e.printStackTrace();}
	}
	public static void closeStreams()
	{
		try
		{
			if (s!=null)s.close();
			if (in!=null)in.close();
			if (out!=null)out.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	public static String getServerHost() {
		return serverHost;
	}
	public static void setServerHost(String serverHost) {
		TCPCommunicator.serverHost = serverHost;
	}
	public static int getServerPort() {
		return serverPort;
	}
	public static void setServerPort(int serverPort) {
		TCPCommunicator.serverPort = serverPort;
	}

    public InitTCPClientTask createInitTCPClientTask() {
        return new InitTCPClientTask();
    }

	public enum TCPWriterErrors{UnknownHostException,IOException,otherProblem,OK}

	public static class InitTCPClientTask extends AsyncTask<Void, Void, Void> {
		private InitTCPClientTask() { }
		@Override
		protected Void doInBackground(Void... params) {
			try {
				s = new Socket(getServerHost(), getServerPort());
				InputStream in = s.getInputStream();
				s.setKeepAlive(true);
				s.setSoTimeout(1200000);
				s.setTcpNoDelay(true);
				s.setSoLinger(true, 1200000);
				out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				byte[] buffer = new byte[65535];
				for(TCPListener listener:allListeners) listener.onTCPConnectionStatusChanged(true);

				do {
					int count = in.read(buffer);
					if (count > 0) {
						for (TCPListener listener : allListeners) {
							listener.onTCPMessageRecievedByteBuffer(buffer, count);
						}
						for (int i = 0; i < count+1;i++){
							buffer[i] = 0;
						}
						//Log.d("TcpClientInputMessage", "sent: " + Arrays.toString(buffer));
					}
				} while (true);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return null;
		}
	}


}