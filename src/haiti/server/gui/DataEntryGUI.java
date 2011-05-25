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

import haiti.server.datamodel.Beneficiary;
import haiti.server.datamodel.Beneficiary.Abbreviated;

import java.io.*;
import java.awt.*;
import java.awt.event.*;   
import java.util.*;
import java.awt.datatransfer.*;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/** 
 * <p>Top-level window for the DataEntryGUI application. 
 *
 *  <P>This class implements the main window and menu system for the
 *  application.  
 */


public class DataEntryGUI extends JFrame implements ActionListener, 
	WindowListener, ClipboardOwner, ItemListener, ListSelectionListener   {

	private Menus menus;
	
	//private TextAreaPlus messagesField, outputField;
	private JTextField firstNameJText, lastNameJText, addressJText, ageJText;
	private JRadioButton maleJRadioButton, femaleJRadioButton;
	private JRadioButton infantMal,infantPrev,motherExp,motherNurs;
	private JButton button2, button3, button1;
//	private String[] communeNames
//	private String[] communeSectionNames= {"Names"};
	private JComboBox communeBox, communeSectionBox;
	private SmsReader reader;
	
	private JPanel welcomePanel, messagesPanel, buttonPanel, formPanel;
	private JPanel geninfoPanel, geninfoGrid, benPanel, mchnPanel;
	private String windowId = "";
	
	private JSplitPane splitPane;
	private JList messageList;
	private String[] messagesArray;
	private JScrollPane listScrollPane = new JScrollPane(); // Where the messages go
    private JScrollPane formScrollPane = new JScrollPane();	// Where the data entry form goes
    
    private String messageFileName;
    private Beneficiary beneficiary;
    
	public TextArea display;// = new TextArea();

	/**
	 * Creates a DataEntryGUI and sets up the user interface.
	 */
	public DataEntryGUI() { 
		super("DataEntryGUI");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(this);
		WindowManager.init(this);

		menus = new Menus(this);
		menus.addMenuBar();
		setMenuBar(Menus.getMenuBar());	    

		this.setupFrame();
		this.setMinimumSize(new Dimension(500,300));

		setResizable(true);
		setSize(WIDTH, HEIGHT);
		setSize(900,700);
		pack();
		setVisible(true);
		Tools.centerWindow(this);
		requestFocus();
	} 

	/**
	 *  Manages the detailed operations of setting up the user interface.
	 */ 
	private void setupFrame() {
		welcomePanel = new JPanel();
		welcomePanel.setBorder(BorderFactory.createTitledBorder("Data Entry"));
		JLabel label = new JLabel("Welcome");
		label.setSize(new Dimension(300,300));
		welcomePanel.add(label);
		this.getContentPane().add(welcomePanel);
	}

	/**
	 * Sets up the panel that used for Beneficiary registration.
	 * @return
	 */
	private JPanel setUpDataEntryPanel() {
		formPanel = new JPanel();
		formPanel.setBorder(BorderFactory.createTitledBorder("Data Entry Form"));
		formPanel.setLayout(new BorderLayout());
		
		geninfoPanel = new JPanel();
		geninfoPanel.setBorder(BorderFactory.createTitledBorder("General Information"));
		geninfoPanel.setLayout(new BoxLayout(geninfoPanel, BoxLayout.Y_AXIS));
		
		geninfoGrid = new JPanel();
		geninfoGrid.setLayout(new GridLayout(3,2));
		geninfoPanel.add(geninfoGrid);
		
		benPanel = new JPanel();
		benPanel.setBorder(BorderFactory.createTitledBorder("Beneficiary"));
		
		mchnPanel = new JPanel();
		mchnPanel.setBorder(BorderFactory.createTitledBorder("MCHN Information"));
		
		buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createTitledBorder("Controls"));
		
		firstNameJText = new JTextField();
		firstNameJText.setColumns(15);
		lastNameJText = new JTextField();
		lastNameJText.setColumns(15);
		
		communeBox = new JComboBox();
		communeSectionBox = new JComboBox();
		addressJText = new JTextField();
		addressJText.setColumns(15);
		ageJText = new JTextField();
		ageJText.setColumns(15);
		maleJRadioButton = new JRadioButton("MALE",false);
		femaleJRadioButton = new JRadioButton("FEMALE",false);

		infantMal = new JRadioButton("Enfant mal nourri",false);
		infantPrev = new JRadioButton("Enfant en prevention",false);
		motherExp = new JRadioButton("Femme enceinte",false);
		motherNurs = new JRadioButton("Femme allaitante",false);
		
		//creating General Information Panel
		geninfoGrid.add(new JLabel("First Name:"));
		geninfoGrid.add(firstNameJText);
		geninfoGrid.add(new JLabel("Last Name:"));
		geninfoGrid.add(lastNameJText);
		geninfoGrid.add(new JLabel("Commune:"));
		geninfoGrid.add(communeBox);
		geninfoGrid.add(new JLabel("Section:"));
		geninfoGrid.add(communeSectionBox);
		geninfoGrid.add(new JLabel("Address:"));
		geninfoGrid.add(addressJText);
		geninfoGrid.add(new JLabel("Age:"));
		geninfoGrid.add(ageJText);
		
		JPanel sexPanel = new JPanel();
		sexPanel.add(new JLabel("Sex:"));
		ButtonGroup sexGroup = new ButtonGroup();
		sexGroup.add(maleJRadioButton);
		sexGroup.add(femaleJRadioButton);
		sexPanel.add(maleJRadioButton);
		sexPanel.add(femaleJRadioButton);
		geninfoPanel.add(sexPanel);
		JPanel empty = new JPanel();
		
		geninfoPanel.add(benPanel);
		
		//benPanel.add(new JLabel("Beneficiary:"));
		ButtonGroup infantGroup = new ButtonGroup();
		infantGroup.add(infantMal);
		infantGroup.add(infantPrev);
		infantGroup.add(motherExp);
		infantGroup.add(motherNurs);
		JPanel infantPanel = new JPanel();
		infantPanel.add(infantMal);
		infantPanel.add(infantPrev);
		infantPanel.add(motherExp);
		infantPanel.add(motherNurs);
		benPanel.add(infantPanel);
		
		
		
		button1 = new JButton("Save");
		button2 = new JButton("Button2");
		button3 = new JButton("Button3");
		button1.addActionListener(this);
		button2.addActionListener(this);
		button3.addActionListener(this); 
		formPanel.add(geninfoPanel,"North");
		formPanel.add(mchnPanel,"Center");
		formPanel.add(buttonPanel,"South");
		buttonPanel.add(button1);
		buttonPanel.add(button2);
		buttonPanel.add(button3);

		return formPanel;
	}
	
	
	/** 
	 * Handles all menu events whenever the user selects a menu item.
	 * @param e the action that was performed
	 */
	public void actionPerformed(ActionEvent e) {
		if ((e.getSource() instanceof MenuItem)) {
			MenuItem item = (MenuItem)e.getSource();
			String cmd = e.getActionCommand();
			doCommand(cmd);
		} else if (e.getSource() instanceof JButton){
		    JButton button = (JButton)e.getSource();
		    if (button.getText().equals("Save")) {
				beneficiary.setStatus(1); // sets the status of the current Beneficiary item to processed
		    	reader.updateMessage(beneficiary);
		   	    System.out.println("Saving data");
		    }
		    if (button.getText().equals("Button2")) {
				beneficiary.setStatus(2); // sets the status of the Beneficiary item to pending
		    	reader.updateMessage(beneficiary);
		    	System.out.println("Sent to Eldivert");
		    }
		    if (button.getText().equals("Button3"))
		    	System.out.println("Button3 action performed");
		}
		
	}
	
//	/**
//	 *  Closes this window, first saving the text in the TextAreas if necessary.
//	 */  
//	public boolean close() {
//		if (!saveTextInTextAreas(false))
//			return false;
//		setVisible(false);
//		WindowManager.removeWindow(this);
//		dispose();
//		return true;    
//	}


	/** 
	 * Executes the command given by its parameter.
	 * @param a String that specifies the desired command.
	 * @throws  
	 */
	public void doCommand(String cmd) {   
		//    	showStatus("doCommand " + cmd);
		if (cmd.equals("About DataEntryGUI...")) 
			showAboutBox();
		else if (cmd.equals(Menus.menus.getString("OpenFile")))  {
			readMessagesIntoGUI("file");
		} 
		else if (cmd.equals(Menus.menus.getString("OpenDB")))  {
			readMessagesIntoGUI("db");
		} 		else if (cmd.equals("Close ...")) {   
			this.close();
		}
		else if (cmd.equals(Menus.menus.getString("Save"))) {
			this.save(false);
		}
		else if (cmd.equals(Menus.menus.getString("SaveAs"))) {
			this.save(false);

		}
//		else if (cmd.equals("Print"))
//			;
////			showStatus("PrintCipher... not yet implemented");
//		else if (cmd.equals("Cut")) {
//			//            showStatus("Cut");
//			TextManager.copyText(true);
//		}
//		else if (cmd.equals("Copy")) {
//			//            showStatus("Copy");
//			TextManager.copyText(false);
//		}
//		else if (cmd.equals("Paste")) {
//			//            showStatus("Paste");
//			TextManager.pasteText();
//		}
//		else if (cmd.equals("Select All")) {
//			//            showStatus("Select All");
//			TextManager.selectAll();
//		}
		else if (cmd.equals("DataEntryGUI")) {
			activate();
		}
		else if (cmd.equals(Menus.menus.getString("Quit")))
			this.quit();
	}   
	

/**
 *  Closes this window, first saving the text in the TextAreas if necessary.
 */  
public boolean close() {
	setVisible(false);
	dispose();
	return true;    
}
	/**
	 * Sets up a split pane with messages on top and the data entry form on the bottom.
	 * @param messages
	 * @param formPanel
	 * @return
	 */
	private JSplitPane setUpSplitPane (String messages[], JPanel formPanel) {
		// Set up the ListScrollPane
		messageList = new JList(messages);
        messageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        messageList.setSelectedIndex(0);
        messageList.addListSelectionListener(this);
        this.listScrollPane = new JScrollPane(messageList);

        this.formScrollPane = new JScrollPane(formPanel);

		//Provide minimum sizes for the two components in the split pane
		Dimension minimumSize = new Dimension(800, 100);
		//listScrollPane.setMinimumSize(minimumSize);
		//formScrollPane.setMinimumSize(minimumSize);
		
        // See http://download.oracle.com/javase/tutorial/uiswing/components/splitpane.html
		//Create a split pane with the two scroll panes in it.
		splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
		                           listScrollPane, formScrollPane);
		splitPane.setOneTouchExpandable(true);
		splitPane.setDividerLocation(100);
		return splitPane;
	}
	
	/**
	 * Uses a FileDialog to pick a file and read its messages into the data
	 * entry form, choosing the first message in the file.
	 * TODO: Error checking that this is a messages file.
	 * @throws IOException 
	 */
	private void readMessagesIntoGUI (String dborfile) {
		FileDialog fd = new FileDialog(this, "Open File", FileDialog.LOAD);
		fd.show();
		
		messageFileName = fd.getDirectory() + fd.getFile();
		reader = new SmsReader(messageFileName);

		if (dborfile.equals("file")) {
			reader.readFile();
		} else {
			reader = new SmsReader();
			reader.readMsgsFromDb(messageFileName);
		}
		messagesArray = reader.getMessagesAsArray();
		
		formPanel = setUpDataEntryPanel();
		beneficiary = new Beneficiary(messagesArray[0], Abbreviated.TRUE);
		fillInDataEntryForm(beneficiary);
		
		this.getContentPane().remove(welcomePanel);
		this.getContentPane().add(setUpSplitPane(messagesArray, formPanel));
		this.pack();
		this.repaint();
		
		//splitPane.setMinimumSize(minimumSize);		
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
	 *  Implementation of the Save and SaveAs commands.
	 */  
	public void save(boolean rename) {
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
	 * Listens to the messages list.
	 * TODO:  Figure out why it appears to be called twice on each click.
	 */
	public void valueChanged(ListSelectionEvent e) {
		JList list = (JList) e.getSource();
		System.out.println("Clicked on  list item " + list.getSelectedValue());
		beneficiary = new Beneficiary(list.getSelectedValue().toString(), Abbreviated.TRUE);
		fillInDataEntryForm(beneficiary);	
	}
	
	/**
	 * Fills in the Data Entry Form.
	 * @param beneficiary 
	 */
	private void fillInDataEntryForm(Beneficiary beneficiary) {
		this.firstNameJText.setText(beneficiary.getFirstName());
		this.lastNameJText.setText(beneficiary.getLastName());
		
		addItemsToComboBox(communeBox, reader.getCommune());
		this.communeBox.setSelectedItem(beneficiary.getCommune());
		addItemsToComboBox(communeSectionBox, reader.getCommuneSection(beneficiary.getCommune()));
	//	this.communeBox.setText(beneficiary.getCommune());
	//	this.communeSectionBox.setText(beneficiary.getCommuneSection());
	//	this.addressJText.setText(beneficiary.getAddress());
		this.ageJText.setText(Integer.toString(beneficiary.getAge()));
		if (beneficiary.getSex().equals(Beneficiary.Sex.MALE)) {
			this.maleJRadioButton.setSelected(true);
		} else {
			this.femaleJRadioButton.setSelected(true);
		}
	}
	
	private void addItemsToComboBox (JComboBox combo, String arr[]) {
		for (int k = 0; k < arr.length; k++) 
			combo.addItem(arr[k]);
	}
	
	/**
	 *  Provides a controlled quit of the program for either
	 *   its applet or application versions.
	 */
	private void quit() {  
		setVisible(false);  
		dispose();
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
//		//       showStatus("DataEntryGUI Window activated");
//		this.toFront();	
//		this.setMenuBar(menus.getMenuBar());
//		Menus.updateMenus();
	}

	/**
	 *  Responds to a window closing event.
	 */
	public void windowClosing(WindowEvent e) {
		quit();
	}
	public void windowActivated(WindowEvent e) { 
//		this.toFront();
//		this.setMenuBar(menus.getMenuBar());
//		Menus.updateMenus();
//		//	activate(); 
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

	
	public void itemStateChanged(ItemEvent arg0) {
		// TODO Auto-generated method stu
	}
	
	/**
	 *  Creates an instance of DataEntryGUI and when run in application mode.
	 */
	public static void main(String args[]) {  //main method
		DataEntryGUI gui = new DataEntryGUI(); 
	}  //end main()


}
