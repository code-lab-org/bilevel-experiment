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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Stroke;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JPanel;

import edu.stevens.code.bilevel.app.DesignerApp;
import edu.stevens.code.bilevel.model.Designer;
import edu.stevens.code.bilevel.model.Manager;

/**
 * A panel that displays the current state of a design space.
 * 
 * @author Paul T. Grogan <pgrogan@stevens.edu>
 * @author Ambrosio Valencia-Romero <avalenci@stevens.edu>
 */
public class ValuePanel extends JPanel {
	private static final long serialVersionUID = -125874855243548180L;
	
	private DesignerApp app;
	private int myRefStrategy, partnerRefStrategy;
	private int partnerStrategy;
	private int[] myDesigns = new int[] {Designer.NUM_DESIGNS/2, Designer.NUM_DESIGNS/2};
	private int[] partnerDesigns = new int[] {Designer.NUM_DESIGNS/2, Designer.NUM_DESIGNS/2};
	private boolean hiddenStates = true;
	private int maxStatesVisible = 5;
	private Object[][] states = new Object[Designer.NUM_DESIGNS][Designer.NUM_DESIGNS];
	private Queue<Object> visibleStates = new LinkedBlockingQueue<Object>(maxStatesVisible);
	private boolean shiftStates = false; // true: show values for matching strategy; false: show values for non-matching strategies
	private boolean splitView = true; // split the display of the selected design for matching/non-matching strategies
	private boolean strategyView = true; // show partner strategy for selected design
	
	/**
	 * Instantiates a new value panel.
	 */
	public ValuePanel() {
		this.setMinimumSize(new Dimension(100,100));
		this.setPreferredSize(new Dimension(200,200));
		this.setOpaque(false);
		for(int i = 0; i < Designer.NUM_DESIGNS; i++) {
			for(int j = 0; j < Designer.NUM_DESIGNS; j++) {
				states[i][j] = new Object();
			}
		}
	}
	
	/**
	 * Shift the display of states between the diagonal/off-diagonal strategy pairs.
	 *
	 * @param shiftStates the shift states
	 */
	public void shiftStates(boolean shiftStates) {
		if(this.shiftStates != shiftStates) {
			this.shiftStates = shiftStates;
			repaint();
		}
	}
	
	/**
	 * Update the state of cells in response to a design/strategy decision.
	 */
	private void updateStates() {
		int myDesign = myDesigns[myRefStrategy];
		int partnerDesign = partnerDesigns[partnerRefStrategy];
        visibleStates.clear();
		visibleStates.add(states[myDesign][partnerDesign]);
		if((partnerDesign - 1) >= 0){
			visibleStates.add(states[myDesign][partnerDesign - 1]);
		}
		if((partnerDesign + 1) <= (Designer.NUM_DESIGNS - 1)){
			visibleStates.add(states[myDesign][partnerDesign + 1]);
		}
		if((myDesign - 1) >= 0){
			visibleStates.add(states[myDesign - 1][partnerDesign]);
		}
		if((myDesign + 1) <= (Designer.NUM_DESIGNS - 1)){
			visibleStates.add(states[myDesign + 1][partnerDesign]);
		}
		repaint();
	}
	
	/**
	 * Bind to a designer application.
	 *
	 * @param app the application
	 * @param myRefStrategy the my reference strategy
	 * @param partnerRefStrategy the partner reference strategy
	 */
	public void bindTo(DesignerApp app, int myRefStrategy, int partnerRefStrategy) {
		this.app = app;
		this.app.getManager().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if(app.getManager().getTimeRemaining() == Manager.MAX_TASK_TIME) {
					partnerDesigns[0] = Designer.NUM_DESIGNS/2;
					partnerDesigns[1] = Designer.NUM_DESIGNS/2;
					partnerStrategy = -1;
					visibleStates.clear();
					repaint();
				}
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
							updateStates();
						}
						if(app.getManager().getTimeRemaining() > Manager.STRATEGY_TIME 
								&& partnerStrategy != designer.getStrategy()) {
							partnerStrategy = designer.getStrategy();
							repaint();
						}
					} else if(designer == app.getController()) {
						if(!Arrays.equals(myDesigns, app.getController().getDesigns())) {
							myDesigns[0] = app.getController().getDesign(0);
							myDesigns[1] = app.getController().getDesign(1);
							updateStates();
						}
					}
				}
			});
		}
		app.getManager().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if(app.getManager().getTimeRemaining() <= Manager.STRATEGY_TIME) {
					if(partnerStrategy != -1) {
						partnerStrategy = -1;
						repaint();
					}
				}
			}
		});
		this.myRefStrategy = myRefStrategy;
		this.partnerRefStrategy = partnerRefStrategy;
	}
	
	/**
	 * Gets the design based on an x-pixel coordinate.
	 *
	 * @param x the x
	 * @return the design
	 */
	public int getDesign(int x) {
		Insets insets = this.getInsets();
		int width = (this.getWidth() - insets.left - insets.right)/(Designer.NUM_DESIGNS+1);
		return x / width - 1;
	}
	
	/**
	 * Paint.
	 *
	 * @param g the graphics
	 */
	public void paint(Graphics g) {
		super.paint(g);
		int myDesign = myDesigns[myRefStrategy];
		int partnerDesign = partnerDesigns[partnerRefStrategy];
		Graphics2D g2D = (Graphics2D) g;
		Insets insets = this.getInsets();
		// number of cells = number of designs + 1 (reason: add tick labels)
		int width = (this.getWidth() - insets.left - insets.right)/(Designer.NUM_DESIGNS + 1);
		int height = (this.getHeight() - insets.top - insets.bottom)/(Designer.NUM_DESIGNS + 1);
		this.setFont(new Font("Arial", Font.BOLD, getFont().getSize()));
		if(splitView) {
			this.setFont(getFont().deriveFont(Math.max(Math.min(Math.min(width/2.8f,height/1.0f), 48), 12)));
		} else {
			this.setFont(getFont().deriveFont(Math.max(Math.min(Math.min(width/2f,height/1.0f), 48), 12)));
		}
		// draw design space cells
		for(int i = 0; i < Designer.NUM_DESIGNS; i++) {
			for(int j = 0; j < Designer.NUM_DESIGNS; j++) {
				int value = 0;
				int[] _myDesigns = new int[2];
				_myDesigns[myRefStrategy] = i;
				_myDesigns[1-myRefStrategy] = myDesigns[1-myRefStrategy];
				int[] _partnerDesigns = new int[2];
				_partnerDesigns[partnerRefStrategy] = j;
				_partnerDesigns[1-partnerRefStrategy] = partnerDesigns[1-partnerRefStrategy];
				if(shiftStates) {
					value = app.getValue(myRefStrategy, _myDesigns, 1-partnerRefStrategy, _partnerDesigns);
				} else {
					value = app.getValue(myRefStrategy, _myDesigns, partnerRefStrategy, _partnerDesigns);
				}
				if(app.getManager().isDesignEnabled() && value >= 0 && value <= 100 && (!hiddenStates || visibleStates.contains(states[i][j]))) {
					g2D.setColor(DesignerUI.VALUE_COLORS[value]);
				} else {
					g2D.setColor(Color.BLACK);
				}
				g2D.fillRect(insets.left + (i+1)*width, insets.top + (Designer.NUM_DESIGNS-j-1)*height, width, height);
			}
		}
		if(app.getManager().isDesignEnabled()) {
			// draw rulers
			g2D.setColor(Color.decode("#7c7b78"));
			int t = 1 + (width*height/(80*80));
			Stroke dashed = new BasicStroke(t, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{t}, 0);
			g2D.setStroke(dashed);
			// draw vertical ruler (horizontal slider)
			g2D.drawRect(insets.left + (myDesign+1)*width, insets.top + 0, width, (Designer.NUM_DESIGNS+1)*height);
			// draw horizontal ruler (vertical slider)
			g2D.drawRect(insets.left + 0, insets.top + (Designer.NUM_DESIGNS-partnerDesign-1)*height, (Designer.NUM_DESIGNS+1)*width, height);
			// draw selected cell
			int value = 0;
			if(shiftStates) {
				value = app.getValue(myRefStrategy, myDesigns, 1-partnerRefStrategy, partnerDesigns);
			} else {
				value = app.getValue(myRefStrategy, myDesigns, partnerRefStrategy, partnerDesigns);
			}
			FontMetrics fm = getFontMetrics(getFont());
			dashed = new BasicStroke(t+1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{t+1}, 0);
			if(splitView) {
				double gap = 0;
				// reference strategy value label in upper left
				int[] upperLeftTriangleX = new int[] {
						(int) (insets.left + (myDesign+1+1-gap)*width),
						(int) (insets.left + (myDesign+1+gap)*width),
						(int) (insets.left + (myDesign+1+gap)*width)
					};
				int[] upperLeftTriangleY = new int[] {
					(int) (insets.top + (Designer.NUM_DESIGNS-partnerDesign-1+gap)*height),
					(int) (insets.top + (Designer.NUM_DESIGNS-partnerDesign-1+gap)*height),
					(int) (insets.top + (Designer.NUM_DESIGNS-partnerDesign-1+1-gap)*height)
				};
				if (value > ValueLabel.VCOLOR_SWITCH) {
					g2D.setColor(Color.BLACK);
				} else {
					g2D.setColor(Color.WHITE);
				}
				String text = new Integer(value).toString();
				int textX = (int) (insets.left + (myDesign+1+1/3.+0.035)*width - fm.getStringBounds(text, g2D).getCenterX());
				int textY = (int) (insets.top + (Designer.NUM_DESIGNS-partnerDesign-1+0.22)*height - fm.getStringBounds(text, g2D).getCenterY() + t/2);
				g2D.drawString(text, textX, textY);
				// shifted value label in lower right
				int[] lowerRightTriangleX = new int[] {
						(int) (insets.left + (myDesign+1+gap)*width - t/2),
						(int) (insets.left + (myDesign+1+1-gap)*width - t/2),
						(int) (insets.left + (myDesign+1+1-gap)*width - t/2)
					};
				int[] lowerRightTriangleY = new int[] {
					(int) (insets.top + (Designer.NUM_DESIGNS-partnerDesign-1+1-gap)*height - t/2),
					(int) (insets.top + (Designer.NUM_DESIGNS-partnerDesign-1+1-gap)*height - t/2),
					(int) (insets.top + (Designer.NUM_DESIGNS-partnerDesign-1+gap)*height - t/2)
				};
				int shiftValue = 0;
				if(shiftStates) {
					shiftValue = app.getValue(myRefStrategy, myDesigns, partnerRefStrategy, partnerDesigns);
				} else {
					shiftValue = app.getValue(myRefStrategy, myDesigns, 1-partnerRefStrategy, partnerDesigns);
				}
				g2D.setColor(DesignerUI.VALUE_COLORS[shiftValue]);
				g2D.fillPolygon(lowerRightTriangleX, lowerRightTriangleY, 3);
				if (shiftValue > ValueLabel.VCOLOR_SWITCH) {
					g2D.setColor(Color.BLACK);
				} else {
					g2D.setColor(Color.WHITE);
				}
				String shiftText = new Integer(shiftValue).toString();
				int shiftTextX = (int) (insets.left + (myDesign+1+2/3.-0.035)*width - fm.getStringBounds(shiftText, g2D).getCenterX());
				int shiftTextY = (int) (insets.top + (Designer.NUM_DESIGNS-partnerDesign-1+0.78)*height - fm.getStringBounds(shiftText, g2D).getCenterY());
				g2D.drawString(shiftText, shiftTextX, shiftTextY);
				g2D.setStroke(dashed);
				g2D.setColor(new Color(147, 93, 116));
				g2D.drawLine(upperLeftTriangleX[0], upperLeftTriangleY[0], upperLeftTriangleX[2], upperLeftTriangleY[2]);
				
				int uLTX[] = {-3,  3,  3};
			    Arrays.setAll(uLTX, i -> upperLeftTriangleX[i] + uLTX[i]);
				int uLTY[] = { 3,  3, -3};
			    Arrays.setAll(uLTY, i -> upperLeftTriangleY[i] + uLTY[i]);
				
				int lRTX[] = { 3, -2, -2};
			    Arrays.setAll(lRTX, i -> lowerRightTriangleX[i] + lRTX[i]);
				int lRTY[] = {-2, -2,  3};
			    Arrays.setAll(lRTY, i -> lowerRightTriangleY[i] + lRTY[i]);
				
				g2D.setStroke(new BasicStroke(t+3f));
				g2D.setColor(Color.MAGENTA);
				if(strategyView && partnerStrategy == partnerRefStrategy) {
					// partner agrees with reference strategy: highlight upper-left value;
					g2D.drawPolygon(uLTX, uLTY, 3);
					g2D.setStroke(new BasicStroke(1.5f));
					g2D.setColor(Color.WHITE);
					g2D.drawPolygon(uLTX, uLTY, 3);
				} else if(strategyView && partnerStrategy == 1 - partnerRefStrategy) {
					// partner disagrees with reference strategy: highlight lower-right value
					g2D.drawPolygon(lRTX, lRTY, 3);
					g2D.setStroke(new BasicStroke(1.5f));
					g2D.setColor(Color.WHITE);
					g2D.drawPolygon(lRTX, lRTY, 3);
				}
			} else {
				// value label in center
				if (value > ValueLabel.VCOLOR_SWITCH) {
					g2D.setColor(Color.BLACK);
				} else {
					g2D.setColor(Color.WHITE);
				}
				String text = new Integer(value).toString();
				int x = (int) (insets.left + (myDesign+1+0.5)*width - fm.getStringBounds(text, g2D).getCenterX());
				int y = (int) (insets.top + (Designer.NUM_DESIGNS-partnerDesign-1+0.5)*height - fm.getStringBounds(text, g2D).getCenterY());
				g2D.drawString(text, x, y);
			}
			// draw tick labels
			g2D.setColor(Color.BLACK);
			for (int i = Designer.MIN_DESIGN_VALUE; i < Designer.MAX_DESIGN_VALUE+1; i++){
				String ticklabel = String.valueOf((char)(i + 64 + 1));
				int xi = (int) (insets.left + (i+0.5+1)*width - fm.getStringBounds(ticklabel, g2D).getCenterX());
				int yi = (int) (insets.top + (Designer.NUM_DESIGNS+1-0.5)*height - fm.getStringBounds(ticklabel, g2D).getCenterY());
				int xj = (int) (insets.left + (0+0.5+0)*width - fm.getStringBounds(ticklabel, g2D).getCenterX());
				int yj = (int) (insets.top + (Designer.NUM_DESIGNS-i-0.5)*height - fm.getStringBounds(ticklabel, g2D).getCenterY());
				g2D.drawString(ticklabel, xi, yi);
				g2D.drawString(ticklabel, xj, yj);
			}
		}
	}
}
