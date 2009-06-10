package aufgabe6.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import aufgabe6.Gui;
import aufgabe6.net.Nachricht.KEYS;

public class ClientKommunikationsThread implements Runnable
{
    private Socket socket;
    private InputStream input;
    private ObjectOutputStream output;
    private boolean abbrechen;
    
    /**
     * Konstruktur, initialisiert Socket und Port
     * @param socket
     */
    public ClientKommunikationsThread(Socket socket) {
        this.abbrechen = false;
        this.socket = socket;
        try {
	        input = this.socket.getInputStream();
	        output = new ObjectOutputStream(this.socket.getOutputStream());
        } catch (Exception e) {
            System.err.println("Konnte auf dem Client einen Kommunikationsthread nicht starten");
            e.printStackTrace();
        }
        Nachricht n = new Nachricht("Client1", "Server");
        n.setValue(KEYS.SPIELER_NAME, Gui.getGui().getNamensFeldInhalt());
        this.sendNachricht(n);
    }

    /**
     * wartet auf Nachrichten und liest sie ein
     */
    @Override
    public void run() {
        while (!this.abbrechen) {
            try {
                if (input.available() > 0) {
                	ObjectInputStream ois = new ObjectInputStream(input);
                    Nachricht newNachricht = (Nachricht)ois.readObject();
                    System.out.println(newNachricht.getEmpfaenger());
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
    public void sendNachricht(Nachricht theNachricht) {
        try {
        	output.writeObject(theNachricht);
        	output.flush();
        } catch (IOException e) {
            System.err.println("Nachricht konnte nicht gesendet werden");
        }
    }
}
