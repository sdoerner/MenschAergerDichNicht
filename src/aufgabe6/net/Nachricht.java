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
        SPIELER_NAME
    }
    private String sender;
    private String empfaenger;
    private TreeMap<KEYS, String> data;

    public Nachricht(String sender, String empfaenger) throws NullPointerException
    {
        if (sender==null || empfaenger==null)
            throw new NullPointerException();
        this.sender = sender;
        this.empfaenger = empfaenger;
        data = new TreeMap<KEYS, String>();
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
}
