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

import haiti.server.datamodel.AttributeManager;
import haiti.server.datamodel.AttributeManager.BeneficiaryCategory;
import haiti.server.datamodel.Beneficiary;
import haiti.server.datamodel.Bulk;
import haiti.server.datamodel.DAO;
import haiti.server.datamodel.TbsManager;
import haiti.server.datamodel.Update;
import haiti.server.datamodel.AttributeManager.Abbreviated;
import haiti.server.datamodel.LocaleManager;
import haiti.server.datamodel.AttributeManager.MessageStatus;
import haiti.server.datamodel.AttributeManager.MessageType;

import java.io.*;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.awt.*;
import java.awt.event.*;
import java.awt.datatransfer.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

/**
 * <p>
 * Top-level window for the DataEntryGUI application.
 * 
 * <P>
 * This class implements the main window and menu system for the application.
 */
public class DataEntryGUI extends JFrame implements WindowListener,
		ListSelectionListener {

	public enum DbSource {
		FILE, DATA_BASE
	};

	public boolean isUser = false;
	public boolean isAdmin = false;

	public Menus mMenuManager;
	private LocaleManager mLocaleManager;
	private DAO mReader;
	private JPanel mWelcomePanel;
	private JLabel mWelcomeLabel;
	private String windowId = "";

	private JSplitPane mSplitPane;
	private JList mMessageList;
	private DefaultListModel mListModel;
	private String[] mMessagesArray;
	private JScrollPane mListScrollPane = new JScrollPane(); // Where the
																// messages go
	private JScrollPane mFormScrollPane = new JScrollPane(); // Where the data
																// entry form
																// goes

	private String mMessagesFileOrDbName;
	private Beneficiary mBeneficiary;
	private Update mUpdate;
	private Bulk mBulk;

	private FormPanel mFormPanel;
	private JPanel mBulkPanel;

	// public TextArea display;// = new TextArea();

	/**
	 * Creates a DataEntryGUI and sets up the user interface.
	 */
	public DataEntryGUI() {
		super("DataEntryGUI");

		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addWindowListener(this);
		// WindowManager.init(this);

		mLocaleManager = new LocaleManager(); // Set's the default locale

		setupFrame();
		this.setMinimumSize(new Dimension(500, 300));
		this.setSize(500, 300);

		setResizable(true);
		pack();
		String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
		try {
			System.out.print(lookAndFeel);
			UIManager.setLookAndFeel(lookAndFeel);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedLookAndFeelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DataEntryGUI.centerWindow(this);
		requestFocus();
		this.setVisible(true);
		mMenuManager = new Menus(this);
		mMenuManager.createMenuBar();
		setMenuBar(Menus.getMenuBar());
	}

	public void setmMessagesFileOrDbName(String mMessagesFileOrDbName) {
		this.mMessagesFileOrDbName = mMessagesFileOrDbName;
	}

	public void messageRepaint() {
		this.mMessageList.repaint();
	}

	public void refreshLocale() {
		mWelcomeLabel
				.setText(LocaleManager.resources.getString("Instructions"));
	}

	/**
	 * Manages the detailed operations of setting up the user interface.
	 */
	private void setupFrame() {
		mWelcomePanel = new JPanel();
		mWelcomePanel.setLayout(new BoxLayout(mWelcomePanel,
				BoxLayout.PAGE_AXIS));
		MultiLineLabel frontPageLabel = new MultiLineLabel("DataEntryGUI "
				+ "v0.1" + "\n" + " \n" + "Humanitarian FOSS Project\n"
				+ "Trinity College, Hartford, CT, USA\n\n"
				+ "DataEntryGUI is free software");
		mWelcomeLabel = new JLabel(
				LocaleManager.resources.getString("Instructions"));
		mWelcomePanel.add(mWelcomeLabel);
		mWelcomePanel.add(frontPageLabel);

		this.getContentPane().add(mWelcomePanel);
	}

	/**
	 * Sets up a split pane with messages on top and the data entry form on the
	 * bottom.
	 * 
	 * @param messages
	 * @param formPanel
	 * @return
	 */
	private JSplitPane setUpSplitPane(String messages[], JPanel formPanel) {
		// Set up the ListScrollPane
		mListModel = new DefaultListModel();
		for (int k = 0; k < messages.length; k++)
			mListModel.add(k, messages[k]);
		mMessageList = new JList(mListModel);
		// mMessageList = new JList(messages);
		mMessageList.setCellRenderer(new CustomRenderer());
		mMessageList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		mMessageList.setSelectedIndex(0);
		mMessageList.addListSelectionListener(this);
		this.mListScrollPane = new JScrollPane(mMessageList,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		// this.mListScrollPane.setHorizontalScrollBar(new JScrollBar());
		// this.mListScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		this.mListScrollPane.setMaximumSize(new Dimension(700, 300));
		// this.mListScrollPane.setSize(300, 200);
		this.mFormScrollPane = new JScrollPane(formPanel);
		// this.mFormScrollPane.setSize(300, 200);

		// See
		// http://download.oracle.com/javase/tutorial/uiswing/components/splitpane.html
		// Create a split pane with the two scroll panes in it.
		mSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, mListScrollPane,
				mFormScrollPane);
		mSplitPane.setOneTouchExpandable(true);
		mSplitPane.setDividerLocation(100);
		// this.mSplitPane.setSize(700, 300);
		return mSplitPane;
	}

	/**
	 * Uses a FileDialog to pick a file and read its messages into the data
	 * entry form, choosing the first message in the file. TODO: Error checking
	 * that this is a messages file.
	 * 
	 * @throws IOException
	 */

	// deprecated
	public void readMessagesIntoGUI(DbSource dbSource) {

		// JFileChooser fd = new JFileChooser();
		// int result = fd.showOpenDialog(this);
		// if (result == JFileChooser.CANCEL_OPTION)
		// return;
		// mMessagesFileOrDbName = fd.getSelectedFile().toString();

		System.out.println("messagefilename = " + mMessagesFileOrDbName);

		if (mReader == null)
			mReader = new DAO(mMessagesFileOrDbName);

		if (dbSource == DbSource.FILE) {
			mReader.readFile();
		} else {
			mReader = new DAO();
			mReader.readUnprocessedMsgsFromDb(mMessagesFileOrDbName);
		}
		mMessagesArray = mReader.getMessagesAsArray();

		mFormPanel = new DataEntryFormStatic(this);
		// mBeneficiary = new Beneficiary(mMessagesArray[0], Abbreviated.TRUE);
		mBeneficiary = new Beneficiary(mMessagesArray[0]);
		mFormPanel.fillInForm(mBeneficiary, mReader);

		// mUpdatePanel = new BeneficiaryUpdateForm(this);
		// mBeneficiary = new Beneficiary(mMessagesArray[0], Abbreviated.TRUE);
		// mUpdatePanel.fillInForm(mBeneficiary,mReader);

		this.getContentPane().remove(mWelcomePanel);
		// this.getContentPane().add(setUpSplitPane(mMessagesArray,
		// mUpdatePanel));
		this.getContentPane().add(setUpSplitPane(mMessagesArray, mFormPanel));
		this.pack();
		DataEntryGUI.centerWindow(this);
		this.repaint();
	}

	public void createNewUser(String username, String password) {
		if (mReader == null)
			mReader = new DAO(mMessagesFileOrDbName);
		mReader.createNewUser(this, mMessagesFileOrDbName, username, password);
	}

	/**
	 * This method reads messages from the given database into the GUI depending
	 * on both status and type
	 * 
	 * @param dbSource
	 *            the database source
	 * @param status
	 *            of the message, including new, pending, processed, etc.
	 * @param type
	 *            of the message, registration or update
	 */
	public void readMessagesIntoGUI(DbSource dbSource, MessageStatus status,
			MessageType type) {

		// JFileChooser fd = new JFileChooser();
		// int result = fd.showOpenDialog(this);
		// if (result == JFileChooser.CANCEL_OPTION)
		// return;
		// mMessagesFileOrDbName = fd.getSelectedFile().toString();

		System.out.println("messagefilename = " + mMessagesFileOrDbName);

		mReader = new DAO(mMessagesFileOrDbName);

		if (dbSource == DbSource.FILE) {
			mReader.readFile();
		} else {
			mReader = new DAO();
			mMessagesArray = mReader.getMessageByStatusAndType(
					mMessagesFileOrDbName, status, type);
		}
		if (type == MessageType.REGISTRATION && status == MessageStatus.UPDATED) {
			mFormPanel = new BeneficiaryUpdateFormStatic(this);
			if (mMessagesArray.length == 0) {
				mMessagesArray = new String[1];
				mMessagesArray[0] = "{Empty}";
			} else {
				mUpdate = new Update(mMessagesArray[0]);
				// mBeneficiary = new Beneficiary(mMessagesArray[0],
				// Abbreviated.TRUE);
				mFormPanel.fillInForm(mUpdate, mReader);

				// mUpdatePanel = new BeneficiaryUpdateForm(this);
				// mBeneficiary = new Beneficiary(mMessagesArray[0],
				// Abbreviated.TRUE);
				// mUpdatePanel.fillInForm(mBeneficiary,mReader);
			}
			this.getContentPane().removeAll(); // WIN!!!!! Yours Truly, Alex and
												// Danny
			// this.getContentPane().remove(mWelcomePanel);
			// this.getContentPane().add(setUpSplitPane(mMessagesArray,
			// mUpdatePanel));
			this.getContentPane().add(
					setUpSplitPane(mMessagesArray, mFormPanel));
			this.pack();
			DataEntryGUI.centerWindow(this);
			this.repaint();
			this.setSize(800, 800);
			this.setLocation(0, 0);
		}
		if (type == MessageType.REGISTRATION && status == MessageStatus.NEW) {
			mFormPanel = new DataEntryFormStatic(this);
			if (mMessagesArray.length == 0) {
				mMessagesArray = new String[1];
				mMessagesArray[0] = "{Empty}";
			} else {
				mBeneficiary = new Beneficiary(mMessagesArray[0]);
				// mBeneficiary = new Beneficiary(mMessagesArray[0],
				// Abbreviated.TRUE);
				mFormPanel.fillInForm(mBeneficiary, mReader);

				// mUpdatePanel = new BeneficiaryUpdateForm(this);
				// mBeneficiary = new Beneficiary(mMessagesArray[0],
				// Abbreviated.TRUE);
				// mUpdatePanel.fillInForm(mBeneficiary,mReader);
			}
			this.getContentPane().removeAll(); // WIN!!!!! Yours Truly, Alex and
												// Danny
			// this.getContentPane().remove(mWelcomePanel);
			// this.getContentPane().add(setUpSplitPane(mMessagesArray,
			// mUpdatePanel));
			this.getContentPane().add(
					setUpSplitPane(mMessagesArray, mFormPanel));
			this.pack();
			DataEntryGUI.centerWindow(this);
			this.repaint();
			this.setSize(800, 800);
			this.setLocation(0, 0);
		} else if (type == MessageType.ATTENDANCE) {
//			try {
//				FileWriter fstream = new FileWriter("out.txt", true);
//				BufferedWriter out = new BufferedWriter(fstream);
//				out.newLine();
//				out.newLine();
//				out.write("-------" + new Date() + "-------");
//				out.newLine();
//				mBulkPanel = new JPanel();
//				mBulkPanel.setLayout(new GridLayout(0, 1));
//				mBulkPanel.add(new JLabel("Dossier Numbers of Absentees:"));
//				for (int i = 0; i < mMessagesArray.length; i++) {
//					mBulk = new Bulk(mMessagesArray[i]);
//					mBulkPanel.add(new JLabel(mBulk.getAvNum()));
//					out.write("Dossier: " + mBulk.getAvNum());
//					out.newLine();
//					mBulk.setStatus(AttributeManager.MessageStatus.PROCESSED);
//					mReader.updateBulk(mBulk, this.mMessagesFileOrDbName);
//				}
//				out.close();
				mReader.getAbsentees(mMessagesFileOrDbName);

//			} catch (Exception e) {// Catch exception if any
//				System.err.println("Error: " + e.getMessage());
//			}
			this.getContentPane().removeAll(); // WIN!!!!! Yours Truly, Alex and
												// Danny
			this.getContentPane().add(
					setUpSplitPane(mMessagesArray, mBulkPanel));
			this.pack();
			DataEntryGUI.centerWindow(this);
			this.repaint();
			this.setSize(800, 800);
		}
	}

	// this.mSplitPane.setSize(700,300);

	/**
	 * Displays the "About DataEntryGUI" blurb.
	 */
	void showAboutBox() {
		// display.append("About DataEntryGUI\n");
		JOptionPane.showMessageDialog(this, "         DataEntryGUI " + "v0.1"
				+ "\n" + " \n" + "Humanitarian FOSS Project\n"
				+ "Trinity College, Hartford, CT, USA\n" + " \n"
				+ "DataEntryGUI is free software.", "About...", 1);
	}

	public void chooseDb() {
		JFileChooser fd = new JFileChooser();
		int result = fd.showOpenDialog(this);
		if (result == JFileChooser.CANCEL_OPTION)
			return;
		String dbName = fd.getSelectedFile().toString();
		setmMessagesFileOrDbName(dbName);
		readMessagesIntoGUI(DbSource.DATA_BASE,
				AttributeManager.MessageStatus.NEW,
				AttributeManager.MessageType.REGISTRATION);
		repaint();
	}

	/**
	 * Listens to the messages list. TODO: Figure out why it appears to be
	 * called twice on each click.
	 */
	public void valueChanged(ListSelectionEvent e) {
		JList list = (JList) e.getSource();
		System.out.println("Clicked on  list item " + list.getSelectedValue());
		// Begin dumb code
		if (mFormPanel instanceof DataEntryFormStatic) {
			mBeneficiary = new Beneficiary(list.getSelectedValue().toString());
			mFormPanel.fillInForm(mBeneficiary, mReader); }
		else if (mFormPanel instanceof BeneficiaryUpdateFormStatic) {
			mUpdate = new Update(list.getSelectedValue().toString());
			mFormPanel.fillInForm(mUpdate, mReader); 
		}
		// mBeneficiary = new Beneficiary(list.getSelectedValue().toString(),
		// Abbreviated.TRUE);
		// mBeneficiary = new Beneficiary(list.getSelectedValue().toString(),
		// Abbreviated.TRUE);

		//mFormPanel.fillInForm(beneficiary, mReader);
		// mUpdatePanel.fillInForm(mBeneficiary, mReader);
	}

	/**
	 * Provides a controlled quit of the program for either its applet or
	 * application versions.
	 */
	public void quit() {
		setVisible(false);
		dispose();
	}

	/**
	 * Returns a reference to the programs Menus object.
	 */
	public Menus getMenus() {
		return mMenuManager;
	}

	/**
	 * Window Manager Interface Responds to a window closing event.
	 */
	public void windowClosing(WindowEvent e) {
		quit();
	}

	public void windowActivated(WindowEvent e) {
	}

	public void windowClosed(WindowEvent e) {
	}

	public void windowDeactivated(WindowEvent e) {
	}

	public void windowDeiconified(WindowEvent e) {
	}

	public void windowIconified(WindowEvent e) {
	}

	public void windowOpened(WindowEvent e) {
	}

	/**
	 * Revises the message's status to "Processed" and updates the SMS Db and
	 * displays the revised status in the GUI's List.
	 */
	public void postMessageToTBS(String dossier) {
		// TODO Auto-generated method stub
		System.out.println(mBeneficiary.toString());
		mBeneficiary.setDossier(dossier);
		TbsManager tbs = new TbsManager();
		String result = tbs.postNewBeneficiary(mBeneficiary);

		if (result.equals(TbsManager.RESULT_OK)) {
			mBeneficiary.setDossier(dossier);
			mBeneficiary.setStatus(AttributeManager.MessageStatus.PROCESSED);
			mReader.updateBeneficiary(mBeneficiary, this.mMessagesFileOrDbName);
			int index = this.mMessageList.getSelectedIndex();
			String msg = mReader.getMessageById(mBeneficiary.getId(),
					this.mMessagesFileOrDbName);
			// System.out.println(msg);
			if (!msg.contains("NOT FOUND")) {
				this.mListModel.set(index, msg);
				this.mMessageList.setSelectedIndex(index++);
				this.mMessageList.repaint();
				// System.out.println("Posted message to TBS Db");
				JOptionPane.showMessageDialog(this, "Posted message to TBS Db",
						"Success", -1);
			} else {
				System.out.println("ERROR in Posting message to TBS Db");
				JOptionPane.showMessageDialog(this,
						"ERROR in Posting message to TBS Db", "ERROR", 0);
			}
		} else {
			System.out.println("ERROR: " + result);
			JOptionPane.showMessageDialog(this, result, "ERROR", 0);
		}

	}

	/**
	 * Revises the message's status to "Pending", updates the SMS Db and
	 * displays the revised status in the GUI list.
	 */
	public void forwardMessageToDbMgr() {
		// TODO Auto-generated method stub
		System.out.println(mBeneficiary.toString());
		mBeneficiary.setStatus(AttributeManager.MessageStatus.PENDING);
		mReader.updateBeneficiary(mBeneficiary, this.mMessagesFileOrDbName);
		int index = this.mMessageList.getSelectedIndex();
		String msg = mReader.getMessageById(mBeneficiary.getId(),
				this.mMessagesFileOrDbName);
		System.out.println(msg);
		if (!msg.contains("NOT FOUND")) {
			this.mListModel.set(index, msg);
			this.mMessageList.setSelectedIndex(index++);
			this.mMessageList.repaint();
			System.out.println("Updated message in TBS Db");
			JOptionPane.showMessageDialog(this, "Updated message status in Db",
					"Success", -1);
		} else {
			System.out.println("ERROR in Forwarding Message to Db Mgr");
			JOptionPane.showMessageDialog(this,
					"ERROR in Forwarding message to Db Manager", "ERROR", 0);
		}
	}

	public void setUpdateStatus(String dossier) {
		// TODO Auto-generated method stub
		// System.out.println(mBeneficiary.toString());
		mUpdate.setDossier(dossier);

		mUpdate.setDossier(dossier);
		mUpdate.setStatus(AttributeManager.MessageStatus.PROCESSED);
		mReader.updateUpdate(mUpdate, this.mMessagesFileOrDbName);
		int index = this.mMessageList.getSelectedIndex();
		String msg = mReader.getMessageById(mUpdate.getId(),
				this.mMessagesFileOrDbName);
		// System.out.println(msg);
		if (!msg.contains("NOT FOUND")) {
			this.mListModel.set(index, msg);
			this.mMessageList.setSelectedIndex(index++);
			this.mMessageList.repaint();
			// System.out.println("Posted message to TBS Db");
			JOptionPane.showMessageDialog(this, "Message processed.",
					"Success", -1);
		} else {
			System.out.println("ERROR in changing status of message");
			JOptionPane.showMessageDialog(this,
					"Error in changing status of message.", "ERROR", 0);
		}

	}

	/**
	 * Utility method to center windows used by the application.
	 * 
	 * @param win
	 */
	public static void centerWindow(Window win) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension winSize = win.getSize();
		int x = (screenSize.width - winSize.width) / 2;
		int y = (screenSize.height - winSize.height) / 4;
		if (y < 0)
			y = 0;
		win.setLocation(x, y);
	}

	/**
	 * Inner class to render list elements.
	 * 
	 * @author rmorelli
	 * 
	 */
	class CustomRenderer extends DefaultListCellRenderer {

		public Component getListCellRendererComponent(JList list, Object value,
				int index, boolean isSelected, boolean hasFocus) {
			JLabel label = (JLabel) super.getListCellRendererComponent(list,
					value, index, isSelected, hasFocus);
			String entry = (String) value;
			System.out.println("entry " + entry);
			if (entry.contains("message_status="
					+ AttributeManager.MessageStatus.PENDING.getCode())) {

				label.setBackground(Color.YELLOW);
			} else if (entry.contains("message_status="
					+ AttributeManager.MessageStatus.PROCESSED.getCode())) {
				label.setBackground(Color.GREEN);

			}
			return (label);
		}
	}

	/**
	 * Creates an instance of DataEntryGUI and when run in application mode.
	 */
	public static void main(String args[]) { // main method
		AttributeManager.init();
		DataEntryGUI gui = new DataEntryGUI();
	} // end main()

}
