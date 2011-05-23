/*
 * File: DataEntryGUI.java
 * 
 * Copyright (C) 2009 R. Morelli
 * 
 * This file is part of DataEntryGUI.
 *
 * DataEntryGUI is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by 
 * the Free Software Foundation; either version 3.0 of the License, or (at
 * your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not visit http://www.gnu.org/licenses/gpl.html.
 */
package haiti.server.gui;



import java.io.*;
import java.awt.*;
import java.awt.event.*;   
import java.util.*;
import java.awt.datatransfer.*;


/** 
 * <p>Top-level window for the DataEntryGUI application. 
 *
 *  <P>This class implements the main window and menu system for the
 *  application.  When the user creates a new file or opens an existing
 *  file, DataEntryGUI opens a separate window, which is implemented by
 *  the <A href="DataEntryFrame.html">DataEntryFrame</A> class.
 *
 * <P>See also
 *  <BLOCKQUOTE>
 *     <BR><A href="DataEntryFrame.html">DataEntryFrame</A>
 *     <BR><A href="Menus.html">Menus</A>
 * </BLOCKQUOTE>
 */


public class DataEntryGUI extends Frame implements ActionListener, WindowListener,
ClipboardOwner
{

	private Menus menus;
	private DataEntryFrame cf;         // Current CipherFrame

	public TextArea display;// = new TextArea();

	/**
	 * Creates a DataEntryGUI. Set applet to null to run as an application.
	 *  This constructor method sets up the user interface and installs
	 *  the various built-in and plug-in ciphers.
	 * @param applet, a reference to an applet.
	 */
	public DataEntryGUI() { 
		super("DataEntryGUI");

		display = new TextArea();
		display.setEditable(false);
		add("Center", display);
		display.setText("Starting DataEntryGUI v0.1\n");
		addWindowListener(this);

		menus = new Menus(this);
		menus.addMenuBar();
		WindowManager.init(this);

		//        setResizable(true);
		Point loc = getPreferredLocation();
		//		Dimension tbSize = toolbar.getPreferredSize();
		setBounds(loc.x, loc.y, 400, 200);
		setVisible(true);
		requestFocus();
	} 


	/**
	 * Calculates a preferred location for the main application window
	 *  based on the screen size.
	 */
	public Point getPreferredLocation() {
		int screenWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
		int windowWidth = 500;//tbsize.width+10;
		double percent;
		if (screenWidth > 832)
			percent = 0.8;
		else
			percent = 0.9;
		int windowX = (int)(percent * (screenWidth - windowWidth));
		if (windowX < 10)
			windowX = 10;
		int windowY = 32;
		return new Point(windowX, windowY);
	}

	/** 
	 * Handles all menu events whenever the user selects a menu item.
	 */
	public void actionPerformed(ActionEvent e) {
		//        System.out.println("Action Event" + e.paramString());
		if ((e.getSource() instanceof MenuItem)) {
			MenuItem item = (MenuItem)e.getSource();
			String cmd = e.getActionCommand();
			doCommand(cmd);
		}
	}

	/** 
	 * Executes the command given by its parameter.
	 * @param a String that specifies the desired command.
	 */
	public void doCommand(String cmd) {   
		DataEntryFrame cf = (DataEntryFrame)WindowManager.getActiveCipherFrame();
		//    	showStatus("doCommand " + cmd);
		if (cmd.equals("About DataEntryGUI...")) 
			showAboutBox();
		if (cmd.equals("New ...")) {
			//            showStatus("New Cipher...");
			cf = new DataEntryFrame(this);
			WindowManager.addWindow(cf);
		}
		else if (cmd.equals("Open File..."))  {
			//            showStatus("Open File...");
			if (cf == null) {
				cf = new DataEntryFrame(this);
				WindowManager.addWindow(cf);
			}
//			String filename = cf.openFile();
//			SmsReader reader = new SmsReader(filename);
//			try {
//				reader.readFile();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			reader.getMessages().toArray();
//			
		} 
		else if (cmd.equals("Close ...")) {   
			//            showStatus("Close Cipher...");
			if (cf != null) {
				cf.close();
			}
		}
		else if (cmd.equals("Save")) {
			//            showStatus("Save Cipher...");
			if (cf != null) 
				cf.save(false);
		}
		else if (cmd.equals("Save As")) {
			//            showStatus("Save As Cipher...");
			if (cf != null) 
				cf.save(true);
		}
		else if (cmd.equals("Print"))
			showStatus("PrintCipher... not yet implemented");
		else if (cmd.equals("Cut")) {
			//            showStatus("Cut");
			TextManager.copyText(true);
		}
		else if (cmd.equals("Copy")) {
			//            showStatus("Copy");
			TextManager.copyText(false);
		}
		else if (cmd.equals("Paste")) {
			//            showStatus("Paste");
			TextManager.pasteText();
		}
		else if (cmd.equals("Select All")) {
			//            showStatus("Select All");
			TextManager.selectAll();
		}
		else if (cmd.equals("DataEntryGUI")) {
			activate();
		}
		else if (cmd.indexOf("CipherFrame") != -1) {
			WindowManager.activateWindow(cmd);
		} 
		else if (cmd.equals("Quit"))
			this.quit();
	}    

	/**
	 *  Displays the "About DataEntryGUI" blurb.
	 */
	void showAboutBox() {   
		//	    display.append("About DataEntryGUI\n");
		MessageDialog d = new MessageDialog(this, "About...", 
				"         DataEntryGUI " + "v0.1" + "\n" +
				" \n" +
				"Humanitarian FOSS Project\n" +
				"Trinity College, Hartford, CT, USA\n" +
				" \n" +
		"DataEntryGUI is free software.");
	}

	/**
	 *  Provides a controlled quit of the program for either
	 *   its applet or application versions.
	 */
	private void quit() {  
		display.append("Quitting\n"); 
		if (!WindowManager.closeAllWindows()) {
			display.append("Canceled\n"); 
			return;            // If any FileSave dialog is canceled, don't quit
		}

		display.append("Setting invisible\n"); 
		setVisible(false);  
	}

	/**
	 *  Returns a reference to the programs Menus object.
	 */
	public Menus getMenus() {
		return menus;
	}

	/**
	 *  Brings the top-level DataEntryGUI frame to the front.
	 */
	private void activate() {
		//       showStatus("DataEntryGUI Window activated");
		this.toFront();	
		this.setMenuBar(menus.getMenuBar());
		Menus.updateMenus();
	}

	/**
	 *  Responds to a window closing event.
	 */
	public void windowClosing(WindowEvent e) {
		quit();
	}
	public void windowActivated(WindowEvent e) { 
		this.toFront();
		this.setMenuBar(menus.getMenuBar());
		Menus.updateMenus();
		//	activate(); 
	}
	public void windowClosed(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}

	/**
	 *  Displays a message on the DataEntryGUI's display.
	 */
	public void showStatus(String msg) {
		display.append(msg + "\n");
	}

	public void lostOwnership (Clipboard clip, Transferable cont) {}


	/**
	 *  Creates an instance of DataEntryGUI and when run in application mode.
	 */
	public static void main(String args[]) {  //main method
		DataEntryGUI gui = new DataEntryGUI(); 
	}  //end main()
}
