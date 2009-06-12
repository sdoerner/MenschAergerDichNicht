package aufgabe6;

import java.util.Vector;

import aufgabe6.net.Client;
import aufgabe6.net.Server;

public class MenschMain {

	
    /**
     * initialisiert und startet das Spiel
     * @param args
     */
    public static void main(String[] args) {
//        Client c = Client.getInstance();
//        c.verbinde("172.16.25.122");
		GuiInterface gui = Gui.getGui();
		gui.starteGui();
		
        //Spiel dasSpiel = new Spiel();
        //dasSpiel.start();
    }

}
