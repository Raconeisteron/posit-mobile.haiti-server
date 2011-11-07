/*
 * File: LocaleManager.java
 * 
 * Copyright (C) 2011 Humanitarian FOSS Project (http://hfoss.org).
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
 *
 */

package haiti.server.datamodel;

import java.util.Locale;
import java.util.ResourceBundle;

public class LocaleManager {
	
	public static Locale[] supportedLocales = {Locale.FRENCH, Locale.ENGLISH};	
	public static ResourceBundle resources;
	public static Locale currentLocale;
	
	public LocaleManager() {
		currentLocale = Locale.FRENCH;
		resources =  ResourceBundle.getBundle("MenusBundle", currentLocale);
	}

}
