/*
 * File: DbWriter.java
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

import haiti.server.datamodel.AttributeManager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;

/**
 * @author cslab
 * 
 */
public class DbWriter {

	private static final String dbName = "C:\\Documents and Settings\\cslab\\Desktop\\haitidb\\haiti.db";

	// private static final String dbName = "/home/rfoeckin/Desktop/haiti.db";
	public enum MessageStatus {
		NEW, PENDING, PROCESSED, DECLINED, ARCHIVED, ALL
	};

	public enum MessageType {
		REGISTRATION, UPDATE, ALL
	};

	public static final String TAG = "SmsReader";
	public static final String DB_MESSAGE_TABLE = "message_log";
	public static final String DB_MESSAGE_ID = "id";
	public static final String DB_MESSAGE_COLUMN = "message_text";
	public static final String DB_MESSAGE_STATUS = "message_status";
	public static final String DB_MESSAGE_CREATED_ON = "created_on";
	public static final String DB_MESSAGE_MODIFIED_ON = "modified_on";
	public static final String DB_MESSAGE_TYPE = "message_type";
	public static final String DB_MESSAGE_SENDER = "sender";
	public static final String SEPARATOR = "&";

	public enum Status {
		UNKNOWN(-1), NEW(0), UPDATED(1), PENDING(2), PROCESSED(3);
		private int code;

		private Status(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}
	};

	public static final int DB_STATUS_NEW = 0;
	public static final int DB_STATUS_PENDING = 1;
	public static final int DB_STATUS_PROCESSED = 2;

	/**
	 * connectDb method to connect to database
	 * 
	 * @param filename
	 *            is the database file name with the path
	 * @return the Connection
	 */
	public static Connection connectDb(String filename) {
		Connection connection = null;
		try {
			Class.forName("org.sqlite.JDBC");
			connection = DriverManager.getConnection("jdbc:sqlite:" + filename);
			return connection;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return connection;
		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			e.printStackTrace();
			return connection;
		}
	}

	/**
	 * updateMessage method update the status and modified time for given
	 * Beneficiary object in database
	 * 
	 * @param b
	 *            is the Beneficiary object
	 */

	public void insertMessage(SmsMessage message) {

		if (message.getAVnum() == null) {
			System.out.println("Invalid Message");
		} else {
			Connection connection = connectDb(dbName);
			try {
				Statement statement = connection.createStatement();
				statement.setQueryTimeout(60); // set timeout to 30 sec.
				statement.execute("INSERT into '" + DB_MESSAGE_TABLE + "' ('"
						+ DB_MESSAGE_COLUMN + "', '" + DB_MESSAGE_SENDER
						+ "', '" + DB_MESSAGE_TYPE + "', '" + DB_MESSAGE_STATUS
						+ "') values ('" + message.getMessage() + "', '"
						+ message.getSender() + "', '"
						+ message.getMessageType() + "', '"
						+ message.getMessageStatus() + "')");
			} catch (SQLException e) {
				// if the error message is "out of memory",
				// it probably means no database file is found
				e.printStackTrace();
			}
		}
	}

	/**
	 * Queues ACK messages and when a certain number are collected,
	 * sends a bulk acknowledgement. 
	 * @param msg
	 * @param phone
	 */
	public void queueACK(SmsMessage sms, String phone) {
		log("queueACK, msg:  " + sms.getMessage() + " phone= " + phone);
		//SmsMessage sms = new SmsMessage(msg, phone);

		try {

			BufferedWriter out = new BufferedWriter(new FileWriter(
					"ack_queue.txt", true));
			out.write(sms.getAVnum() + "\n");

			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
		try {
			BufferedReader in = new BufferedReader(new FileReader("ack_queue.txt"));
			String s = in.readLine();
			int count = 0;
			String msgStr = "";
			while(s != null) {
				++count;
				msgStr += s + AttributeManager.LIST_SEPARATOR;
				s = in.readLine();
			}
			if (count >= 5) {
				sendACK(createBulkAck(msgStr), phone);
				try {

					BufferedWriter out = new BufferedWriter(new FileWriter(
							"ack_queue.txt", false));
					out.write("");

					// Close the output stream
					out.close();
				} catch (Exception e) {// Catch exception if any
					System.err.println("Error: " + e.getMessage());
				}			}
  
		} catch (Exception e) {
			log("queueACK, error: " + e.getStackTrace());
		}
	}
	/**
	 * Queues ACK messages and when a certain number are collected,
	 * sends a bulk acknowledgement. 
	 * @param msg
	 * @param phone
	 */
	
	@SuppressWarnings("deprecation")
	public void sendACK(String msg, String phone) {
		// Constructs the URL string
		//log("sendACK, msg:  " + msg + " phone= " + phone);

		String url = "http://localhost:8011/send/sms/" + phone + "/" + msg
				+ "/";
		log("sendAck, url=" + url);
		String charset = "UTF-8";
		URLConnection connection = null;

		try {
			connection = new URL(url).openConnection();

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connection.setDoOutput(true); // Triggers POST.
		connection.setRequestProperty("Accept-Charset", charset);
		connection.setRequestProperty("Content-Type",
				"application/x-www-form-urlencoded;charset=" + charset);
		OutputStream output = null;
		try {
			try {
				// This implicitly opens the connection to the URL
				// and sends the POST request
				output = connection.getOutputStream();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} finally {
			if (output != null)
				try {
					output.close();
				} catch (IOException logOrIgnore) {
				}
		}
		try {
			// For some reason, this needs to be here or it won't work!
			InputStream response = connection.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private String createBulkAck(String ids) {
		//log("createBulkAck,  incoming ids:  " + ids);
		String ackMessage = "";

		ackMessage = AttributeManager.ABBREV_AV + "=ACK"
				+ AttributeManager.PAIRS_SEPARATOR + "IDS=" + ids;
		//log("createAck, ack message:  " + ackMessage);
		return ackMessage;

	}

	/**
	 * creates an ACK message for an incoming sms, properly formatted for
	 * AcdiVoca. For an SMS that starts AV=n,..., the ACK message is
	 * AV=ACK,IDS=n,...
	 * 
	 * @param sms
	 *            the incoming message
	 */
	private String createAck(SmsMessage sms) {
		//log("createAck,  incoming message:  " + sms.getMessage());
		String ackMessage = "";

		ackMessage = AttributeManager.ABBREV_AV + "=ACK"
				+ AttributeManager.PAIRS_SEPARATOR + "IDS=" + sms.getAVnum()
				+ AttributeManager.LIST_SEPARATOR;
		//log("createAck, ack message:  " + ackMessage);
		return ackMessage;
	}

	public static void log(String message) {
		try {

			BufferedWriter out = new BufferedWriter(new FileWriter("log.txt",
					true));
			String now = new Date(System.currentTimeMillis()).toString()
			+ " " + new Time(System.currentTimeMillis()).toString();

			out.write(now + ": " + message + "\n");
			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	/**
	 * Called by Frontline SMS whenever a new message is received by the modem.
	 * 
	 * @param args
	 *            [0] is the message text
	 * @param args
	 *            [1] is the sender
	 */
	public static void main(String args[]) {
		log("main, args= " + args[0] + " " + args[1]);
		DbWriter dw = new DbWriter();

		SmsMessage a = new SmsMessage(args[0], args[1]);

		dw.insertMessage(a);
		String ackMessage = dw.createAck(a);
		if (ackMessage != null)
			//dw.sendACK(ackMessage, args[1]);
			dw.queueACK(a, args[1]);

	}
}