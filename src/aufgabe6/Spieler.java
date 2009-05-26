package aufgabe6;

import java.util.Vector;

public class Spieler {
	private Vector<Figur> figuren = null;
	private int einstiegspunkt = -1;
	
	public Spieler(int spielerNummer) {
		einstiegspunkt = spielerNummer * 10;
		figuren = new Vector<Figur>(4);
	}
}
