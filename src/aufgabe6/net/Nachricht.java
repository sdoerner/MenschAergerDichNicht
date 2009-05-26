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
public class Nachricht
{

    public static enum KEYS
    {
        SPIELER_NAME
    }
    private String sender;
    private String empfaenger;
    private TreeMap<KEYS, String> data;

    public Nachricht(String sender, String empfaenger)
    {
        this.sender = sender;
        this.empfaenger = empfaenger;
    }

    public String getSender()
    {
        return sender;
    }

    public String getEmpfaenger()
    {
        return empfaenger;
    }
    
    public String getValue(KEYS key)
    {
        return data.get(key);
    }
    
    public void setValue(KEYS key, String value)
    {
        data.put(key, value);
    }

    /**
     * Diese Methode gibt die serialisierte Form der Nachricht zurueck, die
     * ueber das Netz gesendet werden kann.
     * 
     * @return Das serialisierte Objekt als String
     */
    public String getSerialization(){return null;}
}
