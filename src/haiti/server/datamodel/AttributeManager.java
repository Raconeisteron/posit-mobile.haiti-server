/*
 * File: AttributeManager.java
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
	
	/**
	 * Default constructor, inserts all the attributes into a HashMap
	 */
	
	public AttributeManager() {
		abbreviations = new HashMap<String, String>();
		abbreviations.put(HaitiKeys.ABBREV_ATTRIBUTE, HaitiKeys.LONG_ATTRIBUTE);
		abbreviations.put(HaitiKeys.ABBREV_FIRST, HaitiKeys.LONG_FIRST);
		abbreviations.put(HaitiKeys.ABBREV_LAST, HaitiKeys.LONG_LAST);
		abbreviations.put(HaitiKeys.ABBREV_COMMUNE, HaitiKeys.LONG_COMMUNE);
		abbreviations.put(HaitiKeys.ABBREV_COMMUNE_SECTION, HaitiKeys.LONG_COMMUNE_SECTION);
		abbreviations.put(HaitiKeys.ABBREV_ADDRESS, HaitiKeys.LONG_ADDRESS);
		abbreviations.put(HaitiKeys.ABBREV_AGE, HaitiKeys.LONG_AGE);
		abbreviations.put(HaitiKeys.ABBREV_SEX, HaitiKeys.LONG_SEX);
		abbreviations.put(HaitiKeys.ABBREV_BENEFICIARY, HaitiKeys.LONG_BENEFICIARY);
		abbreviations.put(HaitiKeys.ABBREV_NUMBER_IN_HOME, HaitiKeys.LONG_NUMBER_IN_HOME);
		abbreviations.put(HaitiKeys.ABBREV_HEALTH_CENTER, HaitiKeys.LONG_HEALTH_CENTER);
		abbreviations.put(HaitiKeys.ABBREV_DISTRIBUTION_POST, HaitiKeys.LONG_DISTRIBUTION_POST);
		abbreviations.put(HaitiKeys.ABBREV_NAME_CHILD, HaitiKeys.LONG_NAME_CHILD);
		abbreviations.put(HaitiKeys.ABBREV_NAME_WOMAN, HaitiKeys.LONG_NAME_WOMAN);
		abbreviations.put(HaitiKeys.ABBREV_HUSBAND, HaitiKeys.LONG_HUSBAND);
		abbreviations.put(HaitiKeys.ABBREV_FATHER, HaitiKeys.LONG_FATHER);
		abbreviations.put(HaitiKeys.ABBREV_MOTHER_LEADER, HaitiKeys.LONG_MOTHER_LEADER);
		abbreviations.put(HaitiKeys.ABBREV_VISIT_MOTHER, HaitiKeys.LONG_VISIT_MOTHER);
		abbreviations.put(HaitiKeys.ABBREV_AGRICULTURE_1, HaitiKeys.LONG_AGRICULTURE_1);
		abbreviations.put(HaitiKeys.ABBREV_AGRICULTURE_2, HaitiKeys.LONG_AGRICULTURE_2);
		abbreviations.put(HaitiKeys.ABBREV_GIVE_NAME, HaitiKeys.LONG_GIVE_NAME);
		abbreviations.put(HaitiKeys.ABBREV_YES, HaitiKeys.LONG_YES);
		abbreviations.put(HaitiKeys.ABBREV_NO, HaitiKeys.LONG_NO);
		abbreviations.put(HaitiKeys.ABBREV_MALE, HaitiKeys.LONG_MALE);
		abbreviations.put(HaitiKeys.ABBREV_FEMALE, HaitiKeys.LONG_FEMALE);
		abbreviations.put(HaitiKeys.ABBREV_INFANT_CATEGORY, HaitiKeys.LONG_INFANT_CATEGORY);
		abbreviations.put(HaitiKeys.ABBREV_INFANT_MAL, HaitiKeys.LONG_INFANT_MAL);
		abbreviations.put(HaitiKeys.ABBREV_INFANT_PREVENTION, HaitiKeys.LONG_INFANT_PREVENTION);
		abbreviations.put(HaitiKeys.ABBREV_MOTHER_CATEGORY, HaitiKeys.LONG_MOTHER_CATEGORY);
		abbreviations.put(HaitiKeys.ABBREV_MOTHER_EXPECTING, HaitiKeys.LONG_MOTHER_EXPECTING);
		abbreviations.put(HaitiKeys.ABBREV_MOTHER_NURSING, HaitiKeys.LONG_MOTHER_NURSING);
		abbreviations.put(HaitiKeys.ABBREV_DATA, HaitiKeys.LONG_DATA);
		abbreviations.put(HaitiKeys.ABBREV_GENERAL_INFORMATION, HaitiKeys.LONG_GENERAL_INFORMATION);
		abbreviations.put(HaitiKeys.ABBREV_MCHN_INFORMATION, HaitiKeys.LONG_MCHN_INFORMATION);
		abbreviations.put(HaitiKeys.ABBREV_CONTROLS, HaitiKeys.LONG_CONTROLS);
		abbreviations.put(HaitiKeys.ABBREV_STATUS,HaitiKeys.LONG_STATUS);
		abbreviations.put(HaitiKeys.ABBREV_ID,HaitiKeys.LONG_ID);
	}
	
	/**
	 * Maps the short form field names to long form
	 * @param abbreviatedAttributes TRUE if abbreviated, FALSE if not
	 * @param s the String to be mapped to long
	 * @return the long form of the String
	 */
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
	 * TODO:  This method should thoroughly test this class. For example, 
	 * print out all mappings of short to long.
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AttributeManager am = new AttributeManager();
		System.out.print(am.mapToLong(Beneficiary.Abbreviated.FALSE, "f"));
	}

}
