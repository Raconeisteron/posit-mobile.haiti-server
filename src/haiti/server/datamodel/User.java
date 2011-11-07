package haiti.server.datamodel;

import haiti.server.datamodel.AttributeManager;
import haiti.server.gui.DataEntryGUI;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;

public class User extends JFrame implements WindowListener, ActionListener {
	private JButton SUBMIT;
	private JPanel panel;
	private JLabel label1,label2, space;
	private final JTextField  text1,text2;
	private DataEntryGUI mGui;
	private String dbName;
	
	private String username = "";
	private String password = "";
	
	private static final String DB_USER_TABLE = "user";
	
	public User(DataEntryGUI gui) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mGui = gui;
		space = new JLabel();
		
		label1 = new JLabel();
		label1.setText("Username:");
		text1 = new JTextField(15);

		label2 = new JLabel();
		label2.setText("Password:");
		text2 = new JPasswordField(15);
		
		SUBMIT=new JButton("SUBMIT");

		panel=new JPanel(new GridLayout(0,2));
		panel.add(label1);
		panel.add(text1);
		panel.add(label2);
		panel.add(text2);
		panel.add(SUBMIT);
		add(panel,BorderLayout.CENTER);
		SUBMIT.addActionListener(this);
		setTitle("CREATE NEW USER");
		
		mGui.setVisible(false);
		setVisible(true);

		this.setSize(300, 150);
		this.setLocationRelativeTo(mGui);
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() instanceof JButton) {
		    JButton button = (JButton)ae.getSource();
		    if (button.equals(SUBMIT)) {
				username=text1.getText();
				password=text2.getText();
				quit();				
				mGui.setVisible(true);
				mGui.createNewUser(username, password);	
		    }
		}
	}
	public void windowClosing(WindowEvent e) {  quit();  }
	public void windowActivated(WindowEvent e) { 	}
	public void windowClosed(WindowEvent e) {   }
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}

	public void quit() {  
		setVisible(false);
		dispose();
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

}