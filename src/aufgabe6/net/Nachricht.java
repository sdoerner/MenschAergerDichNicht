package aufgabe6.net;

/**
 * Abstrakte Basisklasse fuer alle Nachrichten, d.h. Inhalte von gesendeten
 * Paketen.
 * 
 * @author sdoerner
 * 
 */
public abstract class Nachricht
{

    String sender;

    String empfaenger;

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

    /**
     * Diese Methode gibt die serialisierte Form der Nachricht zurueck, die
     * ueber das Netz gesendet werden kann.
     * 
     * @return Das serialisierte Objekt als String
     */
    public abstract String getSerialization();
}
