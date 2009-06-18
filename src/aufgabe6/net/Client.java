package aufgabe6.net;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Vector;

import javax.swing.JOptionPane;

import aufgabe6.ClientSicht;

/**
 * Diese Klasse sorgt fuer das Verbinden mit dem Server und erstellt dabei einen
 * Kommunikationsthread, ueber den dann Nachrichten ausgetauscht werden koennen.
 * @author sdoerner
 * 
 */
public class Client
{
	// -------------- Singleton -----------//
	private Client()
	{
		serverInfos = new Vector<ServerInfo>();
		this.clientRelevanteDaten = new ClientSicht();
	}

	private static Client instance;

	public static Client getInstance()
	{
		if (instance == null)
			instance = new Client();
		return instance;
	}

	// ------------- Felder -------------//
	// Liste der Server, zu denen wir uns verbinden können (deren Namen werden
	// in der GUI angezeigt)
	private Vector<ServerInfo> serverInfos;
	// eingetragener Spielername beim Verbinden
	private String name;
	// Repraesentation des aktuellen Spielstandes beim Client
	private ClientSicht clientRelevanteDaten = null;
	// Thread fuer den Nachrichtenaustausch
	private ClientKommunikationsThread clientKommunikationsThread;

	// -------------- Zugriffsmethoden -------------//
	public Vector<ServerInfo> getServerInfos()
	{
		return serverInfos;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public ClientSicht getClientRelevanteDaten()
	{
		return clientRelevanteDaten;
	}

	public ClientKommunikationsThread getClientKommunikationsThread()
	{
		return clientKommunikationsThread;
	}

	// ------------ Funktionalitaet -------//

	/**
	 * loescht alle Daten ueber das aktuelle Spiel
	 */
	public void loescheClientRelevanteDaten()
	{
		this.clientRelevanteDaten = new ClientSicht();
		this.clientKommunikationsThread.abbrechen();
		this.clientKommunikationsThread = null;
	}

	/**
	 * Initialisiert einen neuen ClientKommunikationsThread und startet diesen.
	 * @param socket Verbundener socket ueber den der Thread kommunizieren soll.
	 */
	private void initialisiereKommunikationsThread(Socket socket)
	{
		clientKommunikationsThread = new ClientKommunikationsThread(socket,
				this);
		Thread thread = new Thread(clientKommunikationsThread);
		thread.start();
	}

	/**
	 * Verbindet den Client mit einem Server und erstellt einen Kommunikationskanal zwischen diesen.
	 * @param ipAdresse IP-Adresse ("xxx.xxx.xxx.xxx") bzw. gueltiger Alias aus /etc/hosts (z.B. "localhost").
	 * @param name Name des Clients, unter dem wir uns mit dem Server verbinden wollen.
	 * @return Wahr genau dann, wenn die Verbindung erfolgreich war.
	 */
	public boolean verbinde(String ipAdresse, String name)
	{
		this.name = name;
		Socket so;
		try
		{
			InetSocketAddress inet = new InetSocketAddress(ipAdresse, 9999);
			so = new Socket();
			//wir wollen nicht zu lange auf nicht-vorhandene Server warten
			so.setSoTimeout(1000);
			so.connect(inet);
			initialisiereKommunikationsThread(so);
		}
		catch (UnknownHostException e1)
		{
			JOptionPane.showMessageDialog(null,
					"Fehler: Der angegebene Server ist nicht erreichbar",
					"Mensch aergere dich nicht : Fehler",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		catch (IOException e1)
		{
			JOptionPane.showMessageDialog(null,
					"Fehler: Der Server ist nicht mehr erreichbar",
					"Mensch aergere dich nicht : Fehler",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	/**
	 * Veranlasst eine Trennung des Clients vom Server. Wickelt dabei ein
	 * "Beenden-Protokoll" ab, sodas der aktuelle Spielfluss nicht gestoert
	 * wird.
	 */
	public void trenne()
	{
		this.clientKommunikationsThread.sendeTrennen();
	}

	/**
	 * Kapselt die Informationen ueber einen Server, mit dem wir uns verbinden
	 * können, ab.
	 * 
	 */
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
