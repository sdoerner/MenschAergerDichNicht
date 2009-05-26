package aufgabe6.net;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server
{
    private ServerSocket lauschSocket;

    private int port;

    private void initialisiereNeuenClientThread(Socket socket)
    {
        ServerKommunikationsThread serverKom = new ServerKommunikationsThread(socket);
        Thread thread = new Thread(serverKom);
        thread.start();
    }

    public Server(int port)
    {
        this.port = port;
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
                    System.out.println("Lausche auf Port " + port + "...");
                    while (true)
                    {
                        Socket socket = lauschSocket.accept();
                        if (socket!=null)
                            initialisiereNeuenClientThread(socket);
                    }
                } catch (IOException e)
                {
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
        }
    }

}
