package aufgabe6;

import java.util.Calendar;

/**
 * 
 */

/**
 * @author sascha
 *
 */
public class WartenThread extends Thread {
	
	public static Figur gewaehlteFigur= null;
	
	public WartenThread(){
		super.start();
		gewaehlteFigur = null;
	}
	
	public void setGewaehlteFigur(Figur gewaehlteFigur) {
		WartenThread.gewaehlteFigur = gewaehlteFigur;
	}
	
	@Override
	public void run() {
		long ablaufZeitPunkt = System.currentTimeMillis()+10000;
		while(System.currentTimeMillis()<ablaufZeitPunkt && gewaehlteFigur==null);
	}
	
}
