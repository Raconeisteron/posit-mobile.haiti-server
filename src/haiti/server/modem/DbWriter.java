package haiti.server.modem;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Scanner;

public class DbWriter {

	private static final String dbName = "C:\\Documents and Settings\\cslab\\Desktop\\haitidb\\haiti.db";
	//private static final String dbName = "/home/rfoeckin/Desktop/haiti.db";
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
	public static final String DB_MESSAGE_STATUS = "status";
	public static final String DB_MESSAGE_CREATED_ON = "created_on";
	public static final String DB_MESSAGE_MODIFIED_ON = "modified_on";
	public static final String DB_MESSAGE_TYPE = "message_type";
	public static final String DB_MESSAGE_SENDER = "sender";
	public static final String SEPARATOR = "&";

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

		if(message.getAVnum() == -1) {
			System.out.println("Invalid Message");
		}
		else {
			Connection connection = connectDb(dbName);
			try {
				Statement statement = connection.createStatement();
				statement.setQueryTimeout(60); // set timeout to 30 sec.
				statement.execute("INSERT into '" + DB_MESSAGE_TABLE + "' ('"
						+ DB_MESSAGE_COLUMN + "', '" + DB_MESSAGE_SENDER + "', '"
						+ DB_MESSAGE_TYPE + "', '" + DB_MESSAGE_STATUS + "') values ('"
						+ message.getMessage() + "', '" + message.getSender() + "', '" + message.getType() + "', '"
						+ message.getStatus() + "')"); 
			} catch (SQLException e) {
				// if the error message is "out of memory",
				// it probably means no database file is found
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String args[]) {
		
		DbWriter dw = new DbWriter();
		
		SmsMessage a= new SmsMessage(args[0], args[1]);
		
		dw.insertMessage(a);
		
	}
}