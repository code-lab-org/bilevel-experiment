package edu.stevens.code.ptg.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.stevens.code.ptg.Task;

public class TaskPanel extends JPanel {
	private static final long serialVersionUID = -8972417442465106162L;

	private JTextField nameText;
	
	public TaskPanel() {
		this.setLayout(new GridBagLayout());
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		this.add(new JLabel("Name: "), c);
		c.gridx++;
		c.fill = GridBagConstraints.HORIZONTAL;
		nameText = new JTextField(20);
		nameText.setEnabled(false);
		this.add(nameText, c);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy++;
		this.add(new JLabel("Designers: "), c);
		c.gridx++;
	}

	public void bindTo(Task task) {
		nameText.setText(task.getName());
		nameText.setEnabled(true);
		nameText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				task.setName(nameText.getText());
			}
		});
	}
}
