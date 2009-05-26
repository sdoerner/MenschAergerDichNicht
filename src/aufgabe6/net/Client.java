package aufgabe6.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;

public class Client {
	private int port;
	
    private void initialisiereKommunikationsThread(Socket socket)
    {
        ClientKommunikationsThread clientKom = new ClientKommunikationsThread(socket);
        Thread thread = new Thread(clientKom);
        thread.start();
    }
	
    public Client(int port) {
        this.port = port;
    }
    
    public void verbinde(String theIP) {
    	Socket so;
		try {
			InetSocketAddress inet = new InetSocketAddress(theIP, port);
			so = new Socket();
			so.connect(inet);
			initialisiereKommunikationsThread(so);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
}
