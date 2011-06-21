/*
 * File: SmsMessageManager.java
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

package haiti.server.modem;

import java.util.ArrayList;
import java.util.List;

import haiti.server.datamodel.AttributeManager;

/**
 * Class that provides some helper methods for dealing with SMS messages coming
 * from Haiti version of posit.
 * 
 */
public class SmsMessageManager {

	public SmsMessageManager() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * Takes a bulk message of the format AV=-1,dossier&dossier&dossier and
	 * returns a list of messages of the format AV=-1,i=dossier,p=T
	 * 
	 * @param message
	 *            the text of the bulk message
	 * @param sender
	 *            the phone number from which the message was sent
	 */
	public static List<SmsMessage> convertBulkMessage(String message,
			String sender) {
		String[] parts = message.split(AttributeManager.OUTER_DELIM);
		String[] ids = parts[1].split(AttributeManager.LIST_SEPARATOR);
		List<SmsMessage> messages = new ArrayList<SmsMessage>();
		for (String id : ids) {
			SmsMessage sms = new SmsMessage("AV=-1,i=" + id + ",p=T", sender);
			messages.add(sms);
		}
		return messages;
	}

	/**
	 * Gets the message category from a message string.
	 * 
	 * @param message
	 *            the text of the message
	 * @return the category of the message, or more specifically, if the message
	 *         is AV=ACK,id=1... then this method returns "ACK."
	 */
	public static String getMessageCategory(String message) {
		String[] parts = message.split(AttributeManager.OUTER_DELIM);
		String[] attrVal = parts[0].split(AttributeManager.INNER_DELIM);
		return attrVal[1];
	}
}
