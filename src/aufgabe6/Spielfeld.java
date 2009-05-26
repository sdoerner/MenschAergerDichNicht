package aufgabe6;

import java.util.Vector;

/**
 * Spielfeld (Singleton-Klasse) repraesentiert das Spielfeld
 * @author rainer
 *
 */
public class Spielfeld {
	private static Spielfeld instance = null;
	private Vector<Figur> felder = null;
	
	/**
	 * Konstruktor, der die Felder initialisiert
	 */
	private Spielfeld() {
		felder = new Vector<Figur>(56);
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
	 * prueft, ob ein Zug gueltig ist
	 * @param spieler der Spieler, der den Zug ausfuehrt
	 * @param startPosition die Position der Figur, die ziehen soll
	 * @param anzahlSchritte die Anzahl der zurueckzulegenden Schritte
	 * @return wahr, wenn der Zug gueltig ist, sonst falsch
	 */
	public boolean istZugGueltig(Spieler spieler, int startPosition, int anzahlSchritte) {
		byte anzahlFelder = 40;
		int endPosition = (startPosition + anzahlSchritte) % anzahlFelder;
		
		if (felder.size() > startPosition && (felder.get(startPosition).istNaheZiel() || felder.get(startPosition).istInZiel())) {
				endPosition = anzahlFelder - 1 + (spieler.getSpielernummer() * 4) + anzahlSchritte;
				anzahlFelder += 16;
		}

		if (felder.size() > startPosition && (startPosition >= 0) && (startPosition < anzahlFelder) && (endPosition < anzahlFelder) && (anzahlSchritte > 0) && (anzahlSchritte <= 6) &&		// liegen Start- und Endfeld auf dem Spielfeld und ist die Wuerfelzahl zwischen 1 und 6?
				(felder.get(startPosition) != null && felder.get(startPosition).getBesitzer().equals(spieler) && (felder.get(endPosition) == null || !felder.get(endPosition).getBesitzer().equals(spieler))))		// gibt es auf dem Startfeld eine Spielfigur, gehoert sie dem Spieler, und steht auf dem Spielfeld nicht schon eine eigene Figur?
			return true;
		else
			return false;
	}
	
	/**
	 * bewegt eine Figur von einem Feld zu einem anderen, wenn dies moeglich ist
	 * @param spieler der Spieler, der den Zug ausfuehrt
	 * @param startPosition die Position der zu ziehenden Figur
	 * @param anzahlSchritte die Anzahl der zu ziehenden Schritte
	 * @return ob die Figur bewegt wurde
	 */
	public boolean bewegeFigur(Spieler spieler, int startPosition, int anzahlSchritte) {
		if (istZugGueltig(spieler, startPosition, anzahlSchritte)) {
			byte anzahlFelder = 40;
			if (felder.get(startPosition).istNaheZiel() || felder.get(startPosition).istInZiel())
				anzahlFelder += 4;
			
			int endPosition = (startPosition + anzahlSchritte) % anzahlFelder;
			
			if (felder.get(endPosition).getBesitzer() != null)
				felder.get(endPosition).entferne();
			
			felder.set(endPosition, felder.get(startPosition));
			felder.set(startPosition, null);
			return true;
		} else
			return false;
	}
	
	/**
	 * wuerfelt eine Zahl zwischen 1 und 6
	 * @return die gewuerfelte Zahl zwischen 1 und 6
	 */
	public int wuerfeln() {
		return (int)(Math.random() * 6) + 1;
	}
}
