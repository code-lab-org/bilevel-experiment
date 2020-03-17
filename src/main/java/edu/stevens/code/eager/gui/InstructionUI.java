package edu.stevens.code.eager.gui;

import java.awt.Font;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JEditorPane;

import edu.stevens.code.eager.model.Manager;
import edu.stevens.code.eager.model.Task;

public class InstructionUI extends JEditorPane {
	private static final long serialVersionUID = -5921608043683745782L;
	
	public InstructionUI() {
		this.putClientProperty(JEditorPane.HONOR_DISPLAY_PROPERTIES, Boolean.TRUE);
		this.setFont(new Font("Helvetica", Font.PLAIN, getFont().getSize()+8));
		this.setContentType("text/html");
		this.setEditable(false);
		this.setFocusable(false);
	}
	
	private String getText(Manager manager) {
		String text = "<html><center><h1>" + manager.getRoundName() + "</h1>" +
				"<p>Time Limit: " + String.format("%01d:%02d", Manager.MAX_TASK_TIME/60, Manager.MAX_TASK_TIME % 60) + "</p>" +
				"<h2>Assignments</h2>";// +
//				"<table><tr><th>Task</th><td><td><td><th>Designers</th></tr>";
		for(int i = 0; i < Manager.NUM_TASKS; i++) {
//			text += "<tr><td>" + manager.getTask(i).getName() + "</td><td><td><td><td>";
			text += "<tr>" + "</td><td><td><td><td>";
			for(int j = 0; j < Task.NUM_DESIGNERS; j++) {
				text += "Designer " + manager.getTask(i).getDesignerId(j);
				if(j + 1 < Task.NUM_DESIGNERS) {
					text += " and ";
				}
			}
			text += "</td></tr>";
		}
		text += "</table></center></html>";
		return text;
	}

	public void observe(Manager manager) {
		setText(getText(manager));
		manager.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				setText(getText(manager));
			}
		});
	}
}
