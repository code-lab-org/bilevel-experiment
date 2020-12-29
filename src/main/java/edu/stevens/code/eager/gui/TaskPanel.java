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
package edu.stevens.code.eager.gui;

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

import edu.stevens.code.eager.model.Manager;
import edu.stevens.code.eager.model.Task;

/**
 * A panel that displays a task for a manager application.
 * 
 * @author Paul T. Grogan <pgrogan@stevens.edu>
 * @author Ambrosio Valencia-Romero <avalenci@stevens.edu>
 */
public class TaskPanel extends JPanel {
	private static final long serialVersionUID = -8972417442465106162L;

	private JTextField nameText;
	private JComboBox<?>[] designerCombos = new JComboBox[Manager.NUM_DESIGNERS];
	
	/**
	 * Instantiates a new task panel.
	 */
	public TaskPanel() {
		this.setLayout(new GridBagLayout());
		this.setBorder(BorderFactory.createTitledBorder("Task"));
		
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.weighty = 1;
		c.anchor = GridBagConstraints.LINE_END;
		this.add(new JLabel("Name: "), c);
		c.gridx++;
		c.anchor = GridBagConstraints.LINE_START;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.weightx = 1;
		nameText = new JTextField(20);
		nameText.setEnabled(false);
		this.add(nameText, c);
		c.gridwidth = 1;
		c.weightx = 0;
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
	
	/**
	 * Observe a manager for task changes.
	 *
	 * @param manager the manager
	 * @param taskIndex the task index
	 */
	public void observe(Manager manager, int taskIndex) {
		nameText.setText(manager.getTask(taskIndex).getName());
		for(int i = 0; i < Task.NUM_DESIGNERS; i++) {
			designerCombos[i].setSelectedIndex(manager.getTask(taskIndex).getDesignerId(i));
		}
		manager.addObserver(new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				nameText.setText(manager.getTask(taskIndex).getName());
				for(int i = 0; i < Task.NUM_DESIGNERS; i++) {
					designerCombos[i].setSelectedIndex(manager.getTask(taskIndex).getDesignerId(i));
				}
			}
		});
	}

	/**
	 * Bind to a manager to relay user inputs.
	 *
	 * @param manager the manager
	 * @param taskIndex the task index
	 */
	public void bindTo(Manager manager, int taskIndex) {
		nameText.setText(manager.getTask(taskIndex).getName());
		nameText.setEnabled(true);
		nameText.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				manager.setTask(taskIndex, new Task(
						nameText.getText(), 
						manager.getTask(taskIndex).getDesignerIds()
				));
			}
		});
		for(int i = 0; i < Task.NUM_DESIGNERS; i++) {
			final int designerIndex = i;
			designerCombos[i].setSelectedIndex(manager.getTask(taskIndex).getDesignerId(i));
			designerCombos[i].setEnabled(true);
			designerCombos[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					int[] designerIds = new int[Task.NUM_DESIGNERS];
					designerIds[designerIndex] = designerCombos[designerIndex].getSelectedIndex();
					manager.setTask(taskIndex, new Task(
							manager.getTask(taskIndex).getName(),
							designerIds
					));
				}
			});
		}
	}
}
