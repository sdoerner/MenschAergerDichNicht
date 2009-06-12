package aufgabe6.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

public class Client {
	private Vector<ServerInfo> serverInfos;
	private String name;
    private Client() 
    {
        serverInfos = new Vector<ServerInfo>();
    }
    
    
    
    private static Client instance;
    public static Client getInstance()
    {
        if (instance ==null)
            instance = new Client();
        return instance;
    }
	
    private void initialisiereKommunikationsThread(Socket socket)
    {
        ClientKommunikationsThread clientKom = new ClientKommunikationsThread(socket);
        Thread thread = new Thread(clientKom);
        thread.start();
    }
	    
    public void verbinde(String theIP, String name) {
        this.name = name;
    	Socket so;
		try {
			InetSocketAddress inet = new InetSocketAddress(theIP, 9999);
			so = new Socket();
			so.connect(inet);
			initialisiereKommunikationsThread(so);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }
    public Vector<ServerInfo> getServerInfos()
    {
        return serverInfos;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public class ServerInfo
    {
        private String name;
        private String ip;
        public ServerInfo(String name, String ip)
        {
            this.name = name;
            this.setIp(ip);
        }
        public String getName()
        {
            return name;
        }
        public void setIp(String ip)
        {
            this.ip = ip;
        }
        public String getIp()
        {
            return ip;
        }
        
    }
}
