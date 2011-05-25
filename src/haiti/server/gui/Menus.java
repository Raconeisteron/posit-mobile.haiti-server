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


import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.applet.Applet;

/**
 *  Implements a menu system for DataEntryGUI.
 */

public class Menus {

	private static final int STATIC_WINDOW_MENU_ITEMS = 2;

	private static MenuBar mbar;
	public static Menu windowMenu;
	
	private static DataEntryGUI gui;
	

	public Menus(DataEntryGUI gui) {
		this.gui = gui;
	}

	public void addMenuBar() {
		Menu fileMenu = new Menu("File");
//		addMenuItem(fileMenu, "New ...", KeyEvent.VK_N, false);
		addMenuItem(fileMenu, "Open File...", KeyEvent.VK_O, false);
		addMenuItem(fileMenu, "Open Db...", KeyEvent.VK_O, false);
		addMenuItem(fileMenu, "Close ...", KeyEvent.VK_W, false);
		fileMenu.addSeparator();
		addMenuItem(fileMenu, "Save", KeyEvent.VK_S, false);
		addMenuItem(fileMenu, "Save As", KeyEvent.VK_A, false);
		fileMenu.addSeparator();
//		addMenuItem(fileMenu, "Print", KeyEvent.VK_P, false);
//		fileMenu.addSeparator();
		addMenuItem(fileMenu, "Quit", KeyEvent.VK_Q, false);

//		Menu editMenu = new Menu("Edit");
//		addMenuItem(editMenu, "Cut", KeyEvent.VK_X, false);
//		addMenuItem(editMenu, "Copy", KeyEvent.VK_C, false);
//		addMenuItem(editMenu, "Paste", KeyEvent.VK_V, false);
//		addMenuItem(editMenu, "Select All", 0, false);    

		windowMenu = new Menu("Window");
		CheckboxMenuItem item = new CheckboxMenuItem("DataEntryGUI");
		windowMenu.add(item);
		item.setState(true);
		item.addActionListener(gui);    
		windowMenu.addSeparator();

		Menu helpMenu = new Menu("Help");
		addMenuItem(helpMenu, "About DataEntryGUI...", 0, false);

		mbar = new MenuBar();
		mbar.add(fileMenu);
//		mbar.add(editMenu);
		mbar.add(windowMenu);
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

//	public static void updateMenus() {
//		CheckboxMenuItem item = null;  
//		int nItems = windowMenu.getItemCount();  
//		DataEntryFrame cf = WindowManager.getActiveCipherFrame();
//
//		if (cf != null) {           // Mark one Frame active                              
//			item = (CheckboxMenuItem)windowMenu.getItem(0);
//			item.setState(false);            
//			for (int i= STATIC_WINDOW_MENU_ITEMS; i < nItems; i++) {
//				item = (CheckboxMenuItem)windowMenu.getItem(i) ;
//				item.setState( item.getLabel().indexOf(cf.getWindowId()) != -1);
//			}
//			return;
//		}
//		// Mark GUI active
//		item = (CheckboxMenuItem)windowMenu.getItem(0);
//		item.setState(true);
//		for (int i= STATIC_WINDOW_MENU_ITEMS; i < nItems; i++) {
//			item = (CheckboxMenuItem)windowMenu.getItem(i);
//			item.setState(false);
//		}
//		return;
//	}

	public static void extendWindowMenu(String name) {
		if (gui == null) return;
		CheckboxMenuItem item = new CheckboxMenuItem(name);
		item.addActionListener(gui);  
		item.setState(true);
		windowMenu.add(item);
		item = (CheckboxMenuItem)windowMenu.getItem(0);
		item.setState(false);
	}

	public static void trimWindowMenu(String id) {
		for (int k = STATIC_WINDOW_MENU_ITEMS; k < windowMenu.getItemCount(); k++) {
			CheckboxMenuItem item = (CheckboxMenuItem)windowMenu.getItem(k);
			if (item.getLabel().indexOf(id) != -1) {
				windowMenu.remove(item);
				return;
			}
		}
		return;    
	}

}// Menus
