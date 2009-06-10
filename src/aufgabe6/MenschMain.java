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
		GuiInterface gui = Gui.getGui();
		gui.starteGui();
		
        System.out.println("test");
        
        Spiel dasSpiel = new Spiel();
        dasSpiel.start();
    }

}
