package edu.stevens.code.ptg.gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import edu.stevens.code.ptg.Manager;
import edu.stevens.code.ptg.Task;

public class TaskPanel extends JPanel {
	private static final long serialVersionUID = -8972417442465106162L;

	private JTextField nameText;
	private JComboBox<?>[] designerCombos = new JComboBox[Manager.NUM_DESIGNERS];
	
	public TaskPanel() {
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createTitledBorder("Task"));
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.LINE_END;
		this.add(new JLabel("Name: "), c);
		c.gridx++;
		c.anchor = GridBagConstraints.LINE_START;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		nameText = new JTextField(20);
		nameText.setEnabled(false);
		this.add(nameText, c);
		c.gridwidth = 1;
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.gridy++;
		c.anchor = GridBagConstraints.LINE_END;
		this.add(new JLabel("Designers: "), c);
		c.gridx++;
		c.anchor = GridBagConstraints.LINE_START;
		Integer[] ids = new Integer[Manager.NUM_DESIGNERS];
		for(int i = 0; i < Manager.NUM_DESIGNERS; i++) {
			ids[i] = i;
		}
		for(int i = 0; i < Task.NUM_DESIGNERS; i++) {
			JComboBox<Integer> designerCombo = new JComboBox<Integer>(ids);
			designerCombo.setEnabled(false);
			designerCombos[i] = designerCombo;
			this.add(designerCombo, c);
			c.gridx++;
		}
	}
	
	public void observe(Task task) {
		nameText.setText(task.getName());
		for(int i = 0; i < Task.NUM_DESIGNERS; i++) {
			designerCombos[i].setSelectedIndex(task.getDesignerId(i));
		}
		task.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				nameText.setText(task.getName());
				for(int i = 0; i < Task.NUM_DESIGNERS; i++) {
					designerCombos[i].setSelectedIndex(task.getDesignerId(i));
				}
			}
		});
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
		for(int i = 0; i < Task.NUM_DESIGNERS; i++) {
			final int designerIndex = i;
			designerCombos[i].setSelectedIndex(task.getDesignerId(i));
			designerCombos[i].setEnabled(true);
			designerCombos[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					task.setDesignerId(designerIndex, designerCombos[designerIndex].getSelectedIndex());
				}
			});
		}
	}
}
