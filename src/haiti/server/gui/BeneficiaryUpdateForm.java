/*
 * File: DataEntryFrom.java
 * 
 * Copyright (C) 2011 The Humanitarian FOSS Project (http://hfoss.org).
 * 
 * This file is part of POSIT-Haiti Server for ACDI/VOCA.
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
 */

package haiti.server.gui;

import haiti.server.datamodel.AttributeManager;
import haiti.server.datamodel.Beneficiary;
import haiti.server.datamodel.LocaleManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class BeneficiaryUpdateForm extends JPanel implements ActionListener {

	public static final String BUTTON_SAVE="Save";
	public static final String BUTTON_DB_MGR = "DbMgr";
		
	public static final String BORDER_BENEFICIARY_UPDATE = "BeneficiaryUpdateForm";
	public static final String BORDER_BENEFICIARY_INFO = "BeneficiaryInfo";
	public static final String BORDER_UPDATE_INFO = "UpdateInfo";
	public static final String BORDER_CONTROLS = "Controls";

	private DataEntryGUI mGui;

	private JButton 
		toDbMgrButton,
		toDbButton;
	private JPanel 
		welcomePanel, 
		buttonPanel, 
		formPanel, 
		benifPanel, 
		updatePanel;
	private JTextField 
		dossierJText,
		firstNameJText, 
		lastNameJText, 
		dobJText,
		monthsJText,
		transferredWhyJText,
		modificationsWhyJText,
		suspendWhyJText;

	private ButtonGroup  
		infantGroup,
		presentGroup,
		transferredGroup,
		modificationsGroup,
		suspendGroup;

	private JRadioButton 
		radioInfantMal,
		radioInfantPrev,
		radioMotherExp,
		radioMotherNurs,
		radioPresentYes,
		radioPresentNo,
		radioTransferredYes,
		radioTransferredNo,
		radioModificationsYes,
		radioModificationsNo,
		radioSuspendYes,
		radioSuspendNo;

	private JLabel 
		dossierLabel,
		firstNameLabel,
		lastNameLabel,
		dobLabel,
		monthsLabel,
		beneficiaryLabel,
		presentLabel,
		transferredLabel,
		modificationsLabel,
		suspendLabel,
		transferredWhyLabel,
		modificationsWhyLabel,
		suspendWhyLabel;


	public BeneficiaryUpdateForm(DataEntryGUI gui) {
		mGui = gui;
		setUpDataEntryPanel();
	}
	
	
	/**
	 * Called from GUI to fill in data in the form.
	 * @param beneficiary 
	 */
	public void fillInForm(Beneficiary beneficiary, SmsReader reader) {
		this.firstNameJText.setText(beneficiary.getFirstName());
		this.lastNameJText.setText(beneficiary.getLastName());
		
		if (beneficiary.getMotherCategory().equals(Beneficiary.MotherCategory.EXPECTING)) 
			this.radioMotherExp.setSelected(true);
		else 
			this.radioMotherNurs.setSelected(true); 

		if (beneficiary.getInfantCategory().equals(Beneficiary.InfantCategory.MALNOURISHED)) 
			this.radioInfantMal.setSelected(true);
		else 
			this.radioInfantPrev.setSelected(true);
		

	}
	
	/**
	 * Helper method to add an array of items ot a Combo Box.
	 * @param combo
	 * @param arr
	 */
	private void addItemsToComboBox (JComboBox combo, String arr[]) {
		for (int k = 0; k < arr.length; k++) 
			combo.addItem(arr[k]);
	}
	
	/**
	 * Called automatically whenever the Panel is repainted this method
	 * sets all locale dependent strings in the Panel.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		toDbButton.setText(LocaleManager.resources.getString(BUTTON_SAVE));
		this.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_BENEFICIARY_UPDATE)));
		buttonPanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_CONTROLS)));
		benifPanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_BENEFICIARY_INFO)));
		
		dossierLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_DOSSIER));
		firstNameLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_FIRST_NAME));
		lastNameLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_LAST_NAME));
		dobLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_DOB));
		beneficiaryLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_BENEFICIARY));
		radioInfantMal.setText(LocaleManager.resources.getString(AttributeManager.LONG_INFANT_MAL));
		radioInfantPrev.setText(LocaleManager.resources.getString(AttributeManager.LONG_INFANT_PREVENTION));
		radioMotherExp.setText(LocaleManager.resources.getString(AttributeManager.LONG_MOTHER_EXPECTING));
		radioMotherNurs.setText(LocaleManager.resources.getString(AttributeManager.LONG_MOTHER_NURSING));
		monthsLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_MONTHS));
	
		updatePanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_UPDATE_INFO)));
		presentLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_PRESENT));
		radioPresentYes.setText(LocaleManager.resources.getString(AttributeManager.BUTTON_YES));
		radioPresentNo.setText(LocaleManager.resources.getString(AttributeManager.BUTTON_NO));
		
		transferredLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_TRANSFERRED));
		radioTransferredYes.setText(LocaleManager.resources.getString(AttributeManager.BUTTON_YES));
		radioTransferredNo.setText(LocaleManager.resources.getString(AttributeManager.BUTTON_NO));
		transferredWhyLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_WHY));
		
		modificationsLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_MODIFICATIONS));
		radioModificationsYes.setText(LocaleManager.resources.getString(AttributeManager.BUTTON_YES));
		radioModificationsNo.setText(LocaleManager.resources.getString(AttributeManager.BUTTON_NO));
		modificationsWhyLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_WHY));
		
		suspendLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_SUSPEND));
		radioSuspendYes.setText(LocaleManager.resources.getString(AttributeManager.BUTTON_YES));
		radioSuspendNo.setText(LocaleManager.resources.getString(AttributeManager.BUTTON_NO));
		suspendWhyLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_WHY));

	}
	
	/**
	 * Sets up the panel used for Beneficiary registration.
	 * @return
	 */
	private JPanel setUpDataEntryPanel() {
		//Form panel
		//formPanel = new JPanel();
		this.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_BENEFICIARY_UPDATE)));
		this.setLayout(new BorderLayout());
		this.setBackground(Color.WHITE); 
		
		//Beneficiary Information Panel
		benifPanel = new JPanel();
		benifPanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_BENEFICIARY_INFO)));
		benifPanel.setLayout(new GridBagLayout());
		benifPanel.setBackground(Color.WHITE); 
		GridBagConstraints c = new GridBagConstraints();
		dossierJText = new JTextField();
		dossierJText.setColumns(15);
		firstNameJText = new JTextField();
		firstNameJText.setColumns(15);
		lastNameJText = new JTextField();
		lastNameJText.setColumns(15);
		dobJText = new JTextField();
		dobJText.setColumns(15);
		monthsJText = new JTextField();
		monthsJText.setColumns(15);
		c.gridy=0;
		c.gridx=0;
		c.insets=new Insets(10,5,4,2);
		c.anchor = GridBagConstraints.NORTHEAST;
		
		dossierLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_DOSSIER));
		benifPanel.add(dossierLabel,c);
		c.gridy=0;
		c.gridx=1;
		benifPanel.add(dossierJText,c);
		
		firstNameLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_FIRST_NAME));
		c.gridy=1;
		c.gridx=0;
		benifPanel.add(firstNameLabel,c);
		c.gridy=1;
		c.gridx=1;
		benifPanel.add(firstNameJText,c);
		c.gridy=1;
		c.gridx=2;
		lastNameLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_LAST_NAME));
		benifPanel.add(lastNameLabel,c);
		c.gridy=1;
		c.gridx=3;
		benifPanel.add(lastNameJText,c);

		dobLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_DOB));
		c.gridy = 2;
		c.gridx=0;
		benifPanel.add(dobLabel,c);
		c.gridy = 2;
		c.gridx=1;
		benifPanel.add(dobJText,c);
		
		//Beneficiary radio buttons
		c.gridy = 3;
		c.gridx=0;
		beneficiaryLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_BENEFICIARY));
		benifPanel.add(beneficiaryLabel,c);
		infantGroup = new ButtonGroup();
		radioInfantMal = new JRadioButton(LocaleManager.resources.getString(AttributeManager.LONG_INFANT_MAL),false);
		radioInfantPrev = new JRadioButton(LocaleManager.resources.getString(AttributeManager.LONG_INFANT_PREVENTION),false);
		radioMotherExp = new JRadioButton(LocaleManager.resources.getString(AttributeManager.LONG_MOTHER_EXPECTING),false);
		radioMotherNurs = new JRadioButton(LocaleManager.resources.getString(AttributeManager.LONG_MOTHER_NURSING),false);
		infantGroup.add(radioInfantMal);
		infantGroup.add(radioInfantPrev);
		infantGroup.add(radioMotherExp);
		infantGroup.add(radioMotherNurs);
		c.gridy=3;
		c.gridx=1;
		benifPanel.add(radioInfantMal,c);
		c.gridy=3;
		c.gridx=2;
		benifPanel.add(radioInfantPrev,c);
		c.gridy=3;
		c.gridx=3;
		benifPanel.add(radioMotherExp,c);
		c.gridy=3;
		c.gridx=4;
		benifPanel.add(radioMotherNurs,c);
	
		monthsLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_MONTHS));
		c.gridy=4;
		c.gridx=0;
		benifPanel.add(monthsLabel,c);
		c.gridy=4;
		c.gridx=1;
		benifPanel.add(monthsJText,c);
		
		
		//Update panel
		updatePanel = new JPanel();
		updatePanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_UPDATE_INFO)));
		updatePanel.setLayout(new GridBagLayout());
		updatePanel.setBackground(Color.WHITE); 

		c.gridy=0;
		c.gridx=0;
		presentLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_PRESENT));
		updatePanel.add(presentLabel,c);
		
		presentGroup = new ButtonGroup();
		radioPresentYes = new JRadioButton(LocaleManager.resources.getString(AttributeManager.BUTTON_YES),false);
		radioPresentNo = new JRadioButton(LocaleManager.resources.getString(AttributeManager.BUTTON_NO),false);
		presentGroup.add(radioPresentYes);
		presentGroup.add(radioPresentNo);	
		c.gridy=0;
		c.gridx=1;
		updatePanel.add(radioPresentYes,c);
		c.gridy=0;
		c.gridx=2;
		updatePanel.add(radioPresentNo,c);
		
		c.gridy=1;
		c.gridx=0;
		transferredLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_TRANSFERRED));
		updatePanel.add(transferredLabel,c);
		transferredGroup = new ButtonGroup();
		radioTransferredYes = new JRadioButton(LocaleManager.resources.getString(AttributeManager.BUTTON_YES),false);
		radioTransferredNo = new JRadioButton(LocaleManager.resources.getString(AttributeManager.BUTTON_NO),false);
		transferredGroup.add(radioTransferredYes);
		transferredGroup.add(radioTransferredNo);	
		c.gridy=1;
		c.gridx=1;
		updatePanel.add(radioTransferredYes,c);
		c.gridy=1;
		c.gridx=2;
		updatePanel.add(radioTransferredNo,c);
		c.gridy=1;
		c.gridx=3;
		c.insets=new Insets(10,25,4,2);
		transferredWhyLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_WHY));
		updatePanel.add(transferredWhyLabel,c);
		c.gridy=1;
		c.gridx=4;
		c.insets=new Insets(10,5,4,2);
		transferredWhyJText = new JTextField();
		transferredWhyJText.setColumns(15);
		updatePanel.add(transferredWhyJText,c);

		c.gridy=2;
		c.gridx=0;
		modificationsLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_MODIFICATIONS));
		updatePanel.add(modificationsLabel,c);
		modificationsGroup = new ButtonGroup();
		radioModificationsYes = new JRadioButton(LocaleManager.resources.getString(AttributeManager.BUTTON_YES),false);
		radioModificationsNo = new JRadioButton(LocaleManager.resources.getString(AttributeManager.BUTTON_NO),false);
		modificationsGroup.add(radioModificationsYes);
		modificationsGroup.add(radioModificationsNo);	
		c.gridy=2;
		c.gridx=1;
		updatePanel.add(radioModificationsYes,c);
		c.gridy=2;
		c.gridx=2;
		updatePanel.add(radioModificationsNo,c);
		c.gridy=2;
		c.gridx=3;
		c.insets=new Insets(10,25,4,2);
		modificationsWhyLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_WHY));
		updatePanel.add(modificationsWhyLabel,c);
		c.gridy=2;
		c.gridx=4;
		c.insets=new Insets(10,5,4,2);
		modificationsWhyJText = new JTextField();
		modificationsWhyJText.setColumns(15);
		updatePanel.add(modificationsWhyJText,c);
		
		c.gridy=3;
		c.gridx=0;
		suspendLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_SUSPEND));
		updatePanel.add(suspendLabel,c);
		suspendGroup = new ButtonGroup();
		radioSuspendYes = new JRadioButton(LocaleManager.resources.getString(AttributeManager.BUTTON_YES),false);
		radioSuspendNo = new JRadioButton(LocaleManager.resources.getString(AttributeManager.BUTTON_NO),false);
		suspendGroup.add(radioSuspendYes);
		suspendGroup.add(radioSuspendNo);	
		c.gridy=3;
		c.gridx=1;
		updatePanel.add(radioSuspendYes,c);
		c.gridy=3;
		c.gridx=2;
		updatePanel.add(radioSuspendNo,c);
		c.gridy=3;
		c.gridx=3;
		c.insets=new Insets(10,25,4,2);
		suspendWhyLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_WHY));
		updatePanel.add(suspendWhyLabel,c);
		c.gridy=3;
		c.gridx=4;
		c.insets=new Insets(10,5,4,2);
		suspendWhyJText = new JTextField();
		suspendWhyJText.setColumns(15);
		updatePanel.add(suspendWhyJText,c);
			
		
		//Control panel
		buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_CONTROLS)));
		buttonPanel.setBackground(Color.WHITE);
		toDbButton = new JButton(LocaleManager.resources.getString(BUTTON_SAVE));
		this.add(toDbButton);
		toDbMgrButton = new JButton(LocaleManager.resources.getString(BUTTON_DB_MGR));
		toDbButton.addActionListener(this);
		toDbMgrButton.addActionListener(this);
		this.add(benifPanel,"North");
		this.add(updatePanel,"Center");
		this.add(buttonPanel,"South");
		buttonPanel.add(toDbButton);
		buttonPanel.add(toDbMgrButton);

		return formPanel;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton){
		    JButton button = (JButton)e.getSource();
		    if (button.equals(toDbButton)) {
		    	mGui.postMessageToTBS();
//				mBeneficiary.setStatus(DB_STATUS_PROCESSED); // sets the status of the current Beneficiary item to processed
//		    	mReader.updateMessage(mBeneficiary);
		    }
		    else if (button.equals(toDbMgrButton)) {
		    	mGui.forwardMessageToDbMgr();
//				mBeneficiary.setStatus(DB_STATUS_PENDING); // sets the status of the Beneficiary item to pending
//		    	mReader.updateMessage(mBeneficiary);
//		    	//TODO: This should write the message to a file for Db Manager
//		    	System.out.println("Sent to the database manager");
		    }
		}
		
	}

	


}