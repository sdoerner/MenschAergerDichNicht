package aufgabe6;

import java.util.Vector;

public class Spiel extends Thread {
	private static Vector<Spieler> spieler = null;
	private static Spieler aktuellerSpieler = null;

	public Spiel() {
        spieler = new Vector<Spieler>(4);
	}
	
	@Override
	public void run() {
		System.out.println("xxx");
	}

	/**
	 * wuerfelt und laesst den aktuellen Spieler ziehen (bei gewuerfelter 6: Wiederholung)
	 */
	private void wuerfelnUndZiehen() {
		int gewuerfelteZahl;
		
		do {
			gewuerfelteZahl = Spielfeld.getInstance().wuerfeln();
			this.interrupt();
			aktuellerSpieler.ziehe(gewuerfelteZahl);
		} while (gewuerfelteZahl == 6);
	}
	
    /**
     * fuege einen neuen Spieler zum Spiel hinzu
     */
    public void verbindeSpieler(String name) {
    	spieler.add(new Spieler(name, spieler.size(), true));
    }
    
    public void spielen() {
		boolean istSiegerGefunden = false;
        
        while (!istSiegerGefunden) {
        	for (Spieler itSpieler : spieler) {
        		aktuellerSpieler = itSpieler;
        		
        		if (!itSpieler.istDraussen()) {				// hat der Spieler schon eine Figur auf dem Spielfeld?
        			for (int i = 0; i < 3; i++) {			// drei mal wuerfeln, um evt. raus zu kommen
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
}
