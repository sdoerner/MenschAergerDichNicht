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
        SPIELER_PLUS_MINUS,
        SPIELER_X_WUERFELT_Y,	// inklusive komplette Figurenuebermittlung und evt. wer gewonnen hat
        BEWEGUNGS_AUFFORDERUNG,
        HABE_FELD_X_GEKLICKT,
        UNGUELTIGER_ZUG
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
}
