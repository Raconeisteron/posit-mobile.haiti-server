/*
 * File: SmsReader.java
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

package haiti.server.gui;

import haiti.server.datamodel.AttributeManager;
import haiti.server.datamodel.Beneficiary;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
//import java.util.Calendar;
import java.util.Scanner;


/**  
 * A command-line program to read a SMS message file, one message per line.
 *
 *  <BR>To compile: javac -classpath ../../haiti-server.jar:. SmsReader.java
 *  <BR>To run:     java -classpath ../../haiti-server.jar:. SmsReader <filename> [encoding]
 *
 */
public class SmsReader {
	
	public enum MessageStatus {
		UNKNOWN(-1), NEW(0), PENDING(1), PROCESSED(2),  DECLINED(3), ARCHIVED(4), ALL(5);
		
		private int code;
		private MessageStatus(int code) {
			this.code = code;
		}
		public int getCode() {
			return code;
		}
	}
	
	public enum MessageType {
		UNKNOWN(-1), REGISTRATION(0), UPDATE(1), ALL(2);
		
		private int code;
		private MessageType(int code) {
			this.code = code;
		}
		public int getCode() {
			return code;
		}
	}
	
	public static final String TAG = "SmsReader";
	public static final String DB_MESSAGE_TABLE = "message_log";
	public static final String DB_MESSAGE_ID = "id";
	public static final String DB_MESSAGE_COLUMN = "message_text";
	public static final String DB_MESSAGE_STATUS = "message_status";
	public static final String DB_MESSAGE_CREATED_ON = "created_on";
	public static final String DB_MESSAGE_MODIFIED_ON = "modified_on";
	public static final String DB_MESSAGE_TYPE = "message_type";
	public static final String DB_MESSAGE_SENDER = "sender";
	//public static final String AttributeManager.PAIRS_SEPARATOR = AttributeManager.;
	
	public static final int DB_STATUS_NEW = 0;
	public static final int DB_STATUS_PENDING = 1;
	public static final int DB_STATUS_PROCESSED = 2;

	private String filename;
	private String encoding;
	private ArrayList<String> messages = new ArrayList<String>();

	/**
	 * Default constructor.
	 */
	public SmsReader(){
	}
	
	public SmsReader(String filename) { 
		this(filename, System.getProperty("file.encoding"));
	}
	
	public SmsReader(String filename, String encoding) {
		log("Encoding = " + encoding);
		this.filename = filename;
		this.encoding = encoding;
	}
	/**
	 * connectDb method to connect to database
	 * @param filename is the database file name with the path
	 * @return the Connection
	 */
	public Connection connectDb(String filename){
		Connection connection = null;
		try {
			Class.forName(AttributeManager.DB_HOST);
			connection = DriverManager.getConnection(AttributeManager.DB_NAME + filename);
			return connection;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			return connection;
		} catch(SQLException e) {
			// if the error message is "out of memory", 
			// it probably means no database file is found
			e.printStackTrace();
			return connection;
		}
			
	}
	/**
	 * Reads messages from an Sqlite database.
	 * Details for implementing the DataBase can be found on the below link
	 * http://www.xerial.org/trac/Xerial/wiki/SQLiteJDBC#Usage
	 * @param dbName
	 * @throws ClassNotFoundException
	 */
	public void readUnprocessedMsgsFromDb(String dbName) {
		try {
			Connection connection = connectDb(dbName);			
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			
			ResultSet rs = statement.executeQuery(AttributeManager.SELECT_FROM + DB_MESSAGE_TABLE);
			rs.next();
			while(!rs.isAfterLast()) {
				String msg = DB_MESSAGE_ID + AttributeManager.ATTR_VAL_SEPARATOR +  rs.getString(DB_MESSAGE_ID) + AttributeManager.PAIRS_SEPARATOR
					+ DB_MESSAGE_SENDER+AttributeManager.ATTR_VAL_SEPARATOR +rs.getString(DB_MESSAGE_SENDER) + AttributeManager.PAIRS_SEPARATOR
					+ DB_MESSAGE_STATUS + AttributeManager.ATTR_VAL_SEPARATOR + rs.getString(DB_MESSAGE_STATUS) + AttributeManager.PAIRS_SEPARATOR
					+ DB_MESSAGE_TYPE + AttributeManager.ATTR_VAL_SEPARATOR +rs.getString(DB_MESSAGE_TYPE) + AttributeManager.PAIRS_SEPARATOR
					+ DB_MESSAGE_CREATED_ON + AttributeManager.DATE_SEPARATOR + rs.getString(DB_MESSAGE_CREATED_ON) + AttributeManager.PAIRS_SEPARATOR
					+ DB_MESSAGE_MODIFIED_ON  + AttributeManager.DATE_SEPARATOR + rs.getString(DB_MESSAGE_MODIFIED_ON) + AttributeManager.PAIRS_SEPARATOR
					+ rs.getString(DB_MESSAGE_COLUMN);
				System.out.println(msg);

				messages.add(msg);
				rs.next();
			}
			if(connection != null)
				connection.close();
		} catch(SQLException e) {
			// if the error message is "out of memory", 
			// it probably means no database file is found
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads messages from an Sqlite database with the given status and type.
	 * @param dbName is the database path and name
	 * @param status is the enum type containing status of the message
	 * @param type is the enum type containing type of the message
	 * return the array containing messages with the given status and type
	 */
	public String[] getMessageByStatusAndType(String dbName, MessageStatus status, MessageType type) {
		//int statusInt = status.ordinal();
		//int typeInt = type.ordinal();
		ArrayList<String> statusmsg = new ArrayList<String>();
		String arr[] = new String[0];
		try {
			Connection connection = connectDb(dbName);			
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			ResultSet rs = null;
			if (status.getCode() == 5 && type.getCode() == 2)
				rs = statement.executeQuery(AttributeManager.SELECT_FROM + DB_MESSAGE_TABLE +AttributeManager.LINE_ENDER);			
			else if (status.getCode() == 5 && type.getCode() != 2)
				rs = statement.executeQuery(AttributeManager.SELECT_FROM + DB_MESSAGE_TABLE+AttributeManager.WHERE+ DB_MESSAGE_TYPE +AttributeManager.ATTR_VAL_SEPARATOR+type.getCode()+AttributeManager.LINE_ENDER);
			else if (status.getCode() != 5 && type.getCode() == 2)
				rs = statement.executeQuery(AttributeManager.SELECT_FROM + DB_MESSAGE_TABLE+AttributeManager.WHERE+ DB_MESSAGE_STATUS +AttributeManager.ATTR_VAL_SEPARATOR+status.getCode()+AttributeManager.LINE_ENDER);
			else
				rs = statement.executeQuery(AttributeManager.SELECT_FROM + DB_MESSAGE_TABLE+AttributeManager.WHERE+ DB_MESSAGE_TYPE +AttributeManager.ATTR_VAL_SEPARATOR+type.getCode() +AttributeManager.CONJUNCTION + DB_MESSAGE_STATUS+AttributeManager.ATTR_VAL_SEPARATOR+status.getCode()+AttributeManager.LINE_ENDER);
			rs.next();
			while(!rs.isAfterLast()) {
				String msg = DB_MESSAGE_ID + AttributeManager.ATTR_VAL_SEPARATOR +  rs.getString(DB_MESSAGE_ID) + AttributeManager.PAIRS_SEPARATOR 
				+ DB_MESSAGE_SENDER+ AttributeManager.ATTR_VAL_SEPARATOR +rs.getString(DB_MESSAGE_SENDER) + AttributeManager.PAIRS_SEPARATOR
				+ DB_MESSAGE_STATUS + AttributeManager.ATTR_VAL_SEPARATOR + rs.getString(DB_MESSAGE_STATUS) + AttributeManager.PAIRS_SEPARATOR
				+ DB_MESSAGE_TYPE + AttributeManager.ATTR_VAL_SEPARATOR +rs.getString(DB_MESSAGE_TYPE) + AttributeManager.PAIRS_SEPARATOR
				+ DB_MESSAGE_CREATED_ON + AttributeManager.DATE_SEPARATOR + rs.getString(DB_MESSAGE_CREATED_ON) + AttributeManager.PAIRS_SEPARATOR
				+ DB_MESSAGE_MODIFIED_ON  + AttributeManager.DATE_SEPARATOR + rs.getString(DB_MESSAGE_MODIFIED_ON) + AttributeManager.PAIRS_SEPARATOR
				+ rs.getString(DB_MESSAGE_COLUMN);
				statusmsg.add(msg);
				rs.next();
			}
			arr = new String[statusmsg.size()];
			for (int k = 0; k < arr.length; k++) 
				arr[k] = statusmsg.get(k);
			if(connection != null)
				connection.close();
		} catch(SQLException e) {
			// if the error message is "out of memory", 
			// it probably means no database file is found
			e.printStackTrace();
		}
		return arr;
	}
	
	/**
	 * Reads an SMS message from the Db for the given id.
	 * @param id, is the row-id of the message
	 * @param dbName is the full path name to the .db file.
	 * @return a String representing the message or "id NOT FOUND"
	 */
	public String getMessageById(int id, String dbName) {
		System.out.println("Getting message for id= " + id);
		Connection connection = connectDb(dbName);
		Statement statement;
		String msg = null;
		try {
			statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec	
			ResultSet rs = statement.executeQuery(AttributeManager.SELECT_FROM + DB_MESSAGE_TABLE + AttributeManager.WHERE+DB_MESSAGE_ID+ AttributeManager.ATTR_VAL_SEPARATOR + id);
			rs.next();
			if (rs.isAfterLast())
				return id + AttributeManager.NOT_FOUND;

			msg = DB_MESSAGE_ID + AttributeManager.ATTR_VAL_SEPARATOR +  rs.getString(DB_MESSAGE_ID) + AttributeManager.PAIRS_SEPARATOR 
			+ DB_MESSAGE_SENDER+AttributeManager.ATTR_VAL_SEPARATOR +rs.getString(DB_MESSAGE_SENDER) + AttributeManager.PAIRS_SEPARATOR
			+ DB_MESSAGE_STATUS + AttributeManager.ATTR_VAL_SEPARATOR + rs.getString(DB_MESSAGE_STATUS) + AttributeManager.PAIRS_SEPARATOR
			+ DB_MESSAGE_TYPE + AttributeManager.ATTR_VAL_SEPARATOR +rs.getString(DB_MESSAGE_TYPE) + AttributeManager.PAIRS_SEPARATOR
			+ DB_MESSAGE_CREATED_ON + AttributeManager.DATE_SEPARATOR + rs.getString(DB_MESSAGE_CREATED_ON) + AttributeManager.PAIRS_SEPARATOR
			+ DB_MESSAGE_MODIFIED_ON  + AttributeManager.DATE_SEPARATOR + rs.getString(DB_MESSAGE_MODIFIED_ON) + AttributeManager.PAIRS_SEPARATOR
			+ rs.getString(DB_MESSAGE_COLUMN);
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return msg;
	}
	
	/**
	 * updateMessage method update the status and modified time for given
	 * Beneficiary object in database
	 * @param b is the Beneficiary object
	 */
	public void updateMessage(Beneficiary b, String dbName) {
//		String date_format = "yyyy-MM-dd HH:mm:ss";
//		Calendar cal = Calendar.getInstance();
//		SimpleDateFormat sdf = new SimpleDateFormat(date_format);
//		String time = sdf.format(cal.getTime());
		System.out.println("Upating beneficiary record " + b.getId());
		Connection connection = connectDb(dbName);
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(60); // set timeout to 30 sec.

			statement.execute(AttributeManager.UPDATE + DB_MESSAGE_TABLE + AttributeManager.SET+ DB_MESSAGE_STATUS+AttributeManager.ATTR_VAL_SEPARATOR
					+ AttributeManager.SINGLE_QUOTE + b.getStatus().ordinal() + AttributeManager.SINGLE_QUOTE + AttributeManager.WHERE+DB_MESSAGE_ID+AttributeManager.ATTR_VAL_SEPARATOR + b.getId() + AttributeManager.LINE_ENDER);
			
		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			e.printStackTrace();
		}
	}
	
	/**
	 * updateMessage method update the status and modified time for given
	 * Beneficiary object in database
	 * @param b is the Beneficiary object
	 */
	public void insertMessage(String message, String sender, String dbName) {
		System.out.println("Inserting beneficiary record " + message);
		Connection connection = connectDb(dbName);
		try {
			Statement statement = connection.createStatement();
			statement.setQueryTimeout(60); // set timeout to 30 sec.

			statement.execute(AttributeManager.INSERT + DB_MESSAGE_TABLE + AttributeManager.OPEN_PAREN
					+ DB_MESSAGE_COLUMN + AttributeManager.PAIRS_SEPARATOR + DB_MESSAGE_STATUS + AttributeManager.PAIRS_SEPARATOR
					+ DB_MESSAGE_SENDER + AttributeManager.CLOSE_PAREN + AttributeManager.VALUES + AttributeManager.OPEN_PAREN + AttributeManager.SINGLE_QUOTE + message + AttributeManager.SINGLE_QUOTE + AttributeManager.PAIRS_SEPARATOR
					+ DB_STATUS_NEW + AttributeManager.PAIRS_SEPARATOR +  AttributeManager.SINGLE_QUOTE + sender + AttributeManager.SINGLE_QUOTE + AttributeManager.CLOSE_PAREN );
			
		} catch (SQLException e) {
			// if the error message is "out of memory",
			// it probably means no database file is found
			e.printStackTrace();
		}
	}
	/** 
	 * Reads the file line by line into the arraylist. 
	 */
	public void readFile() {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new FileInputStream(filename), encoding);
			while (scanner.hasNextLine()) {
				messages.add(scanner.nextLine());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			scanner.close();
		}
		log("Text read from " + filename + " : " + System.getProperty("line.AttributeManager.PAIRS_SEPARATOR") + toString());
	}
	
	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getEncoding() {
		return encoding;
	}

	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}

	public ArrayList<String> getMessages() {
		return messages;
	}
	
	public String[] getMessagesAsArray() {
		String arr[] = new String[messages.size()];
		for (int k = 0; k < arr.length; k++) 
			arr[k] = messages.get(k);
		return arr;
	}

	public void setMessages(ArrayList<String> messages) {
		this.messages = messages;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int k = 0; k < messages.size(); k++) 
			sb.append(messages.get(k) + System.getProperty("line.AttributeManager.PAIRS_SEPARATOR"));
		return sb.toString();
	}
	
	
	private void log(String s) {
		System.out.println(TAG + " " + s);
	}
	
//	private void readSMS(String s){
//		messages.add(s);
//	}
	
//	public String[] getCommune(){
//		String[] commune = {"Anse a PitresAttributeManger.PAIRS_SEPARATORBainetAttributeManger.PAIRS_SEPARATORBelle AnseAttributeManger.PAIRS_SEPARATORCote de ferAttributeManger.PAIRS_SEPARATORGrand GosierAttributeManger.PAIRS_SEPARATORLa valleeAttributeManger.PAIRS_SEPARATORThiotte"};
//		return commune;
//		
//	} 
//	
//	public String[] getCommuneSection(String commune){
//		String[] communeSection;
//		if (commune.equals("Anse a Pitres")) {
//			communeSection = new String[5];
//			communeSection[0] = "Anse a Pitres";
//			communeSection[1] = "Bois d_Orme";
//			communeSection[2] = "BoucanGuillaume";
//			communeSection[3] = "Centre de Sante Anse a Pitres";
//			communeSection[4] = "Platon Cedre";
//		}
//		else if (commune.equals("Bainet")) {
//			communeSection = new String[12];
//			communeSection[0] = "11eme La vallee";
//			communeSection[1] = "3eme La vallee";
//			communeSection[2] = "4eme La vallee";
//			communeSection[3] = "5eme Haut Gandou";
//			communeSection[4] = "6eme Bas de la croix";
//			communeSection[5] = "8eme orangers";
//			communeSection[6] = "9eme Bas gris gris";
//			communeSection[7] = "Bas Grandou";
//			communeSection[8] = "Bras de gauche";
//			communeSection[9] = "Bresilienne";
//			communeSection[10] = "Haut Grandou";
//			communeSection[11] = "Tou mahot";
//		}
//		else if (commune.equals("Belle Anse")) {
//			communeSection = new String[7];
//			communeSection[0] = "Baie d_Orange";
//			communeSection[1] = "BelAir";
//			communeSection[2] = "Callumette";
//			communeSection[3] = "CorailLamothe";
//			communeSection[4] = "Mabriole";
//			communeSection[5] = "Mapou";
//			communeSection[6] = "Pichon";
//		}
//		else if (commune.equals("Cote de fer")) {
//			communeSection = new String[8];
//			communeSection[0] = "3eme Bras de gauche";
//			communeSection[1] = "6eme Jamais-Vu";
//			communeSection[2] = "Amazone";
//			communeSection[3] = "Boucan Belier";
//			communeSection[4] = "Cote de fer";
//			communeSection[5] = "Gris-Gris";
//			communeSection[6] = "Jamais vus";
//			communeSection[7] = "Labich";
//		}
//		else if (commune.equals("Grand Gosier")) {
//			communeSection = new String[3];
//			communeSection[0] = "Bodarie";
//			communeSection[1] = "CollinedesChaines";
//			communeSection[2] = "Grand Gosier";
//		}		
//		else if (commune.equals("La vallee")) {
//			communeSection = new String[2];
//			communeSection[0] = "1ere Musac";
//			communeSection[1] = "Morne a Brule";
//		}		
//		else if (commune.equals("Thiotte")) {
//			communeSection = new String[2];
//			communeSection[0] = "Pot de Chambre/2eMareMirande";
//			communeSection[1] = "Thiotte 1ereColombier";
//		}
//		else {
//			communeSection = new String[0];
//		}
//		return communeSection;
//		
//	}
	 
   
	/**
	 * This method need to be completely test this class.
	 * @param args
	 * @throws Exception
	 */
    public static void main (String args[]) throws Exception {
//    	if (args.length < 1) {
//    		System.out.println("Usage: java SmsReader <filename>");
//    		System.out.println("This assumes your database is in the /db directory of " +
//    				System.getProperty(AttributeManager.USER_DIRECTORY).toString());
//    		return;
//    	}
    	/*
    	SmsReader reader = new SmsReader();
		String filepath = System.getProperty(AttributeManager.USER_DIRECTORY).toString()+ AttributeManager.DATABASE_PATHNAME + args[0];
		System.out.println("Path = " + filepath);
		for (MessageStatus st : MessageStatus.values()) {
    		System.out.println(st);
    		String[] test = reader.getMessageByStatus(filepath, st);
    	for (MessageStatus st : MessageStatus.values()) {
    		for (MessageType tp : MessageType.values()) {
    		System.out.println(st+ AttributeManager.CONJUNCTION +tp);
    		String[] test = reader.getMessageByStatusAndType(System.getProperty(AttributeManager.USER_DIRECTORY).toString()+ "/db/haiti.db", st, tp);
    		//String[] arr = reader.convertToArray(test);
    		for (int i = 0; i < test.length; i ++) {
            	System.out.println(test[i]);
            }
    	}
    	
    	for (MessageType st : MessageType.values()) {
    		System.out.println(st);
    		String[] test = reader.getMessageByType(filepath, st);
    		//String[] arr = reader.convertToArray(test);
    		for (int i = 0; i < test.length; i ++) {
            	System.out.println(test[i]);
    	}*/
    	if(args.length < 3 || args.length > 3){
    		System.out.println("usage: You need to give at least 3 arguments");
    		System.out.println("java SmsReader filepath message_status message_type");
    		return;
    	}
    	String filepath = System.getProperty(AttributeManager.USER_DIRECTORY).toString()+ AttributeManager.DATABASE_PATHNAME+args[0];
    	System.out.println(filepath);
    	System.out.println(args[1]+ AttributeManager.CONJUNCTION + args[2]);
    	MessageStatus stat = null;
    	MessageType typ = null;
    	SmsReader reader = new SmsReader();
    	for (MessageStatus st : MessageStatus.values()) {
    		if(st.toString().equals(args[1])){
    			stat = st;
    			break;
    		}
    	}
    	for (MessageType t : MessageType.values()) {
    		if(t.toString().equals(args[2])){
    			typ = t;
    			break;
    		}
    	}

    	if (stat == null){
    		System.out.println(args[1] + AttributeManager.DOES_NOT_EXIST);
    		return;
    	}
    	if (typ == null){
    		System.out.println(args[2] + AttributeManager.DOES_NOT_EXIST);
    		return;
    	}
    	String[] arr = reader.getMessageByStatusAndType(filepath, stat, typ);

    	if (arr.length == 0){
    		System.out.println(AttributeManager.MATCH_NOT_FOUND);
    		return;
    	}
    	for (int i = 0; i < arr.length; i ++) {
    		System.out.println(arr[i]);
    	}

    	/*if (args.length < 1) {
    		reader = new SmsReader();
            reader.readUnprocessedMsgsFromDb("jdbc:sqlite:sample.db");
            String[] arr = reader.getMessagesAsArray();
            for (int i = 0; i < arr.length; i ++) {
            	System.out.println(arr[i]);
            }
        } else if (args.length < 2) {
        	reader = new SmsReader(args[0]);  // Uses default encoding
            reader.readFile();
        } else {
            reader = new SmsReader(args[0], args[1]);
            reader.readFile();
        } */  
    }
}

