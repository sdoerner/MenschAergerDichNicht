package aufgabe6;

import aufgabe6.net.Server;

public class MenschMain
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        Server s = new Server(9999);
        s.lausche();
        System.out.println("test");
//        s.schliessen();
    }

}
