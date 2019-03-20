package edu.stevens.code.ptg.gui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.util.Arrays;
import java.util.Observable;
import java.util.Observer;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

import javax.swing.JPanel;

import edu.stevens.code.ptg.Designer;
import edu.stevens.code.ptg.DesignerApp;
import edu.stevens.code.ptg.Manager;

public class ValuePanel extends JPanel {
	private static final long serialVersionUID = -125874855243548180L;
	
	private DesignerApp app;
	private int myStrategy, partnerStrategy;
	private int[] myDesigns = new int[] {Designer.NUM_DESIGNS/2, Designer.NUM_DESIGNS/2};
	private int[] partnerDesigns = new int[] {Designer.NUM_DESIGNS/2, Designer.NUM_DESIGNS/2};
	private boolean hiddenStates = true;
	private int maxStatesVisible = 5;//9+4;//(int) Math.pow(Designer.NUM_DESIGNS,2);
	private Object[][] states = new Object[Designer.NUM_DESIGNS][Designer.NUM_DESIGNS];
	private Queue<Object> visibleStates = new LinkedBlockingQueue<Object>(maxStatesVisible);
	private boolean shiftStates = false;
	private boolean splitView = true;
	
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
	
	public void shiftStates(boolean shiftStates) {
		if(this.shiftStates != shiftStates) {
			this.shiftStates = shiftStates;
			repaint();
		}
	}
	
	private void updateStates() {
		int myDesign = myDesigns[myStrategy];
		int partnerDesign = partnerDesigns[partnerStrategy];
				
		
		/* SHOW EXPLORED CELLS ONLY */
//		if(visibleStates.contains(states[myDesign][partnerDesign])) {
//			visibleStates.remove(states[myDesign][partnerDesign]);
//		} else if(visibleStates.size() >= maxStatesVisible) {
//			visibleStates.poll();
//		}
		
		/* REMOVE ALL */
        visibleStates.clear(); /* Comment if showing explored cells (lines above) */
		
		/* CURRENT CELL */
		visibleStates.add(states[myDesign][partnerDesign]);
		
		/* SOUTH 1 */
		if ((partnerDesign - 1) >= 0){
			visibleStates.add(states[myDesign][partnerDesign - 1]);
		}
		
		/* NORTH 1 */
		if ((partnerDesign + 1) <= (Designer.NUM_DESIGNS - 1)){
			visibleStates.add(states[myDesign][partnerDesign + 1]);
		}
				
		/* SOUTH 2 */
//		if ((partnerDesign - 2) >= 0){
//			visibleStates.add(states[myDesign][partnerDesign - 2]);
//		}
		
		/* NORTH 2 */
//		if ((partnerDesign + 2) <= (Designer.NUM_DESIGNS - 1)){
//			visibleStates.add(states[myDesign][partnerDesign + 2]);
//		}

		/* WEST 1 */
		if ((myDesign - 1) >= 0){
			visibleStates.add(states[myDesign - 1][partnerDesign]);
			
		    /* SOUTHWEST 2 */
//			if ((partnerDesign - 1) >= 0){
//				visibleStates.add(states[myDesign - 1][partnerDesign - 1]);
//			}
	        /* NORTHWEST 2 */
//			if ((partnerDesign + 1) <= (Designer.NUM_DESIGNS - 1)){
//				visibleStates.add(states[myDesign - 1][partnerDesign + 1]);
//			}
		}

		/* EAST 1 */
		if ((myDesign + 1) <= (Designer.NUM_DESIGNS - 1)){
			visibleStates.add(states[myDesign + 1][partnerDesign]);
		
	        /* SOUTHEAST 2 */
//			if ((partnerDesign - 1) >= 0){
//				visibleStates.add(states[myDesign + 1][partnerDesign - 1]);
//			}
        	/* NORTHEAST 2 */
//			if ((partnerDesign + 1) <= (Designer.NUM_DESIGNS - 1)){
//				visibleStates.add(states[myDesign + 1][partnerDesign + 1]);
//			}
		}

		/* WEST 2 */
//		if ((myDesign - 2) >= 0){
//			visibleStates.add(states[myDesign - 2][partnerDesign]);
//		}

		/* EAST 2 */
//		if ((myDesign + 2) <= (Designer.NUM_DESIGNS - 1)){
//			visibleStates.add(states[myDesign + 2][partnerDesign]);
//		}
		repaint();
	}
	
	public void bindTo(DesignerApp app, int myStrategy, int partnerStrategy) {
		this.app = app;
		this.app.getManager().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				if(app.getManager().getTimeRemaining() == Manager.MAX_TASK_TIME) {
					partnerDesigns[0] = Designer.NUM_DESIGNS/2;
					partnerDesigns[1] = Designer.NUM_DESIGNS/2;
					visibleStates.clear();
					/* Next line would add cell (0,0) to the visible states.
					 * I commented (removed) it! */
					//visibleStates.add(states[0][0]);
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
		this.myStrategy = myStrategy;
		this.partnerStrategy = partnerStrategy;
	}
	
	public int getDesign(int x) {
		Insets insets = this.getInsets();
		int width = (this.getWidth() - insets.left - insets.right)/(Designer.NUM_DESIGNS+1);
		return x / width - 1;
	}
	
	public void paint(Graphics g) {
		super.paint(g);
		
		int myDesign = myDesigns[myStrategy];
		int partnerDesign = partnerDesigns[partnerStrategy];
			
		Graphics2D g2D = (Graphics2D) g;
		
		Insets insets = this.getInsets();
		/* Number of cells = Number of designs + 1; Reason: adding tick labels */ 
		int width = (this.getWidth() - insets.left - insets.right)/(Designer.NUM_DESIGNS + 1);
		int height = (this.getHeight() - insets.top - insets.bottom)/(Designer.NUM_DESIGNS + 1);

		this.setFont(new Font("Arial", Font.BOLD, getFont().getSize()));
		if(splitView) {
			this.setFont(getFont().deriveFont(Math.max(Math.min(Math.min(width/2.6f,height/1.0f), 48), 12)));
		} else {
			this.setFont(getFont().deriveFont(Math.max(Math.min(Math.min(width/2f,height/1.0f), 48), 12)));
		}
		
		for(int i = 0; i < Designer.NUM_DESIGNS; i++) {
			for(int j = 0; j < Designer.NUM_DESIGNS; j++) {
				int value = 0;
				int[] _myDesigns = new int[2];
				_myDesigns[myStrategy] = i;
				_myDesigns[1-myStrategy] = myDesigns[1-myStrategy];
				int[] _partnerDesigns = new int[2];
				_partnerDesigns[partnerStrategy] = j;
				_partnerDesigns[1-partnerStrategy] = partnerDesigns[1-partnerStrategy];
				if(shiftStates) {
					value = app.getValue(myStrategy, _myDesigns, 1-partnerStrategy, _partnerDesigns);
				} else {
					value = app.getValue(myStrategy, _myDesigns, partnerStrategy, _partnerDesigns);
				}
				if(app.getManager().isDesignEnabled() && value >= 0 && value <= 100 && (!hiddenStates || visibleStates.contains(states[i][j]))) {
					g2D.setColor(DesignerUI.VALUE_COLORS[value]);
//					if(Designer.VALUE_DELTA == 5) {
//						g2D.setColor(DesignerUI.VALUE_COLORS_21[value/Designer.VALUE_DELTA]);
//					} else if(Designer.VALUE_DELTA == 2) {
//						g2D.setColor(DesignerUI.VALUE_COLORS_51[value/Designer.VALUE_DELTA]);
//					}
				} else {
					g2D.setColor(Color.BLACK);
				}
				g2D.fillRect(insets.left + (i+1)*width, insets.top + (Designer.NUM_DESIGNS-j-1)*height, width, height);
			}
		}
		if(app.getManager().isDesignEnabled()) {
			
			
			/** Tick label background */
			/*  Horizontal tick label background
			if (myStrategy == 0) {
				g2D.setColor(Color.RED);
			} else if (myStrategy == 1) {
				g2D.setColor(Color.BLUE);
			}
			g2D.fillRect(width, (Designer.NUM_DESIGNS)*height, (Designer.NUM_DESIGNS)*width, height);
			/*  Vertical tick labels background
			if (partnerStrategy == 0) {
				g2D.setColor(Color.RED);
			} else if (partnerStrategy == 1) {
				g2D.setColor(Color.BLUE);
			}
			g2D.fillRect(0, 0, width, (Designer.NUM_DESIGNS)*height);
			*/
			
			/** Rulers */
			g2D.setColor(new Color(147, 93, 116));
			int t = 1+(width*height/(80*80));
			g2D.setStroke(new BasicStroke(t));
			/*  Vertical ruler (horizontal slider */
			g2D.drawRect(insets.left + (myDesign+1)*width, insets.top + 0, width, (Designer.NUM_DESIGNS+1)*height);
			/*  Horizontal ruler (vertical slider */
			g2D.drawRect(insets.left + 0, insets.top + (Designer.NUM_DESIGNS-partnerDesign-1)*height, (Designer.NUM_DESIGNS+1)*width, height);
			
			/* Selected cell */
			int value = 0;
			if(shiftStates) {
				value = app.getValue(myStrategy, myDesigns, 1-partnerStrategy, partnerDesigns);
			} else {
				value = app.getValue(myStrategy, myDesigns, partnerStrategy, partnerDesigns);
			}
			FontMetrics fm = getFontMetrics(getFont());
			if(splitView) {
				int shiftValue = 0;
				if(shiftStates) {
					shiftValue = app.getValue(myStrategy, myDesigns, partnerStrategy, partnerDesigns);
				} else {
					shiftValue = app.getValue(myStrategy, myDesigns, 1-partnerStrategy, partnerDesigns);
				}
				// split cell in half, recolor lower right triangle
				int[] xPoints = new int[] {
					insets.left + (myDesign+1)*width,
					insets.left + (myDesign+1+1)*width,
					insets.left + (myDesign+1+1)*width
				};
				int[] yPoints = new int[] {
					insets.top + (Designer.NUM_DESIGNS-partnerDesign-1+1)*height,
					insets.top + (Designer.NUM_DESIGNS-partnerDesign-1+1)*height,
					insets.top + (Designer.NUM_DESIGNS-partnerDesign-1)*height
				};
				g2D.setColor(DesignerUI.VALUE_COLORS[shiftValue]);
				g2D.fillPolygon(xPoints, yPoints, 3);
				// value label in upper left
				if (value > 45) {
					g2D.setColor(Color.BLACK);
				} else {
					g2D.setColor(Color.WHITE);
				}
				String text = new Integer(value).toString();
				int x = (int) (insets.left + (myDesign+1+1/3.)*width - fm.getStringBounds(text, g2D).getCenterX());
				int y = (int) (insets.top + (Designer.NUM_DESIGNS-partnerDesign-1+0.25)*height - fm.getStringBounds(text, g2D).getCenterY());
				g2D.drawString(text, x, y);
				// shifted value label in lower right
				if (shiftValue > 45) {
					g2D.setColor(Color.BLACK);
				} else {
					g2D.setColor(Color.WHITE);
				}
				String shiftText = new Integer(shiftValue).toString();
				int shiftX = (int) (insets.left + (myDesign+1+2/3.)*width - fm.getStringBounds(shiftText, g2D).getCenterX());
				int shiftY = (int) (insets.top + (Designer.NUM_DESIGNS-partnerDesign-1+0.75)*height - fm.getStringBounds(shiftText, g2D).getCenterY());
				g2D.drawString(shiftText, shiftX, shiftY);
			} else {
				// value label in center
				if (value > 45) {
					g2D.setColor(Color.BLACK);
				} else {
					g2D.setColor(Color.WHITE);
				}
				String text = new Integer(value).toString();
				int x = (int) (insets.left + (myDesign+1+0.5)*width - fm.getStringBounds(text, g2D).getCenterX());
				int y = (int) (insets.top + (Designer.NUM_DESIGNS-partnerDesign-1+0.5)*height - fm.getStringBounds(text, g2D).getCenterY());
				g2D.drawString(text, x, y);
			}
			// outline selected cell
			g2D.setColor(Color.MAGENTA);
			g2D.setStroke(new BasicStroke(t+2));
			g2D.drawRect(insets.left + (myDesign+1)*width, insets.top + (Designer.NUM_DESIGNS-partnerDesign-1)*height, width, height);

			
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
