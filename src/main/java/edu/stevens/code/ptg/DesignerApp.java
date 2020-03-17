package edu.stevens.code.ptg;

import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.AbstractAction;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import edu.stevens.code.ptg.gui.DesignerAppPanel;
import edu.stevens.code.ptg.gui.DesignerUI3;
import edu.stevens.code.ptg.io.Ambassador;
import edu.stevens.code.ptg.io.ZmqAmbassador;

/**
 * The Class DesignerApp.
 */
public class DesignerApp implements App {
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
			throw new IllegalArgumentException(String.format("invalid designer id (%d)", id));
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
			ambassador = new ZmqAmbassador();
		}

		ambassador.connectDesigner(this, federationName);
		
		designer.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				ambassador.updateDesigner(designer, arg);
			}
		});
		
		DesignerApp self = this;

        SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {								
				JFrame f = new JFrame();
				f.setIconImages(App.ICONS);
				// DesignerAppPanel panel = new DebugDesignerAppPanel();
				DesignerAppPanel panel = new DesignerUI3();
//				panel.setPreferredSize(new Dimension(1920,1080));
				panel.bindTo(self);
				f.setContentPane(panel);
				f.setTitle(designer.toString());
				f.setVisible(true);
		        f.pack();
		        f.setLocationRelativeTo(null);
//		        f.setExtendedState(JFrame.MAXIMIZED_BOTH);
		        f.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						kill();
					}
		        });
				
		    	AbstractAction toggleFullscreen = new AbstractAction() {
		    		private static final long serialVersionUID = -3717417473415884817L;

		    		@Override
		    		public void actionPerformed(ActionEvent e) {
		    			if(!f.isUndecorated()) {
		    				// make full-screen
		    				f.setVisible(false);
		    				f.dispose();
		    				f.setUndecorated(true);
		    		 		GraphicsEnvironment.getLocalGraphicsEnvironment()
		    						.getDefaultScreenDevice().setFullScreenWindow(f);
		    		 		f.setVisible(true);
		    			} else {
		    				// make windowed
		    				f.setVisible(false);
		    				f.dispose();
		    				f.setUndecorated(false);
		    				GraphicsEnvironment.getLocalGraphicsEnvironment()
		    						.getDefaultScreenDevice().setFullScreenWindow(null);
		    				f.pack();
		    				f.repaint();
		    				f.setLocationRelativeTo(null);
		    				f.setVisible(true);
		    			}
		    		}
		    	};

		    	panel.getInputMap(DesignerAppPanel.WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("F11"), "toggleFullscreen");
		    	panel.getActionMap().put("toggleFullscreen", toggleFullscreen);
			}
        });
	}
	
	/* (non-Javadoc)
	 * @see edu.stevens.code.ptg.App#kill()
	 */
	@Override
	public void kill() {
		ambassador.disconnect();
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
			throw new IllegalArgumentException(String.format("invalid designer index (%d)", index));
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
	
	/**
	 * Gets the design partner.
	 *
	 * @return the design partner
	 */
	public Designer getDesignPartner() {
		int partnerId = getManager().getDesignPartner(getController().getId());
		if(partnerId >= 0) {
			return getDesigner(partnerId);
		} else {
			return null;
		}
	}
	
	/**
	 * Gets the value.
	 *
	 * @param myStrategy the my strategy
	 * @param myDesign the my design
	 * @param partnerStrategy the partner strategy
	 * @param partnerDesign the partner design
	 * @return the value
	 */
	public int getValue(int myStrategy, int[] myDesign, int partnerStrategy, int[] partnerDesign) {
		return manager.getValue(designer.getId(), myStrategy, myDesign, partnerStrategy, partnerDesign);
	}
}
