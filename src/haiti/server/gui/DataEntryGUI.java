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
import javax.swing.Box;
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
import javax.swing.SwingConstants;
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
	
	//Strings used throughout form
//	public static final String first_name="First Name: ",last_name="Last Name: ",commune="Commune: ",section="Section: ",address="Address: ",age="Age: ",sex="Sex: ",beneficiary="Beneficiary: ",house_people="              Number of persons in the house:";
	public static final String first_name="First Name: ",last_name="Last Name: ",commune="Commune: ",section="Section: ",address="Address: ",age="Age: ",sex="Sex: ",strBeneficiary="Beneficiary: ",house_people="              Number of persons in the house:";
	public static final String hc="Health Center",dp="Distribution post:",name_child="Responsible name (if child):",name_woman="Responsible name (if pregnant woman):",husband="Husband name (if woman):",father="Father's name (if child):",leader="Are you a mother leader?:",visit="Do you visit a mother leader?:";
	public static final String agri1="Is someone in your family in the",agri2="Agriculture Program of ACDI/VOCA?:",give_name="If yes, give the name:",yes="Yes",no="No";
	public static final String male="MALE",female="FEMALE",infantMal="Enfant mal nourri",infantPrev="Enfant en prevention",motherExp="Femme enceinte",motherNurs="Femme allaitante";
	public static final String data="Data Entry Form",gen="General Information",mchn="MCHN Information",controls="Controls";
	public static final String save="Save",quit="Quit",open_file="Open File",openfile="OpenFile",openDB="OpenDB",save_as="SaveAs",about="About DataEntryGUI...",close="Close ...";
	
	private TextAreaPlus messagesField, outputField;
	private JTextField firstNameJText, lastNameJText, addressJText, ageJText, peopleInHouseJText;
	private JTextField healthCenterJText, dpJText, guardianChildJText, guardianWomanJText, husbandJText, fatherJText, agriPersonJText; 
	private JRadioButton radioMale, radioFemale;
	private JRadioButton radioInfantMal,radioInfantPrev,radioMotherExp,radioMotherNurs;
	private JRadioButton radioMotherLeaderYes, radioMotherLeaderNo, radioVisitYes, radioVisitNo, radioAgriYes, radioAgriNo;
	private JButton button2, button3, button1;
	private ButtonGroup sexGroup, infantGroup, agriGroup, motherGroup, motherVisitGroup;
	private JComboBox communeBox, communeSectionBox;
	private SmsReader reader;
	
	private JPanel welcomePanel, messagesPanel, buttonPanel, formPanel, geninfoPanel, mchnPanel;
	private String windowId = "";
	
	private JSplitPane splitPane;
	private JList messageList;
	private String[] messagesArray;
	private JScrollPane listScrollPane = new JScrollPane(); // Where the messages go
    private JScrollPane formScrollPane = new JScrollPane();	// Where the data entry form goes
    
    private String messageFileName;
    private Beneficiary beneficiary;
    
	public TextArea display;// = new TextArea();

	public static final int DB_STATUS_NEW = 0;
	public static final int DB_STATUS_PENDING = 1;
	public static final int DB_STATUS_PROCESSED = 2;
	
	/**
	 * Creates a DataEntryGUI and sets up the user interface.
	 */
	public DataEntryGUI() { 
		super("DataEntryGUI");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(this);//Male
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
		
		//Form panel
		formPanel = new JPanel();
		formPanel.setBorder(BorderFactory.createTitledBorder(data));
		formPanel.setLayout(new BorderLayout());
		formPanel.setBackground(Color.WHITE); 
		
		//General Information Panel
		geninfoPanel = new JPanel();
		geninfoPanel.setBorder(BorderFactory.createTitledBorder(gen));
		geninfoPanel.setLayout(new GridBagLayout());
		geninfoPanel.setBackground(Color.WHITE); 
		GridBagConstraints c = new GridBagConstraints();

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
		c.gridy=0;
		c.gridx=0;
		c.insets=new Insets(10,5,4,2);
		c.anchor = c.NORTHEAST;
		geninfoPanel.add(new JLabel(first_name),c);
		c.gridy=0;
		c.gridx=1;
		geninfoPanel.add(firstNameJText,c);
		c.gridy=0;
		c.gridx=2;
		geninfoPanel.add(new JLabel("last_name"),c);
		c.gridy=0;
		c.gridx=3;
		geninfoPanel.add(lastNameJText,c);
		c.gridy=1;
		c.gridx=0;
		geninfoPanel.add(new JLabel(commune),c);
		c.gridy=1;
		c.gridx=1;
		geninfoPanel.add(communeBox,c);
		c.gridy=1;
		c.gridx=2;
		geninfoPanel.add(new JLabel(section),c);
		c.gridy=1;
		c.gridx=3;
		geninfoPanel.add(communeSectionBox,c);
		c.gridy=2;
		c.gridx=0;
		geninfoPanel.add(new JLabel(address),c);
		c.gridy=2;
		c.gridx=1;
		geninfoPanel.add(addressJText,c);
		c.gridy=2;
		c.gridx=2;
		geninfoPanel.add(new JLabel(age),c);
		c.gridy=2;
		c.gridx=3;
		geninfoPanel.add(ageJText,c);
		
		
		//Sex radio buttons
		c.gridy=3;
		c.gridx=0;
		geninfoPanel.add(new JLabel(sex),c);
		sexGroup = new ButtonGroup();
		radioMale = new JRadioButton(male,false);
		radioFemale = new JRadioButton(female,false);
		sexGroup.add(radioMale);
		sexGroup.add(radioFemale);
		c.gridy=3;
		c.gridx=1;
		geninfoPanel.add(radioMale,c);
		c.gridy=3;
		c.gridx=2;
		geninfoPanel.add(radioFemale,c);

		
		//Beneficiary radio buttons
		c.gridy = 4;
		c.gridx=0;
		geninfoPanel.add(new JLabel(strBeneficiary),c);
		infantGroup = new ButtonGroup();
		radioInfantMal = new JRadioButton(infantMal,false);
		radioInfantPrev = new JRadioButton(infantPrev,false);
		radioMotherExp = new JRadioButton(motherExp,false);
		radioMotherNurs = new JRadioButton(motherNurs,false);
		infantGroup.add(radioInfantMal);
		infantGroup.add(radioInfantPrev);
		infantGroup.add(radioMotherExp);
		infantGroup.add(radioMotherNurs);
		c.gridy=4;
		c.gridx=1;
		geninfoPanel.add(radioInfantMal,c);
		c.gridy=4;
		c.gridx=2;
		geninfoPanel.add(radioInfantPrev,c);
		c.gridy=4;
		c.gridx=3;
		geninfoPanel.add(radioMotherExp,c);
		c.gridy=4;
		c.gridx=4;
		geninfoPanel.add(radioMotherNurs,c);
	
		//People in house
		c.gridy = 5;
		c.gridx=0;
		peopleInHouseJText = new JTextField();
		peopleInHouseJText.setColumns(15);
		geninfoPanel.add(new JLabel(house_people),c);
		c.gridy=5;
		c.gridx=1;
		geninfoPanel.add(peopleInHouseJText,c);
		
		//MCHN panel
		mchnPanel = new JPanel();
		mchnPanel.setBorder(BorderFactory.createTitledBorder(mchn));
		mchnPanel.setLayout(new GridBagLayout());
		mchnPanel.setBackground(Color.WHITE); 

		c.gridy=0;
		c.gridx=0;
		healthCenterJText = new JTextField();
		healthCenterJText.setColumns(15);
		dpJText = new JTextField();
		dpJText.setColumns(15);
		guardianChildJText = new JTextField();
		guardianChildJText.setColumns(15);
		guardianWomanJText = new JTextField();
		guardianWomanJText.setColumns(15);
		husbandJText = new JTextField();
		husbandJText.setColumns(15);
		fatherJText = new JTextField();
		fatherJText.setColumns(15);
		mchnPanel.add(new JLabel(hc),c);
		c.gridy=0;
		c.gridx=1;
		mchnPanel.add(healthCenterJText,c);
		c.gridy=0;
		c.gridx=2;
		mchnPanel.add(new JLabel(dp),c);
		c.gridy=0;
		c.gridx=3;
		mchnPanel.add(dpJText,c);
		c.gridy=1;
		c.gridx=0;
		mchnPanel.add(new JLabel(name_child),c);
		c.gridy=1;
		c.gridx=1;
		mchnPanel.add(guardianChildJText,c);
		c.gridy=1;
		c.gridx=2;
		mchnPanel.add(new JLabel(name_woman),c);
		c.gridy=1;
		c.gridx=3;
		mchnPanel.add(guardianWomanJText,c);
		c.gridy=2;
		c.gridx=0;
		mchnPanel.add(new JLabel(husband),c);
		c.gridy=2;
		c.gridx=1;
		mchnPanel.add(husbandJText,c);
		c.gridy=2;
		c.gridx=2;
		mchnPanel.add(new JLabel(father),c);
		c.gridy=2;
		c.gridx=3;
		mchnPanel.add(fatherJText,c);

		motherGroup = new ButtonGroup();
		radioMotherLeaderYes = new JRadioButton(yes,false);
		radioMotherLeaderNo = new JRadioButton(no,false);
		motherGroup.add(radioMotherLeaderYes);
		motherGroup.add(radioMotherLeaderNo);   
		c.gridy=3;
		c.gridx=0;
		mchnPanel.add(new JLabel(leader),c);
		c.gridy=3;
		c.gridx=1;
		mchnPanel.add(radioMotherLeaderYes,c);
		c.gridy=3;
		c.gridx=2;
		mchnPanel.add(radioMotherLeaderNo,c);

		motherVisitGroup = new ButtonGroup();
		radioVisitYes = new JRadioButton(yes,false);
		radioVisitNo = new JRadioButton(no,false);
		motherVisitGroup.add(radioVisitYes);
		motherVisitGroup.add(radioVisitNo);
		c.gridy=4;
		c.gridx=0;
		mchnPanel.add(new JLabel(visit),c);
		c.gridy=4;
		c.gridx=1;
		mchnPanel.add(radioVisitYes,c);
		c.gridy=4;
		c.gridx=2;
		mchnPanel.add(radioVisitNo,c);

		agriGroup = new ButtonGroup();
		radioAgriYes = new JRadioButton(yes,false);
		radioAgriNo = new JRadioButton(no,false);
		agriGroup.add(radioAgriYes);
		agriGroup.add(radioAgriNo);
		c.gridy=5;
		c.gridx=0;
		c.insets=new Insets(10,5,0,2);
		mchnPanel.add(new JLabel(agri1),c);
		c.insets=new Insets(0,5,4,2);
		c.gridy=6;
		c.gridx=0;
		mchnPanel.add(new JLabel(agri2),c);
		c.insets=new Insets(0,5,4,2);
		c.gridy=6;
		c.gridx=1;
		mchnPanel.add(radioAgriYes,c);
		c.gridy=6;
		c.gridx=2;
		mchnPanel.add(radioAgriNo,c);
		
		agriPersonJText = new JTextField();
		agriPersonJText.setColumns(15);
		c.gridy=7;
		c.gridx=0;
		c.insets=new Insets(10,5,4,2);
		mchnPanel.add(new JLabel(give_name),c);
		c.gridy=7;
		c.gridx=1;
		mchnPanel.add(agriPersonJText,c);

		buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createTitledBorder(controls));
		buttonPanel.setBackground(Color.WHITE);
		button1 = new JButton(save);
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
				beneficiary.setStatus(DB_STATUS_PROCESSED); // sets the status of the current Beneficiary item to processed
		    	reader.updateMessage(beneficiary);
		   	    System.out.println("Saving data");
		    }
		    if (button.getText().equals("Button2")) {
				beneficiary.setStatus(DB_STATUS_PENDING); // sets the status of the Beneficiary item to pending
		    	reader.updateMessage(beneficiary);
		    	System.out.println("Sent to the database manager");
		    }
		    if (button.getText().equals("Button3"))
		    	System.out.println("DATABASE WIPED");
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
		if (cmd.equals(about)) 
			showAboutBox();
		else if (cmd.equals(Menus.menus.getString(openfile)))  {
			readMessagesIntoGUI("file");
		} 
		else if (cmd.equals(Menus.menus.getString(openDB)))  {
			readMessagesIntoGUI("db");
		} 		else if (cmd.equals(close)) {   
			this.close();
		}
		else if (cmd.equals(Menus.menus.getString(save))) {
			this.save(false);
		}
		else if (cmd.equals(Menus.menus.getString(save_as))) {
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
		else if (cmd.equals(Menus.menus.getString(quit)))
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
		FileDialog fd = new FileDialog(this, open_file, FileDialog.LOAD);
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
			this.radioMale.setSelected(true);
		} else {
			this.radioFemale.setSelected(true);
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
