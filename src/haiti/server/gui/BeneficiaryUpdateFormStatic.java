package haiti.server.gui;

/*
 * File: BeneficiaryUpdateFormStatic.java
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

import haiti.server.datamodel.AttributeManager;
import haiti.server.datamodel.DAO;
import haiti.server.datamodel.LocaleManager;
import haiti.server.datamodel.Update;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class BeneficiaryUpdateFormStatic extends FormPanel implements ActionListener {

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
//		welcomePanel, 
		buttonPanel, 
//		formPanel, 
		benifPanel, 
		updatePanel;
	
	private JLabel
		dossier,
		firstName, 
		lastName,
		dob,
//		months,
		beneficiaryCategory,
		present,
		transferredWhy,
		modificationsWhy,
		suspendWhy,
		change,
		changeType;

//	private ButtonGroup  
//		infantGroup,
//		presentGroup,
//		transferredGroup,
//		modificationsGroup,
//		suspendGroup;

//	private JRadioButton 
//		radioInfantMal,
//		radioInfantPrev,
//		radioMotherExp,
//		radioMotherNurs,
//		radioPresentYes,
//		radioPresentNo,
//		radioTransferredYes,
//		radioTransferredNo,
//		radioModificationsYes,
//		radioModificationsNo,
//		radioSuspendYes,
//		radioSuspendNo;

	private JLabel 
		dossierLabel,
		firstNameLabel,
		lastNameLabel,
		dobLabel,
//		monthsLabel,
		beneficiaryLabel,
		presentLabel,
		transferredLabel,
		modificationsLabel,
		suspendLabel,
		transferredWhyLabel,
		modificationsWhyLabel,
		suspendWhyLabel,
		changeLabel,
		changeTypeLabel;


	public BeneficiaryUpdateFormStatic(DataEntryGUI gui) {
		mGui = gui;
		setUpDataEntryPanel();
	}
	
	
	/**
	 * Called from GUI to fill in data in the form.
	 * @param update 
	 */
	public void fillInForm(Object o, DAO reader) {
		Update update = (Update)o;
		this.dossier.setText(update.getDossier());
		
		this.firstName.setText(update.getFirstName());
		this.lastName.setText(update.getLastName());
		
		beneficiaryCategory.setText(update.getBeneficiaryCategory().name());
		dob.setText(update.getDob());
		present.setText(update.getPresent().name());
		change.setText(update.getChange().name());
		changeType.setText(update.getChangeType());
		
//		if (update.getBeneficiaryCategory().equals(AttributeManager.BeneficiaryCategory.EXPECTING)) 
//			this.radioMotherExp.setSelected(true);
//		if (update.getBeneficiaryCategory().equals(AttributeManager.BeneficiaryCategory.NURSING)) 
//			this.radioMotherNurs.setSelected(true); 
//		if (update.getBeneficiaryCategory().equals(AttributeManager.BeneficiaryCategory.MALNOURISHED)) 
//			this.radioInfantMal.setSelected(true);
//		if (update.getBeneficiaryCategory().equals(AttributeManager.BeneficiaryCategory.PREVENTION)) 
//			this.radioInfantPrev.setSelected(true);
	}
	
	/**
	 * Helper method to add an array of items ot a Combo Box.
	 * @param combo
	 * @param arr
	 */
//	private void addItemsToComboBox (JComboBox combo, String arr[]) {
//		for (int k = 0; k < arr.length; k++) 
//			combo.addItem(arr[k]);
//	}
	
	/**
	 * Called automatically whenever the Panel is repainted this method
	 * sets all locale dependent strings in the Panel.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		toDbButton.setText(LocaleManager.resources.getString(BUTTON_SAVE));
		this.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_BENEFICIARY_UPDATE)));
//		buttonPanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_CONTROLS)));
		benifPanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_BENEFICIARY_INFO)));
		
		dossierLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_DOSSIER));
		firstNameLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_FIRST_NAME));
		lastNameLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_LAST_NAME));
		dobLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_DOB));
		beneficiaryLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_BENEFICIARY_CATEGORY));
//		radioInfantMal.setText(LocaleManager.resources.getString(AttributeManager.LONG_INFANT_MAL));
//		radioInfantPrev.setText(LocaleManager.resources.getString(AttributeManager.LONG_INFANT_PREVENTION));
//		radioMotherExp.setText(LocaleManager.resources.getString(AttributeManager.LONG_MOTHER_EXPECTING));
//		radioMotherNurs.setText(LocaleManager.resources.getString(AttributeManager.LONG_MOTHER_NURSING));
//		monthsLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_MONTHS));
	
		updatePanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_UPDATE_INFO)));
		presentLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_PRESENT));
//		radioPresentYes.setText(LocaleManager.resources.getString(AttributeManager.FORM_YES));
//		radioPresentNo.setText(LocaleManager.resources.getString(AttributeManager.FORM_NO));
		
//		transferredLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_TRANSFERRED));
//		radioTransferredYes.setText(LocaleManager.resources.getString(AttributeManager.FORM_YES));
//		radioTransferredNo.setText(LocaleManager.resources.getString(AttributeManager.FORM_NO));
//		transferredWhyLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_WHY));
		
//		modificationsLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_MODIFICATIONS));
//		radioModificationsYes.setText(LocaleManager.resources.getString(AttributeManager.FORM_YES));
//		radioModificationsNo.setText(LocaleManager.resources.getString(AttributeManager.FORM_NO));
//		modificationsWhyLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_WHY));
		
//		suspendLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_SUSPEND));
//		radioSuspendYes.setText(LocaleManager.resources.getString(AttributeManager.FORM_YES));
//		radioSuspendNo.setText(LocaleManager.resources.getString(AttributeManager.FORM_NO));
//		suspendWhyLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_WHY));

		changeLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_CHANGE));
		changeTypeLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_CHANGE_TYPE));
		
	}
	
	/**
	 * Sets up the panel used for Beneficiary registration.
	 * @return
	 */
	protected void setUpDataEntryPanel() {
		//Form panel
		//formPanel = new JPanel();
		this.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_BENEFICIARY_UPDATE)));
		this.setLayout(new GridLayout(0,1));
		//this.setBackground(Color.WHITE); 
		
		//Beneficiary Information Panel
		benifPanel = new JPanel();
		benifPanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_BENEFICIARY_INFO)));
		benifPanel.setLayout(new GridLayout(0,4));
		//benifPanel.setBackground(Color.WHITE); 
//		GridBagConstraints c = new GridBagConstraints();
		dossier = new JLabel();
		firstName = new JLabel();
//		firstName.setColumns(15);
		lastName = new JLabel();
//		lastName.setColumns(15);
		dob = new JLabel();
//		dobJText.setColumns(15);
//		months = new JLabel();
//		months.setColumns(15);
//		c.gridy=0;
//		c.gridx=0;
//		c.insets=new Insets(10,5,4,2);
//		c.anchor = GridBagConstraints.NORTHEAST;
		
		dossierLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_DOSSIER));
		benifPanel.add(dossierLabel);
//		c.gridy=0;
//		c.gridx=1;
		benifPanel.add(dossier);
		
		firstNameLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_FIRST_NAME));
//		c.gridy=1;
//		c.gridx=0;
		benifPanel.add(firstNameLabel);
//		c.gridy=1;
//		c.gridx=1;
		benifPanel.add(firstName);
//		c.gridy=1;
//		c.gridx=2;
		lastNameLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_LAST_NAME));
		benifPanel.add(lastNameLabel);
//		c.gridy=1;
//		c.gridx=3;
		benifPanel.add(lastName);

		dobLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_DOB));
//		c.gridy = 2;
//		c.gridx=0;
		benifPanel.add(dobLabel);
//		c.gridy = 2;
//		c.gridx=1;
		benifPanel.add(dob);
		
		//Beneficiary radio buttons
//		c.gridy = 3;
//		c.gridx=0;
		beneficiaryLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_BENEFICIARY_CATEGORY));
		benifPanel.add(beneficiaryLabel);
		beneficiaryCategory = new JLabel();
		benifPanel.add(beneficiaryCategory);
//		infantGroup = new ButtonGroup();
//		radioInfantMal = new JRadioButton(LocaleManager.resources.getString(AttributeManager.LONG_INFANT_MAL),false);
//		radioInfantPrev = new JRadioButton(LocaleManager.resources.getString(AttributeManager.LONG_INFANT_PREVENTION),false);
//		radioMotherExp = new JRadioButton(LocaleManager.resources.getString(AttributeManager.LONG_MOTHER_EXPECTING),false);
//		radioMotherNurs = new JRadioButton(LocaleManager.resources.getString(AttributeManager.LONG_MOTHER_NURSING),false);
//		infantGroup.add(radioInfantMal);
//		infantGroup.add(radioInfantPrev);
//		infantGroup.add(radioMotherExp);
//		infantGroup.add(radioMotherNurs);
//		c.gridy=3;
//		c.gridx=1;
//		benifPanel.add(radioInfantMal);
//		c.gridy=3;
//		c.gridx=2;
//		benifPanel.add(radioInfantPrev);
//		c.gridy=3;
//		c.gridx=3;
//		benifPanel.add(radioMotherExp);
//		c.gridy=3;
//		c.gridx=4;
//		benifPanel.add(radioMotherNurs);
	
//		monthsLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_MONTHS));
//		c.gridy=4;
//		c.gridx=0;
//		benifPanel.add(monthsLabel);
//		c.gridy=4;
//		c.gridx=1;
//		benifPanel.add(months);
		
		
		//Update panel
		updatePanel = new JPanel();
		updatePanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_UPDATE_INFO)));
		updatePanel.setLayout(new GridLayout(0,4));
		//updatePanel.setBackground(Color.WHITE); 

//		c.gridy=0;
//		c.gridx=0;
		presentLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_PRESENT));
		updatePanel.add(presentLabel);
		present = new JLabel();
		updatePanel.add(present);
		
		changeLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_CHANGE));
		changeTypeLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_CHANGE_TYPE));
		change = new JLabel();
		changeType = new JLabel();
		
		updatePanel.add(changeLabel);
		updatePanel.add(change);
		updatePanel.add(changeTypeLabel);
		updatePanel.add(changeType);
		
//		presentGroup = new ButtonGroup();
//		radioPresentYes = new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_YES),false);
//		radioPresentNo = new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_NO),false);
//		presentGroup.add(radioPresentYes);
//		presentGroup.add(radioPresentNo);	
//		c.gridy=0;
//		c.gridx=1;
//		updatePanel.add(radioPresentYes);
//		c.gridy=0;
//		c.gridx=2;
//		updatePanel.add(radioPresentNo);
		
//		c.gridy=1;
//		c.gridx=0;
//		transferredLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_TRANSFERRED));
//		updatePanel.add(transferredLabel);
//		transferredGroup = new ButtonGroup();
//		radioTransferredYes = new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_YES),false);
//		radioTransferredNo = new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_NO),false);
//		transferredGroup.add(radioTransferredYes);
//		transferredGroup.add(radioTransferredNo);	
//		c.gridy=1;
//		c.gridx=1;
//		updatePanel.add(radioTransferredYes);
//		c.gridy=1;
//		c.gridx=2;
//		updatePanel.add(radioTransferredNo);
//		c.gridy=1;
//		c.gridx=3;
//		c.insets=new Insets(10,25,4,2);
//		transferredWhyLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_WHY));
//		updatePanel.add(transferredWhyLabel);
//		c.gridy=1;
//		c.gridx=4;
//		c.insets=new Insets(10,5,4,2);
//		transferredWhy = new JLabel();
//		transferredWhy.setColumns(15);
//		updatePanel.add(transferredWhy);

//		c.gridy=2;
//		c.gridx=0;
//		modificationsLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_MODIFICATIONS));
//		updatePanel.add(modificationsLabel);
//		modificationsGroup = new ButtonGroup();
//		radioModificationsYes = new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_YES),false);
//		radioModificationsNo = new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_NO),false);
//		modificationsGroup.add(radioModificationsYes);
//		modificationsGroup.add(radioModificationsNo);	
//		c.gridy=2;
//		c.gridx=1;
//		updatePanel.add(radioModificationsYes);
//		c.gridy=2;
//		c.gridx=2;
//		updatePanel.add(radioModificationsNo);
//		c.gridy=2;
//		c.gridx=3;
//		c.insets=new Insets(10,25,4,2);
//		modificationsWhyLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_WHY));
//		updatePanel.add(modificationsWhyLabel);
//		c.gridy=2;
//		c.gridx=4;
//		c.insets=new Insets(10,5,4,2);
//		modificationsWhy = new JLabel();
//		modificationsWhy.setColumns(15);
//		updatePanel.add(modificationsWhy);
		
//		c.gridy=3;
//		c.gridx=0;
//		suspendLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_SUSPEND));
//		updatePanel.add(suspendLabel);
//		suspendGroup = new ButtonGroup();
//		radioSuspendYes = new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_YES),false);
//		radioSuspendNo = new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_NO),false);
//		suspendGroup.add(radioSuspendYes);
//		suspendGroup.add(radioSuspendNo);	
//		c.gridy=3;
//		c.gridx=1;
//		updatePanel.add(radioSuspendYes);
//		c.gridy=3;
//		c.gridx=2;
//		updatePanel.add(radioSuspendNo);
//		c.gridy=3;
//		c.gridx=3;
//		c.insets=new Insets(10,25,4,2);
//		suspendWhyLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_WHY));
//		updatePanel.add(suspendWhyLabel);
//		c.gridy=3;
//		c.gridx=4;
//		c.insets=new Insets(10,5,4,2);
//		suspendWhy = new JLabel();
//		suspendWhy.setColumns(15);
//		updatePanel.add(suspendWhy);
			
		
		//Control panel
		buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_CONTROLS)));
		//buttonPanel.setBackground(Color.WHITE);
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

//		return formPanel;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton){
		    JButton button = (JButton)e.getSource();
		    if (button.equals(toDbButton)) {
		    	mGui.postMessageToTBS(dossier.getText());
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