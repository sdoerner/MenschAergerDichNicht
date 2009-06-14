package aufgabe6.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import aufgabe6.Gui;
import aufgabe6.net.Nachricht.KEYS;
import aufgabe6.net.Nachricht.NACHRICHTEN_TYP;


/**
 * regelt die clientseitige Kommunikation
 *
 */
public class ClientKommunikationsThread implements Runnable
{
    private Socket socket;
    private InputStream input;
    private ObjectInputStream ois = null;
    private ObjectOutputStream output;
    private boolean abbrechen;
    private Client client;
    
    /**
     * Konstruktur, initialisiert Socket und Port
     * @param socket
     */
    public ClientKommunikationsThread(Socket socket, Client client) {
        this.abbrechen = false;
        this.socket = socket;
        this.client = client;
        try {
	        input = this.socket.getInputStream();
	        output = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (Exception e) {
            System.err.println("Konnte auf dem Client einen Kommunikationsthread nicht starten");
            e.printStackTrace();
        }
        Nachricht n = new Nachricht(this.client.getName(), NACHRICHTEN_TYP.SPIELER_PLUS_MINUS);
        n.setValue(KEYS.SPIELER_NAME, Gui.getGui().getSpielerNamensFeldInhalt());
        this.sendeNachricht(n);
    }

    /**
     * wartet auf Nachrichten und liest sie ein
     */
    @Override
    public void run() {
        while (!this.abbrechen) {
            try {
                if (input.available() > 0) {
                	if (ois==null)
                		ois = new ObjectInputStream(input);
                    Nachricht newNachricht = (Nachricht)ois.readObject();
                    
                    verarbeiteNachricht(newNachricht);
                }
            } catch (Exception e) {
                System.err.println("Fehler beim Lesen einer Nachricht");
                e.printStackTrace();
            }
        }
    }
    
    /**
     * sendet eine Nachricht
     * @param theNachricht die zu sendende Nachricht
     */
    public void sendeNachricht(Nachricht theNachricht) {
        try {
        	output.writeObject(theNachricht);
        	output.flush();
        } catch (IOException e) {
            System.err.println("Nachricht konnte nicht gesendet werden");
        }
    }
    
    public void sendeBewegungsAufforderung(int x)
    {
        Nachricht n = new Nachricht(this.client.getName(),NACHRICHTEN_TYP.BEWEGUNGS_AUFFORDERUNG);
        n.setValue(KEYS.FIGUREN_POSITION, ""+x);
        System.out.println("new Value: "+ n.getValue(KEYS.FIGUREN_POSITION));
        System.out.println(n.getSender());
        this.sendeNachricht(n);
    }
    
    /**
     * leitet die Nachricht an alle wichtigen Instanzen weiter und schreibt ins Log
     * @param theNachricht die zu verarbeitende Nachricht
     */
    private void verarbeiteNachricht(Nachricht theNachricht) {
    	switch (theNachricht.getNachrichtenTyp()) {
    	case SPIELER_PLUS_MINUS:
    		// Client.getInstance().getClientSicht().verarbeiteNachricht();
			// TODO aktualisiere GUI mit den neuen Figurenwerten
			// TODO schreibe dies ins Log
    		System.out.println("received Server hello from " + theNachricht.getSender()); 		// DEBUG
    		System.out.println("Spielernummer: " + theNachricht.getValue(KEYS.SPIELER_NUMMER));
    		break;
    	case SPIELER_X_WUERFELT_Y:
	    	// Client.getInstance().getClientSicht().verarbeiteNachricht();
			// TODO aktualisiere GUI mit den neuen Figurenwerten
			// TODO schreibe dies ins Log
    		break;
    	case UNGUELTIGER_ZUG:
    		// TODO fuehre letzte Operation nochmal aus (wenn ich selbst zuletzt dran gewesen, nochmal Klick erforderlich, ansonsten warten)
    		// TODO schreibe dies ins Log
    		break;
    	case SPIELER_X_HAT_GEWONNEN:
			break;	//	TODO fuehre eine Spiel-vorbei-Prozedur aus und schreibe das ins Log
    	}
    }
}
