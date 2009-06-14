package aufgabe6;

import java.util.Vector;

import aufgabe6.net.ServerKommunikationsThread;

public class Spiel extends Thread {
	private static Vector<Spieler> spieler = null;
	private static Spieler aktuellerSpieler = null;
	private int gewaehlteFigurenPosition;

	public void setGewaehlteFigurenPosition(int gewaehlteFigurenPosition)
    {
        this.gewaehlteFigurenPosition = gewaehlteFigurenPosition;
    }
	
    public Spiel() {
        spieler = new Vector<Spieler>(4);
	}
	
	@Override
	public void run() {
		System.out.println("xxx");
		this.spielen();
	}

	/**
	 * wuerfelt und laesst den aktuellen Spieler ziehen (bei gewuerfelter 6: Wiederholung)
	 */
	private void wuerfelnUndZiehen() {
		int gewuerfelteZahl;
		
		do {
			gewuerfelteZahl = Spielfeld.getInstance().wuerfeln();
			//TODO: Clients über das Würfelergebnis und den letzten Zug informieren, damit der, der dran ist, setzen kann
			boolean zugErfolgreich = false;
			while (!zugErfolgreich)
			{
				try
				{
					synchronized (this)
					{
						this.wait();
					}
				}
				catch (InterruptedException e)
				{
					e.printStackTrace();
				}
				System.out.println("es geht weiter mit figur"
						+ this.gewaehlteFigurenPosition);
				zugErfolgreich = !aktuellerSpieler.ziehe(
						this.gewaehlteFigurenPosition, gewuerfelteZahl);
				if (!zugErfolgreich)
					aktuellerSpieler.getServerKommunikationsThread()
							.sendeZugUngueltig();
			}
		}
		while (gewuerfelteZahl == 6);
	}
	
    /**
     * fuege einen neuen Spieler zum Spiel hinzu und gibt seinen index zurück
     */
    public int verbindeSpieler(String name, ServerKommunikationsThread thread) {
    	Spieler s = new Spieler(name, spieler.size(), true);
    	s.setServerKommunikationsThread(thread);
    	spieler.add(s);
    	return spieler.indexOf(s);
    }
    
    /**
     * Hauptschleife
     */
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
    
    public ClientSicht toClientView() {
    	return new ClientSicht(spieler);
    }
}
