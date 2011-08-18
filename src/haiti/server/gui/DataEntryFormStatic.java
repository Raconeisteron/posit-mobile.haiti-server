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
import haiti.server.datamodel.AttributeManager.YnQuestion;
import haiti.server.datamodel.LocaleManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.JComponent;

public class DataEntryFormStatic extends FormPanel implements ActionListener, KeyListener {

	public static final String BUTTON_SAVE="Save";
	public static final String BUTTON_DB_MGR = "DbMgr";
	public static final String LABEL_DOSSIER = "Dossier";

	
	public static final String BORDER_DATA_ENTRY = "DataEntryForm";
	public static final String BORDER_GEN_INFO = "GeneralInfo";
	public static final String BORDER_MCHN_INFO = "MCHNInfo";
	public static final String BORDER_CONTROLS = "Controls";
	public static final String BORDER_AGRI_INFO = "AgricultureInfo";

	private DataEntryGUI mGui;

	private JButton 
		toDbMgrButton,
		toDbButton;

	private JPanel 
		welcomePanel, 
		buttonPanel, 
		formPanel, 
		geninfoPanel, 
		mchnPanel,
		agriPanel,
		agriCatePanel,
		seedTypesPanel,
		toolsPanel,
		organizationPanel;
	
	private JLabel 
		firstName, 
		lastName, 
		address, 
		dateOfBirth, 
		communeBox, 
		communeSectionBox,
		peopleInHouse,
		healthCenter, 
		dp, 
		guardianChild, 
		guardianWoman, 
		husband, 
		father, 
		agriPerson,
		sex,
		beneficiaryCategory,
		motherLeader,
		motherVisit,
		agri1,
		agriCategory,
		land,
		seedTypes,
		seedQuantity,
		tools,
		health;
	
	private JRadioButton
		radioFAO,
		radioSAVE,
		radioCROSE,
		radioPLAN,
		radioMARDNR,
		radioOrganizationOther,
	
		radioFarmer,
		radioMuso,
		radioCattleRancher,
		radioStoreOwner,
		radioFisherman,
		radioArtisan,
		radioOther,

		radioVegetables,
		radioCereal,
		radioTubers,
		radioTree,
		radioGrafting,
		radioCoffee,
		
		radioHoe,
		radioPickaxe,
		radioWheelbarrow,
		radioMachette,
		radioPruningKnife,
		radioShovel,
		radioCrowbar;
		
	
	private JLabel 
		firstNameLabel,
		lastNameLabel,
		communeLabel,
		communeSectionLabel,
		addressLabel,
		dateOfBirthLabel,
		sexLabel,
		beneficiaryCategoryLabel,
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
//		agr2Label,
		giveNameLabel,
		agriCategoryLabel,
		landLabel,
		seedTypesLabel,
		seedQuantityLabel,
		toolsLabel,
		organizationLabel,
		dossierLabel,
		healthLabel,
		giveNameLabel2,
		healthPerson;
	
	private JTextField
		dossier;

	/**
	 * Constructor of the form, calls the setup and stores the GUI
	 * it is connected to
	 * @param gui the gui to which this form will be added
	 */
	public DataEntryFormStatic(DataEntryGUI gui) {
		mGui = gui;
		setUpDataEntryPanel();
	}
	

	/**
	 * Fills in the Data Entry Form.
	 * @param beneficiary 
	 */
	public void fillInForm(Object o, DAO reader) {
		Beneficiary beneficiary = (Beneficiary)o;
		System.out.println("Filling in form with beneficiary = \n" + beneficiary);

		radioFarmer.setSelected(false);
		radioMuso.setSelected(false);
		radioCattleRancher.setSelected(false);
		radioStoreOwner.setSelected(false);
		radioFisherman.setSelected(false);
		radioArtisan.setSelected(false);
		radioOther.setSelected(false);
		radioVegetables.setSelected(false);
		radioCereal.setSelected(false);
		radioTubers.setSelected(false);
		radioTree.setSelected(false);
		radioGrafting.setSelected(false);
		radioHoe.setSelected(false);
		radioPickaxe.setSelected(false);
		radioWheelbarrow.setSelected(false);
		radioMachette.setSelected(false);
		radioPruningKnife.setSelected(false);
		radioShovel.setSelected(false);
		radioCrowbar.setSelected(false);
		radioFAO.setSelected(false);
		radioSAVE.setSelected(false);
		radioPLAN.setSelected(false);
		radioCROSE.setSelected(false);
		radioMARDNR.setSelected(false);
		radioOrganizationOther.setSelected(false);

		this.firstName.setText(beneficiary.getFirstName());
		this.lastName.setText(beneficiary.getLastName());
		
		this.communeBox.setText(beneficiary.getCommune());
		this.communeSectionBox.setText(AttributeManager.mapToLong(true, beneficiary.getCommuneSection()));
		this.address.setText(beneficiary.getAddress());
		this.dateOfBirth.setText(beneficiary.getDob());
		this.peopleInHouse.setText(Integer.toString(beneficiary.getNumberInHome()));
		
		if (beneficiary.getBeneficiaryCategory().equals(AttributeManager.BeneficiaryCategory.EXPECTING)) 
			this.beneficiaryCategory.setText(LocaleManager.resources.getString(AttributeManager.FORM_MOTHER_EXPECTING));
		else if (beneficiary.getBeneficiaryCategory().equals(AttributeManager.BeneficiaryCategory.NURSING)) 
			this.beneficiaryCategory.setText(LocaleManager.resources.getString(AttributeManager.FORM_MOTHER_NURSING));
		else if (beneficiary.getBeneficiaryCategory().equals(AttributeManager.BeneficiaryCategory.MALNOURISHED)) 
			this.beneficiaryCategory.setText(LocaleManager.resources.getString(AttributeManager.FORM_INFANT_MAL));
		else if (beneficiary.getBeneficiaryCategory().equals(AttributeManager.BeneficiaryCategory.PREVENTION))
			this.beneficiaryCategory.setText(LocaleManager.resources.getString(AttributeManager.FORM_INFANT_PREVENTION));
		else if (beneficiary.getBeneficiaryCategory().equals(AttributeManager.BeneficiaryCategory.AGRI))
			this.beneficiaryCategory.setText(LocaleManager.resources.getString(AttributeManager.FORM_AGRI));
				
		if (beneficiary.getSex().equals(AttributeManager.Sex.M))
			this.sex.setText(AttributeManager.FORM_MALE);
		else
			this.sex.setText(AttributeManager.FORM_FEMALE);
		
		this.healthCenter.setText(beneficiary.getHealthCenter());
		this.dp.setText(beneficiary.getDistributionPost());
		this.guardianChild.setText(beneficiary.getGuardianChild());
		this.guardianWoman.setText(beneficiary.getGuardianWoman());
//		this.husband.setText(beneficiary.getHusband());
//		this.father.setText(beneficiary.getFather());
		this.agriPerson.setText(beneficiary.getAgriPerson());
		this.motherLeader.setText(beneficiary.getIsMotherLeader().name());
		this.motherVisit.setText(beneficiary.getVisitMotherLeader().name());
		this.agri1.setText(beneficiary.getIsAgri().name());
		
		if (beneficiary.getIsFarmer() == YnQuestion.Y)
			radioFarmer.setSelected(true);
		if (beneficiary.getIsMuso() == YnQuestion.Y)
			radioMuso.setSelected(true);
		if (beneficiary.getIsRancher() == YnQuestion.Y)
			radioCattleRancher.setSelected(true);
		if (beneficiary.getIsStoreOwner() == YnQuestion.Y)
			radioStoreOwner.setSelected(true);
		if (beneficiary.getIsFisherman() == YnQuestion.Y)
			radioFisherman.setSelected(true);
		if (beneficiary.getIsOther() == YnQuestion.Y)
			radioOther.setSelected(true);
		if (beneficiary.getIsFAO() == YnQuestion.Y)
			radioFAO.setSelected(true);
		if (beneficiary.getIsSAVE() == YnQuestion.Y)
			radioSAVE.setSelected(true);
		if (beneficiary.getIsCROSE() == YnQuestion.Y)
			radioCROSE.setSelected(true);
		if (beneficiary.getIsPLAN() == YnQuestion.Y)
			radioPLAN.setSelected(true);
		if (beneficiary.getIsMARDNR() == YnQuestion.Y)
			radioMARDNR.setSelected(true);
		if (beneficiary.getIsOrganizationOther() == YnQuestion.Y)
			radioOrganizationOther.setSelected(true);
		if (beneficiary.getGetsVeggies() == YnQuestion.Y)
			radioVegetables.setSelected(true);
		if (beneficiary.getGetsCereal() == YnQuestion.Y)
			radioCereal.setSelected(true);
		if (beneficiary.getGetsTubers() == YnQuestion.Y)
			radioTubers.setSelected(true);
		if (beneficiary.getGetsTrees() == YnQuestion.Y)
			radioTree.setSelected(true);
		if (beneficiary.getGetsGrafting()==YnQuestion.Y)
			radioGrafting.setSelected(true);
		if (beneficiary.getGetsCoffee()==YnQuestion.Y)
			radioCoffee.setSelected(true);
		if (beneficiary.getGetsHoe() == YnQuestion.Y)
			radioHoe.setSelected(true);
		if (beneficiary.getGetsPickaxe() == YnQuestion.Y)
			radioPickaxe.setSelected(true);
		if (beneficiary.getGetsWheelbarrow() == YnQuestion.Y)
			radioWheelbarrow.setSelected(true);
		if (beneficiary.getGetsMachette() == YnQuestion.Y)
			radioMachette.setSelected(true);
		if (beneficiary.getGetsSerpette() == YnQuestion.Y)
			radioPruningKnife.setSelected(true);
		if (beneficiary.getGetsPelle() == YnQuestion.Y)
			radioShovel.setSelected(true);
		if (beneficiary.getGetsBarreAMines() == YnQuestion.Y)
			radioCrowbar.setSelected(true);
		this.health.setText(beneficiary.getIsHealth().name());
		
		mGui.setSize(1200, 800);

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
	 * @param g the graphics to be painted
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		toDbButton.setText(LocaleManager.resources.getString(BUTTON_SAVE));
		this.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_DATA_ENTRY)));
		buttonPanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_CONTROLS)));
		geninfoPanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_GEN_INFO)));
		firstNameLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_FIRST_NAME));
		lastNameLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_LAST_NAME));
		communeLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_COMMUNE));
		communeSectionLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_SECTION));
		addressLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_ADDRESS));
		dateOfBirthLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_DOB));
		sexLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_SEX));

		beneficiaryCategoryLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_BENEFICIARY_CATEGORY));
		nInHouseLabel.setText(LocaleManager.resources.getString(AttributeManager.LONG_NUMBER_IN_HOME));
		
		mchnPanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_MCHN_INFO)));
		healthCenterLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_HEALTH_CENTER));
		distributionLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_DISTRIBUTION_POST));
		childLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_NAME_CHILD));
		womanLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_NAME_WOMAN));
//		husbandLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_HUSBAND));
//		fatherLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_FATHER));

		leaderLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_MOTHER_LEADER));
		visitLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_VISIT_MOTHER));
		agr1Label.setText(LocaleManager.resources.getString(AttributeManager.FORM_AGRICULTURE_1));
//		agr2Label.setText(LocaleManager.resources.getString(AttributeManager.FORM_AGRICULTURE_2));
		giveNameLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_GIVE_NAME));

		agriCategoryLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_AGRICULTURE_CATEGORY));
		radioFarmer.setText(LocaleManager.resources.getString(AttributeManager.FORM_FARMER));
		radioMuso.setText(LocaleManager.resources.getString(AttributeManager.FORM_MUSO));
		radioCattleRancher.setText(LocaleManager.resources.getString(AttributeManager.FORM_CATTLE_RANCHER));
		radioStoreOwner.setText(LocaleManager.resources.getString(AttributeManager.FORM_STORE_OWNER));
		radioFisherman.setText(LocaleManager.resources.getString(AttributeManager.FORM_FISHERMAN));
		radioOther.setText(LocaleManager.resources.getString(AttributeManager.FORM_OTHER));	
	
		landLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_LAND));
		
		seedTypesLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_SEED_TYPE));
		radioVegetables.setText(LocaleManager.resources.getString(AttributeManager.FORM_VEGETABLES));
		radioCereal.setText(LocaleManager.resources.getString(AttributeManager.FORM_CEREAL));
		radioTubers.setText(LocaleManager.resources.getString(AttributeManager.FORM_TUBERS));
		radioTree.setText(LocaleManager.resources.getString(AttributeManager.FORM_TREE));
		radioGrafting.setText(LocaleManager.resources.getString(AttributeManager.FORM_GRAFTING));
		radioCoffee.setText(LocaleManager.resources.getString(AttributeManager.FORM_COFFEE));
//		seedQuantityLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_SEED_QUANTITY));

		toolsLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_TOOLS));
		radioHoe.setText(LocaleManager.resources.getString(AttributeManager.FORM_HOE));
		radioPickaxe.setText(LocaleManager.resources.getString(AttributeManager.FORM_PICKAXE));
		radioWheelbarrow.setText(LocaleManager.resources.getString(AttributeManager.FORM_WHEELBARROW));
		radioMachette.setText(LocaleManager.resources.getString(AttributeManager.FORM_MACHETTE));
		radioPruningKnife.setText(LocaleManager.resources.getString(AttributeManager.FORM_PRUNING_KNIFE));
		radioShovel.setText(LocaleManager.resources.getString(AttributeManager.FORM_SHOVEL));
		radioCrowbar.setText(LocaleManager.resources.getString(AttributeManager.FORM_CROWBAR));
		
		healthLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_HEALTH));
		giveNameLabel2.setText(LocaleManager.resources.getString(AttributeManager.FORM_GIVE_NAME));

		organizationLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_ORGANIZATIONS));
		
		dossierLabel.setText(LocaleManager.resources.getString(LABEL_DOSSIER));
	}
	
	/**
	 * Sets up the panel used for Beneficiary registration.
	 */
	protected void setUpDataEntryPanel() {
		//Form panel
		formPanel = new JPanel();
		formPanel.setBorder(BorderFactory.createEmptyBorder());
		formPanel.setLayout(new GridLayout(0,1));
		formPanel.setBackground(Color.WHITE);
		
		this.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_DATA_ENTRY)));
		this.setLayout(new BorderLayout());
		this.setBackground(Color.WHITE); 
		
		//General Information Panel
		geninfoPanel = new JPanel();
		geninfoPanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_GEN_INFO)));
		geninfoPanel.setLayout(new GridLayout(0,4));
		geninfoPanel.setBackground(Color.WHITE); 
		GridBagConstraints c = new GridBagConstraints();

		firstName = new JLabel();
		lastName = new JLabel();
		communeBox = new JLabel();
		communeSectionBox = new JLabel();
		address = new JLabel();
		dateOfBirth = new JLabel();
		sex = new JLabel();
		beneficiaryCategory = new JLabel();
		c.gridy=0;
		c.gridx=0;
		//c.insets=new Insets(10,5,4,40);
		c.anchor = GridBagConstraints.NORTHWEST;
		firstNameLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_FIRST_NAME));
		geninfoPanel.add(firstNameLabel,c);
		c.gridy=0;
		c.gridx=1;
		geninfoPanel.add(firstName,c);
		c.gridy=0;
		c.gridx=2;
		lastNameLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_LAST_NAME));
		geninfoPanel.add(lastNameLabel,c);
		c.gridy=0;
		c.gridx=3;
		geninfoPanel.add(lastName,c);
		c.gridy=1;
		c.gridx=0;
		communeLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_COMMUNE));
		geninfoPanel.add(communeLabel,c);
		c.gridy=1;
		c.gridx=1;
		geninfoPanel.add(communeBox,c);
		c.gridy=1;
		c.gridx=2;
		communeSectionLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_SECTION));
		geninfoPanel.add(communeSectionLabel,c);
		c.gridy=1;
		c.gridx=3;
		geninfoPanel.add(communeSectionBox,c);
		c.gridy=2;
		c.gridx=0;
		addressLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_ADDRESS));
		geninfoPanel.add(addressLabel,c);
		c.gridy=2;
		c.gridx=1;
		geninfoPanel.add(address,c);
		c.gridy=2;
		c.gridx=2;
		dateOfBirthLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_AGE));
		geninfoPanel.add(dateOfBirthLabel,c);
		c.gridy=2;
		c.gridx=3;
		geninfoPanel.add(dateOfBirth,c);
		c.gridy=3;
		c.gridx=0;
		sexLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_SEX));
		geninfoPanel.add(sexLabel,c);
		c.gridy=3;
		c.gridx=1;
		
//		sexGroup = new ButtonGroup();
//		radioMale = new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_MALE),false);
//		radioFemale = new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_FEMALE),false);
//		sexGroup.add(radioMale);
//		sexGroup.add(radioFemale);
//		geninfoPanel.add(radioMale,c);
//		c.gridy=3;
//		c.gridx=2;
//		geninfoPanel.add(radioFemale,c);
		geninfoPanel.add(sex,c);
		c.gridy=3;
		c.gridx=2;
		beneficiaryCategoryLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_BENEFICIARY_CATEGORY));
		geninfoPanel.add(beneficiaryCategoryLabel,c);
		c.gridy=3;
		c.gridx=3;
//		infantGroup = new ButtonGroup();
//		radioInfantMal = new JRadioButton(LocaleManager.resources.getString(AttributeManager.LONG_INFANT_MAL),false);
//		radioInfantPrev = new JRadioButton(LocaleManager.resources.getString(AttributeManager.LONG_INFANT_PREVENTION),false);
//		radioMotherExp = new JRadioButton(LocaleManager.resources.getString(AttributeManager.LONG_MOTHER_EXPECTING),false);
//		radioMotherNurs = new JRadioButton(LocaleManager.resources.getString(AttributeManager.LONG_MOTHER_NURSING),false);
//		infantGroup.add(radioInfantMal);
//		infantGroup.add(radioInfantPrev);
//		infantGroup.add(radioMotherExp);
//		infantGroup.add(radioMotherNurs);
//		c.gridy=4;
//		c.gridx=1;
//		geninfoPanel.add(radioInfantMal,c);
//		c.gridy=4;
//		c.gridx=2;
//		geninfoPanel.add(radioInfantPrev,c);
//		c.gridy=4;
//		c.gridx=3;
//		geninfoPanel.add(radioMotherExp,c);
//		c.gridy=4;
//		c.gridx=4;
//		geninfoPanel.add(radioMotherNurs,c);
		geninfoPanel.add(beneficiaryCategory,c);

	
		//People in house
		c.gridy = 5;
		c.gridx=0;
		peopleInHouse = new JLabel();
		nInHouseLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.LONG_NUMBER_IN_HOME));
		geninfoPanel.add(nInHouseLabel,c);
		c.gridy=5;
		c.gridx=1;
		geninfoPanel.add(peopleInHouse,c);
		
		//MCHN panel
		mchnPanel = new JPanel();
		mchnPanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_MCHN_INFO)));
		mchnPanel.setLayout(new GridLayout(0,4));
		mchnPanel.setBackground(Color.WHITE); 

		healthCenter = new JLabel();
		dp = new JLabel();
		guardianChild = new JLabel();
		guardianWoman = new JLabel();
		husband = new JLabel();
		father = new JLabel();
		
		c.gridy=0;
		c.gridx=0;
		healthCenterLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_HEALTH_CENTER));
		mchnPanel.add(healthCenterLabel,c);
		c.gridy=0;
		c.gridx=1;
		mchnPanel.add(healthCenter,c);
		c.gridy=0;
		c.gridx=2;
		distributionLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_DISTRIBUTION_POST));
		mchnPanel.add(distributionLabel,c);
		c.gridy=0;
		c.gridx=3;
		mchnPanel.add(dp,c);
		c.gridy=1;
		c.gridx=0;
		childLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_NAME_CHILD));
		mchnPanel.add(childLabel,c);
		c.gridy=1;
		c.gridx=1;
		mchnPanel.add(guardianChild,c);
		c.gridy=1;
		c.gridx=2;
		womanLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_NAME_WOMAN));
		mchnPanel.add(womanLabel,c);
		c.gridy=1;
		c.gridx=3;
		mchnPanel.add(guardianWoman,c);
//		c.gridy=2;
//		c.gridx=0;
//		fatherLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_FATHER));
//		mchnPanel.add(fatherLabel,c);
//		c.gridy=2;
//		c.gridx=1;
//		mchnPanel.add(father,c);
//		c.gridy=2;
//		c.gridx=2;
//		husbandLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_HUSBAND));
//		mchnPanel.add(husbandLabel,c);
//		c.gridy=2;
//		c.gridx=3;
//		mchnPanel.add(husband,c);

		motherLeader = new JLabel();
		c.gridy=3;
		c.gridx=0;
		leaderLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_MOTHER_LEADER));
		mchnPanel.add(leaderLabel,c);
		c.gridy=3;
		c.gridx=1;
		mchnPanel.add(motherLeader,c);
//		mchnPanel.add(radioMotherLeaderYes,c);
		c.gridy=3;
		c.gridx=2;

		motherVisit = new JLabel();
		c.gridy=4;
		c.gridx=0;
		visitLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_VISIT_MOTHER));
		mchnPanel.add(visitLabel,c);
		c.gridy=4;
		c.gridx=1;
		mchnPanel.add(motherVisit,c);
//		mchnPanel.add(radioVisitYes,c);
		c.gridy=4;
		c.gridx=2;
//		mchnPanel.add(radioVisitNo,c);

		c.gridy=5;
		c.gridx=0;
//		c.insets=new Insets(10,5,0,40);
		agr1Label = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_AGRICULTURE_1));
		mchnPanel.add(agr1Label);
//		c.insets=new Insets(0,5,4,40);
//		c.gridy=6;
//		c.gridx=0;
//		agr2Label = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_AGRICULTURE_2));
//		mchnPanel.add(agr2Label,c);
		//c.insets=new Insets(0,5,4,40);
		c.gridy=6;
		c.gridx=1;
		agri1 = new JLabel();
		mchnPanel.add(agri1);
//		mchnPanel.add(radioAgriYes,c);
		c.gridy=6;
		c.gridx=2;
//		mchnPanel.add(radioAgriNo,c);
		
		agriPerson = new JLabel();
		c.gridy=7;
		c.gridx=0;
		//c.insets=new Insets(10,5,4,40);
		giveNameLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_GIVE_NAME));
		mchnPanel.add(giveNameLabel);
		c.gridy=7;
		c.gridx=1;
		mchnPanel.add(agriPerson);
		
		agriPanel = new JPanel();
		agriPanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_AGRI_INFO)));
		agriPanel.setLayout(new GridLayout(0, 4));
		agriPanel.setBackground(Color.WHITE);
		
		agriCategoryLabel=new JLabel();
		c.gridy=0;
		c.gridx=0;
		//c.insets=new Insets(10,5,4,70);
		agriPanel.add(agriCategoryLabel,c);
		c.gridy=0;
		c.gridx=1;
//		agriCategory = new JLabel();
//		agriPanel.add(agriCategory,c);

		agriCatePanel = new JPanel();
		agriCatePanel.setBorder(BorderFactory.createEmptyBorder());
		agriCatePanel.setLayout(new GridLayout(0,2));
		agriCatePanel.setBackground(Color.WHITE);
		radioFarmer=new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_FARMER),false);
		radioMuso=new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_MUSO),false);
		radioCattleRancher=new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_CATTLE_RANCHER),false);
		radioStoreOwner=new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_STORE_OWNER),false);
		radioFisherman=new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_FISHERMAN),false);
		radioArtisan=new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_ARTISAN),false);
		radioOther=new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_OTHER),false);
		agriCatePanel.add(radioFarmer);
		agriCatePanel.add(radioMuso);
		agriCatePanel.add(radioCattleRancher);
		agriCatePanel.add(radioStoreOwner);
		agriCatePanel.add(radioFisherman);
		agriCatePanel.add(radioArtisan);
		agriCatePanel.add(radioOther);
		radioFarmer.setEnabled(false);
		radioMuso.setEnabled(false);
		radioCattleRancher.setEnabled(false);
		radioStoreOwner.setEnabled(false);
		radioFisherman.setEnabled(false);
		radioArtisan.setEnabled(false);
		radioOther.setEnabled(false);
		agriPanel.add(agriCatePanel);
		
		landLabel=new JLabel();
		c.gridy=0;
		c.gridx=2;
		agriPanel.add(landLabel,c);
		land=new JLabel();
		c.gridy=0;
		c.gridx=3;
		agriPanel.add(land,c);
		
		seedTypesLabel=new JLabel();
		c.gridy=1;
		c.gridx=0;
		agriPanel.add(seedTypesLabel,c);
		seedTypes=new JLabel();
		c.gridy=1;
		c.gridx=1;
//		agriPanel.add(seedTypes,c);
		
		seedTypesPanel = new JPanel();
		seedTypesPanel.setBorder(BorderFactory.createEmptyBorder());
		seedTypesPanel.setLayout(new GridLayout(0,2));
		seedTypesPanel.setBackground(Color.WHITE);
		radioVegetables=new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_VEGETABLES),false);
		radioCereal=new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_CEREAL),false);
		radioTubers=new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_TUBERS),false);
		radioTree=new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_TREE),false);
		radioGrafting=new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_GRAFTING),false);
		radioCoffee=new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_COFFEE),false);
		seedTypesPanel.add(radioVegetables);
		seedTypesPanel.add(radioCereal);
		seedTypesPanel.add(radioTubers);
		seedTypesPanel.add(radioTree);
		seedTypesPanel.add(radioGrafting);
		seedTypesPanel.add(radioCoffee);
		radioVegetables.setEnabled(false);
		radioCereal.setEnabled(false);
		radioTubers.setEnabled(false);
		radioTree.setEnabled(false);
		radioGrafting.setEnabled(false);
		radioCoffee.setEnabled(false);
		agriPanel.add(seedTypesPanel);
		
//		seedQuantityLabel=new JLabel();
//		c.gridy=1;
//		c.gridx=2;
//		agriPanel.add(seedQuantityLabel,c);
//		seedQuantity=new JLabel();
//		c.gridy=1;
//		c.gridx=3;
//		agriPanel.add(seedQuantity,c);
		
		toolsLabel=new JLabel();
		c.gridy=2;
		c.gridx=0;
		agriPanel.add(toolsLabel,c);
		tools=new JLabel();
		c.gridy=2;
		c.gridx=1;
//		agriPanel.add(tools,c);
		
		toolsPanel = new JPanel();
		toolsPanel.setBorder(BorderFactory.createEmptyBorder());
		toolsPanel.setLayout(new GridLayout(0,2));
		toolsPanel.setBackground(Color.WHITE);
		radioHoe=new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_HOE),false);
		radioPickaxe=new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_PICKAXE),false);
		radioWheelbarrow=new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_WHEELBARROW),false);
		radioMachette=new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_MACHETTE),false);
		radioPruningKnife=new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_PRUNING_KNIFE),false);
		radioShovel=new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_SHOVEL),false);
		radioCrowbar=new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_CROWBAR),false);
		toolsPanel.add(radioHoe);
		toolsPanel.add(radioPickaxe);
		toolsPanel.add(radioWheelbarrow);
		toolsPanel.add(radioMachette);
		toolsPanel.add(radioPruningKnife);
		toolsPanel.add(radioShovel);
		toolsPanel.add(radioCrowbar);
		radioHoe.setEnabled(false);
		radioPickaxe.setEnabled(false);
		radioWheelbarrow.setEnabled(false);
		radioMachette.setEnabled(false);
		radioPruningKnife.setEnabled(false);
		radioShovel.setEnabled(false);
		radioCrowbar.setEnabled(false);
		agriPanel.add(toolsPanel);
		
		c.gridy=2;
		c.gridx=0;
//		JLabel fakeLabel = new JLabel();
//		agriPanel.add(fakeLabel);
//		JLabel fakeLabel2 = new JLabel();
//		agriPanel.add(fakeLabel2);
		
		organizationLabel=new JLabel();
		c.gridy=3;
		c.gridx=0;
		agriPanel.add(organizationLabel,c);
		c.gridy=3;
		c.gridx=1;
		
		organizationPanel = new JPanel();
		organizationPanel.setBorder(BorderFactory.createEmptyBorder());
		organizationPanel.setLayout(new GridLayout(0,2));
		organizationPanel.setBackground(Color.WHITE);
		radioFAO=new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_FAO),false);
		radioSAVE=new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_SAVE_ORG),false);
		radioCROSE=new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_CROSE),false);
		radioPLAN=new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_PLAN),false);
		radioMARDNR=new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_MARDNR),false);
		radioOrganizationOther=new JRadioButton(LocaleManager.resources.getString(AttributeManager.FORM_OTHER_ORG),false);
		organizationPanel.add(radioFAO);
		organizationPanel.add(radioSAVE);
		organizationPanel.add(radioCROSE);
		organizationPanel.add(radioPLAN);
		organizationPanel.add(radioMARDNR);
		organizationPanel.add(radioOrganizationOther);
		radioFAO.setEnabled(false);
		radioSAVE.setEnabled(false);
		radioCROSE.setEnabled(false);
		radioPLAN.setEnabled(false);
		radioMARDNR.setEnabled(false);
		radioOrganizationOther.setEnabled(false);
		agriPanel.add(organizationPanel);
		
		JLabel fakeLabel = new JLabel();
		agriPanel.add(fakeLabel);
		JLabel fakeLabel2 = new JLabel();
		agriPanel.add(fakeLabel2);

		healthLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_HEALTH));
		agriPanel.add(healthLabel);
		health = new JLabel();
		agriPanel.add(health);
		
		healthPerson = new JLabel();
		giveNameLabel2 = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_GIVE_NAME));
		agriPanel.add(giveNameLabel2);
		agriPanel.add(healthPerson);		

		buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources.getString(BORDER_CONTROLS)));
		buttonPanel.setBackground(Color.WHITE);
		
		
		dossierLabel = new JLabel();
		dossierLabel.setText(LocaleManager.resources.getString(LABEL_DOSSIER));
		dossier = new JTextField(10);
		dossier.addKeyListener(this);
		
		toDbButton = new JButton(LocaleManager.resources.getString(BUTTON_SAVE));
		toDbButton.setEnabled(false);
		toDbMgrButton = new JButton(LocaleManager.resources.getString(BUTTON_DB_MGR));
		toDbMgrButton.setEnabled(false);
		toDbButton.addActionListener(this);
		toDbMgrButton.addActionListener(this);
		
		formPanel.add(mchnPanel);
		formPanel.add(agriPanel);
		
		this.add(geninfoPanel, "North");
		this.add(formPanel, "Center");
		this.add(buttonPanel, "South");
		
		buttonPanel.add(dossierLabel);
		buttonPanel.add(dossier);
		buttonPanel.add(toDbButton);
		buttonPanel.add(toDbMgrButton);

//		return formPanel;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton){
		    JButton button = (JButton)e.getSource();
		    if (button.equals(toDbButton)) {
		    	
		    	mGui.postMessageToTBS(dossier.getText());
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