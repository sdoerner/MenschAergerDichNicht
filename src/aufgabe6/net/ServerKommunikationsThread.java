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

public class ServerKommunikationsThread implements Runnable
{
    private Socket socket;
    private Server server;
    private InputStream is;
    private OutputStream os;
    private ObjectInputStream ois=null;
    private ObjectOutputStream oos;
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
	        oos = new ObjectOutputStream(os);
        } catch (Exception e)
        {
            System.err.println("Konnte auf dem Server einen Kommunikationsthread nicht starten");
        }
        
    }
    
    
    //--------------------------  Empfangs-Teil -------------------------------------//

    @Override
    public void run()
    {
        //String s;
     //dauerhaft auf Nachrichten vom Client warten
        while (!this.abbrechen && socket.isConnected())
        {
            try
            {
                if (is.available() > 0)
                {
                	if (ois==null)
                		ois = new ObjectInputStream(is);
                    Nachricht n = (Nachricht) ois.readObject();
					switch (n.getNachrichtenTyp())
					{
					case SPIELER_PLUS_MINUS:
						bearbeiteSpielerPlusMinus(n);
						break;
					case BEWEGUNGS_AUFFORDERUNG:
						bearbeiteBewegungsAufforderung(n);
						break;
					}
					//TODO: DEBUG-Code
					Gui.getGui().appendToTextPane(n.getLogMessage());
				}

            } catch (Exception e)
            {
                System.err.println("exp: "+ e.getMessage());
                e.printStackTrace();
            }
        }
    		try
    		{
    			ois.close();
    			socket.close();
    			
    		}catch (IOException e){
    			e.printStackTrace();
    		}
    }
    
    private void bearbeiteBewegungsAufforderung(Nachricht n)
    {
        int position = Integer.parseInt(n.getValue(KEYS.FIGUREN_POSITION));
        MenschMain.getDasSpiel().setGewaehlteFigurenPosition(position);
        synchronized(MenschMain.getDasSpiel())
        {
        	MenschMain.getDasSpiel().notify();
        }
    }
    
    private void bearbeiteSpielerPlusMinus(Nachricht n)
    {
        Nachricht nOut = new Nachricht(server.getServerName(), NACHRICHTEN_TYP.SPIELER_PLUS_MINUS);
        boolean trenne = false;
        
    	if ((n.getValue(KEYS.SPIELER_NUMMER) == null) || (Byte.parseByte(n.getValue(KEYS.SPIELER_NUMMER)) > 0)) {
	    	int index = MenschMain.getDasSpiel().verbindeSpieler(n.getValue(KEYS.SPIELER_NAME),this);
	    	
	        nOut.setValue(KEYS.SPIELER_NUMMER, ""+ (index+1));
	        nOut.setValue(KEYS.SPIELER_NAME, "" + n.getValue(KEYS.SPIELER_NAME));
	        nOut.setValue(KEYS.FIGUREN, "" + MenschMain.getDasSpiel().toClientSicht());
    	} else {
    		byte spielerNummer = Byte.parseByte(n.getValue(KEYS.SPIELER_NUMMER));
    		
    		MenschMain.getDasSpiel().trenneSpieler((byte)-(spielerNummer + 1));
	        nOut.setValue(KEYS.SPIELER_NUMMER, n.getValue(KEYS.SPIELER_NUMMER));
	        nOut.setValue(KEYS.SPIELER_NAME, n.getValue(KEYS.SPIELER_NAME));
	        trenne = true;
    	}
    	
        this.server.sendeNachrichtAnAlleClients(nOut);
		if (trenne)
		{
			this.server.trenneClient(this);
			this.abbrechen = true;
		}
        Gui.getGui().setStartenKnopfZustand(true);
    }
    
    
    //---------------------------------  sende-Teil ------------------------------------//
    
    public void sendeNachricht(Nachricht n)
    {
        try{
	        oos.writeObject(n);
	        oos.flush();
        }
        catch (IOException e){
            System.err.println("Fehler beim Senden der Nachricht.");
        }
    }
    
    public void sendeZugUngueltig()
    {
    		Nachricht n = new Nachricht(this.server.getServerName(), NACHRICHTEN_TYP.UNGUELTIGER_ZUG);
            sendeNachricht(n);
    }
    
    public void sendeWuerfelZahl(String spielerName, int spielerIndex, int wuerfelZahl) {
		Nachricht wuerfelNachricht = new Nachricht(this.server.getServerName(), NACHRICHTEN_TYP.SPIELER_X_WUERFELT_Y);
		wuerfelNachricht.setValue(KEYS.SPIELER_NAME, spielerName);
		wuerfelNachricht.setValue(KEYS.SPIELER_NUMMER, ""+spielerIndex);
		wuerfelNachricht.setValue(KEYS.WUERFELZAHL, ""+wuerfelZahl);
        wuerfelNachricht.setValue(KEYS.FIGUREN, "" + MenschMain.getDasSpiel().toClientSicht());
		this.server.sendeNachrichtAnAlleClients(wuerfelNachricht);
    }

    public void sendeSpielerHatGewonnen(String spielerName)
    {
		Nachricht n = new Nachricht(this.server.getServerName(), NACHRICHTEN_TYP.SPIELER_X_HAT_GEWONNEN);
		n.setValue(KEYS.SPIELER_NAME, spielerName);
		this.server.sendeNachrichtAnAlleClients(n);
    }
}
