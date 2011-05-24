/*
 * File: AttributeManager.java
 * 
 * Copyright (C) 2011 Humanitarian FOSS Project
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

import java.util.*;

/**
 * Manages attributes.
 *
 */
public class AttributeManager {
	
	private HashMap<String,String> abbreviations;
	
	public AttributeManager() {
		abbreviations = new HashMap<String, String>();
		abbreviations.put("f", "firstName");
		abbreviations.put("l", "lastName");
		abbreviations.put("c", "commune");
		abbreviations.put("cs", "communeSection");
		abbreviations.put("a", "age");
		abbreviations.put("n", "numberInHome");
		abbreviations.put("ic", "infantCategory");
		abbreviations.put("mc", "motherCategory");
	}
	
	public String mapToLong(Beneficiary.Abbreviated abbreviatedAttributes, String s){
		if (abbreviatedAttributes == Beneficiary.Abbreviated.TRUE) {
			String str = abbreviations.get(s);
			if (str != null)
				return str;
			else 
				return "";
		}
		else
			return s;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AttributeManager am = new AttributeManager();
		System.out.print(am.mapToLong(Beneficiary.Abbreviated.FALSE, "f"));
	}

}
