package haiti.server.gui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class LoginScreen extends JFrame implements WindowListener, ActionListener {
	private JButton SUBMIT;
	private JPanel panel;
	private JLabel label1,label2;
	private final JTextField  text1,text2;
	private DataEntryGUI mGui;
	
	public LoginScreen(DataEntryGUI gui) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mGui = gui;
		
		label1 = new JLabel();
		label1.setText("Username:");
		text1 = new JTextField(15);

		label2 = new JLabel();
		label2.setText("Password:");
		text2 = new JPasswordField(15);

		SUBMIT=new JButton("SUBMIT");

		panel=new JPanel(new GridLayout(3,1));
		panel.add(label1);
		panel.add(text1);
		panel.add(label2);
		panel.add(text2);
		panel.add(new JLabel());
		panel.add(SUBMIT);
		add(panel,BorderLayout.CENTER);
		SUBMIT.addActionListener(this);
		setTitle("LOGIN FORM");

		this.setSize(250, 125);
		this.setLocationRelativeTo(mGui);
	}

	public void actionPerformed(ActionEvent ae)
	{
		String value1=text1.getText();
		String value2=text2.getText();
		if (value1.equals("b") && value2.equals("b")) {
			mGui.isUser = true;
			this.setVisible(false);
			mGui.setVisible(true);
			mGui.mMenuManager = new Menus(mGui);
			mGui.mMenuManager.createMenuBar();
			mGui.setMenuBar(Menus.getMenuBar());
		}
		else if (value1.equals("r") && value2.equals("a")) {
			mGui.isAdmin = true;
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
}