/**
 * 
 */
package aufgabe6;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 * @author sascha
 *
 */
public class Gui implements GuiInterface {
	
	private static final Dimension MAIN_FRAME_MIN_DIM = new Dimension(800,600);

	private JFrame mainFrame = null;
	
	private static Gui singleTon = null;
	
	public static Gui getGui(){
		if(Gui.singleTon == null){
			Gui.singleTon = new Gui();
		}
		return Gui.singleTon;
	}
	
	private Gui(){
		this.mainFrame = new JFrame("Mensch ärger dich nicht");
		this.mainFrame.setMinimumSize(MAIN_FRAME_MIN_DIM);
		this.mainFrame.setUndecorated(false);
		this.mainFrame.setLocationByPlatform(true);
		
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* (non-Javadoc)
	 * @see aufgabe6.GuiInterface#aenderFigurPosition(aufgabe6.Figur, int, int)
	 */
	@Override
	public void aenderFigurPosition(Figur f, int vorher, int nachher) {
		// TODO Auto-generated method stub

	}

	@Override
	public void starteGui() {
		this.mainFrame.setVisible(true);
		
	}

}
