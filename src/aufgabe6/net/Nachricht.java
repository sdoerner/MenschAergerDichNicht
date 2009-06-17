package aufgabe6.net;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * Abstrakte Basisklasse fuer alle Nachrichten, d.h. Inhalte von gesendeten
 * Paketen.
 * 
 * @author sdoerner
 * 
 */
public class Nachricht implements Serializable
{
    private static final long serialVersionUID = -6825244032568593070L;

    public static enum KEYS
    {
        SPIELER_NAME,
        SPIELER_NUMMER,
        FIGUREN_POSITION,
        WUERFELZAHL,
        FIGUREN,
        GEKLICKTES_FELD
    }
    
    public static enum NACHRICHTEN_TYP
    {
        SPIELER_PLUS_MINUS,		// Client <-> Server
        SPIELER_X_WUERFELT_Y,	// Client <- Server inklusive komplette Figurenuebermittlung und evt. wer gewonnen hat
        BEWEGUNGS_AUFFORDERUNG,	// Client -> Server
        UNGUELTIGER_ZUG,		// Client <- Server
        SPIELER_X_HAT_GEWONNEN	// Client <- Server
    }
    
    private String sender;
    private TreeMap<KEYS, String> data;
    private NACHRICHTEN_TYP nachrichtenTyp;

    public Nachricht(String sender, NACHRICHTEN_TYP typ) throws NullPointerException
    {
        if (sender==null)
            throw new NullPointerException();
        this.sender = sender;
        data = new TreeMap<KEYS, String>();
        this.nachrichtenTyp = typ;
    }

    public String getSender()
    {
        return sender;
    }

    public String getValue(KEYS key)
    {
        return data.get(key);
    }
    
    public void setValue(KEYS key, String value)
    {
        data.put(key, value);
    }

    public void setNachrichtenTyp(NACHRICHTEN_TYP nachrichtenTyp)
    {
        this.nachrichtenTyp = nachrichtenTyp;
    }

    public NACHRICHTEN_TYP getNachrichtenTyp()
    {
        return nachrichtenTyp;
    }
    
    public String getLogMessage() {
    	switch (this.nachrichtenTyp) {
    	case SPIELER_X_HAT_GEWONNEN:
    		return this.getValue(KEYS.SPIELER_NAME) + " hat gewonnen!";
    	case SPIELER_X_WUERFELT_Y:
    		return this.getValue(KEYS.SPIELER_NAME) + " hat eine " + this.getValue(KEYS.WUERFELZAHL) + " gewuerfelt.";
    	case UNGUELTIGER_ZUG:
    		return "Der versuchte Zug ist ungueltig.";
    	case SPIELER_PLUS_MINUS:		// wird nur vom Server verwendet, Logs vom Client bezüglich dieser Nachricht werden in ClientSicht gesetzt
    		if ((this.getValue(KEYS.SPIELER_NUMMER) == null) || (Byte.parseByte(this.getValue(KEYS.SPIELER_NUMMER)) >= 0))
    			return this.getValue(KEYS.SPIELER_NAME) + " ist dem Spiel beigetreten.";
    		else
    			return "Spieler " + String.valueOf(-Byte.parseByte(this.getValue(KEYS.SPIELER_NUMMER))) + " hat das Spiel verlassen.";
    	case BEWEGUNGS_AUFFORDERUNG:
    		if (this.getValue(KEYS.FIGUREN_POSITION)!=null && (Integer.parseInt(this.getValue(KEYS.FIGUREN_POSITION))>=-100))
    			return "Bewegungsanfrage fuer Position " +this.getValue(KEYS.FIGUREN_POSITION);
    	default: return null;
    	}
    }
}
