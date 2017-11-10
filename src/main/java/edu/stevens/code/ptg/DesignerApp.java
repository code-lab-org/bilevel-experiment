package edu.stevens.code.ptg;

import java.awt.FlowLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import edu.stevens.code.eager.designer.DesignerUI;
import edu.stevens.code.ptg.gui.DesignerPanel;
import edu.stevens.code.ptg.gui.ManagerPanel;
import edu.stevens.code.ptg.hla.Ambassador;
import hla.rti1516e.exceptions.RTIexception;

/**
 * The Class DesignerApp.
 */
public class DesignerApp implements App {
    private static final Logger logger = LogManager.getLogger(DesignerApp.class);

	private final Designer[] designers = new Designer[Manager.NUM_DESIGNERS];
	private Designer self = null;
	private Manager manager = new Manager();
	private Ambassador ambassador = null;
	
	/** AMVRO: Test to create a DesignerUI panel
	 * from PTG's DesignerApp class. 
	 */
	private DesignerUI dUI;
	
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
		self = designers[id];
		
		/* AMVRO: Creating the dUI object. */
		dUI = new DesignerUI(id);
//		dUI.setGame("PB01");
//		dUI.setDefaultUI();
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
		
		self.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				try {
					ambassador.updateDesigner(self);
				} catch (RTIexception e) {
					logger.error(e);
				}
			}
		});

        SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JFrame f = new JFrame();
				JPanel p = new JPanel();
				p.setLayout(new FlowLayout());
				
				/** AMVRO: Test to create a DesignerUI panel
				 * from PTG's DesignerApp class. 
				 * 
				 */
				JFrame fUI = new JFrame();
				dUI.setJFrameUI(fUI);
				dUI.setGame("SH01");
				dUI.setDefaultUI();
				
				
				for(Designer designer : designers) {
					DesignerPanel dPanel = new DesignerPanel();
					if(!designer.equals(self)) {
						dPanel.observe(designer);
					} else {
						dPanel.bindTo(self);
					}
					p.add(dPanel);
				}
				ManagerPanel mPanel = new ManagerPanel();
				mPanel.observe(getManager());
				p.add(mPanel);
				f.setContentPane(p);
				f.setTitle(self.toString());
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
	public void useMouse(JFrame j_frame, DesignerUI dUI){
		
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
	 * @see edu.stevens.code.ptg.App#getSelf()
	 */
	@Override
	public Designer getSelf() {
		return self;
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
