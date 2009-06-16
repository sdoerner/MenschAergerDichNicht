package aufgabe6.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.JOptionPane;

import aufgabe6.ClientSicht;

public class Client {
	private Vector<ServerInfo> serverInfos;
	private String name;
	private ClientSicht clientRelevanteDaten = null;
	private ClientKommunikationsThread clientKommunikationsThread;
	
    public ClientKommunikationsThread getClientKommunikationsThread()
	{
		return clientKommunikationsThread;
	}

	private Client() 
    {
        serverInfos = new Vector<ServerInfo>();
        this.clientRelevanteDaten = new ClientSicht();
    }
    
	/**
	 * loescht alle Daten des aktuellen Spiels
	 */
    public void loescheClientRelevanteDaten() {
    	this.clientRelevanteDaten = new ClientSicht();
    	this.clientKommunikationsThread = null;
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
        clientKommunikationsThread = new ClientKommunikationsThread(socket, this);
        Thread thread = new Thread(clientKommunikationsThread);
        thread.start();
    }
	    
    public boolean verbinde(String theIP, String name) {
        this.name = name;
    	Socket so;
		try {
			InetSocketAddress inet = new InetSocketAddress(theIP, 9999);
			so = new Socket();
			so.setSoTimeout(1000);
			so.connect(inet);
			initialisiereKommunikationsThread(so);
		} catch (UnknownHostException e1) {
			JOptionPane.showMessageDialog(null, "Fehler: Der angegebene Server ist nicht erreichbar", "Mensch aergere dich nicht : Fehler", JOptionPane.ERROR_MESSAGE);
			return false;
		} catch (IOException e1) {
			JOptionPane.showMessageDialog(null, "Fehler: Der Server ist nicht mehr erreichbar", "Mensch aergere dich nicht : Fehler", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
    }
    
    public void trenne() {
        this.clientKommunikationsThread.sendeTrennen();
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
    
    public ClientSicht getClientRelevanteDaten() {
		return clientRelevanteDaten;
	}
}
