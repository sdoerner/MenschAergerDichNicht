package aufgabe6;

/**
 * Startklasse. kann das Spiel (neu) starten
 *
 */
public class MenschMain {

	private static Spiel dasSpiel;
	
	/**
	 * getter fuer das aktuelle Spiel
	 * @return das aktuelle Spiel
	 */
    public static Spiel getDasSpiel()
    {
        return dasSpiel;
    }
    /**
     * initialisiert das Spiel
     * @param args
     */
    public static void main(String[] args) {
		Gui.getGui().starteGui();
		
        dasSpiel = new Spiel();
    }
    
    /**
     * erzeugt ein neues Spiel, wenn das aktuelle nicht mehr läuft
     */
    public static void neuesSpiel()
    {
    	if (!dasSpiel.isAlive())
    		dasSpiel = new Spiel();
    }
    
    /**
     * wartet, bis das Spiel vorbei ist, und erzeugt danach ein neues
     */
    public static void spielNeustarten()
    {
    	while (dasSpiel!=null && dasSpiel.isAlive())
    		Thread.yield();
    	dasSpiel = new Spiel();
    }

}
