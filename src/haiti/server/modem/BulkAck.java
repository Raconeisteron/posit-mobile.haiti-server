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

import java.util.Iterator;
import java.util.List;

/**
 * Class to store bulk acknowledgments for the Haiti version of POSIT of the form:
 * AV=ACK, IDS=1,2,3..
 *
 */
public class BulkAck {
	
	private List<Integer> ids;
	private String message = "AV=ACK,IDS=";
	private String sender;
	
	/**
	 * Constructor that also builds the ACK message from a list of id's.
	 * @param ids list of id's to include in the ACK message
	 * @param sender sender from which the messages were sent
	 */
	public BulkAck(List<String> ids, String sender) {
		this.sender=sender;
		for (String id : ids){
			message+=id+"&"; 
		}
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}
	public List<Integer> getIds() {
		return ids;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSender() {
		return sender;
	}
}
