package aufgabe6.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

import aufgabe6.MenschMain;
import aufgabe6.Spielfeld;
import aufgabe6.net.Nachricht.KEYS;
import aufgabe6.net.Nachricht.NACHRICHTEN_TYP;

public class ServerKommunikationsThread implements Runnable
{
    private Socket socket;
    private Server server;
    private InputStream is;
    private OutputStream os;
    private boolean abbrechen;
    
    public ServerKommunikationsThread(Socket socket, Server server)
    {
        this.abbrechen = false;
        this.socket = socket;
        this.server = server;
        try{
//        socket.setKeepAlive(true);
        
        is=this.socket.getInputStream();
        os=this.socket.getOutputStream();
        Nachricht n = new Nachricht(server.getServerName(), NACHRICHTEN_TYP.SPIELER_PLUS_MINUS);
        this.sendeNachricht(n);
//        sc = new Scanner(is);
        } catch (Exception e)
        {
            System.err.println("Konnte auf dem Server einen Kommunikationsthread nicht starten");
        }
        
    }

    @Override
    public void run()
    {
        String s;
     //dauerhaft auf Nachrichten vom Client warten
        while (!this.abbrechen)
        {
            try
            {
                if (is.available() > 0)
                {
                    ObjectInputStream ois = new ObjectInputStream(is);
                    Nachricht n = (Nachricht) ois.readObject();
                    if (n.getNachrichtenTyp()==NACHRICHTEN_TYP.SPIELER_PLUS_MINUS)
                        System.out.println("received Client hello from " + n.getValue(KEYS.SPIELER_NAME));
                    if (n.getNachrichtenTyp()==NACHRICHTEN_TYP.BEWEGUNGS_AUFFORDERUNG)
                        bearbeiteBewegungsAufforderung(n);
                }

            } catch (Exception e)
            {
                System.err.println("exp");
                e.printStackTrace();
            }
        }
    }
    
    public void sendeNachricht(Nachricht n)
    {
        try{
        ObjectOutputStream oos = new ObjectOutputStream(os);
        oos.writeObject(n);
        oos.flush();
        }
        catch (IOException e){
            System.err.println("Fehler beim Senden der Nachricht.");
        }
    }
    
    public void sendString(String s)
    {
        try
        {
        os.write(s.getBytes());
        }
        catch (IOException e)
        {
            System.err.println("Konnte nicht ");
        }
    }
    private void bearbeiteBewegungsAufforderung(Nachricht n)
    {
        int position = Integer.parseInt(n.getValue(KEYS.FIGUREN_POSITION));
        MenschMain.getDasSpiel().setGewaehlteFigurenPosition(position);
        MenschMain.getDasSpiel().notify();
    }
}
