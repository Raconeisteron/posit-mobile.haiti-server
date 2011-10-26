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
import haiti.server.datamodel.DAO;
import haiti.server.datamodel.AttributeManager.MessageStatus;
import haiti.server.datamodel.AttributeManager.MessageType;

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
	public static List<SmsMessage> convertBulkAbsenteeMessage(String message,
			String sender) {
		try {
			String[] parts = message.split(AttributeManager.OUTER_DELIM);
			String[] ids = parts[4].split(AttributeManager.LIST_SEPARATOR);
			List<SmsMessage> messages = new ArrayList<SmsMessage>();
			for (String id : ids) {
				SmsMessage sms = new SmsMessage(id,
						AttributeManager.MessageStatus.NEW,
						AttributeManager.MessageType.ATTENDANCE,
						AttributeManager.ABBREV_AV + "=" + id + ","
								+ AttributeManager.ABBREV_DOSSIER + "=" + id
								+ "," + AttributeManager.ABBREV_Q_PRESENT + "="
								+ AttributeManager.ABBREV_TRUE, sender);
				messages.add(sms);
			}
			return messages;
		} catch (Exception e) {
			e.printStackTrace();
			DbWriter.log("Invalid message format for message: " + message);
		}

		return null;
	}

	/**
	 * Gets the message category from a message string.
	 * 
	 * @param message
	 *            the text of the message
	 * @return the category of the message, or more specifically, if the message
	 *         is AV=n,id=1... then this method returns "n"
	 */
	public static String getMessageCategory(String message) {
		try {
			String[] parts = message.split(AttributeManager.OUTER_DELIM);
			String[] attrVal = parts[0].split(AttributeManager.INNER_DELIM);
			return attrVal[1];
		} catch (Exception e) {
			DbWriter.log("Invalid message format for message: " + message);
		}
		return null;
	}
}
