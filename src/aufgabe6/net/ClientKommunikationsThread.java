package aufgabe6.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ClientKommunikationsThread implements Runnable
{
    private Socket socket;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Scanner scan;
    private boolean abbrechen;
    
    /**
     * Konstruktur, initialisiert Socket und Port
     * @param socket
     */
    public ClientKommunikationsThread(Socket socket) {
        this.abbrechen = false;
        this.socket = socket;
        try {
	        input = (ObjectInputStream)this.socket.getInputStream();
	        output =(ObjectOutputStream)this.socket.getOutputStream();
	        scan = new Scanner(input);
        } catch (Exception e) {
            System.err.println("Konnte auf dem Server einen Kommunikationsthread nicht starten");
        }
    }

    /**
     * wartet auf Nachrichten und liest sie ein
     */
    @Override
    public void run() {
        while (!this.abbrechen) {
            try {
                if (scan.hasNextLine()) {
                    Nachricht newNachricht = (Nachricht)input.readObject();
                    System.out.println(newNachricht.getValue(Nachricht.KEYS.SPIELER_NAME));
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
        } catch (IOException e) {
            System.err.println("Nachricht konnte nicht gesendet werden");
        }
    }
}
