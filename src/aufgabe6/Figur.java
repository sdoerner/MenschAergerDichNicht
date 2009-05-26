package aufgabe6;

/**
 * Figur repraesentiert eine Spielfigur inklusive ihrem Besitzer (Spieler) und ihrer Position auf dem Spielfeld
 * @author rainer
 *
 */
public class Figur {
	private Spieler besitzer;
	private int position;
	
	/**
	 * Konstruktor; setzt den Besitzer und die Position auf -1 (noch nicht im Spiel)
	 * @param besitzer der Besitzer der Figur
	 */
	public Figur(Spieler besitzer) {
		this.besitzer = besitzer;
		this.position = -1;
	}
	
	/**
	 * bewegt die Figur um eine bestimmte Anzahl an Schritten
	 * @param anzahlSchritte die Anzahl der Schritte, um die die Figur bewegt werden soll
	 */
	public void bewege(int anzahlSchritte) {
		this.position += anzahlSchritte;
		
		if (!istInZiel() && !istNaheZiel())
			this.position %= 40;
	}
	
	/**
	 * gibt die aktuelle Position der Figur auf dem Spielbrett zurueck
	 * @return die aktuelle Position
	 */
	public int getPosition() {
		return position;
	}

	/**
	 * gibt den Besitzer der Figur zurueck
	 * @return
	 */
	public Spieler getBesitzer() {
		return besitzer;
	}
	
	/**
	 * entfernt diese Figur aus dem Spiel
	 */
	public void entferne() {
		this.position = -1;
	}
	
	/**
	 * gibt an, ob die Figur nahe an ihrem Ziel, aber noch auf dem Spielbrett ist
	 * @return wahr, wenn die Figur in 6 Schritten das Ziel erreichen kann, sonst falsch
	 */
	public boolean istNaheZiel() {
		return (this.position + 39) % 40 > ((besitzer.getEinstiegspunkt() - 6) + 39) % 40;
	}
	
	/**
	 * gibt an, ob die Figur schon im Ziel ist
	 * @return
	 */
	public boolean istInZiel() {
		return this.position > 39;
	}
}
