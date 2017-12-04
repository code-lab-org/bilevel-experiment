package edu.stevens.code.ptg.gui;

import java.util.Observable;
import java.util.Observer;

import javax.swing.JEditorPane;

import edu.stevens.code.ptg.Manager;
import edu.stevens.code.ptg.Task;

public class InstructionUI extends JEditorPane {
	private static final long serialVersionUID = -5921608043683745782L;
	
	public InstructionUI() {
		super("text/html", "<html></html>");
		this.setEditable(false);
		this.setFocusable(false);
	}
	
	private String getText(Manager manager) {
		String text = "<html><center><h1>" + manager.getRoundName() + "</h1>" +
				"<p>Time Limit: " + Manager.MAX_TASK_TIME + " seconds</p>" +
				"<h2>Assignments</h2>" +
				"<table><tr><th>Task</th><th>Designers</th></tr>";
		for(int i = 0; i < Manager.NUM_TASKS; i++) {
			text += "<tr><td>" + manager.getTask(i).getName() + "</td><td>";
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
