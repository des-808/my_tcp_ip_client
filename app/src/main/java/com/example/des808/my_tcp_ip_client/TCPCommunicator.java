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
import java.io.InputStreamReader;
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
		InitTCPClientTask task = new InitTCPClientTask();
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
			        Log.d("TcpClient", "sent: " + outMsg);
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
			s.close();
			in.close();
			out.close();
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

	public class InitTCPClientTask extends AsyncTask<Void, Void, Void>
	{
		public InitTCPClientTask() { }
		@Override
		protected Void doInBackground(Void... params) {
			try
			{
				   s = new Socket(getServerHost(), getServerPort());
		          in = new BufferedReader(new InputStreamReader(s.getInputStream()));
				 out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
				 StringBuilder inMsg = new StringBuilder();
				final int length = 65535;
				char[] charBuffer = new char[length];
		         for(TCPListener listener:allListeners) listener.onTCPConnectionStatusChanged(true);

		        while(true)
		        {
					/*int count = in.read(charBuffer, 0, length);
					if(count >= 0){
						for (TCPListener listener : allListeners) listener.onTCPMessageRecievedCharBuffer(charBuffer,count,length);//*/
						/////
						int count = in.read(charBuffer, 0, 65535);
						int index = 0;
						if(count >= 0){
							do {
								if((charBuffer[index])!='\n'&&(charBuffer[index])!=0x0D&&(charBuffer[index]>=0&&(charBuffer[index])<32))
								{
									inMsg.append(DecimalToHex.toHex(charBuffer[index]));
								}
								else
								{
									inMsg.append((charBuffer[index]));
								}
								index++;
							}while (count!=index);
							for (TCPListener listener : allListeners) listener.onTCPMessageRecieved(String.valueOf((inMsg)));//
							inMsg.delete(0,count); // обнуляем буфер
							count = -1;// обнуляем счетчик
					}
		        }
			}
			catch (UnknownHostException e) 		{e.printStackTrace();}
			catch (IOException e) 				{e.printStackTrace();}
			catch (IndexOutOfBoundsException e)	{e.printStackTrace();}
			return null;
		}
	}
	public enum TCPWriterErrors{UnknownHostException,IOException,otherProblem,OK}

	/*public static void EventBusTransmitterInt(int idf){
		message_event event = new message_event();
		event.setMessage( idf );
		EventBus.getDefault().post( event );
	}*/
}
