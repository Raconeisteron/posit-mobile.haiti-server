/*
 * File: Tools.java
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

 * Credits: CryptoToolJ is modeled after the ImageJ program which is written by
 *  Wayne Rasband of the National Institutes of Health. ImageJ is in the public domain.
 *
 */

package haiti.server.gui;

import java.awt.*;

/**
 * <p>Implements a collection of static utility methods.
 *
 */
public class Tools {

	public static void centerWindow( Window win) {
	    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	    Dimension winSize = win.getSize();
	    int x = (screenSize.width - winSize.width)/2;
	    int y = (screenSize.height - winSize.height)/4;
	    if (y < 0) 
		y = 0;
	    win.setLocation(x, y);    
	}

	public static String getProviderDirectory() {
	    if (System.getProperty("os.name").equals("Mac OS X"))	    
		return System.getProperty("java.class.path") + System.getProperty("file.separator") + "plugins";
	    else
		return System.getProperty("java.class.path") + System.getProperty("file.separator") + "plugins";
	}

	public static String getPluginDirectory() {
	    if (System.getProperty("os.name").equals("Mac OS X"))
		return System.getProperty("java.class.path") + System.getProperty("file.separator") + "plugins";
	    else
		return System.getProperty("java.class.path") + System.getProperty("file.separator") + "plugins";
	}

	public static String getProviderPackageName() {
		return "plugins";
	}

	public static String getAnalyzerPackageName() {
		return "pluginanalyzers";
	}

	public static String getAnalyzerDirectory() {
	    if (System.getProperty("os.name").equals("Mac OS X"))
		return System.getProperty("java.class.path") + System.getProperty("file.separator") + "pluginanalyzers";
	    else
		return System.getProperty("java.class.path") + System.getProperty("file.separator") + "pluginanalyzers";
	}

}
