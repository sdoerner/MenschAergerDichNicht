package aufgabe6.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import aufgabe6.Gui;
import aufgabe6.MenschMain;
import aufgabe6.net.Nachricht.KEYS;
import aufgabe6.net.Nachricht.NACHRICHTEN_TYP;

/**
 * Regelt den Nachrichtenaustausch auf Serverseite. Steht zu genau einem Client in Verbindung.
 * @author sdoerner
 *
 */
public class ServerKommunikationsThread implements Runnable
{
    private Socket socket;
    private Server server;
    private InputStream is;
    private OutputStream os;
    private ObjectInputStream ois=null;
    private ObjectOutputStream oos;
    private boolean abbrechen;
    
    /**
     * Erstellt einen neuen Kommunikationthread.
     * @param socket Verbundener Socket, ueber den kommuniziert werden soll.
     * @param server Luke, ich bin dein Vater!
     */
    public ServerKommunikationsThread(Socket socket, Server server)
    {
    	//uebergebene Werte uebernehmen
        this.abbrechen = false;
        this.socket = socket;
        this.server = server;
        try{
        	//Streams auf die Sockets legen
	        is=this.socket.getInputStream();
	        os=this.socket.getOutputStream();
	        oos = new ObjectOutputStream(os);
        } catch (Exception e)
        {
            System.err.println("Konnte auf dem Server einen Kommunikationsthread nicht starten");
        }
        
    }
    
    
    //--------------------------  Empfangs-Teil -------------------------------------//

    /*
     * Wartet auf ankommende Nachrichten und verarbeitet diese.
     */
    @Override
    public void run()
    {
     //dauerhaft auf Nachrichten vom Client warten
        while (!this.abbrechen && socket.isConnected())
        {
            try
            {
                if (is.available() > 0)//wenn ein Paket ankommt...
                {
                	if (ois==null)
                		ois = new ObjectInputStream(is);
                	//Nachricht extrahieren...
                    Nachricht n = (Nachricht) ois.readObject();
                    //und entsprechend verarbeiten
					switch (n.getNachrichtenTyp())
					{
					case SPIELER_PLUS_MINUS:
						bearbeiteSpielerPlusMinus(n);
						break;
					case BEWEGUNGS_AUFFORDERUNG:
						bearbeiteBewegungsAufforderung(n);
						break;
					}
				}

            } catch (Exception e)
            {
                System.err.println("exp: "+ e.getMessage());
                e.printStackTrace();
            }
        }
        //beim Abbrechen alle Verbindungen schliessen
    		try
    		{
    			ois.close();
    			socket.close();
    			
    		}catch (IOException e){
    			e.printStackTrace();
    		}
    }
    
    /**
     * Eine eingehende Bewegungsaufforderungsnachricht verarbeiten.
     * @param nachricht Die eingehende Nachricht.
     */
    private void bearbeiteBewegungsAufforderung(Nachricht nachricht)
    {
    	//welche Figur soll bewegt werden?
        int position = Integer.parseInt(nachricht.getValue(KEYS.FIGUREN_POSITION));
        //gewaehlte Figurenposition an das Spiel uebergeben...
        MenschMain.getDasSpiel().setGewaehlteFigurenPosition(position);
        //und Thread aufwecken
        synchronized(MenschMain.getDasSpiel())
        {
        	MenschMain.getDasSpiel().notify();
        }
    }
    
    /**
     * Paket fuer neu erstellte und beendete Verbindungen verarbeiten.
     * @param nachricht Die eingegangene Nachricht.
     */
    private void bearbeiteSpielerPlusMinus(Nachricht nachricht)
    {
        Nachricht nOut = new Nachricht(server.getServerName(), NACHRICHTEN_TYP.SPIELER_PLUS_MINUS);
        boolean trenne = false;
        
    	if ((nachricht.getValue(KEYS.SPIELER_NUMMER) == null) || (Byte.parseByte(nachricht.getValue(KEYS.SPIELER_NUMMER)) > 0)) {
    		//neu verbundener Spieler
	    	int index = MenschMain.getDasSpiel().verbindeSpieler(nachricht.getValue(KEYS.SPIELER_NAME),this);
	    	
	        nOut.setValue(KEYS.SPIELER_NUMMER, ""+ (index+1));
	        nOut.setValue(KEYS.SPIELER_NAME, "" + nachricht.getValue(KEYS.SPIELER_NAME));
	        nOut.setValue(KEYS.FIGUREN, "" + MenschMain.getDasSpiel().zuClientSicht());
    	} else {
    		//Spieler, der sich von uns trennen moechte :-(
    		byte spielerNummer = Byte.parseByte(nachricht.getValue(KEYS.SPIELER_NUMMER));
    		
    		MenschMain.getDasSpiel().trenneSpieler((byte)-(spielerNummer + 1));
	        nOut.setValue(KEYS.SPIELER_NUMMER, nachricht.getValue(KEYS.SPIELER_NUMMER));
	        nOut.setValue(KEYS.SPIELER_NAME, nachricht.getValue(KEYS.SPIELER_NAME));
	        trenne = true;
    	}
    	
    	//Andere Clients ueber den Familienzuwachs oder die Beerdigung informieren.
        this.server.sendeNachrichtAnAlleClients(nOut);
		if (trenne)
		{
			this.server.trenneClient(this);
			this.abbrechen = true;
			if (MenschMain.getDasSpiel().aktuelleSpielerNummer()==-Byte.parseByte(nachricht.getValue(KEYS.SPIELER_NUMMER))-1){
				//Wenn der aktuelle Spieler disconnected, müssen wir nicht mehr auf sein Würfelergebnis warten.
				MenschMain.getDasSpiel().setGewaehlteFigurenPosition(-2);
				synchronized (MenschMain.getDasSpiel())
				{
					MenschMain.getDasSpiel().notify();
				}
			}
		}
		if (!trenne)
			Gui.getGui().setStartenKnopfZustand(true);
    }
    
    
    //---------------------------------  sende-Teil ------------------------------------//
    
    /**
     * Sendet eine vorbereitete Nachricht an den verbundenen Client.
     * @param nachricht Die zu sendende Nachricht.
     */
    public void sendeNachricht(Nachricht nachricht)
    {
        try{
	        oos.writeObject(nachricht);
	        oos.flush();
        }
        catch (IOException e){
            System.err.println("Fehler beim Senden der Nachricht.");
        }
    }
    
    /**
     * Informiere den Client, dass der gewaehlte Zug ungueltig ist.
     */
    public void sendeZugUngueltig()
    {
    		Nachricht n = new Nachricht(this.server.getServerName(), NACHRICHTEN_TYP.UNGUELTIGER_ZUG);
            sendeNachricht(n);
    }
    
    /**
     * Sende den aktuellen Spielstatus.
     * @param spielerName Name des Spielers.
     * @param spielerIndex Index(Nummer) des Spielers.
     * @param wuerfelZahl Die gewuerfelte Augenzahl.
     */
    public void sendeWuerfelZahl(String spielerName, int spielerIndex, int wuerfelZahl) {
		Nachricht wuerfelNachricht = new Nachricht(this.server.getServerName(), NACHRICHTEN_TYP.SPIELER_X_WUERFELT_Y);
		wuerfelNachricht.setValue(KEYS.SPIELER_NAME, spielerName);
		wuerfelNachricht.setValue(KEYS.SPIELER_NUMMER, ""+spielerIndex);
		wuerfelNachricht.setValue(KEYS.WUERFELZAHL, ""+wuerfelZahl);
        wuerfelNachricht.setValue(KEYS.FIGUREN, "" + MenschMain.getDasSpiel().zuClientSicht());
		this.server.sendeNachrichtAnAlleClients(wuerfelNachricht);
    }

    /**
     * Informiere den Client, dass ein Spieler gewonnen hat.
     * @param spielerName Name des Gewinners.
     */
    public void sendeSpielerHatGewonnen(String spielerName)
    {
		Nachricht n = new Nachricht(this.server.getServerName(), NACHRICHTEN_TYP.SPIELER_X_HAT_GEWONNEN);
		n.setValue(KEYS.SPIELER_NAME, spielerName);
		this.server.sendeNachrichtAnAlleClients(n);
    }
}
