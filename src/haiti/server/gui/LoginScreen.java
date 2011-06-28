package haiti.server.gui;

import haiti.server.datamodel.AttributeManager;

import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.*;

public class LoginScreen extends JFrame implements WindowListener, ActionListener {
	private JButton SUBMIT, ChangePw, OK;
	private JPanel panel;
	private JLabel label1,label2, npwLabel, space;
	private final JTextField  text1,text2, npw;
	private DataEntryGUI mGui;
	private String dbName;
	
	public static final String DB_USER_TABLE = "user";
	
	public LoginScreen(DataEntryGUI gui) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mGui = gui;
		npw = new JPasswordField(15);
		space = new JLabel();
		
		label1 = new JLabel();
		label1.setText("Username:");
		text1 = new JTextField(15);

		label2 = new JLabel();
		label2.setText("Password:");
		text2 = new JPasswordField(15);
		
		SUBMIT=new JButton("SUBMIT");
		ChangePw = new JButton("Change Password");

		panel=new JPanel(new GridLayout(0,2));
		panel.add(label1);
		panel.add(text1);
		panel.add(label2);
		panel.add(text2);
		panel.add(ChangePw);
		panel.add(SUBMIT);
		add(panel,BorderLayout.CENTER);
		SUBMIT.addActionListener(this);
		ChangePw.addActionListener(this);
		setTitle("LOGIN FORM");

		this.setSize(300, 150);
		this.setLocationRelativeTo(mGui);
	}

	public void actionPerformed(ActionEvent ae)
	{
		if (ae.getSource() instanceof JButton){
		    JButton button = (JButton)ae.getSource();
		    if (button.equals(SUBMIT)) {
				String value1=text1.getText();
				String value2=text2.getText();
				String pw = null;
				int role = -1;
				JFileChooser fd = new JFileChooser();
				int result = fd.showOpenDialog(this);
				if (result == JFileChooser.CANCEL_OPTION) 
					return;
				dbName = fd.getSelectedFile().toString();
				mGui.setmMessagesFileOrDbName(dbName);
				
				try {
					Connection connection = connectDb(dbName);			
					Statement statement = connection.createStatement();
		//			statement.setQueryTimeout(30);  // set timeout to 30 sec.
					
					ResultSet rs = statement.executeQuery(AttributeManager.SELECT_FROM + DB_USER_TABLE
							+ AttributeManager.WHERE + "username='" + value1 + AttributeManager.SINGLE_QUOTE);
					pw = rs.getString("password");
					role = Integer.parseInt(rs.getString("role"));
					if(connection != null)
						connection.close();
					} catch(SQLException e) {
					// if the error message is "out of memory", 
					// it probably means no database file is found
					e.printStackTrace();
				}
				if (value2.equals(pw)) {
					if (role == 1) {
						mGui.isUser = true;
					}
					else if (role == 0) {
						mGui.isAdmin = true;				
					}
					this.setVisible(false);
					mGui.setVisible(true);
					mGui.mMenuManager = new Menus(mGui);
					mGui.mMenuManager.createMenuBar();
					mGui.setMenuBar(Menus.getMenuBar());
				}
				else{
		//			System.out.println("enter the valid username and password");
					JOptionPane.showMessageDialog(this,"Incorrect login or password",
							"Error",JOptionPane.ERROR_MESSAGE);
				}
		    }
		    else if (button.equals(ChangePw)) {
		    	OK = new JButton("OK");
		    	npwLabel = new JLabel("New Password:");
		    	remove(panel);
		    	panel.remove(SUBMIT);
		    	panel.remove(ChangePw);
		    	panel.add(npwLabel);
		    	panel.add(npw);
		    	panel.add(space);
		    	panel.add(OK);
		    	panel.repaint();
		    	add(panel);
				OK.addActionListener(this);
		    	pack();
		    	repaint();
		    }
		    else if (button.equals(OK)) {
				String value1=text1.getText();
				String value2=text2.getText();
				String pw = null;
				int role = -1;
				JFileChooser fd = new JFileChooser();
				int result = fd.showOpenDialog(this);
				if (result == JFileChooser.CANCEL_OPTION) 
					return;
				dbName = fd.getSelectedFile().toString();
				
				try {
					Connection connection = connectDb(dbName);			
					Statement statement = connection.createStatement();
		//			statement.setQueryTimeout(30);  // set timeout to 30 sec.
					
					ResultSet rs = statement.executeQuery(AttributeManager.SELECT_FROM + DB_USER_TABLE + AttributeManager.WHERE + "username='" + value1 + AttributeManager.SINGLE_QUOTE);
					pw = rs.getString("password");
					role = Integer.parseInt(rs.getString("role"));
					if (role == 0) {
						JOptionPane.showMessageDialog(this,"Admin Password cannot be changed",
								"Error",JOptionPane.ERROR_MESSAGE);						
					}
					else {
						if (value2.equals(pw)) {
							statement.executeUpdate("Update user set password='" + npw.getText() + "' where username='" + value1 + "'");
						}
						else {
							//			System.out.println("enter the valid username and password");
										JOptionPane.showMessageDialog(this,"Incorrect login or password",
												"Error",JOptionPane.ERROR_MESSAGE);
						}
					}
					if(connection != null)
						connection.close();
					} catch(SQLException e) {
					// if the error message is "out of memory", 
					// it probably means no database file is found
					e.printStackTrace();
				}
		    	remove(panel);
		    	text1.setText("");
		    	text2.setText("");
		    	npw.setText("");
		    	panel.remove(npw);
		    	panel.remove(npwLabel);
		    	panel.remove(space);
		    	panel.remove(OK);
		    	panel.add(ChangePw);
		    	panel.add(SUBMIT);
		    	panel.repaint();
		    	add(panel);
		    	pack();
		    	repaint();
		    }
		}
	}
	public void windowClosing(WindowEvent e) {  quit();  }
	public void windowActivated(WindowEvent e) { 	}
	public void windowClosed(WindowEvent e) { mGui.dispose(); }
	public void windowDeactivated(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}

	public void quit() {  
		setVisible(false);
		mGui.dispose();
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