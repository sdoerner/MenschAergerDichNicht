package aufgabe6.net;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Scanner;

public class ServerKommunikationsThread implements Runnable
{
    private Socket socket;
    private InputStream is;
    private DataInputStream dis;
    private InputStreamReader isr;
    private DataOutputStream dos;
    private boolean abbrechen;
    private Scanner sc;
    
    public ServerKommunikationsThread(Socket socket)
    {
        this.abbrechen = false;
        this.socket = socket;
        try{
//        socket.setKeepAlive(true);
        
        is= socket.getInputStream();
        sc = new Scanner(is);
        } catch (Exception e)
        {
            System.err.println("Konnte auf dem Server einen Kommunikationsthread nicht starten");
        }
        
    }

    @Override
    public void run()
    {
     //dauerhaft auf Nachrichten vom Client warten
        while (!this.abbrechen)
        {
            try
            {
                if (sc.hasNextLine())
                {
                    System.out.println(sc.nextLine());
                }
            } catch (Exception e)
            {
                System.err.println("exp");
                e.printStackTrace();
            }
        }
    }

}
