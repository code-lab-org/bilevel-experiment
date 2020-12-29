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
package edu.stevens.code.bilevel.gui.manager;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.Gson;

import edu.stevens.code.bilevel.app.ManagerApp;
import edu.stevens.code.bilevel.model.Session;

/**
 * A menu bar for the manager application.
 * 
 * @author Paul T. Grogan <pgrogan@stevens.edu>
 */
public class ManagerAppMenuBar extends JMenuBar {
	private static final long serialVersionUID = -8166334008886803755L;
    private static final Logger logger = LogManager.getLogger(ManagerAppMenuBar.class);
	
	private final Gson gson = new Gson();

	/**
	 * Instantiates a new manager application menu bar.
	 *
	 * @param app the application
	 */
	public ManagerAppMenuBar(ManagerApp app) {
		JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
		fileChooser.setDialogTitle("Open Session");
		fileChooser.setFileFilter(new FileNameExtensionFilter("Session JSON files", "json"));
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic(KeyEvent.VK_F);
		JMenuItem openItem = new JMenuItem("Open");
		openItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(fileChooser.showOpenDialog(getRootPane()) == JFileChooser.APPROVE_OPTION) {
					File file = fileChooser.getSelectedFile();
					try {
						BufferedReader reader = new BufferedReader(new FileReader(file));
						Session session = gson.fromJson(reader, Session.class);
						app.setSession(session);
					} catch(FileNotFoundException ex) {
						logger.error(e);
					}
				}
			}
		});
		openItem.setMnemonic(KeyEvent.VK_O);
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		fileMenu.add(openItem);
		add(fileMenu);
	}
}
