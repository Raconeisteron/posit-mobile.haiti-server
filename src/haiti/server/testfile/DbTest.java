package haiti.server.testfile;


import java.awt.FileDialog;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DbTest
{
	public static final String DB_MESSAGE_TABLE = "message_log";
	public static final String DB_MESSAGE_ID = "id";
	public static final String DB_MESSAGE_TIME = "timestamp";
	public static final String DB_MESSAGE_COLUMN = "message_text";
	public static final String DB_MESSAGE_STATUS = "processed";
	
	public static void main(String[] args) throws ClassNotFoundException
	{
		// load the sqlite-JDBC driver using the current class loader
		Class.forName("org.sqlite.JDBC");

		Connection connection = null;
		try
		{
			String path = "jdbc:sqlite:" + System.getProperty("user.dir").toString() + "/db/haiti.db";
			System.out.println("PATH=" + path);
			
			
			// create a database connection
			connection = DriverManager.getConnection(path);
			//connection = DriverManager.getConnection("jdbc:sqlite:db/haiti.db");
			connection = DriverManager.getConnection("jdbc:sqlite:/Users/rmorelli/workspace_hg_posit/haiti-server-initial/db/haiti.db");
			//connection = DriverManager.getConnection("jdbc:sqlite:/Users/rmorelli/workspace_hg_posit/haiti-server-initial/db/haiti.db");

			Statement statement = connection.createStatement();
			System.out.println(statement.toString());
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			
			ResultSet rs = statement.executeQuery("select * from message_log");
			rs.next();
			while(!rs.isAfterLast()) {
				// Write the result set
				//System.out.println("msg = " + rs.getString("message_text"));
				String msg = rs.getString(DB_MESSAGE_ID) + "|" 
					+ rs.getString(DB_MESSAGE_STATUS) + "|"
					+ rs.getString(DB_MESSAGE_TIME) + "|" 
					+ rs.getString(DB_MESSAGE_COLUMN);
				System.out.println(msg);
				rs.next();
			}
			System.out.println("Done");
		}
		catch(SQLException e)
		{
			// if the error message is "out of memory", 
			// it probably means no database file is found
			System.err.println(e.getMessage());
		}
		finally
		{
			try
			{
				if(connection != null)
					connection.close();
			}
			catch(SQLException e)
			{
				// connection close failed.
				System.err.println(e);
			}
		}
	}
}