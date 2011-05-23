/*
 * File: DataEntryFrame.java
 * 
 * Copyright (C) 2009 R. Morelli
 * 
 * This file is part of CryptoToolJ.
 *
 * CryptoToolJ is free software; you can redistribute it and/or modify
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
 * <P>Description: Implements a Java 1.1 version of a GUI tool for using and analyzing
 *  historical ciphers.
 * 
 */

package haiti.server.gui;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/**
 * <p>Provides a user interface for performing encryption and 
 * decryption operations on a particular message. It supports file I/O
 * (for the application version). 
 * 
 */

public class DataEntryFrame extends Frame implements ActionListener, ItemListener,
WindowListener, ClipboardOwner, ListSelectionListener
{
	public static final int WIDTH = 500;
	public static final int HEIGHT = 500;

	private DataEntryGUI dataEntryGUI;     //reference to the dataEntryGUI

	private TextAreaPlus messagesField, outputField;
	private JTextField encodingField;
	private JButton button2, button3, button1;
	private JPanel messagesPanel, buttonPanel, formPanel;
	private Choice selectionBox; //drop down menu for something?
	private String windowId = "";
	public boolean isActivated = false;
	private JSplitPane splitPane;
	private Component formScrollPane;
	private Component listScrollPane;
	private JList list;
	private String[] listMessages = {"one", "two"};
	 

	/**
	 * This constructor creates a frame with a menubar and text
	 *  areas for plaintext and ciphertext. The menubar includes
	 *  a drop down list of the available cipher engines.
	 */ 
	public DataEntryFrame(DataEntryGUI gui)  {  //constructor
		super("Haiti Server");
		dataEntryGUI = gui;

		this.setupFrame();
		addWindowListener(this);

		setMenuBar(Menus.getMenuBar());	    
		setResizable(true);
		setSize(WIDTH, HEIGHT);
		pack();
		setVisible(true);
		Tools.centerWindow(this);
		requestFocus();
	}  //end constructor()

	/**
	 * Renames the window with descriptive title that refers
	 *  to the type of cipher engine being used.
	 */ 
	public void renameWithWindowId(String id) {
		String s = getTitle();
		int index = s.indexOf(":");
//		setTitle(s.substring(0, index) + id + s.substring(index));
	}

	/**
	 *  Manages the detailed operations of setting up the user interface,
	 *  including setting up a ChoiceBox containing the names of both
	 *  the  built-in and plugin ciphers.
	 */ 
	private void setupFrame() {
			
		FileDialog fd = new FileDialog(this, "Open File", FileDialog.LOAD);
		fd.show();
		String filename = fd.getDirectory() + fd.getFile();
		SmsReader reader = new SmsReader(filename);
//		SmsReader reader = new SmsReader("../sample_data/sms_log.txt");
		try {
			reader.readFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		listMessages = reader.getMessagesAsArray();
		
        list = new JList(listMessages);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        
        JScrollPane listScrollPane = new JScrollPane(list);
        JScrollPane formScrollPane = new JScrollPane(list);
		// See http://download.oracle.com/javase/tutorial/uiswing/components/splitpane.html
		//Create a split pane with the two scroll panes in it.
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
		                           listScrollPane, formScrollPane);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(150);

		//Provide minimum sizes for the two components in the split pane
		Dimension minimumSize = new Dimension(100, 50);
		listScrollPane.setMinimumSize(minimumSize);
		formScrollPane.setMinimumSize(minimumSize);
		selectionBox = new Choice();
		selectionBox.select("Substitution (Default)");
		selectionBox.addItemListener(this);    

		setSelection();
		
		TitledBorder bI = BorderFactory.createTitledBorder("Messages"); 
		messagesPanel = new JPanel();
		formPanel = new JPanel();
		messagesField = new TextAreaPlus(this, "Use open file to view messages", bI);
		messagesPanel.add(messagesField);
		messagesPanel.setBorder(bI);
		
		messagesField.setEditable(true);
//		outputField = new TextAreaPlus(this, "Output Text Area", bO);
//		outputField.setEditable(true);
//		outputPanel.add(outputField);
//		outputPanel.setBorder(bO);
		
		buttonPanel = new JPanel();
		button1 = new JButton("Button1");
		button2 = new JButton("Button2");
		button3 = new JButton("Button3");
		encodingField = new JTextField("default");
		button2.setEnabled(false);
		button3.setEnabled(false);
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this);  
		buttonPanel.add(selectionBox);     
		buttonPanel.add(button1);
		buttonPanel.add(button2);
		buttonPanel.add(button3);
//		buttonPanel.add(new Label("Encoding:"));
//		buttonPanel.add(encodingField);
		
		add(buttonPanel, "North");
		add(splitPane,"Center");
//		add(messagesPanel, "Center");
//		add(formPanel, "South");
	}

	/**
	 * Creates an instance of the cipher selected by the user. 
	 */ 
	private void setSelection() {   
	}

//	/**
//	 * Opens a file and reads its text into the InputField
//	 */  
//	public void openFile() {
//	   FileDialog fd = new FileDialog(this, "Open File", FileDialog.LOAD);
//	        //fd.show();
//	        String fileName = fd.getFile();
//	        String dirName = fd.getDirectory();
//	        
//
//		messagesField.openFile(encodingField.getText());
//		messagesField.markFileSaved();
//	}
	
	/**
	 * Opens a file and reads its text into the InputField
	 */  
	public String openFile() {
	   FileDialog fd = new FileDialog(this, "Open File", FileDialog.LOAD);
	   return fd.getFile();
	}

	/**
	 * Saves the text is response to Save or SaveAs commands.
	 */  
	public boolean saveTextInTextAreas(boolean rename) {
		int result = 0;
		if (messagesField.isDirty()) {
			result = messagesField.saveToFile();
			if (result == FileSaveMessageDialog.CANCEL_OPTION)  // Cancel stops close
				return false;
			else if (result == FileSaveMessageDialog.SAVE_OPTION)
				messagesField.save(encodingField.getText(), messagesField.getFile() == null);    // Save or don't
		}
		return true;        
	}

	/**
	 * Handles user's choices in the ChoiceBox.
	 */  
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == selectionBox) {
			setSelection();
			String s = getTitle();
			int index = s.indexOf(":");
			button2.setEnabled(false);
			button3.setEnabled(false);
			button1.setEnabled(true);        
		}
	}


	/**
	 * Handles all menu selections for the buttons.
	 */  
	public void actionPerformed(ActionEvent e) {
//		KeyDialog keyDialog = null;
//		if (e.getSource() == encodeButton) {
//			try {
//				cipher.init(key);
//				outputField.setText(cipher.encrypt(inputField.getText()) );  //Encrypt
//			}
//			catch (Exception ee) {
//				System.out.println("Exception: " + ee.getMessage());
//				ee.printStackTrace();
//			} // END: try
//		} else if (e.getSource() == decodeButton) {
//			try {
//				cipher.init(key);
//				outputField.setText(cipher.decrypt(inputField.getText()) );  //Decrypt
//			} 
//			catch (Exception ee) {
//				System.out.println("Exception: " + ee.getMessage());
//				ee.printStackTrace();
//			}  // END: try                
//		} else if (e.getSource() == keyButton) {
//			if (key == null) {
//				try {  
//					keywordString  = (String) Class.forName(keyClassName).getDeclaredField("DEFAULT_KEYWORD_STRING").get(null);
//					keyPromptString = (String) Class.forName(keyClassName).getDeclaredField("DEFAULT_KEY_DESCRIPTOR_PROMPT_STRING").get(null);
//				} catch(Exception ee) { //specific catches can be implemented
//					System.out.println(ee.getMessage());
//					ee.printStackTrace();
//				}   
//			}
//			//           System.out.println("Opening Key Dialog");
//			if (keyDialog == null) 
//				keyDialog = new KeyDialog(this, cipher.getAlphabetRangeOptions(), 
//						"Enter " + cipher.getAlgorithm() + " Key and Choose Alphabet Parameters");
//			keyDialog.setKeyText(keywordString);
//			int dialogOption = keyDialog.showKeyDialog();	        
//
//			//	   cryptoTool.showStatus("Returned from key dialog " + dialogOption);
//			//	   requestFocus();
//			//	   cryptoTool.showStatus("Requested focus");
//
//			if (dialogOption == KeyDialog.APPROVE_OPTION) {
//				//	       cryptoTool.showStatus("Approved Key");
//				try {  
//					key = HistoricalKey.getInstance(cipher.getAlgorithm(), cipher.getProvider());
//					key.init(keyDialog.getKeyAreaText() + "/" + keyDialog.getSelectedAlphabetChoice());
//					if (key != null) {
//						encodeButton.setEnabled(true);
//						decodeButton.setEnabled(true);
//					} 
//					//		 cryptoTool.showStatus("Done setting buttons");
//				} catch(Exception ex) {
//					ex.printStackTrace();
//				}
//			} //end if APPROVE_OPTION          
//		}
	} //actionPerformed()    


	/**
	 * Returns a reference to the TextArea that currently has focus.
	 */  
	public TextAreaPlus getTextAreaWithFocus() {
		if (messagesField.isInFocus()) 
			return messagesField;
		else if (outputField.isInFocus()) 
			return outputField;
		else 
			return null;
	}

	/**
	 *  Closes this window, first saving the text in the TextAreas if necessary.
	 */  
	public boolean close() {
		if (!saveTextInTextAreas(false))
			return false;
		setVisible(false);
		WindowManager.removeWindow(this);
		dispose();
		return true;    
	}

	/**
	 *  Implementation of the Save and SaveAs commands.
	 */  
	public void save(boolean rename) {
		TextAreaPlus tap = getTextAreaWithFocus();
		if (tap == null)
			return;
		if (rename || tap.isDirty())
			//            tap.save(rename);
			tap.save(encodingField.getText(), rename);
	}

	/**
	 *  Sets this window's id number.
	 */  
	public void setWindowId( int n ) {
		windowId = "" + n;
	}
	/**
	 *  Gets this window's id number.
	 */  
	public String getWindowId() { return windowId; }

	/**
	 *  Causes this window to be the front window.
	 */  
	public void activate() {
		isActivated = true;
		this.toFront();
		dataEntryGUI.getMenus();
		this.setMenuBar(Menus.getMenuBar());
		Menus.updateMenus();
	}

	public void windowActivated(WindowEvent e) {
		isActivated = true;
		this.toFront();
		dataEntryGUI.getMenus();
		this.setMenuBar(Menus.getMenuBar());
		Menus.updateMenus();
	}

	public void windowDeactivated(WindowEvent e) {
		isActivated = false;
	}

	/**
	 *  Closes this window and is subsidiary windows, if any.
	 */  
	public void windowClosing(WindowEvent e) {
		dataEntryGUI.doCommand("Close ...");
	}

	// Interface methods
	public void windowClosed(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}    

	public void lostOwnership (Clipboard clip, Transferable cont) {}

	public void valueChanged(ListSelectionEvent arg0) {
		// TODO Auto-generated method stub
		System.out.println("Clicked on  list item " + arg0.getFirstIndex());
	}
} // HaitiServerFrame
