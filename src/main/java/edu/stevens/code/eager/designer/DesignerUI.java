package edu.stevens.code.eager.designer;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import edu.stevens.code.eager.ui.DesignSpaceUI;
import edu.stevens.code.eager.ui.DesignUI;
import edu.stevens.code.eager.ui.NormalFormUI;
import edu.stevens.code.ptg.DesignerApp;

public class DesignerUI extends JTabbedPane {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -108921279029996748L;
	
	/** Contents of the JTabbedPane */
	public final JPanel instructionsPanel = new JPanel();
	public final DesignUI[] designsUI = new DesignUI[2];
	public final DesignSpaceUI designSpace;
	public final NormalFormUI normalForm = new NormalFormUI();
	
	/** Constructor */
	public DesignerUI(String game_file) {
		
		designsUI[0] = new DesignUI(game_file, 0);
		designsUI[1] = new DesignUI(game_file, 1);
		designSpace = new DesignSpaceUI(game_file);
		
		int nTabs = 5;
		Dimension dim = new Dimension(1920/nTabs - 23, 30);
		
		this.addTab("Instructions", instructionsPanel);
		this.add("Red", designsUI[0]);
		this.add("Blue", designsUI[1]);
		this.add("Design Space", designSpace);
		this.add("Final Decision", normalForm);

		JLabel tab0 = new JLabel("Instructions");
		JLabel tab1 = new JLabel("Red");
		JLabel tab2 = new JLabel("Blue");
		JLabel tab3 = new JLabel("Design Space");
		JLabel tab4 = new JLabel("Final Decision");
		tab0.setPreferredSize(dim);
		tab1.setPreferredSize(dim);
		tab2.setPreferredSize(dim);
		tab3.setPreferredSize(dim);
		tab4.setPreferredSize(dim);
		
		this.setTabComponentAt(0,tab0);
		this.setTabComponentAt(1,tab1);
		this.setTabComponentAt(2,tab2);
		this.setTabComponentAt(3,tab3);
		this.setTabComponentAt(4,tab4);
		
		
	}
	
	
	public static void main(String[] args) {
		
		/* Create frame that will contain the content defined
		 * through the MainUI object (which I have named "main_frame").
		 */
		JFrame frame = new JFrame("Collective Design Laboratory");
		
		/* Create the aforementioned "main_frame" object of class MainUI. */
		final DesignerUI dTabbedPanels = new DesignerUI("SH01");
		
		frame.add(dTabbedPanels);		
		frame.setIconImages(DesignerApp.ICONS);
		
		
		/* Here I tell Java to remove the title bar */
		frame.setUndecorated(false);
		
		/* Because there is no title bar (see previous comment),
		 * the interface needs to be closed using Alt+F4.
		 */
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		/* Initialize JFrame in maximized mode: */
		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		
		/* This is the JFrame is packed */
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);

	}

}
