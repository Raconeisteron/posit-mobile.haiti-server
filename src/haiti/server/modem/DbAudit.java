package haiti.server.modem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import haiti.server.datamodel.AttributeManager;



public class DbAudit {

	private enum MessageType {
		bulk, change, registration, other
	};
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
	 * Logs a message in the log file.
	 * 
	 * @param message
	 *            the message to log.
	 */
	public static void log(String message) {
		try {

			BufferedWriter out = new BufferedWriter(new FileWriter("ErrorLog.txt",
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
	 * Logs a message in the log file.
	 * 
	 * @param message
	 *            the message to log.
	 */
	public static void auditLog(String message) {
		try {

			BufferedWriter out = new BufferedWriter(new FileWriter("AuditLog.txt",
					true));
			out.write(message + "\n");
			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {

		if(args.length == 3){
			String dbName = args[0];
			String phone = args[1];
			String date = args[2];
			String di  = "";
			Connection connection = connectDb(dbName);
			try {
				Statement statement = connection.createStatement();
				statement.setQueryTimeout(60);
				String sqlStatement = "SELECT message_text " +
						"FROM message_log " +
						"WHERE sender = '" + phone + "' " +
						"AND date(created_on) = '"+ date +"';";
				log(sqlStatement);
				ResultSet rs = statement.executeQuery( sqlStatement );

				int absentees = 0;
				int change0 = 0;
				int change1 = 0;
				int change2 = 0;
				int change3 = 0;
				int change4 = 0;
				int change5 = 0;
				int change6 = 0;
				int change7 = 0;
				int change8 = 0;
				int newRegis = 0;
				while(rs.next()){
					String message = rs.getString("message_text") ;
					String[] msgArr  = message.split(",");
					MessageType msgTyp;
					// identify the message type
					if (msgArr.length == 5)
						msgTyp = MessageType.bulk;
					else if (msgArr.length == 18)
						msgTyp = MessageType.change;
					else
						msgTyp = MessageType.other;
					// handle each type
					if (msgTyp.equals(MessageType.bulk)){
						String[] abs = msgArr[4].split("&");
						absentees += abs.length;
					}
					if (msgTyp.equals(MessageType.change)){
						String[] typeArr = msgArr[5].split("=");
						String  tag  = typeArr[0];
						String	type = typeArr[1];
						if(tag.equals("s")){
							if(type.equals("0")){
								newRegis++;
							}
							if(type.equals("1")){
								String[] qArr 	= msgArr[14].split("=");
								String qTag     = qArr[0];
								String question = qArr[1]; 
								if (qTag.equals("ch")){
									if(question.equals("0")){
										change0++;
									}
									else if(question.equals("1")){
										change1++;
									}
									else if(question.equals("2")){
										change2++;
									}
									else if(question.equals("3")){
										change3++;
									}
									else if(question.equals("4")){
										change4++;
									}
									else if(question.equals("5")){
										change5++;
									}
									else if(question.equals("6")){
										change6++;
									}
									else if(question.equals("7")){
										change7++;
									}
									else if(question.equals("8")){
										change8++;
									}
									else {
										continue;
									}
								}
							}
						}
					}
				}
				rs.close();
				connection.close();
				String write = "\n*******************************************************\n";
				write += "REPORT FROM PHONE : "+ phone + "\n";
				write += "DATE : "+ date + "\n\n";
				write += "Absentees: " + absentees+ "\n\n";
				write += "CHANGES"+ "\n\n";
				write += String.format("%-50s %3d","Transfer from Pregnant to Lactating: ", change0)+ "\n";
				write += String.format("%-50s %3d","Transfer from Lactating to Prevention:" , change1)+ "\n";
				write += String.format("%-50s %3d","Transfer due to Location Change:",change2)+ "\n";
				write += String.format("%-50s %3d","Abortion:",change3)+ "\n";
				write += String.format("%-50s %3d","Changed Beneficiary Data:",change4)+ "\n";
				write += String.format("%-50s %3d","Deceased:",change5)+ "\n";
				write += String.format("%-50s %3d","Fraud:",change6)+ "\n";
				write += String.format("%-50s %3d","Completed Program:",change7)+ "\n";
				write += String.format("%-50s %3d","Other:",change8)+ "\n\n";
				write += "NEW REGISTRATION"+ "\n";
				write += "New Registrations: "+newRegis+ "\n";
				write += "\n*******************************************************\n";
				auditLog(write);
			} catch (SQLException e) {
				log(e.getMessage());

			}




		}
		else{
			log("Wrong input format! Usage: DbAudit.jar <db_name> <ph_number> <date>" );			
		}



	}

}
