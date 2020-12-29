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
package edu.stevens.code.bilevel.gui.designer;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;

import edu.stevens.code.bilevel.app.DesignerApp;
import edu.stevens.code.bilevel.model.Designer;
import edu.stevens.code.bilevel.model.Manager;

/**
 * A label that displays the color-coded value of a decision.
 * 
 * @author Paul T. Grogan <pgrogan@stevens.edu>
 * @author Ambrosio Valencia-Romero <avalenci@stevens.edu>
 */
public class ValueLabel extends JLabel {
	private static final long serialVersionUID = -125874855243548180L;
	public static final int VCOLOR_SWITCH = 66;
	
	private DesignerApp app;
	private int myStrategy, partnerStrategy;
	private int[] myDesigns = new int[] {Designer.NUM_DESIGNS/2, Designer.NUM_DESIGNS/2};
	private int[] partnerDesigns = new int[] {Designer.NUM_DESIGNS/2, Designer.NUM_DESIGNS/2};
	
	/**
	 * Instantiates a new value label.
	 */
	public ValueLabel() {
		this.setPreferredSize(new Dimension(200,200));
		this.setOpaque(true);
		this.setVerticalAlignment(JLabel.CENTER);
		this.setHorizontalAlignment(JLabel.CENTER);
		this.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				setFont(getFont().deriveFont(Math.max(Math.min(Math.min(getWidth()/4f,getHeight()/2f), 48), 12)));
			}
		});
	}
	
	/**
	 * Binds this label to the actions of a designer application.
	 *
	 * @param app the application
	 * @param myStrategy the my strategy
	 * @param partnerStrategy the partner strategy
	 */
	public void bindTo(DesignerApp app, int myStrategy, int partnerStrategy) {
		this.app = app;
		this.app.getManager().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if(app.getManager().getTimeRemaining() == Manager.MAX_TASK_TIME) {
					partnerDesigns[0] = Designer.NUM_DESIGNS/2;
					partnerDesigns[1] = Designer.NUM_DESIGNS/2;
				}
				updateLabel();
			}
		});
		for(Designer designer : app.getDesigners()) {
			designer.addObserver(new Observer() {
				@Override
				public void update(Observable o, Object arg) {
					if(designer == app.getDesignPartner() && designer.isReadyToShare()) {
						if(!Arrays.equals(partnerDesigns, designer.getDesigns())) {
							partnerDesigns[0] = designer.getDesign(0);
							partnerDesigns[1] = designer.getDesign(1);
							updateLabel();
						}
					} else if(designer == app.getController()) {
						if(!Arrays.equals(myDesigns, app.getController().getDesigns())) {
							myDesigns[0] = app.getController().getDesign(0);
							myDesigns[1] = app.getController().getDesign(1);
							updateLabel();
						}
					}
				}
			});
		}
		this.myStrategy = myStrategy;
		this.partnerStrategy = partnerStrategy;
	}
	
	/**
	 * Update the label in response to a design/strategy decision.
	 */
	protected void updateLabel() {
		int value = app.getValue(myStrategy, myDesigns, partnerStrategy, partnerDesigns);
		if(app.getManager().isDesignEnabled() && value >= 0 && value <= 100) {
			this.setBackground(DesignerUI.VALUE_COLORS[ (int) Math.round(value) ]);
			this.setText(new Integer(value).toString());
			if (value > VCOLOR_SWITCH) {
				this.setForeground(Color.BLACK);
			} else {
				this.setForeground(Color.WHITE);
			}
		} else {
			this.setBackground(Color.BLACK);
			this.setText("");
		}
	}
}
