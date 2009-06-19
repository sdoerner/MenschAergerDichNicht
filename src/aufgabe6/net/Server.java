package aufgabe6.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import aufgabe6.Gui;
import aufgabe6.MenschMain;
import aufgabe6.Spiel;

/**
 * Diese Klasse dient dem erstellen eines Servers, der auf Port 9999 lauscht. Es
 * koennen neue Clients verbunden und auch wieder getrennt werden. Fuer jeden
 * von diesen wird ein seperater Kommunikations-Thread eingerichtet.
 * 
 * @author sdoerner
 * 
 */
public class Server
{
	// Socket fuer ankommende Verbindungen
	private ServerSocket lauschSocket;
	// Port, auf dem gelauscht werden soll
	private int port;
	private String serverName;
	// alle verwalteten Kommunikationsthreads, die mit den Clients verbunden
	// sind.
	private List<ServerKommunikationsThread> clientListe = null;
	private boolean lauschenAbbrechen;

	/**
	 * @return Den Namen des Servers.
	 */
	public String getServerName()
	{
		return serverName;
	}

	/**
	 * Initialisiert einen neuen Kommunikationsthread, der mit einem Client
	 * Nachrichten austauscht.
	 * 
	 * @param socket
	 *            Netzwerk-Socket, auf dem der Nachrichtenaustausch stattfinden
	 *            soll.
	 */
	private void initialisiereNeuenClientThread(Socket socket)
	{
		// TODO: Punkt 1 -> KommunikationsThread erstellen, in die Liste aufnehmen und starten.
	}

	/**
	 * Erstellt einen neuen Server.
	 * 
	 * @param port
	 *            Port auf, dem der Server später lauschen soll.
	 * @param name
	 *            Name des Servers.
	 */
	public Server(int port, String name)
	{
		this.port = port;
		this.serverName = name;
		this.clientListe = new LinkedList<ServerKommunikationsThread>();
	}

	/**
	 * Veranlasst den Server zu einem bind() mit dem angegebenen Port, auf dem
	 * er fortan auf Client-Anfragen lauscht.
	 * 
	 * @return Wahr, wenn der Socket erfolgreicht geoeffnet werden konnte.
	 */
	public boolean lausche()
	{
		this.lauschenAbbrechen = false;
		try
		{
			this.lauschSocket = new ServerSocket(this.port);
		}
		catch (IOException e)
		{
			//tritt z.B. auf, wenn der Port schon belegt ist.
			System.err.println("Konnte keinen Socket auf Port " + port
					+ "oeffnen.");
			return false;
		}
		
		//neuer Thread mit dem Ohr an der Netzwerk-Schnittstelle
		Thread thread = new Thread()
		{
			public void run()
			{
				Gui.getGui().appendToTextPane("Server wurde erstellt.");
				
				// TODO: Punkt 1 -> Dauerhaft auf neue Daten Warten, 
				//						bis lauschenAbbrechen gesetzt wird
				//			Tipp: Für jede akzeptierte Verbindung muss ein neuer 
				//				  ClientThread initialisiert werden

				//bei "abbrechen" Verbindung beenden
				try
				{
					lauschSocket.close();
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			}
		};
		thread.start();
		return true;
	}

	/**
	 * Laesst den lausch-Thread auslaufen.
	 */
	public void schliessen()
	{
		this.lauschenAbbrechen = true;
	}

	/**
	 * Sendet eine Nachricht an alle verbunden Clients.
	 * @param dieNachricht
	 */
	public void sendeNachrichtAnAlleClients(Nachricht dieNachricht)
	{
		for (ServerKommunikationsThread itServKom : clientListe)
			itServKom.sendeNachricht(dieNachricht);
	}

	/**
	 * Entfernt einen Client aus der Clientliste und beendet ggf. das aktuelle Spiel.
	 * @param serverKom
	 */
	public void trenneClient(ServerKommunikationsThread serverKom)
	{
		this.clientListe.remove(serverKom);
		if (this.clientListe.isEmpty())
		{
			// Spiel-Thread auslaufen lassen
			MenschMain.getDasSpiel().abbrechen();
			Spiel s = MenschMain.getDasSpiel();
			synchronized (s)
			{
				s.notify();
			}
			// und neuen Thread starten
			MenschMain.spielNeustarten();
			//starten koennen wir aber ohne Spieler noch nicht
			Gui.getGui().setStartenKnopfZustand(false);
		}
	}
}
