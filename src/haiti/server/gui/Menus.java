/*
 * File: Menus.java
 * 
 * Copyright (C) 2011 The Humanitarian FOSS Project (http://hfoss.org)
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


import haiti.server.datamodel.LocaleManager;
import haiti.server.datamodel.User;

import haiti.server.datamodel.LocaleManager;
import haiti.server.gui.DataEntryGUI.DbSource;
import haiti.server.gui.DAO.MessageStatus;
import haiti.server.gui.DAO.MessageType;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import java.applet.Applet;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

/**
 *  Implements a menu system for DataEntryGUI.
 */

public class Menus implements ActionListener {
	
	public static final String MENU_FILE = "File";
	public static final String MENU_OPEN_FILE = "OpenFile";
	public static final String MENU_FILTER = "Filter";

	public static final String MENU_BENEFICIARY_FILTER = "BeneficiaryFilter";
	public static final String MENU_BENEFICIARY_FILTER_NEW = "NewBeneficiary";
	public static final String MENU_BENEFICIARY_FILTER_PENDING = "PendingBeneficiary";
	public static final String MENU_BENEFICIARY_FILTER_PROCESSED = "ProcessedBeneficiary";
	public static final String MENU_BENEFICIARY_FILTER_ARCHIVED = "ArchivedBeneficiary";
	public static final String MENU_BENEFICIARY_FILTER_ALL = "AllBeneficiaries";
	public static final String MENU_UPDATE_FILTER = "UpdateFilter";
	public static final String MENU_UPDATE_FILTER_NEW = "NewUpdate";
	public static final String MENU_UPDATE_FILTER_PENDING = "PendingUpdate";
	public static final String MENU_UPDATE_FILTER_PROCESSED = "ProcessedUpdate";
	public static final String MENU_UPDATE_FILTER_ARCHIVED = "ArchivedUpdate";
	public static final String MENU_UPDATE_FILTER_ALL = "AllUpdates";
	public static final String MENU_ALL = "All";
	public static final String MENU_QUIT = "Quit";
	public static final String MENU_ABOUT = "About";  // About DataEntryGUI...";
	public static final String MENU_LOCALE = "Locale";
	public static final String MENU_ENGLISH = "English";
	public static final String MENU_FRENCH = "French";
	public static final String MENU_HELP = "Help";
	public static final String MENU_ADMIN = "Admin";
	public static final String MENU_CREATE = "CreateNewUser";
	public static final String MENU_ARCHIVE_DB = "ArchiveDb";
	public static final String MENU_OUTPUT_ARCHIVE = "OutputArchive";
	public static final String MENU_MOBILE_DB = "MobileDb";

	
	public static Locale[] supportedLocales = {Locale.FRENCH, Locale.ENGLISH};	
	private static MenuBar mbar;	
	private static DataEntryGUI gui;
	
	public Menus(DataEntryGUI gui) {
		this.gui = gui;
	}

	/**
	 * Creates the Menu Bar for the main data entry form
	 */
	public void createMenuBar() {
		Menu fileMenu = new Menu(LocaleManager.resources.getString(MENU_FILE));
		Menu openDbMenu = new Menu(LocaleManager.resources.getString(MENU_FILTER));
		addMenuItem(fileMenu, LocaleManager.resources.getString(MENU_OPEN_FILE), KeyEvent.VK_N, false);
		fileMenu.add(openDbMenu);
		addMenuItem(fileMenu, LocaleManager.resources.getString(MENU_QUIT), KeyEvent.VK_Q, false);

		Menu localeMenu = new Menu(LocaleManager.resources.getString(MENU_LOCALE));
		addMenuItem(localeMenu, LocaleManager.resources.getString(MENU_ENGLISH), KeyEvent.VK_E, false);	
		addMenuItem(localeMenu, LocaleManager.resources.getString(MENU_FRENCH), KeyEvent.VK_F, false);	

		Menu beneficiaryMenu = new Menu(LocaleManager.resources.getString(MENU_BENEFICIARY_FILTER));
		Menu updateMenu = new Menu(LocaleManager.resources.getString(MENU_UPDATE_FILTER));

		openDbMenu.add(beneficiaryMenu);
		openDbMenu.add(updateMenu);
//		addMenuItem(openDbMenu, LocaleManager.resources.getString(MENU_ALL), KeyEvent.VK_A, false);
		
		addMenuItem(beneficiaryMenu, LocaleManager.resources.getString(MENU_BENEFICIARY_FILTER_NEW), 0, false);
		addMenuItem(updateMenu, LocaleManager.resources.getString(MENU_UPDATE_FILTER_NEW), 0, false);
		
		mbar = new MenuBar();
		mbar.add(fileMenu);
		mbar.add(localeMenu);

		if (gui.isAdmin) {
			addMenuItem(beneficiaryMenu, LocaleManager.resources.getString(MENU_BENEFICIARY_FILTER_PENDING), 0, false);
			addMenuItem(beneficiaryMenu, LocaleManager.resources.getString(MENU_BENEFICIARY_FILTER_PROCESSED), 0 , false);
			addMenuItem(beneficiaryMenu, LocaleManager.resources.getString(MENU_BENEFICIARY_FILTER_ARCHIVED), 0, false);
			addMenuItem(beneficiaryMenu, LocaleManager.resources.getString(MENU_BENEFICIARY_FILTER_ALL), 0, false);
			addMenuItem(updateMenu, LocaleManager.resources.getString(MENU_UPDATE_FILTER_PENDING), 0, false);
			addMenuItem(updateMenu, LocaleManager.resources.getString(MENU_UPDATE_FILTER_PROCESSED), 0, false);
			addMenuItem(updateMenu, LocaleManager.resources.getString(MENU_UPDATE_FILTER_ARCHIVED), 0, false);
			addMenuItem(updateMenu, LocaleManager.resources.getString(MENU_UPDATE_FILTER_ALL), 0, false);

			Menu adminMenu = new Menu(LocaleManager.resources.getString(MENU_ADMIN));
			addMenuItem(adminMenu, LocaleManager.resources.getString(MENU_CREATE), KeyEvent.VK_C, false);

			mbar.add(adminMenu);
		}
		
		Menu helpMenu = new Menu(LocaleManager.resources.getString(MENU_HELP));
		addMenuItem(helpMenu, LocaleManager.resources.getString(MENU_ABOUT), 0, false);

		mbar.setHelpMenu(helpMenu);
		gui.setMenuBar(mbar);
	}

	/**
	 * Utility method to create menu items that use the item's label (text) as its
	 * action command.
	 * @param menu the parent menu.
	 * @param label the menu item's label and action command.
	 * @param shortcut
	 * @param shift
	 */
	void addMenuItem(Menu menu, String label, int shortcut, boolean shift) {
		MenuItem item;
		if (shortcut==0)
			item = new MenuItem(label);
		else {
			if (shift) {
				item = new MenuItem(label, new MenuShortcut(shortcut, true));
			} else {
				item = new MenuItem(label, new MenuShortcut(shortcut));
			}
		}
		item.setActionCommand(label);
		menu.add(item);
		item.addActionListener(this);
	}

	/**
	 * Returns menu bar to the GUI.
	 * @return
	 */
	public static MenuBar getMenuBar() {
		return mbar;
	}

	/**
	 * Handles all Menu actions
	 */
	public void actionPerformed(ActionEvent e) {
		if ((e.getSource() instanceof MenuItem)) {

			String selectedMenuItemText = e.getActionCommand();
			System.out.println("Doing Menu Item:  " + selectedMenuItemText);

			if (selectedMenuItemText.equals(LocaleManager.resources.getString(MENU_ABOUT))) {
				gui.showAboutBox();
				createMenuBar();
				gui.setMenuBar(Menus.getMenuBar());	
				gui.repaint();				
			}
			else if (selectedMenuItemText.equals(LocaleManager.resources.getString(MENU_OPEN_FILE)))  {
				gui.readMessagesIntoGUI(DbSource.FILE);
			} 
			else if (selectedMenuItemText.equals(LocaleManager.resources.getString(MENU_BENEFICIARY_FILTER_NEW))) {
				gui.readMessagesIntoGUI(DbSource.DATA_BASE, MessageStatus.NEW, MessageType.REGISTRATION);
				createMenuBar();
				gui.setMenuBar(Menus.getMenuBar());	
				gui.repaint();
			} 
			else if (selectedMenuItemText.equals(LocaleManager.resources.getString(MENU_BENEFICIARY_FILTER_PENDING))) {
				gui.readMessagesIntoGUI(DbSource.DATA_BASE, MessageStatus.PENDING, MessageType.REGISTRATION);
				createMenuBar();
				gui.setMenuBar(Menus.getMenuBar());	
				gui.repaint();
			} 
			else if (selectedMenuItemText.equals(LocaleManager.resources.getString(MENU_BENEFICIARY_FILTER_PROCESSED))) {
				gui.readMessagesIntoGUI(DbSource.DATA_BASE, MessageStatus.PROCESSED, MessageType.REGISTRATION);
				createMenuBar();
				gui.setMenuBar(Menus.getMenuBar());	
				gui.repaint();
			} 
			else if (selectedMenuItemText.equals(LocaleManager.resources.getString(MENU_BENEFICIARY_FILTER_ARCHIVED))) {
				gui.readMessagesIntoGUI(DbSource.DATA_BASE, MessageStatus.ARCHIVED, MessageType.REGISTRATION);
				createMenuBar();
				gui.setMenuBar(Menus.getMenuBar());	
				gui.repaint();
			}  
			else if (selectedMenuItemText.equals(LocaleManager.resources.getString(MENU_BENEFICIARY_FILTER_ALL))) {
				gui.readMessagesIntoGUI(DbSource.DATA_BASE, MessageStatus.ALL, MessageType.REGISTRATION);
				createMenuBar();
				gui.setMenuBar(Menus.getMenuBar());	
				gui.repaint();
			}  
			else if (selectedMenuItemText.equals(LocaleManager.resources.getString(MENU_UPDATE_FILTER_NEW))) {
				gui.readMessagesIntoGUI(DbSource.DATA_BASE, MessageStatus.NEW, MessageType.UPDATE);
				createMenuBar();
				gui.setMenuBar(Menus.getMenuBar());	
				gui.repaint();
			} 
			else if (selectedMenuItemText.equals(LocaleManager.resources.getString(MENU_UPDATE_FILTER_PENDING))) {
				gui.readMessagesIntoGUI(DbSource.DATA_BASE, MessageStatus.PENDING, MessageType.UPDATE);
				createMenuBar();
				gui.setMenuBar(Menus.getMenuBar());	
				gui.repaint();
			} 
			else if (selectedMenuItemText.equals(LocaleManager.resources.getString(MENU_UPDATE_FILTER_PROCESSED))) {
				gui.readMessagesIntoGUI(DbSource.DATA_BASE, MessageStatus.PROCESSED, MessageType.UPDATE);
				createMenuBar();
				gui.setMenuBar(Menus.getMenuBar());	
				gui.repaint();
			} 
			else if (selectedMenuItemText.equals(LocaleManager.resources.getString(MENU_UPDATE_FILTER_ARCHIVED))) {
				gui.readMessagesIntoGUI(DbSource.DATA_BASE, MessageStatus.ARCHIVED, MessageType.UPDATE);
				createMenuBar();
				gui.setMenuBar(Menus.getMenuBar());	
				gui.repaint();
			}  
			else if (selectedMenuItemText.equals(LocaleManager.resources.getString(MENU_UPDATE_FILTER_ALL))) {
				gui.readMessagesIntoGUI(DbSource.DATA_BASE, MessageStatus.ALL, MessageType.UPDATE);
				createMenuBar();
				gui.setMenuBar(Menus.getMenuBar());	
				gui.repaint();
			}  
			else if (selectedMenuItemText.equals(LocaleManager.resources.getString(MENU_ALL))) {
				gui.readMessagesIntoGUI(DbSource.DATA_BASE, MessageStatus.ALL, MessageType.ALL);
				createMenuBar();
				gui.setMenuBar(Menus.getMenuBar());	
				gui.repaint();
			}
			else if (selectedMenuItemText.equals(LocaleManager.resources.getString(MENU_CREATE))) {
				User newUser = new User(gui);
			} 
			else if (selectedMenuItemText.equals(LocaleManager.resources.getString(MENU_ARCHIVE_DB))) {
			} 
			else if (selectedMenuItemText.equals(LocaleManager.resources.getString(MENU_QUIT))) {
				gui.quit();
			} 
			else if (selectedMenuItemText.equals(LocaleManager.resources.getString(MENU_ENGLISH))) {
				LocaleManager.currentLocale = Locale.ENGLISH;
				LocaleManager.resources = ResourceBundle.getBundle("MenusBundle", LocaleManager.currentLocale);
				System.out.println("Changing language to English "  + LocaleManager.currentLocale.toString());
				createMenuBar();
				gui.setMenuBar(Menus.getMenuBar());	 
				gui.repaint();
			}
			else if (selectedMenuItemText.equals(LocaleManager.resources.getString(MENU_FRENCH))) {
				LocaleManager.currentLocale = Locale.FRENCH;
				LocaleManager.resources = ResourceBundle.getBundle("MenusBundle", LocaleManager.currentLocale);
				System.out.println("Changing language to French " + LocaleManager.currentLocale.toString());
				createMenuBar();
				gui.setMenuBar(Menus.getMenuBar());	
				gui.repaint();
			}
		} 
	}
}
