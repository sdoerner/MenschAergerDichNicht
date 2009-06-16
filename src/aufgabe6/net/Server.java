package aufgabe6.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

import aufgabe6.Gui;

public class Server
{
    private ServerSocket lauschSocket;
    private int port;
    private String serverName;
    private List<ServerKommunikationsThread> clientListe = null;
    
    public String getServerName()
    {
        return serverName;
    }

    private void initialisiereNeuenClientThread(Socket socket)
    {
        ServerKommunikationsThread serverKom = new ServerKommunikationsThread(socket, this);
        clientListe.add(serverKom);
        Thread thread = new Thread(serverKom);
        thread.start();
    }

    public Server(int port, String name)
    {
        this.port = port;
        this.serverName = name;
        this.clientListe = new LinkedList<ServerKommunikationsThread>();
    }

    public boolean lausche()
    {
        try
        {
            this.lauschSocket = new ServerSocket(this.port);
        } catch (IOException e)
        {
            System.err.println("Konnte keinen Socket auf Port " + port
                    + "oeffnen.");
            return false;
        }

        Thread thread = new Thread()
        {
            public void run()
            {
                try
                {
                	Gui.getGui().appendToTextPane("Server wurde erstellt.");
                    while (true)
                    {
                        Socket socket = lauschSocket.accept();
                        if (socket!=null)
                            initialisiereNeuenClientThread(socket);
                    }
                } catch (IOException e)
                {
                	e.printStackTrace();
                }
            }
        };
        thread.start();
        return true;
    }

    public boolean lauscht()
    {
        return (lauschSocket != null) && lauschSocket.isBound();
    }

    public void schliessen()
    {
        try
        {
            lauschSocket.close();
        } catch (IOException e)
        {
        	e.printStackTrace();
        }
    }
    
    public void sendeNachrichtAnAlleClients(Nachricht dieNachricht) {
    	for (ServerKommunikationsThread itServKom : clientListe)
    		itServKom.sendeNachricht(dieNachricht);
    }
    
    public void trenneClient(ServerKommunikationsThread serverKom) {
    	this.clientListe.remove(serverKom);
    }
}
