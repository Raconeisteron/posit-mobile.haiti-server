package haiti.server.gui;

import haiti.server.datamodel.Beneficiary;
import haiti.server.datamodel.DAO;

import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

public class FormPanel extends JPanel implements ActionListener{
	public void fillInForm(Object o, DAO reader) {}
	protected void paintComponent(Graphics g) { super.paintComponent(g); }
	protected void setUpDataEntryPanel() {}
	public void actionPerformed(ActionEvent e) {}
}
