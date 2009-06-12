package aufgabe6;

import java.util.Vector;

/**
 * Spieler repraesentiert einen der maximal 4 Spieler, die mitspielen
 * @author rainer
 *
 */
public class Spieler {
	private Vector<Figur> figuren = null;
	private int einstiegspunkt = -1;
	private int spielerNummer = -1;
	private String name = null;
	private boolean isNetwork = false;
	
	/**
	 * Konstruktur, der den Einstiegspunkt setzt und die Figuren initialisiert
	 * @param spielerNummer die Nummer des Spielers (zwischen 0 und 3)
	 */
	public Spieler(String name, int spielerNummer, boolean isNetwork) {
		this.einstiegspunkt = spielerNummer * 10;
		this.name = name;
		this.spielerNummer = spielerNummer;
		this.figuren = new Vector<Figur>(4);
		this.isNetwork = isNetwork;
		
		for (int i = 0; i < 4; i++)
			this.figuren.add(new Figur(this));
	}
	
	/**
	 * fuehrt einen Zug durch
	 * @param anzahlSchritte die gewuerfelte Zahl (= Anzahl der zuruecklegbaren Schritte)
	 * @return ob eine Figur bewegt werden konnte
	 */
	public boolean ziehe(int figurnummerposition, int anzahlSchritte) {
//		Thread warte = new WartenThread();
//		try {
//			warte.join();
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		Figur figur = null;
		for (int i=0;i<figuren.size();i++)
		{
		    figur = figuren.get(i);
		    if (figur.getPosition()==figurnummerposition)
		        break;
		}
		if (figur==null || figurnummerposition!=figur.getPosition())
		    return false;
		
		if (Spielfeld.getInstance().bewegeFigur(this, figur.getPosition(), anzahlSchritte)) {
			figur.bewege(anzahlSchritte);
			return true;
		} else
			return false;
	}
	
	/**
	 * gibt den Einstiegspunkt dieses Spielers zurueck
	 * @return der Einstiegspunkt
	 */
	public int getEinstiegspunkt() {
		return this.einstiegspunkt;
	}
	
	/**
	 * gibt die Spielernummer zurueck
	 * @return die Spielernummer
	 */
	public int getSpielernummer() {
		return this.spielerNummer;
	}
	
	/**
	 * gibt an, ob ein Spieler gewonnen hat (= alle Figuren im Ziel)
	 * @return true, wenn er gewonnen hat, sonst false
	 */
	public boolean hatGewonnen() {
		for (Figur itFigur : figuren) {
			if (!itFigur.istInZiel())
				return false;
		}
		
		return true;
	}
	
	/**
	 * liefert den Namen des Spielers
	 * @return der Name des Spielers
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * gibt an, ob ein Spieler schon Figuren auf dem Brett hat
	 * @return wahr, wenn dieser Spieler Figuren auf dem Brett hat
	 */
	public boolean istDraussen() {
		for (Figur itFigur : figuren) {
			if (itFigur.getPosition() > -1)
				return true;
		}
		
		return false;
	}

	/**
	 * holt die erste Figur, die noch außerhalb des Spielfelds steht, auf das Spielfeld
	 */
	public void kommRaus() {
		for (Figur itFig : figuren) {
			if (itFig.getPosition() == -1) {
				itFig.aufStart();
				break;
			}
		}
	}
	
	public Vector<Figur> getFiguren() {
		return figuren;
	}
}
