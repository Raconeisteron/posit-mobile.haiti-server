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
import haiti.server.gui.DAO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.sql.Array;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cslab
 * 
 */
public class DbWriter {

	private static final String dbName = "C:\\Documents and Settings\\cslab\\Desktop\\haitidb\\haiti.db";

	public enum MessageStatus {
		NEW, PENDING, PROCESSED, DECLINED, ARCHIVED, ALL
	};

	public enum MessageType {
		REGISTRATION, UPDATE, ABSENT, ALL
	};

	public static final String TAG = "SmsReader";
	public static final String DB_MESSAGE_TABLE = "message_log";
	public static final String DB_MESSAGE_ID = "id";
	public static final String DB_MESSAGE_AV_NUM = "av_num";
	public static final String DB_MESSAGE_COLUMN = "message_text";
	public static final String DB_MESSAGE_STATUS = "message_status";
	public static final String DB_MESSAGE_CREATED_ON = "created_on";
	public static final String DB_MESSAGE_MODIFIED_ON = "modified_on";
	public static final String DB_MESSAGE_TYPE = "message_type";
	public static final String DB_MESSAGE_SENDER = "sender";
	public static final String DB_MESSAGE_ACKED = "acked";
	public static final String SEPARATOR = "&";

	public static final String DB_ACK_COUNT_TABLE = "ack_counter";
	public static final String DB_ACK_COUNT_SENDER = "sender";
	public static final String DB_ACK_COUNT_FIELD = "count";

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
			log(e.getMessage());
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

	public boolean insertMessage(SmsMessage message) {
		if (message.getAVnum() == null) {
			log("Message has no AV number, should be of the format AV=n,... : "
					+ message);
			return false;
		} else {
			Connection connection = connectDb(dbName);
			try {
				Statement statement = connection.createStatement();
				statement.setQueryTimeout(60);
				statement.execute("INSERT into '" + DB_MESSAGE_TABLE + "' ('"
						+ DB_MESSAGE_COLUMN + "', '" + DB_MESSAGE_SENDER
						+ "', '" + DB_MESSAGE_TYPE + "', '" + DB_MESSAGE_STATUS
						+ "', '" + DB_MESSAGE_AV_NUM + "') values ('"
						+ message.getMessage() + "', '" + message.getSender()
						+ "', '" + message.getMessageType() + "', '"
						+ message.getMessageStatus() + "', '"
						+ message.getAVnum() + "')");
			} catch (SQLException e) {
				log(e.getMessage());
				return false;
			}
		}
		return true;
	}

	/**
	 * Called when first receiving a message to update the DB_ACK_COUNT_TABLE to
	 * queue up the message for acknowledgment and start the counter.
	 * 
	 * @param sms
	 */
	public boolean ackMessageInDb(SmsMessage sms) {
		if (sms.getSender() == null) {
			log("Sms has no sender attribute: " + sms);
		} else {
			Connection connection = connectDb(dbName);
			try {
				Statement statement = connection.createStatement();
				statement.setQueryTimeout(60);
				statement.execute("INSERT OR IGNORE INTO " + DB_ACK_COUNT_TABLE
						+ " VALUES ('" + sms.getSender() + "'," + 0 + ")");
				statement.execute("UPDATE " + DB_ACK_COUNT_TABLE + " SET "
						+ DB_ACK_COUNT_FIELD + "=" + DB_ACK_COUNT_FIELD
						+ " + 1  WHERE " + DB_ACK_COUNT_SENDER + "='"
						+ sms.getSender() + "'");
			} catch (SQLException e) {
				log(e.getMessage());
				return false;
			}
		}
		return true;
	}

	/**
	 * Change the DB_MESSAGE_ACKED field in the database to true fora certain AV
	 * number and sender.
	 * 
	 * @param avNum
	 *            the AV number of the entry to ack
	 * @param sender
	 *            the sender
	 */
	public boolean markAcked(String avNum, String sender) {
		Connection connection = connectDb(dbName);
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(60); // set timeout to 30 sec.
			statement.execute("UPDATE " + DB_MESSAGE_TABLE + " SET "
					+ DB_MESSAGE_ACKED + "= 1 WHERE " + DB_MESSAGE_SENDER
					+ "='" + sender + "' AND " + DB_MESSAGE_AV_NUM + "='"
					+ avNum + "'");

		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			log(e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Change the DB_MESSAGE_ACKED field in the database to -1 to indicate that
	 * this message came in as part of a bulk message.
	 * 
	 * @param avNum
	 *            the AV number of the entry to ack
	 * @param sender
	 *            the sender
	 */
	public boolean markAsAbsent(String avNum, String sender) {
		Connection connection = connectDb(dbName);
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(60); // set timeout to 30 sec.
			statement.execute("UPDATE " + DB_MESSAGE_TABLE + " SET "
					+ DB_MESSAGE_TYPE + "="
					+ DAO.MessageType.ATTENDENCE.getCode() + " WHERE "
					+ DB_MESSAGE_SENDER + "='" + sender + "' AND "
					+ DB_MESSAGE_AV_NUM + "= '" + avNum + "'");

		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			log(e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Reset the count field in the DB_ACK_COUNT table. Only used when the
	 * number of queued messages is greater than
	 * AttributeManager.ACK_MESSAGES_AT.
	 * 
	 * @param sender
	 *            the phone whose count will be reset
	 */
	public boolean resetCount(String sender) {
		Connection connection = connectDb(dbName);
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(60); // set timeout to 30 sec.
			statement.execute("UPDATE " + DB_ACK_COUNT_TABLE + " SET "
					+ DB_ACK_COUNT_FIELD + "= 0 WHERE " + DB_ACK_COUNT_SENDER
					+ "='" + sender + "'");

		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			log(e.getMessage());
			return false;
		}
		return true;
	}

	/**
	 * Gets all phones from the database that have enough queued messages to
	 * acknowledge, i.e. where the number of queued messages is greater than
	 * AttributeManager.ACK_MESSAGES_AT.
	 * 
	 * @return a list of the phones
	 */
	public List<String> getPhonesReadyToSendBulkAcks() {
		Connection connection = connectDb(dbName);
		List<String> phones = new ArrayList<String>();
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(60); // set timeout to 30 sec.
			ResultSet result = statement.executeQuery("select * from "
					+ DB_ACK_COUNT_TABLE + " WHERE " + DB_ACK_COUNT_FIELD
					+ " >= " + AttributeManager.ACK_MESSAGES_AT);
			while (result.next()) {
				phones.add(result.getString(DB_ACK_COUNT_SENDER));
			}
			return phones;

		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			log(e.getMessage());
		}
		return null;
	}

	/**
	 * Gets all phones from the database that have enough queued messages to
	 * acknowledge, i.e. where the number of queued messages is greater than
	 * AttributeManager.ACK_MESSAGES_AT.
	 * 
	 * @return a list of the phones
	 */
	public boolean shouldPhoneSendAck(SmsMessage msg) {
		Connection connection = connectDb(dbName);
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(60); // set timeout to 30 sec.
			ResultSet result = statement.executeQuery("select * from "
					+ DB_ACK_COUNT_TABLE + " WHERE " + DB_ACK_COUNT_FIELD
					+ " >= " + msg.getMsgTotal() + " and "
					+ DB_ACK_COUNT_SENDER + " = " + msg.getSender());
			int count = 0;
			while (result.next())
				count++;
			if (count > 0)
				return true;
			else
				return false;

		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			log(e.getMessage());
		}
		return false;
	}

	/**
	 * Gets ids from the database that are not yet acknowledged based on the
	 * phone number that sent them.
	 * 
	 * @param sender
	 *            the phone number that sent the messages
	 * @return
	 */
	public List<String> getUnackedIdsByPhone(String sender) {
		if (sender == null) {
			log("Sender is null");
		} else {
			Connection connection = connectDb(dbName);
			List<String> ids = new ArrayList<String>();
			try {
				Statement statement = connection.createStatement();
				statement.setQueryTimeout(60); // set timeout to 30 sec.
				ResultSet result = statement.executeQuery("select * from "
						+ DB_MESSAGE_TABLE + " where " + DB_MESSAGE_SENDER
						+ "=" + sender + " and " + DB_MESSAGE_TYPE + "!=2 and "
						+ DB_MESSAGE_ACKED + "=0");
				while (result.next()) {
					ids.add(result.getString(DB_MESSAGE_AV_NUM));
				}
				return ids;

			} catch (SQLException e) {
				// if the error message is "out of memory",
				// it probably means no database file is found
				log(e.getMessage());
			}
		}
		return null;
	}

	/**
	 * Queues ACK messages and when a certain number are collected, sends a bulk
	 * acknowledgment.
	 * 
	 * @param msg
	 * @param phone
	 */
	public boolean queueAck(SmsMessage sms) {
		log("queueACK, msg:  " + sms.getMessage() + " phone= "
				+ sms.getSender());
		if (ackMessageInDb(sms)) {
			if (shouldPhoneSendAck(sms)) {
				List<String> ids = getUnackedIdsByPhone(sms.getSender());
				if (ids != null) {
					if (ids.size() < 30) {
						BulkAck bulkAck = new BulkAck(ids, sms.getSender());
						sendAck(bulkAck);
					} else { // Quick fix to prevent going over 160 characters
						BulkAck bulkAck1 = new BulkAck(ids.subList(0,
								ids.size() / 2), sms.getSender());
						BulkAck bulkAck2 = new BulkAck(ids.subList(
								ids.size() / 2 + 1, ids.size() - 1),
								sms.getSender());
						sendAck(bulkAck1);
						sendAck(bulkAck2);
					}
					for (String id : ids) {
						markAcked(id, sms.getSender());
					}
					resetCount(sms.getSender());
					return true;
				} else {
					log("No messages found to ack for phone: "
							+ sms.getSender());
					return true;
				}
			}
			return true;
		} else
			log("Message not ack'd successfully");
		return false;
	}

	/**
	 * Queues ACK messages and when a certain number are collected, sends a bulk
	 * acknowledgement.
	 * 
	 * @param msg
	 * @param phone
	 */

	@SuppressWarnings("deprecation")
	public void sendAck(BulkAck ack) {
		// Constructs the URL string
		log("sendACK, msg:  " + ack.getMessage());

		String url = "http://localhost:8011/send/sms/" + ack.getSender() + "/"
				+ ack.getMessage() + "/";
		log("sendAck, url=" + url);
		String charset = "UTF-8";
		URLConnection connection = null;

		try {
			connection = new URL(url).openConnection();

		} catch (MalformedURLException e) {
			log(e.getMessage());
		} catch (IOException e) {
			log(e.getMessage());
		}
		connection.setDoOutput(true); // Triggers POST.

		OutputStream output = null;
		try {
			try {
				// This implicitly opens the connection to the URL
				// and sends the POST request
				output = connection.getOutputStream();
			} catch (IOException e) {
				log(e.getMessage());
			}
		} finally {
			if (output != null)
				try {
					output.close();
				} catch (IOException e) {
					log(e.getMessage());
				}
		}
		try {
			// For some reason, this needs to be here or it won't work!
			InputStream response = connection.getInputStream();
		} catch (IOException e) {
			log(e.getMessage());
		}

	}

	/**
	 * Logs a message in the log file.
	 * 
	 * @param message
	 *            the message to log.
	 */
	public static void log(String message) {
		try {

			BufferedWriter out = new BufferedWriter(new FileWriter("log.txt",
					true));
			String now = new Date(System.currentTimeMillis()).toString() + " "
					+ new Time(System.currentTimeMillis()).toString();

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
		// SmsMessage sms=new
		// SmsMessage("AV=4,mn=1:10,i=fafa,ms=0,t=0","%2B14406662498");
		// System.out.println(dw.shouldPhoneSendAck(sms));

		if (args[0] != null && args[1] != null) {
			log("Received message in main(), raw message: " + args[0]
					+ " sender: " + args[1]);
			DbWriter dw = new DbWriter();
			String message = args[0];
			String sender = args[1];
			try {
				message = URLDecoder.decode(message, "UTF-8");
				sender = URLDecoder.decode(sender, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				log(e.getMessage());
			}

			sender = sender.replace("+", "");
			// If its a bulk message, parse it and create individual messages
			// out of it
			String id = SmsMessageManager.getMessageCategory(message);
			if (Integer.parseInt(id) < 0) {
				SmsMessage bulkSms = new SmsMessage(message, sender);
				// SmsMessage bulkSms = new
				// SmsMessage(AttributeManager.ABBREV_AV
				// + "=" + id + "," + AttributeManager.ABBREV_MESSAGE_TYPE
				// + "=" + SmsReader.MessageType.ABSENTEE.getCode() +
				// AttributeManager.ABBREV_MESSAGE_NUMBER +
				// sender);
				if (dw.insertMessage(bulkSms)) {
					if (dw.queueAck(bulkSms)) {
						List<SmsMessage> messages = SmsMessageManager
								.convertBulkAbsenteeMessage(message, sender);
						for (SmsMessage sms : messages) {
							dw.insertMessage(sms);
							// dw.markAsAbsent(sms.getAVnum(), sms.getSender());
						}
					} else
						log("Unable to queue acknowledgment for bulk SMS: "
								+ bulkSms);
				} else
					log("Unable to insert bulk SMS message: " + bulkSms);
			} else { // Its a normal message, proceed as normal
				SmsMessage sms = new SmsMessage(message, sender);

				dw.insertMessage(sms);
				dw.queueAck(sms);
			}
		} else {
			log("Command line arguments were null.");
		}
	}
}
