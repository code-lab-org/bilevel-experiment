package edu.stevens.code.ptg.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import edu.stevens.code.ptg.Designer;
import edu.stevens.code.ptg.DesignerApp;

public class DesignerUI extends DesignerAppPanel {
	private static final long serialVersionUID = 1163389143406697128L;

	private JLabel timeLabel;
	private DesignUI[] designUIs = new DesignUI[Designer.NUM_STRATEGIES];
	
	public DesignerUI() {
		this.setLayout(new BorderLayout());
		JPanel timePanel = new JPanel(new FlowLayout());
		timePanel.add(new JLabel("Time Remaining:"));
		timeLabel = new JLabel("0", JLabel.RIGHT);
		timePanel.add(timeLabel);
		this.add(timePanel, BorderLayout.NORTH);
		JTabbedPane tabbedPane = new JTabbedPane();
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			designUIs[i] = new DesignUI(i);
			tabbedPane.addTab("Design " + i, designUIs[i]);
		}
		JPanel strategyPanel = new JPanel();
		tabbedPane.addTab("Final", strategyPanel);
		
		this.add(tabbedPane, BorderLayout.CENTER);
	}
	
	@Override
	public void bindTo(DesignerApp app) {
		app.getController().setReadyToShare(true);
		for(int i = 0; i < Designer.NUM_STRATEGIES; i++) {
			designUIs[i].bindTo(app);
		}
		app.getManager().addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				timeLabel.setText(new Integer(app.getManager().getTimeRemaining()).toString());
			}
		});
	}
}
