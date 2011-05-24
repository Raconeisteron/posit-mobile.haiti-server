package haiti.server.testfile;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class DbTest
{
  public static void main(String[] args) throws ClassNotFoundException
  {
    // load the sqlite-JDBC driver using the current class loader
    Class.forName("org.sqlite.JDBC");
    
    Connection connection = null;
    try
    {
      // create a database connection
      connection = DriverManager.getConnection("jdbc:sqlite:sample.db");
      Statement statement = connection.createStatement();
      statement.setQueryTimeout(30);  // set timeout to 30 sec.
      
      statement.executeUpdate("drop table if exists beneficiary");
      statement.executeUpdate("create table beneficiary (sms string)");
      statement.executeUpdate("insert into  beneficiary values('f=tom&l=jones&a=11 Main Street&c=Hartford&z=06106')");
      statement.executeUpdate("insert into beneficiary values('f=sarah&l=smith&a=12 Main Street&c=Hartford&z=06106')");
      ResultSet rs = statement.executeQuery("select * from beneficiary");
      while(rs.next())
      {
        // read the result set
        System.out.println("sms = " + rs.getString("sms"));
        
      }
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