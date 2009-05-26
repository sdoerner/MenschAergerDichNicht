package aufgabe6.net;

import java.net.Socket;

public class ServerKommunikationsThread implements Runnable
{
    private Socket socket;
    
    public ServerKommunikationsThread(Socket socket)
    {
        this.socket = socket;
    }

    @Override
    public void run()
    {
        // TODO Auto-generated method stub
        
    }

}
