package aufgabe6;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Vector;

import aufgabe6.net.Nachricht;

/**
 * beinhaltet die Informationen ueber das Spiel, die der Client vom Server erhaelt
 * @author rainer
 *
 */
public class ClientSicht implements Serializable {
	private int[][] spielerFiguren = null;
	private String[] spielerName = null;
	private byte meineNummer = -1;
	
	/**
	 * initialisiert eine neue Clientview
	 */
	public ClientSicht() {
		this.spielerFiguren = new int[4][4];
		for (int itSpieler = 0; itSpieler < 4; itSpieler++)
			Arrays.fill(spielerFiguren[itSpieler], -2);
		
		this.spielerName = new String[4];
		Arrays.fill(spielerName, "");
	}
	
	/**
	 * erzeuge ClientSicht aus einem Spieler-Vektor (vom Server aufzurufen)
	 * @param theSpieler der Spielervektor
	 */
	public ClientSicht(Vector<Spieler> theSpieler) {
		for (int itSpieler = 0; itSpieler < theSpieler.size(); itSpieler++) {
			if (theSpieler.get(itSpieler) != null) {
				for (int itFigur = 0; itFigur < 4; itFigur++)
					spielerFiguren[itSpieler][itFigur] = theSpieler.get(itSpieler).getFiguren().get(itFigur).getPosition();
			} else
				Arrays.fill(spielerFiguren[itSpieler], -1);
		}
	}
	
	/**
	 * verarbeite eine Nachricht und aktualisiere die ClientSicht
	 * @param theNachricht die zu verarbeitende Nachricht
	 */
	public void verarbeiteNachricht(Nachricht theNachricht) {
		// TODO implementieren
	}
	
	public int[][] getSpielerFiguren() {
		return spielerFiguren;
	}
	
	public byte getMeineNummer() {
		return meineNummer;
	}
}
