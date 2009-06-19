package aufgabe6.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import aufgabe6.Gui;
import aufgabe6.net.Nachricht.KEYS;
import aufgabe6.net.Nachricht.NACHRICHTEN_TYP;

/**
 * Regelt den Nachrichtenaustausch auf Clientseite.
 * 
 */
public class ClientKommunikationsThread implements Runnable
{

	// -------Felder ----//
	private Socket socket;
	private InputStream input;
	private ObjectInputStream ois = null;
	private ObjectOutputStream output;
	private boolean abbrechen;
	private Client client;

	/**
	 * Erstellt einen neuen Kommunikationsthread, der Nachrichten mit dem Server
	 * austauscht.
	 * 
	 * @param socket
	 *            Fertig verbundener Socket, ueber den die Kommunikation laeuft.
	 * @param client
	 *            Referenz auf unseren Erzeuger.
	 */
	public ClientKommunikationsThread(Socket socket, Client client)
	{
		// Werte übernehmen und Streams an den Socket anschliessen
		this.abbrechen = false;
		this.socket = socket;
		this.client = client;
		try
		{
			input = this.socket.getInputStream();
			output = new ObjectOutputStream(this.socket.getOutputStream());
		}
		catch (Exception e)
		{
			System.err
					.println("Konnte auf dem Client einen Kommunikationsthread nicht starten");
			e.printStackTrace();
		}
		// beim Server anmelden
		Nachricht n = new Nachricht(this.client.getName(),
				NACHRICHTEN_TYP.SPIELER_PLUS_MINUS);
		n.setValue(KEYS.SPIELER_NAME, Gui.getGui()
						.getSpielerNamensFeldInhalt());
		this.sendeNachricht(n);
	}

	/**
	 * Den Kommunikationsthread sobald wie moeglich auslaufen lassen.
	 */
	public void abbrechen()
	{
		this.abbrechen = true;
	}

	/**
	 * wartet auf Nachrichten und verarbeitet sie
	 */
	@Override
	public void run()
	{
		while (!this.abbrechen && socket.isConnected())
		{
			try
			{
				if (input.available() > 0)
				{// wenn etwas zu lesen ist...
					if (ois == null)
						ois = new ObjectInputStream(input);
					// lies die Nachricht ein
					Nachricht newNachricht = (Nachricht) ois.readObject();
					// und verarbeite sie
					verarbeiteNachricht(newNachricht);
				}
			}
			catch (ClassNotFoundException e)
			{
				System.err
						.println("Fehler beim Lesen einer Nachricht: Class not found");
				e.printStackTrace();
			}
			catch (IOException e)
			{
				System.err.println("Fehler beim Lesen einer Nachricht: IO");
				e.printStackTrace();
			}
		}
		// beim Beenden...
		try
		{
			// alle Verbindungen kappen...
			ois.close();
			socket.close();
			// ...und GUI anpassen
			aufgabe6.Gui.getGui().entferneSpielfeld();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * Sendet eine vorbereitete Nachricht an den verbunden Server.
	 * 
	 * @param nachricht
	 *            die zu sendende Nachricht
	 */
	public void sendeNachricht(Nachricht nachricht)
	{
		try
		{
			output.writeObject(nachricht);
			output.flush();
		}
		catch (IOException e)
		{
			System.err.println("Nachricht konnte nicht gesendet werden");
		}
	}

	/**
	 * Sendet an den Server, dass die Figur an Position position bewegt werden
	 * soll.
	 * 
	 * @param position
	 *            die Position der zu bewegenden Figur
	 */
	public void sendeBewegungsAufforderung(int position)
	{
		Nachricht n = new Nachricht(this.client.getName(),
				NACHRICHTEN_TYP.BEWEGUNGS_AUFFORDERUNG);
		n.setValue(KEYS.FIGUREN_POSITION, "" + position);
		this.sendeNachricht(n);
	}

	/**
	 * Sendet eine Trennen-Nachricht an den Server.
	 */
	public void sendeTrennen()
	{
		Nachricht trennNachricht = new Nachricht(this.client.getName(),
				NACHRICHTEN_TYP.SPIELER_PLUS_MINUS);
		trennNachricht.setValue(KEYS.SPIELER_NUMMER, String
				.valueOf(-(this.client.getClientRelevanteDaten()
						.getMeineNummer() + 1)));
		trennNachricht.setValue(KEYS.SPIELER_NAME, Gui.getGui()
				.getSpielerNamensFeldInhalt());
		this.sendeNachricht(trennNachricht);
	}

	/**
	 * Unterscheidet die Nachrichtenarten, leitet die Nachricht an alle
	 * wichtigen Instanzen weiter und schreibt ins Log.
	 * 
	 * @param nachricht
	 *            die zu verarbeitende Nachricht
	 */
	private void verarbeiteNachricht(Nachricht nachricht)
	{
		switch (nachricht.getNachrichtenTyp())
		{
		case SPIELER_PLUS_MINUS:
			Client.getInstance().getClientRelevanteDaten().verarbeiteNachricht(
					nachricht);
			Gui.getGui().repaintSpielfeld();
			break;
		case SPIELER_X_WUERFELT_Y:
			Client.getInstance().getClientRelevanteDaten().verarbeiteNachricht(
					nachricht);
			Gui.getGui().repaintSpielfeld();
			Gui.getGui().appendToTextPane(nachricht.getLogMessage());
			break;
		case UNGUELTIGER_ZUG:
			Client.getInstance().getClientRelevanteDaten().setZugAusstehend(
					true);
			Gui.getGui().appendToTextPane(nachricht.getLogMessage());
			break;
		// TODO: Punkt 2 -> SPIELER_X_HAT_GEWONNEN verarbeiten
		}
	}
}
