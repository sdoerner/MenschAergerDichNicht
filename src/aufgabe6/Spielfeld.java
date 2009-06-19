package aufgabe6;

import java.util.Vector;

/**
 * Spielfeld (Singleton-Klasse) repraesentiert das Spielfeld
 * @author rainer
 *
 */
public class Spielfeld {
	private static Spielfeld instance = null;
	private Vector<Figur> felder;
	
	/**
	 * Konstruktor, der die Felder initialisiert
	 */
	private Spielfeld() {
		felder = new Vector<Figur>(56);
		felder.setSize(56);
	}
	
	/**
	 * gibt die Singleton-Instanz zurueck
	 * @return
	 */
	public static Spielfeld getInstance() {
		if (instance == null)
			instance = new Spielfeld();
		
		return instance;
	}

	/**
	 * rechnet solange 40 auf eine Zahl, bis sie mindestens 0 ist
	 * @param dieZahl die zu erhoehende Zahl
	 * @return die erhoehte Zahl
	 */
	private int add40(int dieZahl) {
		while (dieZahl < 0)
			dieZahl += 40;
		
		return dieZahl;
	}
	
	/**
	 * prueft, ob ein Zug gueltig ist
	 * @param spieler der Spieler, der den Zug ausfuehrt
	 * @param startPosition die Position der Figur, die ziehen soll
	 * @param anzahlSchritte die Anzahl der zurueckzulegenden Schritte
	 * @return wahr, wenn der Zug gueltig ist, sonst falsch
	 */
	public boolean istZugGueltig(Spieler spieler, int startPosition, int anzahlSchritte) {
		if(felder.get(startPosition)==null)
			return false;
		
		byte anzahlFelder = 40;
		int endPosition = (startPosition + anzahlSchritte) % anzahlFelder;

		if ((felder.get(startPosition).istNaheZiel() || felder.get(startPosition).istInZiel())) {		// wenn in der Naehe vom Ziel, erhoehe die Feldgroesse um die Zielfelder
				if (startPosition + anzahlSchritte > add40(spieler.getEinstiegspunkt() - 1))		// wenn durch das Wuerfeln das Ziel erreicht wird, gehe in eines der Zielfelder
					endPosition = (anzahlFelder - 1) + (spieler.getSpielernummer() * 4) + (startPosition + anzahlSchritte - add40(spieler.getEinstiegspunkt() - 1));
				anzahlFelder += 16;
		}

		if ((startPosition >= 0) && (startPosition < anzahlFelder) && (endPosition < anzahlFelder) && (anzahlSchritte > 0) && (anzahlSchritte <= 6) &&		// liegen Start- und Endfeld auf dem Spielfeld und ist die Wuerfelzahl zwischen 1 und 6?
				(endPosition < 40 + (spieler.getSpielernummer() * 4) + 4) && felder.get(startPosition).getBesitzer().equals(spieler) && (felder.get(endPosition) == null || !felder.get(endPosition).getBesitzer().equals(spieler)))		// gehört die Spielfigur auf dem Startfeld dem Spieler, und steht auf dem Spielfeld nicht schon eine eigene Figur?
			return true;
		else {
			return false;
		}
	}
	
	/**
	 * bewegt eine Figur von einem Feld zu einem anderen, wenn dies moeglich ist
	 * @param spieler der Spieler, der den Zug ausfuehrt
	 * @param startPosition die Position der zu ziehenden Figur
	 * @param anzahlSchritte die Anzahl der zu ziehenden Schritte
	 * @return die neue Position der Figur, wenn sie bewegt wurde; sonst -1
	 */
	public int bewegeFigur(Spieler spieler, int startPosition, int anzahlSchritte) {
		if (istZugGueltig(spieler, startPosition, anzahlSchritte)) {
			byte anzahlFelder = 40;
			int endPosition = (startPosition + anzahlSchritte) % anzahlFelder;
						
			if ((felder.get(startPosition).istNaheZiel() || felder.get(startPosition).istInZiel())) {		// wenn in der Naehe vom Ziel, erhoehe die Feldgroesse um die Zielfelder
				if (startPosition + anzahlSchritte > add40(spieler.getEinstiegspunkt() - 1)) {		// wenn durch das Wuerfeln das Ziel erreicht wird, gehe in eines der Zielfelder
					endPosition = (anzahlFelder - 1) + (spieler.getSpielernummer() * 4) + (startPosition + anzahlSchritte - add40(spieler.getEinstiegspunkt() - 1));
				}
			}
			
			if (felder.get(endPosition) != null)
				felder.get(endPosition).entferne();
			
			felder.set(endPosition, felder.get(startPosition));
			felder.set(startPosition, null);
			return endPosition;
		} else
			return -1;
	}
	
	public boolean kannRausKommen(Spieler spieler, Figur figur) {
		if((figur.getPosition() != -1) || (felder.get(spieler.getEinstiegspunkt())!=null && felder.get(spieler.getEinstiegspunkt()).getBesitzer().equals(spieler)))
			return false;
		else
			return true;
	}
	
	/**
	 * versucht, eine Spielfigur aus dem Startbereich ins Spiel zhu holen.
	 * schlägt gegnerische Figuren, falls sie sich auf dem Feld befinden.
	 * @param spieler der Spieler, dessen Figur heraus kommen soll
	 * @param figur die herauszuholende Figur
	 * @return ob die Figur erfolgreich heraus geholt werden konnte
	 */
	public boolean kommRaus(Spieler spieler, Figur figur)
	{
		if(!kannRausKommen(spieler, figur))
			return false;
		if (felder.get(spieler.getEinstiegspunkt())!=null)
			felder.get(spieler.getEinstiegspunkt()).entferne();
		felder.set(spieler.getEinstiegspunkt(), figur);
		return true;
	}
	
	/**
	 * wuerfelt eine Zahl zwischen 1 und 6
	 * @return die gewuerfelte Zahl zwischen 1 und 6
	 */
	public byte wuerfeln() {
		return (byte)(Math.random() * 6 + 1);
	}
	
	/**
	 * gibt die Felder des Spielfelds, die eventuell mit Figuren besetzt sind, zurueck
	 * @return die Felder/Figuren des Spielfelds
	 */
	public Vector<Figur> getWahrscheinlichFiguren(){
		return this.felder;
	}
}
