/******************************************************************************
 * Copyright 2020 Stevens Institute of Technology, Collective Design Lab
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *****************************************************************************/
package edu.stevens.code.bilevel.app;

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

import edu.stevens.code.bilevel.gui.designer.DesignerAppPanel;
import edu.stevens.code.bilevel.gui.designer.DesignerUI;
import edu.stevens.code.bilevel.io.Ambassador;
import edu.stevens.code.bilevel.io.ZmqAmbassador;
import edu.stevens.code.bilevel.model.Designer;
import edu.stevens.code.bilevel.model.Manager;

/**
 * The software application used by designers.
 * 
 * @author Paul T. Grogan <pgrogan@stevens.edu>
 * @author Ambrosio Valencia-Romero <avalenci@stevens.edu>
 */
public class DesignerApp implements App {
	private final Designer[] designers = new Designer[Manager.NUM_DESIGNERS];
	private Designer designer = null;
	private Manager manager = new Manager();
	private Ambassador ambassador = null;
	
	/**
	 * Instantiates a new designer application for the designer with specified id.
	 *
	 * @param id the designer id
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

	@Override
	public void init(String federationName) {
		// initialize the ambassador
		if(ambassador == null) {
			ambassador = new ZmqAmbassador();
		}
		// connect the ambassador
		ambassador.connectDesigner(this, federationName);
		// add an observer to the designer to process ambassador updates
		designer.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				ambassador.updateDesigner(designer, arg);
			}
		});
		// launch the graphical user interface
		DesignerApp self = this;
        SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {								
				JFrame f = new JFrame();
				f.setIconImages(App.ICONS);
				// using version 3 of the designer user interface here
				DesignerAppPanel panel = new DesignerUI();
				panel.bindTo(self);
				f.setContentPane(panel);
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
				// add an action (triggered by f11) to toggle fullscreen
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
	
	@Override
	public void kill() {
		ambassador.disconnect();
		System.exit(0);
	}

	@Override
	public Designer getController() {
		return designer;
	}

	@Override
	public Designer getDesigner(int index) {
		if(index < 0 || index >= Manager.NUM_DESIGNERS) {
			throw new IllegalArgumentException(String.format("invalid designer index (%d)", index));
		}
		return designers[index];
	}

	@Override
	public Designer[] getDesigners() {
		return Arrays.copyOf(designers, Manager.NUM_DESIGNERS);
	}

	@Override
	public Manager getManager() {
		return manager;
	}
	
	/**
	 * Convenience function to get the design partner.
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
	 * Convenience function to get the value of a pair of design/strategy decisions.
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
