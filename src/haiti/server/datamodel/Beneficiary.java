/*
 * File: Beneficiary.java
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

import java.util.Locale;
import java.util.ResourceBundle;

import java.util.Locale;
import java.util.ResourceBundle;

public class Beneficiary {
	
	public enum Sex {MALE, FEMALE};
	public enum InfantCategory {MALNOURISHED, PREVENTION};
	public enum MotherCategory {EXPECTING, NURSING};
	public enum Abbreviated {TRUE, FALSE};
	//public enum Status {NEW, PENDING, PROCESSED};

	private String firstName;
	private String lastName;
	private String commune;
	private String communeSection;
	private int age;
	private Sex sex; 
	private int numberInHome;
	private InfantCategory infantCategory;
	private MotherCategory motherCategory;
	private int id;
	private int status;

	public static Locale[] supportedLocales = {Locale.FRENCH, Locale.ENGLISH};
		
	public static ResourceBundle messages;
	private Locale currentLocale;
	
	/**
	 * Default constructor
	 */
	public Beneficiary() {
		this("first","last", 
				"commune", "section",
				0, 
				Sex.MALE,
				InfantCategory.PREVENTION, MotherCategory.NURSING, 
				0);
		currentLocale = Locale.ENGLISH;
		messages = ResourceBundle.getBundle("MessagesBundle", currentLocale);
	}

	/**
	 * Constructs a beneficiary from a string of attribute=value pairs of
	 * the following form: attr1=value1&attr2=val2& ... &attrN=valueN
	 * @param attributeValueString
	 * @param abbreviatedAttributes true iff the attribute names are abbreviated, eg. fn
	 */
	public Beneficiary (String attributeValueString, Abbreviated abbreviatedAttributes) {
		currentLocale = Locale.ENGLISH;
		messages = ResourceBundle.getBundle("MessagesBundle", currentLocale);
		split(attributeValueString, "&", "=", abbreviatedAttributes);
		
	}

	public Beneficiary(String firstName, String lastName, 
			String commune, String communeSection,
			int age,
			Sex sex, 
			InfantCategory infantCategory, 
			MotherCategory motherCategory, 
			int numberInHome) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.commune = commune;
		this.communeSection = communeSection;
		this.sex = sex;
		this.infantCategory = infantCategory;
		this.motherCategory = motherCategory;
		this.numberInHome = numberInHome;
		currentLocale = Locale.ENGLISH;
		messages = ResourceBundle.getBundle("MessagesBundle", currentLocale);
	}
	
	/**
	 * Splits a string of the form attr1=value1&attr2=val2& ... &attrN=valueN
	 * @param s is the string being split
	 * @param outerDelim defines the delimiter
	 * @param abbreviated are the attributes abbreviated
	 */
	private void split(String s, String outerDelim, String innerDelim, Abbreviated abbreviated) {
		String temp[] = s.split(outerDelim);
		for (int k = 0; k < temp.length; k++) {
			String temp2[] = temp[k].split(innerDelim);	
			
			AttributeManager am = new AttributeManager();
			String ss = am.mapToLong(abbreviated, temp2[0]);
			System.out.println(messages.getString("Attribute") + " = " + ss);
			
			if (ss.equals(messages.getString("firstName")))
				firstName=temp2[1];
			else if (ss.equals(messages.getString("lastName")))
				lastName=temp2[1];
			else if (ss.equals(messages.getString("commune")))
				commune=temp2[1];
			else if (ss.equals(messages.getString("communeSection")))
				communeSection=temp2[1];
			else if (ss.equals(messages.getString("infantCategory")))
				infantCategory=InfantCategory.valueOf(temp2[1]);
			else if (ss.equals(messages.getString("motherCategory")))
				motherCategory=MotherCategory.valueOf(temp2[1]);
			else if (ss.equals(messages.getString("sex")))
				sex=Sex.valueOf(temp2[1]);
			else if (ss.equals(messages.getString("age")))
				age=Integer.parseInt(temp2[1]);
			else if (ss.equals(messages.getString("numberInHome")))
				numberInHome=Integer.parseInt(temp2[1]);
			else if (ss.equals("id"))
				id = Integer.parseInt(temp2[1]);
			else if (ss.equals("status"))
				status = Integer.parseInt(temp2[1]);
		}
		
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCommune() {
		return commune;
	}

	public void setCommune(String commune) {
		this.commune = commune;
	}

	public String getCommuneSection() {
		return communeSection;
	}

	public void setCommuneSection(String communeSection) {
		this.communeSection = communeSection;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public int getNumberInHome() {
		return numberInHome;
	}

	public void setNumberInHome(int numberInHome) {
		this.numberInHome = numberInHome;
		currentLocale = Locale.ENGLISH;
		messages = ResourceBundle.getBundle("MessagesBundle", currentLocale);
	}

	public InfantCategory getInfantCategory() {
		return infantCategory;
	}

	public void setInfantCategory(InfantCategory infantCategory) {
		this.infantCategory = infantCategory;
	}

	public MotherCategory getMotherCategory() {
		return motherCategory;
	}

	public void setMotherCategory(MotherCategory motherCategory) {
		this.motherCategory = motherCategory;
	}
	
	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String toString(String separator) {
		return 
		messages.getString("firstName") + "=" + firstName + separator +
		messages.getString("lastName") + "=" + lastName + separator +
		messages.getString("commune") + "=" + commune + separator +
		messages.getString("communeSection") + "=" + communeSection + separator +
		messages.getString("age") + "=" + age + separator +
		messages.getString("numberInHome") + "=" + numberInHome + separator +
		messages.getString("infantCategory") + "=" + infantCategory + separator +
		messages.getString("motherCategory") + "=" + motherCategory;
	}
	
	public String toString() {
		return toString(System.getProperty("line.separator"));
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Beneficiary ben = new Beneficiary();
		// System.out.println(ben.toString("&"));
		// System.out.println(ben.toString());
		ben = new Beneficiary(messages.getString("firstName") + "=first&"
				+ messages.getString("lastName") + "=last&"
				+ messages.getString("commune") + "=commune&"
				+ messages.getString("communeSection") + "=section&"
				+ messages.getString("age") + "=0&"
				+ messages.getString("numberInHome") + "=0&"
				+ messages.getString("infantCategory") + "=PREVENTION&"
				+ messages.getString("motherCategory") + "=NURSING",
				Abbreviated.FALSE);
		System.out.println(ben.toString());
	}

}
