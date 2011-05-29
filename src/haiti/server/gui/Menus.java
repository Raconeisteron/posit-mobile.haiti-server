/*
 * File: Menus.java
 * 
 * Copyright (C) 2011 The Humanitarian FOSS Project (http://hfoss.org)
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


import haiti.server.datamodel.LocaleManager;

import haiti.server.datamodel.LocaleManager;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;
import java.applet.Applet;

/**
 *  Implements a menu system for DataEntryGUI.
 */

public class Menus {
	
	public static final String MENU_FILE = "File";
	public static final String MENU_OPEN_FILE = "OpenFile";
	public static final String MENU_OPEN_DB = "OpenDB";
	public static final String MENU_QUIT = "Quit";
	public static final String MENU_ABOUT = "About DataEntryGUI...";
	public static final String MENU_LOCALE = "Locale";
	public static final String MENU_ENGLISH = "English";
	public static final String MENU_FRENCH = "French";
	public static final String MENU_HELP = "Help";
	
	public static Locale[] supportedLocales = {Locale.FRENCH, Locale.ENGLISH};

	private static final int STATIC_WINDOW_MENU_ITEMS = 2;
	
	private static MenuBar mbar;	
	private static DataEntryGUI gui;
	
	public static ResourceBundle menus;
	public static Locale currentLocale;

	public Menus(DataEntryGUI gui) {
		this.gui = gui;
		currentLocale = Locale.FRENCH;
		menus =  ResourceBundle.getBundle("MenusBundle", currentLocale);
	}

	public void createMenuBar() {
		Menu fileMenu = new Menu(menus.getString(MENU_FILE));
		addMenuItem(fileMenu, menus.getString(MENU_OPEN_FILE), KeyEvent.VK_N, false);
		addMenuItem(fileMenu, menus.getString(MENU_OPEN_DB), KeyEvent.VK_O, false);
		addMenuItem(fileMenu, menus.getString(MENU_QUIT), KeyEvent.VK_Q, false);

		Menu localeMenu = new Menu(menus.getString(MENU_LOCALE));
		addMenuItem(localeMenu, menus.getString(MENU_ENGLISH), KeyEvent.VK_E, false);	
		addMenuItem(localeMenu, menus.getString(MENU_FRENCH), KeyEvent.VK_F, false);	

		Menu helpMenu = new Menu(menus.getString(MENU_HELP));
		addMenuItem(helpMenu, MENU_ABOUT, 0, false);

		mbar = new MenuBar();
		mbar.add(fileMenu);
		mbar.add(localeMenu);
		mbar.setHelpMenu(helpMenu);
		gui.setMenuBar(mbar);
	}

	void addMenuItem(Menu menu, String label, int shortcut, boolean shift) {
		MenuItem item;
		if (shortcut==0)
			item = new MenuItem(label);
		else {
			if (shift) {
				item = new MenuItem(label, new MenuShortcut(shortcut, true));
				//                 shortcuts.put(new Integer(shortcut+200),label);
			} else {
				item = new MenuItem(label, new MenuShortcut(shortcut));
				//                 shortcuts.put(new Integer(shortcut),label);
			}
		}
		item.setActionCommand(label);
		menu.add(item);
		item.addActionListener(gui);
	}

	public static MenuBar getMenuBar() {
		return mbar;
	}
}
