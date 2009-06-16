package aufgabe6;

import aufgabe6.net.ServerKommunikationsThread;

public class Spiel extends Thread {
	private static Spieler[] spieler = null;
	private static Spieler aktuellerSpieler = null;
	private int gewaehlteFigurenPosition;

	public void setGewaehlteFigurenPosition(int gewaehlteFigurenPosition)
    {
        this.gewaehlteFigurenPosition = gewaehlteFigurenPosition;
    }
	
    public Spiel() {
        spieler = new Spieler[4];
        for (int i = 0; i < 4; i++)
        	spieler[i] = null;
	}
	
	@Override
	public void run() {
		Gui.getGui().appendToTextPane("Das Spiel wurde gestartet.");
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
	 * berechnet den ersten freien Platz im Spielerarray
	 * @return der erste freie Platz im Spielerarray (zwischen 0 und 3), wenn noch ein Platz frei ist; sonst -1
	 */
	private byte ersterFreierPlatz() {
		for (byte i = 0; i < 4; i++)
			if (spieler[i] == null)
				return i;
		
		return -1;
	}
	
    /**
     * fuege einen neuen Spieler zum Spiel hinzu und gibt seinen Index zurück
     */
    public int verbindeSpieler(String name, ServerKommunikationsThread thread) {
    	byte spielerPlatz = ersterFreierPlatz();
    	
    	if (spielerPlatz == -1)
    		return -1;
    	else {
	    	Spieler s = new Spieler(name, spielerPlatz, true);
	    	s.setServerKommunikationsThread(thread);
	    	spieler[spielerPlatz] = s;
	    	return spielerPlatz;
    	}
    }
    
    public void trenneSpieler(byte spielerNummer) {
    	spieler[spielerNummer] = null;
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
        //benachrichtige alle Clients ueber das Spielende 
        int indexGewinner = aktuellerSpieler.getSpielernummer();
        for (Spieler itSpieler: spieler)
        	itSpieler.getServerKommunikationsThread().sendeSpielerHatGewonnen(indexGewinner);
    }
    
    /**
     * gibt die aktuellen Positionen der Spielfiguren als ClientSicht zurueck
     * @return die aktuellen Spielfigurpositionen als ClientSicht
     */
    public ClientSicht toClientSicht() {
    	return new ClientSicht(spieler);
    }
}
