package aufgabe6.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
	private int port;
	
    public Client(int port) {
        this.port = port;
    }
    
    public void lausche(String theIP) {
    	Socket so;
		try {
			so = new Socket(theIP, port);
			BufferedReader br = new BufferedReader(new InputStreamReader(so.getInputStream()));
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
}
