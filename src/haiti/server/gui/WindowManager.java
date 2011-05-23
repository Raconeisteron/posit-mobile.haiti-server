/*
 * File: WindowManager.java
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



import java.awt.*;
import java.util.*;


/** 
 * <p>A collection of static methods used to manage CryptoTool windows. 
 * 
 *
 */
public class WindowManager {

    private static Vector windowList = new Vector();
    private static int id = 1001;
    private static DataEntryGUI gui;
	
    public static void init(DataEntryGUI gui) {
	gui = gui;
    }

    public static DataEntryGUI getCryptoTool() {
        return gui;
    }
    
    public static DataEntryFrame getActiveCipherFrame() {
	for (int k = 0; k < windowList.size(); k++) {
	    DataEntryFrame cf = (DataEntryFrame) windowList.elementAt(k);
	    if (cf.isActivated) {
		return cf;
	    }
	}
	return null;
    }	

    public static int getWindowCount() {
	return windowList.size();
    }

    public synchronized static void addWindow(DataEntryFrame win) {
	windowList.addElement(win);
	win.setWindowId(id);
	String idStr = "" + id;
	id++;
        Menus.extendWindowMenu("CipherFrame " + idStr);
        win.renameWithWindowId(idStr);
    }


    /** Closes the current window and removes it from the window list. */
    public synchronized static void removeWindow(DataEntryFrame win) {
	int index = windowList.indexOf(win);
	if (index == -1)
	    return;  // not on the window list
	Menus.trimWindowMenu(win.getWindowId());
	windowList.removeElementAt(index);
    }

    /** Closes all CipherFrame windows. Stops and returns false if any "save changes" dialog is canceled. */
    public synchronized static boolean closeAllWindows() {
	for (int k = 0; k < windowList.size(); k++) {
	    DataEntryFrame cf = (DataEntryFrame)windowList.elementAt(k);
	    if (!cf.close())
		return false;
	}
	return true;
    }

    /** Activates a window selected from the Window menu. */
    synchronized static void activateWindow(String menuItemLabel) {
	for (int i=0; i < windowList.size(); i++) {
	    DataEntryFrame win = (DataEntryFrame)windowList.elementAt(i);
	    String id = win.getWindowId();
	    if (menuItemLabel.indexOf(id) != -1) {
		win.activate();
		break;
	    }
	}
    }
        
}








