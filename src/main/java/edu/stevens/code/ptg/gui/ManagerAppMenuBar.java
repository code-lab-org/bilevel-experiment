package edu.stevens.code.ptg.gui;

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

import edu.stevens.code.ptg.ManagerApp;
import edu.stevens.code.ptg.Session;

public class ManagerAppMenuBar extends JMenuBar {
	private static final long serialVersionUID = -8166334008886803755L;
    private static final Logger logger = LogManager.getLogger(ManagerAppMenuBar.class);
	
	private final Gson gson = new Gson();

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
