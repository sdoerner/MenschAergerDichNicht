package aufgabe6;

import java.util.Vector;
import aufgabe6.net.Server;

public class MenschMain {
	private static Vector<Spieler> spieler = null;
	private static Spieler aktuellerSpieler = null;

	/**
	 * wuerfelt und laesst den aktuellen Spieler ziehen (bei gewuerfelter 6: Wiederholung)
	 */
	private static void wuerfelnUndZiehen() {
		int gewuerfelteZahl;
		
		do {
			gewuerfelteZahl = Spielfeld.getInstance().wuerfeln();
			aktuellerSpieler.ziehe(gewuerfelteZahl);
		} while (gewuerfelteZahl == 6);
	}
	
    /**
     * initialisiert und startet das Spiel
     * @param args
     */
    public static void main(String[] args) {
		GuiInterface gui = Gui.getGui();
		gui.starteGui();
		
		
		
		Server s = new Server(9999);
        s.lausche();
        System.out.println("test");
    	
        
        
        spieler = new Vector<Spieler>(4);
        verbindeSpieler("Sascha");
        
        boolean istSiegerGefunden = false;
        
        while (!istSiegerGefunden) {
        	for (Spieler itSpieler : spieler) {
        		aktuellerSpieler = itSpieler;
        		
        		if (!itSpieler.istDraussen()) {
        			for (int i = 0; i < 3; i++) {
        				int gewuerfelteZahl = Spielfeld.getInstance().wuerfeln();
        				
        				if (gewuerfelteZahl == 6) {
        					itSpieler.kommRaus();
        					itSpieler.ziehe(6);
        					wuerfelnUndZiehen();
        					break;
        				}
        			}
        		} else
        			wuerfelnUndZiehen();
        		
	        	if (itSpieler.hatGewonnen()) {
	        		istSiegerGefunden = true;
	        		break;
	        	}
        	}
        }
    }

    /**
     * fuege einen neuen Spieler zum Spiel hinzu
     */
    public static void verbindeSpieler(String name) {
    	spieler.add(new Spieler(name, spieler.size()));
    }
}
