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
import haiti.server.datamodel.DAO;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
import java.net.URLEncoder;
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
import java.util.Properties;
import java.io.*;

/**
 * @author cslab
 * 
 */
public class DbImporter {


	public DbImporter() {
		super();
		//readConfigFile();
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

		if (args[0] != null && args[1] != null) {
			log("Received message in main(), raw message: " + args[0]
					+ " sender: " + args[1]);
			DbWriter dw = new DbWriter();
			
			// sender name
			String sender = args[1];
			// start reading
			try{
				// Open the file that is the first 
				// command line parameter
				File file = new File(args[0]);		
				FileInputStream fstream = new FileInputStream(file);
				// Get the object of DataInputStream
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(new InputStreamReader(in));
				String message;
				DbWriter.smsFlag = false;
				//Read File Line By Line
				while ((message = br.readLine()) != null)   {
					try {
						message = URLDecoder.decode(message, "UTF-8");
						sender = URLDecoder.decode(sender, "UTF-8");
					} catch (UnsupportedEncodingException e) {
						log(e.getMessage());
					}
					// If its a bulk message, parse it and create individual messages
					// out of it
					String id = SmsMessageManager.getMessageCategory(message);
					if (Integer.parseInt(id) < 0) {
						SmsMessage bulkSms = new SmsMessage(message, sender);
						if (dw.insertMessage(bulkSms)) {
							if (dw.queueAck(bulkSms)) {
								List<SmsMessage> messages = SmsMessageManager
										.convertBulkAbsenteeMessage(message, sender);
								for (SmsMessage sms : messages) {
									dw.insertMessage(sms);
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
				}
				DbWriter.smsFlag = true;
				//Close the input stream
				in.close();
			}catch (Exception e){//Catch exception if any
				DbWriter.smsFlag = true;
				System.err.println("Error: " + e.getMessage());
			}

		} else {
			log("Command line arguments were null.");
		}
	}
}
