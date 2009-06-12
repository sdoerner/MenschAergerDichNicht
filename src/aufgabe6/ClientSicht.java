package aufgabe6;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Vector;

import aufgabe6.net.Nachricht;

/**
 * beinhaltet die Informationen ueber das Spiel, die für den Client relevant sind.
 * kann vom Server erstellt und dem Client übersendet werden, damit dieser den aktuellen Spielstand kennt
 * @author rainer
 *
 */
public class ClientSicht implements Serializable {
	private static final long serialVersionUID = 1L;
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
				Arrays.fill(spielerFiguren[itSpieler], -2);
		}
	}
	
	/**
	 * verarbeite eine Nachricht und aktualisiere die ClientSicht
	 * @param theNachricht die zu verarbeitende Nachricht
	 */
	public void verarbeiteNachricht(Nachricht theNachricht) {
		switch (theNachricht.getNachrichtenTyp()) {
		case SPIELER_PLUS_MINUS:
			String derName = theNachricht.getValue(Nachricht.KEYS.SPIELER_NAME);
			Byte dieNummer = Byte.parseByte(theNachricht.getValue(Nachricht.KEYS.SPIELER_NUMMER));
			
			if (dieNummer < 0) {		// Spieler soll geloescht werden
				this.spielerName[-dieNummer] = "";
				Arrays.fill(this.spielerFiguren[-dieNummer], -2);
			} else {					// Spieler soll hinzugefuegt werden
				if (this.meineNummer == -1)
					this.meineNummer = dieNummer;
				this.spielerName[dieNummer] = derName;
			}
			break;
		case SPIELER_X_WUERFELT_Y:
			//byte dieSpielerNummer = Byte.parseByte(theNachricht.getValue(Nachricht.KEYS.SPIELER_NUMMER));
			//byte wuerfelZahl = Byte.parseByte(theNachricht.getValue(Nachricht.KEYS.WUERFELZAHL));
			String nachrichtFiguren = theNachricht.getValue(Nachricht.KEYS.FIGUREN);
			
			this.spielerFiguren = figurenFromString(nachrichtFiguren);
			break;
		}
	}
	
	public String toString() {
		String retString = "";
		
		for (int i = 0; i < 4; i++)
			retString += spielerFiguren[i] + ",";
			
		return retString;
	}
	
	private int[][] figurenFromString(String theString) {
		int[][] retArr = new int[4][4];
		
		String[] splitString = theString.split(";");
		
		for (int i = 0; i < splitString.length - 1; i++) {
			String[] splitString2 = splitString[i].split(",");
			
			for (int j = 0; j < splitString2.length; j++)
				retArr[i][j] = Integer.parseInt(splitString2[j]);
		}
		
		return retArr;
	}
	
	public int[][] getSpielerFiguren() {
		return spielerFiguren;
	}
	
	public byte getMeineNummer() {
		return meineNummer;
	}
}
