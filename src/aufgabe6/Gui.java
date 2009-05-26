/**
 * 
 */
package aufgabe6;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.TextField;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.tree.DefaultMutableTreeNode;

/**
 * @author sascha
 *
 */
public class Gui implements GuiInterface {
	
	private static final Dimension FENSTER_MIN_DIM = new Dimension(800,600);

	private static final int NAMENSFELD_MAX_BREITE = 20;

	private JFrame fenster = null;
	
	private JPanel serverSicht = null;
	
	private JTree spielerAnsicht = null;
	
	private DefaultMutableTreeNode wurzelKnotenSpielerAnsicht = null;
	
	private JScrollPane serverAnsichtsContainer = null;
	
	private DefaultMutableTreeNode wurzelKnotenServerAnsicht = null;
	
	private JTextField namensFeld = null;
	
	private JPanel knopfContainerServerAnsicht = null;
	
	private JButton erstellKnopf = null;
	
	private JButton auffrischKnopf = null;
	
	private JButton verbindeKnopf = null;
	
	private JButton trenneKnopf = null;
	
	private JTextPane spielNachrichten = null;
	
	private JPanel spielfeld = null;

	private JPanel spielfeldContainer = null;
	
	private static Gui singleTon = null;
	
	public static Gui getGui(){
		if(Gui.singleTon == null){
			Gui.singleTon = new Gui();
		}
		return Gui.singleTon;
	}
	
	private Gui(){
		
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
		
		this.fenster = new JFrame("Mensch ärger dich nicht");
		this.fenster.setMinimumSize(FENSTER_MIN_DIM);
		this.fenster.setUndecorated(false);
		this.fenster.setLocationByPlatform(true);
		this.fenster.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JSplitPane unterteiler = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT); 
		unterteiler.setResizeWeight(1d);
		unterteiler.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		unterteiler.setDividerSize(5);
		
		this.spielfeldContainer  = new JPanel();
		
		this.spielfeld = new JPanel(new GridLayout(11,11));
		this.spielfeld.setBackground(Color.WHITE);
		this.spielfeld.setBorder(BorderFactory.createLoweredBevelBorder());
		
		spielfeldContainer.add(this.spielfeld);
		
		unterteiler.setLeftComponent(spielfeldContainer);
		
		JPanel rechtesUnterFenster = new JPanel(new BorderLayout());
		rechtesUnterFenster.setPreferredSize(new Dimension(200,1));
		
		this.serverSicht = new JPanel(new BorderLayout());
		
		JTree serverAnsicht = new JTree();
		
		this.wurzelKnotenServerAnsicht = new DefaultMutableTreeNode("verfügbare Server");
		
		serverAnsicht = new JTree(this.wurzelKnotenServerAnsicht);
		
		this.serverAnsichtsContainer = new JScrollPane(serverAnsicht);
		
		this.serverSicht.add(this.serverAnsichtsContainer,BorderLayout.NORTH);
		
		this.knopfContainerServerAnsicht = new JPanel(new GridLayout(2,2));
		
		this.namensFeld  = new JTextField("Spielername", NAMENSFELD_MAX_BREITE);
		this.namensFeld.setEditable(true);
		
		this.knopfContainerServerAnsicht.add(this.namensFeld);
		
		this.erstellKnopf = new JButton("erstellen");
		
		this.knopfContainerServerAnsicht.add(this.erstellKnopf);
		
		this.auffrischKnopf = new JButton("auffrischen");
		
		this.knopfContainerServerAnsicht.add(this.auffrischKnopf);
		
		this.verbindeKnopf = new JButton("verbinden");
		
		this.knopfContainerServerAnsicht.add(this.verbindeKnopf);
		
		this.serverSicht.add(this.knopfContainerServerAnsicht, BorderLayout.CENTER);
		
		rechtesUnterFenster.add(this.serverSicht, BorderLayout.NORTH);
		
		this.wurzelKnotenSpielerAnsicht = new DefaultMutableTreeNode();
		
		this.spielerAnsicht = new JTree(this.wurzelKnotenSpielerAnsicht);
		
		this.trenneKnopf = new JButton("trennen");
		
		this.spielNachrichten = new JTextPane();
		this.spielNachrichten.setEditable(true);
		
		JScrollPane nachrichtenContainer = new JScrollPane(this.spielNachrichten);
		
		rechtesUnterFenster.add(nachrichtenContainer, BorderLayout.CENTER);
		
		unterteiler.setRightComponent(rechtesUnterFenster);
		
		this.fenster.add(unterteiler);
		
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
		this.fenster.pack();
		this.passeSpielfeldAn();
		this.fenster.setVisible(true);
	}

	private void passeSpielfeldAn() {
		int groesse = Math.min(this.spielfeldContainer.getWidth(), this.spielfeldContainer.getHeight());
		this.spielfeld.setPreferredSize(new Dimension(groesse,groesse));
		this.spielfeldContainer.validate();	
	}
}
