package edu.stevens.code.ptg;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JToggleButton;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.stevens.code.eager.designer.DesignerUI;
import edu.stevens.code.eager.ui.DesignSpaceUI;
import edu.stevens.code.ptg.gui.DesignerAppPanel;
import edu.stevens.code.ptg.hla.Ambassador;
import hla.rti1516e.exceptions.RTIexception;

/**
 * The Class DesignerApp.
 */
public class DesignerApp implements App {
    private static final Logger logger = LogManager.getLogger(DesignerApp.class);

	private final Designer[] designers = new Designer[Manager.NUM_DESIGNERS];
	private Designer designer = null;
	private Manager manager = new Manager();
	private Ambassador ambassador = null;
	
	/**
	 * Instantiates a new designer app.
	 *
	 * @param id the id
	 */
	public DesignerApp(int id) {
		if(id < 0 || id >= Manager.NUM_DESIGNERS) {
			throw new IllegalArgumentException("invalid designer id");
		}
		for(int i = 0; i < Manager.NUM_DESIGNERS; i++) {
			Designer d = new Designer();
			d.setId(i);
			designers[i] = d;
		}
		designer = designers[id];
	}

	/* (non-Javadoc)
	 * @see edu.stevens.code.ptg.App#init(java.lang.String)
	 */
	@Override
	public void init(String federationName) {
		if(ambassador == null) {
			try {
				ambassador = new Ambassador();
			} catch (RTIexception ex) {
				logger.error(ex);
			}
		}
		
		try {
			ambassador.connectDesigner(this, federationName);
		} catch (RTIexception ex) {
			logger.error(ex);
		}
		
		designer.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				try {
					ambassador.updateDesigner(designer);
				} catch (RTIexception e) {
					logger.error(e);
				}
			}
		});
		
		DesignerApp self = this;

        SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {				
				/** AMVRO: Test to create a DesignerUI panel
				 * from PTG's DesignerApp class. 
				 * 
				 */
				JFrame fUI = new JFrame("Designer " + String.valueOf(designer.getId()));
//				JTabbedPane tUI = new JTabbedPane();
				DesignerUI dUI = new DesignerUI("SH01");
				
				/* AMVRO: Creating the dUI object. */
//				DesignSpaceUI dUI = new DesignSpaceUI();
				
//				tUI.add("Design Space",dUI);
				
//				JLabel designSpaceLabel = new JLabel("Design Space");
//				designSpaceLabel.setPreferredSize(new Dimension(200, 30));
//				tUI.setTabComponentAt(0, designSpaceLabel);
				
//				fUI.add(tUI);
				fUI.add(dUI);
				
//				fUI.setContentPane(dUI);
				
				
				/* Because there is no title bar (see previous comment),
				 * the interface needs to be closed using Alt+F4.
				 */
				fUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				/* Set Icon */
//				DesignerUI.loadIcons(fUI);
				
				/* Initialize JFrame in maximized mode: */
				fUI.setExtendedState(JFrame.MAXIMIZED_BOTH);
				
				/* This is the JFrame is packed */
				fUI.pack();
				fUI.setLocationRelativeTo(null);
				fUI.setVisible(true);
				
				fUI.setIconImages(ICONS);
				

				final JToggleButton max_button = new JToggleButton();
				max_button.setBounds(1920-50, 0, 50, 50);
				max_button.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						fUI.dispose();
						if (max_button.isSelected()) {
							fUI.setUndecorated(true);
						} else {
							fUI.setUndecorated(false);
						}
						fUI.setVisible(true);
					}
				});
//				dUI.add(max_button);
				dUI.designSpace.add(max_button);
//				
//				dUI.useMouse(fUI);
//				dUI.setLayout(null);
//				
//				dUI.observe(manager, designers);
				dUI.designSpace.observe(manager, designers);
				dUI.designUI[0].observe(manager, designers);
				dUI.designUI[1].observe(manager, designers);
				
                for(Designer designer : designers) {                    
                    if(designer.equals(designer)) {
//                        dUI.bindTo(self);
                        dUI.designSpace.bindTo(designer);
                        dUI.designUI[0].bindTo(designer);
                        dUI.designUI[1].bindTo(designer);
                    }
//                    dUI.setGame("SH01");
                }

				JFrame f = new JFrame();
				f.setContentPane(new DesignerAppPanel(self));
				f.setTitle(designer.toString());
				f.setVisible(true);
		        f.pack();
		        f.setLocationRelativeTo(null);
		        f.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						kill();
					}
		        });
			}
        });
	}
	
	/** AMVRO: Method originally from DesignerUI class
	 * 
	 * @param j_frame
	 * @param dUI
	 */
	public void useMouse(JFrame j_frame, DesignSpaceUI dUI){
		
		j_frame.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent me) {
				
				/* The next two lines take the (x,y) position of the MouseEvent "me"
				 * and assign it to xAi and xAj.
				 * I might use main_frame.setXAi(int) and main_frame.setXAj(int)
				 * instead of main_frame.xAi = int and main_frame.xAj = int,
				 * respectively; that could be a better practice. IDK!
				 */
				
				dUI.setXscreen(me.getX());
				dUI.setYscreen(me.getY());
				
				/* Here it is where I repaint the contents in the "main_frame". */
				dUI.repaint();
				}
        });
		
	}
	

	/* (non-Javadoc)
	 * @see edu.stevens.code.ptg.App#kill()
	 */
	@Override
	public void kill() {
		try {
			ambassador.disconnect();
		} catch (RTIexception ex) {
			logger.error(ex);
		}
		System.exit(0);
	}

	/* (non-Javadoc)
	 * @see edu.stevens.code.ptg.App#getController()
	 */
	@Override
	public Designer getController() {
		return designer;
	}

	/* (non-Javadoc)
	 * @see edu.stevens.code.ptg.App#getDesigner(int)
	 */
	@Override
	public Designer getDesigner(int index) {
		if(index < 0 || index >= Manager.NUM_DESIGNERS) {
			throw new IllegalArgumentException("invalid designer index");
		}
		return designers[index];
	}

	/* (non-Javadoc)
	 * @see edu.stevens.code.ptg.App#getDesigners()
	 */
	@Override
	public Designer[] getDesigners() {
		return Arrays.copyOf(designers, Manager.NUM_DESIGNERS);
	}

	/* (non-Javadoc)
	 * @see edu.stevens.code.ptg.App#getManager()
	 */
	@Override
	public Manager getManager() {
		return manager;
	}
}
