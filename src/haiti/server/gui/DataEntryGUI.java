/*
 * File: DataEntryGUI.java
 * 
 * Copyright (C) 2011 The Humanitarian FOSS Project (http://hfoss.org).
 * 
 * This file is part of POSIT-Haiti Server.
 *
 * POSIT-Haiti Server is free software; you can redistribute it and/or modify
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
import haiti.server.datamodel.HaitiKeys;
import haiti.server.datamodel.LocaleManager;
import haiti.server.datamodel.Beneficiary.Abbreviated;

import java.io.*;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.awt.*;
import java.awt.event.*;   
import java.awt.datatransfer.*;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;


/** 
 * <p>Top-level window for the DataEntryGUI application. 
 *
 *  <P>This class implements the main window and menu system for the
 *  application.  
 */
public class DataEntryGUI extends JFrame implements WindowListener, ListSelectionListener   {

	public enum DbSource {FILE, DATA_BASE};

	
	private Menus mMenuManager;
	private LocaleManager  mLocaleManager;
	private SmsReader mReader;
	private JPanel mWelcomePanel ;
	private String windowId = "";
	
	private JSplitPane mSplitPane;
	private JList mMessageList;
	private DefaultListModel mListModel;
	private String[] mMessagesArray;
	private JScrollPane mListScrollPane = new JScrollPane(); // Where the messages go
    private JScrollPane mFormScrollPane = new JScrollPane();	// Where the data entry form goes
    
    private String mMessagesFileOrDbName;
    private Beneficiary mBeneficiary;
    
    private DataEntryForm mFormPanel;
    
	//public TextArea display;// = new TextArea();
	
	/**
	 * Creates a DataEntryGUI and sets up the user interface.
	 */
	public DataEntryGUI() { 
		super("DataEntryGUI");

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(this);
//		WindowManager.init(this);
		
		mLocaleManager = new LocaleManager();  // Set's the default locale

		mMenuManager = new Menus(this);
		mMenuManager.createMenuBar();
		setMenuBar(Menus.getMenuBar());	  
		
		this.setupFrame();
		this.setMinimumSize(new Dimension(500,300));

		setResizable(true);
		setSize(WIDTH, HEIGHT);
		setSize(900,700);
		pack();
		setVisible(true);
		DataEntryGUI.centerWindow(this);
		requestFocus();
	} 
	
	

	/**
	 *  Manages the detailed operations of setting up the user interface.
	 */ 
	private void setupFrame() {
		mWelcomePanel = new JPanel();
		MultiLineLabel welcome = 
			new MultiLineLabel(
					"DataEntryGUI " + "v0.1" + "\n" +
					" \n" +
					"Humanitarian FOSS Project\n" +
					"Trinity College, Hartford, CT, USA\n\n" +
					"DataEntryGUI is free software");
		
		mWelcomePanel.add(welcome);
		this.getContentPane().add(mWelcomePanel);
	}
 	
	/**
	 * Sets up a split pane with messages on top and the data entry form on the bottom.
	 * @param messages
	 * @param formPanel
	 * @return
	 */
	private JSplitPane setUpSplitPane (String messages[], JPanel formPanel) {
		// Set up the ListScrollPane
		mListModel = new DefaultListModel();
		for (int k = 0; k < messages.length; k++)
			mListModel.add(k, messages[k]);
		mMessageList = new JList(mListModel);
		//mMessageList = new JList(messages);
	    mMessageList.setCellRenderer(new CustomRenderer());
        mMessageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mMessageList.setSelectedIndex(0);
        mMessageList.addListSelectionListener(this);
        this.mListScrollPane = new JScrollPane(mMessageList);
        this.mFormScrollPane = new JScrollPane(formPanel);
		
        // See http://download.oracle.com/javase/tutorial/uiswing/components/splitpane.html
		//Create a split pane with the two scroll panes in it.
		mSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
		                           mListScrollPane, mFormScrollPane);
		mSplitPane.setOneTouchExpandable(true);
		mSplitPane.setDividerLocation(100);
		return mSplitPane;
	}
	
	/**
	 * Uses a FileDialog to pick a file and read its messages into the data
	 * entry form, choosing the first message in the file.
	 * TODO: Error checking that this is a messages file.
	 * @throws IOException 
	 */
	public void readMessagesIntoGUI (DbSource dbSource) {
		
		JFileChooser fd = new JFileChooser();
		int result = fd.showOpenDialog(this);
		if (result == JFileChooser.CANCEL_OPTION) 
			return;
		mMessagesFileOrDbName = fd.getSelectedFile().toString();
		
		System.out.println("messagefilename = " + mMessagesFileOrDbName);

		mReader = new SmsReader(mMessagesFileOrDbName);

		if (dbSource == DbSource.FILE) {
			mReader.readFile();
		} else {
			mReader = new SmsReader();
			mReader.readUnprocessedMsgsFromDb(mMessagesFileOrDbName);
		}
		mMessagesArray = mReader.getMessagesAsArray();
		
		mFormPanel = new DataEntryForm(this);
		mBeneficiary = new Beneficiary(mMessagesArray[0], Abbreviated.TRUE);
		mFormPanel.fillInForm(mBeneficiary,mReader);
		
		this.getContentPane().remove(mWelcomePanel);
		this.getContentPane().add(setUpSplitPane(mMessagesArray, mFormPanel));
		this.pack();
		DataEntryGUI.centerWindow(this);
		this.repaint();
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
	 * Listens to the messages list.
	 * TODO:  Figure out why it appears to be called twice on each click.
	 */
	public void valueChanged(ListSelectionEvent e) {
		JList list = (JList) e.getSource();
		System.out.println("Clicked on  list item " + list.getSelectedValue());
		mBeneficiary = new Beneficiary(list.getSelectedValue().toString(), Abbreviated.TRUE);
		mFormPanel.fillInForm(mBeneficiary, mReader);	
	}
	
	/**
	 *  Provides a controlled quit of the program for either
	 *   its applet or application versions.
	 */
	public void quit() {  
		setVisible(false);  
		dispose();
	}

	/**
	 *  Returns a reference to the programs Menus object.
	 */
	public Menus getMenus() {
		return mMenuManager;
	}


	/**
	 * Window Manager Interface
	 *  Responds to a window closing event.
	 */
	public void windowClosing(WindowEvent e) {  quit();  }
	public void windowActivated(WindowEvent e) { 	}
	public void windowClosed(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}

	/**
	 * Revises the message's status to "Processed" and updates the SMS Db and
	 * displays the revised status in the GUI's List.
	 */
	public void postMessageToTBS() {
		// TODO Auto-generated method stub
		System.out.println(mBeneficiary.toString());
		TbsManager tbs = new TbsManager();
		String result = tbs.postNewBeneficiary(mBeneficiary);
		
		if (result.equals(TbsManager.RESULT_OK)) {
			mBeneficiary.setStatus(Beneficiary.Status.PROCESSED); // sets the status of the current Beneficiary item to processed
			mReader.updateMessage(mBeneficiary, this.mMessagesFileOrDbName);
			int index = this.mMessageList.getSelectedIndex();
			String msg = mReader.getMessageById(mBeneficiary.getId(), this.mMessagesFileOrDbName);
			System.out.println(msg);
			if (!msg.contains("NOT FOUND")) {
				this.mListModel.set(index,msg);
				this.mMessageList.setSelectedIndex(index++);
				this.mMessageList.repaint();
				System.out.println("Posted message to TBS Db");
			} else {
				System.out.println("ERROR in Posting message to TBS Db");
			}
		} else {
			System.out.println("ERROR: " + result);
		}
	}


	/**
	 * Revises the message's status to "Pending", updates the SMS Db and
	 * displays the revised status in the GUI list.
	 */
	public void forwardMessageToDbMgr() {
		// TODO Auto-generated method stub
		System.out.println(mBeneficiary.toString());
		mBeneficiary.setStatus(Beneficiary.Status.PENDING); // sets the status of the current Beneficiary item to processed
		mReader.updateMessage(mBeneficiary, this.mMessagesFileOrDbName);
		int index = this.mMessageList.getSelectedIndex();
		String msg = mReader.getMessageById(mBeneficiary.getId(), this.mMessagesFileOrDbName);
		System.out.println(msg);
		if (!msg.contains("NOT FOUND")) {
			this.mListModel.set(index,msg);
			this.mMessageList.setSelectedIndex(index++);
			this.mMessageList.repaint();
			System.out.println("Posted message to TBS Db");
		} else {
			System.out.println("ERROR in Forwarding Message to Db Mgr");
		}
	}

	/**
	 * Utility method to center windows used by the application.
	 * @param win
	 */
	public static void centerWindow( Window win) {
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    Dimension winSize = win.getSize();
	    int x = (screenSize.width - winSize.width)/2;
	    int y = (screenSize.height - winSize.height)/4;
	    if (y < 0) 
		y = 0;
	    win.setLocation(x, y);    
	}
	

	/**
	 * Inner class to render list elements.
	 * @author rmorelli
	 *
	 */
	class CustomRenderer extends DefaultListCellRenderer {
		
		  public Component getListCellRendererComponent(JList list,
		                                                Object value,
		                                                int index,
		                                                boolean isSelected,
		                                                boolean hasFocus) {
		    JLabel label =
		      (JLabel)super.getListCellRendererComponent(list,
		                                                 value,
		                                                 index,
		                                                 isSelected,
		                                                 hasFocus);
		    String entry = (String)value;
		    if (entry.contains("status=2")) {
		    	label.setBackground(Color.RED);
		    } else if (entry.contains("status=1")) {
		    	label.setBackground(Color.YELLOW);
		    	label.setForeground(Color.RED);
		    }
		    return(label);
		  }
		}
	
	/**
	 *  Creates an instance of DataEntryGUI and when run in application mode.
	 */
	public static void main(String args[]) {  //main method
		DataEntryGUI gui = new DataEntryGUI(); 
	}  //end main()

	

}
