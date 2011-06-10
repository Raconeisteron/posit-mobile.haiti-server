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

import haiti.server.datamodel.Beneficiary;
import haiti.server.datamodel.HaitiKeys;
import haiti.server.datamodel.LocaleManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

public class DataEntryForm extends JPanel implements ActionListener, KeyListener {

	public static final String BUTTON_SAVE="Save";
	public static final String BUTTON_DB_MGR = "DbMgr";
	public static final String LABEL_DOSSIER = "Dossier";

	
	public static final String BORDER_DATA_ENTRY = "DataEntryForm";
	public static final String BORDER_GEN_INFO = "GeneralInfo";
	public static final String BORDER_MCHN_INFO = "MCHNInfo";
	public static final String BORDER_CONTROLS = "Controls";

	private DataEntryGUI mGui;

	private JButton 
		toDbMgrButton,
		toDbButton;
	private JPanel 
		welcomePanel, 
		buttonPanel, 
		formPanel, 
		geninfoPanel, 
		mchnPanel;
	private JTextField 
		firstNameJText, 
		lastNameJText, 
		addressJText, 
		ageJText, 
		peopleInHouseJText,
		healthCenterJText, 
		dpJText, 
		guardianChildJText, 
		guardianWomanJText, 
		husbandJText, 
		fatherJText, 
		agriPersonJText,
		dossierJText;
	
	private ButtonGroup 
		sexGroup, 
		infantGroup, 
		agriGroup, 
		motherGroup, 
		motherVisitGroup;
	private JRadioButton 
		radioMale, 
		radioFemale,
		radioInfantMal,
		radioInfantPrev,
		radioMotherExp,
		radioMotherNurs,
		radioMotherLeaderYes, 
		radioMotherLeaderNo, 
		radioVisitYes, 
		radioVisitNo, 
		radioAgriYes, 
		radioAgriNo;

	private JComboBox 
		communeBox, 
		communeSectionBox;
	private JLabel 
		firstNameLabel,
		lastNameLabel,
		communeLabel,
		communeSectionLabel,
		addressLabel,
		ageLabel,
		sexLabel,
		beneficiaryLabel,
		nInHouseLabel,
		healthCenterLabel,
		distributionLabel,
		childLabel,
		womanLabel,
		husbandLabel,
		fatherLabel,
		leaderLabel,
		visitLabel,
		agr1Label,
		agr2Label,
		giveNameLabel,
		dossierLabel;


	public DataEntryForm(DataEntryGUI gui) {
		mGui = gui;
		setUpDataEntryPanel();
	}
	

	/**
	 * Fills in the Data Entry Form.
	 * @param beneficiary 
	 */
	public void fillInForm(Beneficiary beneficiary, SmsReader reader) {
		this.firstNameJText.setText(beneficiary.getFirstName());
		this.lastNameJText.setText(beneficiary.getLastName());
		
		addItemsToComboBox(communeBox, reader.getCommune());
		this.communeBox.setSelectedItem(beneficiary.getCommune());
		addItemsToComboBox(communeSectionBox, reader.getCommuneSection(beneficiary.getCommune()));
	//	this.communeBox.setText(beneficiary.getCommune());
	//	this.communeSectionBox.setText(beneficiary.getCommuneSection());
		this.addressJText.setText(beneficiary.getAddress());
		this.ageJText.setText(Integer.toString(beneficiary.getAge()));
		this.peopleInHouseJText.setText(Integer.toString(beneficiary.getNumberInHome()));
		
		if (beneficiary.getMotherCategory().equals(Beneficiary.MotherCategory.EXPECTING)) 
			this.radioMotherExp.setSelected(true);
		else 
			this.radioMotherNurs.setSelected(true); 

		if (beneficiary.getInfantCategory().equals(Beneficiary.InfantCategory.MALNOURISHED)) 
			this.radioInfantMal.setSelected(true);
		else 
			this.radioInfantPrev.setSelected(true);
		
		if (beneficiary.getSex().equals(Beneficiary.Sex.MALE)) {
			this.radioMale.setSelected(true);
		} else {
			this.radioFemale.setSelected(true);
		}
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
		this.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_DATA_ENTRY)));
		buttonPanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_CONTROLS)));
		geninfoPanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_GEN_INFO)));
		firstNameLabel.setText(LocaleManager.resources.getString(HaitiKeys.FORM_FIRST_NAME));
		lastNameLabel.setText(LocaleManager.resources.getString(HaitiKeys.FORM_LAST_NAME));
		communeLabel.setText(LocaleManager.resources.getString(HaitiKeys.FORM_COMMUNE));
		communeSectionLabel.setText(LocaleManager.resources.getString(HaitiKeys.FORM_SECTION));
		addressLabel.setText(LocaleManager.resources.getString(HaitiKeys.FORM_ADDRESS));
		ageLabel.setText(LocaleManager.resources.getString(HaitiKeys.FORM_AGE));
		sexLabel.setText(LocaleManager.resources.getString(HaitiKeys.FORM_SEX));
		radioMale.setText(LocaleManager.resources.getString(HaitiKeys.FORM_MALE));
		radioFemale.setText(LocaleManager.resources.getString(HaitiKeys.FORM_FEMALE));

		beneficiaryLabel.setText(LocaleManager.resources.getString(HaitiKeys.FORM_BENEFICIARY));
		radioInfantMal.setText(LocaleManager.resources.getString(HaitiKeys.LONG_INFANT_MAL));
		radioInfantPrev.setText(LocaleManager.resources.getString(HaitiKeys.LONG_INFANT_PREVENTION));
		radioMotherExp.setText(LocaleManager.resources.getString(HaitiKeys.LONG_MOTHER_EXPECTING));
		radioMotherNurs.setText(LocaleManager.resources.getString(HaitiKeys.LONG_MOTHER_NURSING));
		nInHouseLabel.setText(LocaleManager.resources.getString(HaitiKeys.LONG_NUMBER_IN_HOME));
		
		mchnPanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_MCHN_INFO)));
		healthCenterLabel.setText(LocaleManager.resources.getString(HaitiKeys.FORM_HEALTH_CENTER));
		distributionLabel.setText(LocaleManager.resources.getString(HaitiKeys.FORM_DISTRIBUTION_POST));
		childLabel.setText(LocaleManager.resources.getString(HaitiKeys.FORM_NAME_CHILD));
		womanLabel.setText(LocaleManager.resources.getString(HaitiKeys.FORM_NAME_WOMAN));
		husbandLabel.setText(LocaleManager.resources.getString(HaitiKeys.FORM_FATHER));
		fatherLabel.setText(LocaleManager.resources.getString(HaitiKeys.FORM_HUSBAND));

		leaderLabel.setText(LocaleManager.resources.getString(HaitiKeys.FORM_MOTHER_LEADER));
		visitLabel.setText(LocaleManager.resources.getString(HaitiKeys.FORM_VISIT_MOTHER));
		agr1Label.setText(LocaleManager.resources.getString(HaitiKeys.FORM_AGRICULTURE_1));
		agr2Label.setText(LocaleManager.resources.getString(HaitiKeys.FORM_AGRICULTURE_2));
		giveNameLabel.setText(LocaleManager.resources.getString(HaitiKeys.FORM_GIVE_NAME));
		dossierLabel.setText(LocaleManager.resources.getString(LABEL_DOSSIER));
	}
	
	/**
	 * Sets up the panel used for Beneficiary registration.
	 * @return
	 */
	private JPanel setUpDataEntryPanel() {
		//Form panel
		//formPanel = new JPanel();
		this.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_DATA_ENTRY)));
		this.setLayout(new BorderLayout());
		this.setBackground(Color.WHITE); 
		
		//General Information Panel
		geninfoPanel = new JPanel();
		geninfoPanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_GEN_INFO)));
		geninfoPanel.setLayout(new GridBagLayout());
		geninfoPanel.setBackground(Color.WHITE); 
		GridBagConstraints c = new GridBagConstraints();

		firstNameJText = new JTextField();
		firstNameJText.setColumns(15);
		lastNameJText = new JTextField();
		lastNameJText.setColumns(15);
		communeBox = new JComboBox();
		communeSectionBox = new JComboBox();
		addressJText = new JTextField();
		addressJText.setColumns(15);
		ageJText = new JTextField();
		ageJText.setColumns(15);
		c.gridy=0;
		c.gridx=0;
		c.insets=new Insets(10,5,4,2);
		c.anchor = GridBagConstraints.NORTHEAST;
		firstNameLabel = new JLabel(LocaleManager.resources.getString(HaitiKeys.FORM_FIRST_NAME));
		geninfoPanel.add(firstNameLabel,c);
		c.gridy=0;
		c.gridx=1;
		geninfoPanel.add(firstNameJText,c);
		c.gridy=0;
		c.gridx=2;
		lastNameLabel = new JLabel(LocaleManager.resources.getString(HaitiKeys.FORM_LAST_NAME));
		geninfoPanel.add(lastNameLabel,c);
		c.gridy=0;
		c.gridx=3;
		geninfoPanel.add(lastNameJText,c);
		c.gridy=1;
		c.gridx=0;
		communeLabel = new JLabel(LocaleManager.resources.getString(HaitiKeys.FORM_COMMUNE));
		geninfoPanel.add(communeLabel,c);
		c.gridy=1;
		c.gridx=1;
		geninfoPanel.add(communeBox,c);
		c.gridy=1;
		c.gridx=2;
		communeSectionLabel = new JLabel(LocaleManager.resources.getString(HaitiKeys.FORM_SECTION));
		geninfoPanel.add(communeSectionLabel,c);
		c.gridy=1;
		c.gridx=3;
		geninfoPanel.add(communeSectionBox,c);
		c.gridy=2;
		c.gridx=0;
		addressLabel = new JLabel(LocaleManager.resources.getString(HaitiKeys.FORM_ADDRESS));
		geninfoPanel.add(addressLabel,c);
		c.gridy=2;
		c.gridx=1;
		geninfoPanel.add(addressJText,c);
		c.gridy=2;
		c.gridx=2;
		ageLabel = new JLabel(LocaleManager.resources.getString(HaitiKeys.FORM_AGE));
		geninfoPanel.add(ageLabel,c);
		c.gridy=2;
		c.gridx=3;
		geninfoPanel.add(ageJText,c);
		
		
		//Sex radio buttons
		c.gridy=3;
		c.gridx=0;
		sexLabel = new JLabel();
		sexLabel.setText(LocaleManager.resources.getString(HaitiKeys.FORM_SEX));
		geninfoPanel.add(sexLabel,c);
		sexGroup = new ButtonGroup();
		radioMale = new JRadioButton(LocaleManager.resources.getString(HaitiKeys.FORM_MALE),false);
		radioFemale = new JRadioButton(LocaleManager.resources.getString(HaitiKeys.FORM_FEMALE),false);
		sexGroup.add(radioMale);
		sexGroup.add(radioFemale);
		c.gridy=3;
		c.gridx=1;
		geninfoPanel.add(radioMale,c);
		c.gridy=3;
		c.gridx=2;
		geninfoPanel.add(radioFemale,c);

		
		//Beneficiary radio buttons
		c.gridy = 4;
		c.gridx=0;
		beneficiaryLabel = new JLabel(LocaleManager.resources.getString(HaitiKeys.FORM_BENEFICIARY));
		geninfoPanel.add(beneficiaryLabel,c);
		infantGroup = new ButtonGroup();
		radioInfantMal = new JRadioButton(LocaleManager.resources.getString(HaitiKeys.LONG_INFANT_MAL),false);
		radioInfantPrev = new JRadioButton(LocaleManager.resources.getString(HaitiKeys.LONG_INFANT_PREVENTION),false);
		radioMotherExp = new JRadioButton(LocaleManager.resources.getString(HaitiKeys.LONG_MOTHER_EXPECTING),false);
		radioMotherNurs = new JRadioButton(LocaleManager.resources.getString(HaitiKeys.LONG_MOTHER_NURSING),false);
		infantGroup.add(radioInfantMal);
		infantGroup.add(radioInfantPrev);
		infantGroup.add(radioMotherExp);
		infantGroup.add(radioMotherNurs);
		c.gridy=4;
		c.gridx=1;
		geninfoPanel.add(radioInfantMal,c);
		c.gridy=4;
		c.gridx=2;
		geninfoPanel.add(radioInfantPrev,c);
		c.gridy=4;
		c.gridx=3;
		geninfoPanel.add(radioMotherExp,c);
		c.gridy=4;
		c.gridx=4;
		geninfoPanel.add(radioMotherNurs,c);
	
		//People in house
		c.gridy = 5;
		c.gridx=0;
		peopleInHouseJText = new JTextField();
		peopleInHouseJText.setColumns(3);
		nInHouseLabel = new JLabel(LocaleManager.resources.getString(HaitiKeys.LONG_NUMBER_IN_HOME));
		geninfoPanel.add(nInHouseLabel,c);
		c.gridy=5;
		c.gridx=1;
		geninfoPanel.add(peopleInHouseJText,c);
		
		//MCHN panel
		mchnPanel = new JPanel();
		mchnPanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_MCHN_INFO)));
		mchnPanel.setLayout(new GridBagLayout());
		mchnPanel.setBackground(Color.WHITE); 

		c.gridy=0;
		c.gridx=0;
		healthCenterJText = new JTextField();
		healthCenterJText.setColumns(15);
		dpJText = new JTextField();
		dpJText.setColumns(15);
		guardianChildJText = new JTextField();
		guardianChildJText.setColumns(15);
		guardianWomanJText = new JTextField();
		guardianWomanJText.setColumns(15);
		husbandJText = new JTextField();
		husbandJText.setColumns(15);
		fatherJText = new JTextField();
		fatherJText.setColumns(15);
		
		healthCenterLabel = new JLabel(LocaleManager.resources.getString(HaitiKeys.FORM_HEALTH_CENTER));
		mchnPanel.add(healthCenterLabel,c);
		c.gridy=0;
		c.gridx=1;
		mchnPanel.add(healthCenterJText,c);
		c.gridy=0;
		c.gridx=2;
		distributionLabel = new JLabel(LocaleManager.resources.getString(HaitiKeys.FORM_DISTRIBUTION_POST));
		mchnPanel.add(distributionLabel,c);
		c.gridy=0;
		c.gridx=3;
		mchnPanel.add(dpJText,c);
		c.gridy=1;
		c.gridx=0;
		childLabel = new JLabel(LocaleManager.resources.getString(HaitiKeys.FORM_NAME_CHILD));
		mchnPanel.add(childLabel,c);
		c.gridy=1;
		c.gridx=1;
		mchnPanel.add(guardianChildJText,c);
		c.gridy=1;
		c.gridx=2;
		womanLabel = new JLabel(LocaleManager.resources.getString(HaitiKeys.FORM_NAME_WOMAN));
		mchnPanel.add(womanLabel,c);
		c.gridy=1;
		c.gridx=3;
		mchnPanel.add(guardianWomanJText,c);
		c.gridy=2;
		c.gridx=0;
		husbandLabel = new JLabel(LocaleManager.resources.getString(HaitiKeys.FORM_FATHER));
		mchnPanel.add(husbandLabel,c);
		c.gridy=2;
		c.gridx=1;
		mchnPanel.add(husbandJText,c);
		c.gridy=2;
		c.gridx=2;
		fatherLabel = new JLabel(LocaleManager.resources.getString(HaitiKeys.FORM_HUSBAND));
		mchnPanel.add(fatherLabel,c);
		c.gridy=2;
		c.gridx=3;
		mchnPanel.add(fatherJText,c);

		motherGroup = new ButtonGroup();
		radioMotherLeaderYes = new JRadioButton(LocaleManager.resources.getString(HaitiKeys.BUTTON_YES),false);
		radioMotherLeaderNo = new JRadioButton(LocaleManager.resources.getString(HaitiKeys.BUTTON_NO),false);
		motherGroup.add(radioMotherLeaderYes);
		motherGroup.add(radioMotherLeaderNo);   
		c.gridy=3;
		c.gridx=0;
		leaderLabel = new JLabel(LocaleManager.resources.getString(HaitiKeys.FORM_MOTHER_LEADER));
		mchnPanel.add(leaderLabel,c);
		c.gridy=3;
		c.gridx=1;
		mchnPanel.add(radioMotherLeaderYes,c);
		c.gridy=3;
		c.gridx=2;
		mchnPanel.add(radioMotherLeaderNo,c);

		motherVisitGroup = new ButtonGroup();
		radioVisitYes = new JRadioButton(LocaleManager.resources.getString(HaitiKeys.BUTTON_YES),false);
		radioVisitNo = new JRadioButton(LocaleManager.resources.getString(HaitiKeys.BUTTON_NO),false);
		motherVisitGroup.add(radioVisitYes);
		motherVisitGroup.add(radioVisitNo);
		c.gridy=4;
		c.gridx=0;
		visitLabel = new JLabel(LocaleManager.resources.getString(HaitiKeys.FORM_VISIT_MOTHER));
		mchnPanel.add(visitLabel,c);
		c.gridy=4;
		c.gridx=1;
		mchnPanel.add(radioVisitYes,c);
		c.gridy=4;
		c.gridx=2;
		mchnPanel.add(radioVisitNo,c);

		agriGroup = new ButtonGroup();
		radioAgriYes = new JRadioButton(LocaleManager.resources.getString(HaitiKeys.BUTTON_YES),false);
		radioAgriNo = new JRadioButton(LocaleManager.resources.getString(HaitiKeys.BUTTON_NO),false);
		agriGroup.add(radioAgriYes);
		agriGroup.add(radioAgriNo);
		c.gridy=5;
		c.gridx=0;
		c.insets=new Insets(10,5,0,2);
		agr1Label = new JLabel(LocaleManager.resources.getString(HaitiKeys.FORM_AGRICULTURE_1));
		mchnPanel.add(agr1Label,c);
		c.insets=new Insets(0,5,4,2);
		c.gridy=6;
		c.gridx=0;
		agr2Label = new JLabel(LocaleManager.resources.getString(HaitiKeys.FORM_AGRICULTURE_2));
		mchnPanel.add(agr2Label,c);
		c.insets=new Insets(0,5,4,2);
		c.gridy=6;
		c.gridx=1;
		mchnPanel.add(radioAgriYes,c);
		c.gridy=6;
		c.gridx=2;
		mchnPanel.add(radioAgriNo,c);
		
		agriPersonJText = new JTextField();
		agriPersonJText.setColumns(15);
		c.gridy=7;
		c.gridx=0;
		c.insets=new Insets(10,5,4,2);
		giveNameLabel = new JLabel(LocaleManager.resources.getString(HaitiKeys.FORM_GIVE_NAME));
		mchnPanel.add(giveNameLabel,c);
		c.gridy=7;
		c.gridx=1;
		mchnPanel.add(agriPersonJText,c);

		buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_CONTROLS)));
		buttonPanel.setBackground(Color.WHITE);
		
		dossierLabel = new JLabel();
		dossierLabel.setText(LocaleManager.resources.getString(LABEL_DOSSIER));
		dossierJText = new JTextField(10);
		dossierJText.addKeyListener(this);
		
		toDbButton = new JButton(LocaleManager.resources.getString(BUTTON_SAVE));
		toDbButton.setEnabled(false);
		toDbMgrButton = new JButton(LocaleManager.resources.getString(BUTTON_DB_MGR));
		toDbMgrButton.setEnabled(false);
		toDbButton.addActionListener(this);
		toDbMgrButton.addActionListener(this);
		
		this.add(geninfoPanel,"North");
		this.add(mchnPanel,"Center");
		this.add(buttonPanel,"South");
		
		buttonPanel.add(dossierLabel);
		buttonPanel.add(dossierJText);
		buttonPanel.add(toDbButton);
		buttonPanel.add(toDbMgrButton);

		return formPanel;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton){
		    JButton button = (JButton)e.getSource();
		    if (button.equals(toDbButton)) {
		    	mGui.postMessageToTBS();
		    }
		    else if (button.equals(toDbMgrButton)) {
		    	mGui.forwardMessageToDbMgr();
		    }
		}
		
	}


	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		toDbButton.setEnabled(true);
		toDbMgrButton.setEnabled(true);
	}

	


}
