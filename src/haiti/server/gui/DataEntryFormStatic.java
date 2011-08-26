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
import haiti.server.datamodel.DAO;
import haiti.server.datamodel.AttributeManager.YnQuestion;
import haiti.server.datamodel.LocaleManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
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

public class DataEntryFormStatic extends FormPanel implements ActionListener,
		KeyListener {

	public static final String BUTTON_SAVE = "Save";
	public static final String BUTTON_DB_MGR = "DbMgr";
	public static final String LABEL_DOSSIER = "Dossier";

	public static final String BORDER_DATA_ENTRY = "DataEntryForm";
	public static final String BORDER_GEN_INFO = "GeneralInfo";
	public static final String BORDER_MCHN_INFO = "MCHNInfo";
	public static final String BORDER_CONTROLS = "Controls";
	public static final String BORDER_AGRI_INFO = "AgricultureInfo";

	private DataEntryGUI mGui;

	private JButton toDbMgrButton, toDbButton;

	private JPanel welcomePanel, buttonPanel, formPanel, geninfoPanel,
			mchnPanel, agriPanel, agriCategoryPanel, seedTypesPanel,
			toolsPanel, organizationPanel;

	private JLabel firstName, lastName, address, dateOfBirth, communeBox,
			communeSectionBox, peopleInHouse, healthCenter, dp, guardianChild,
			guardianWoman, husband, father, agriPersonLabel, sex,
			beneficiaryCategory, motherLeader, motherVisit, agriAnswerLabel,
			agriCategory, land, seedTypes, seedQuantity, tools, health;

	private JRadioButton radioFAO, radioSAVE, radioCROSE, radioPLAN,
			radioMARDNR, radioOrganizationOther,

			radioFarmer, radioMuso, radioCattleRancher, radioStoreOwner,
			radioFisherman, radioArtisan, radioOther,

			radioVegetables, radioCereal, radioTubers, radioTree,
			radioGrafting, radioCoffee,

			radioHoe, radioPickaxe, radioWheelbarrow, radioMachette,
			radioPruningKnife, radioShovel, radioCrowbar;

	private JLabel firstNameLabel, lastNameLabel, communeLabel,
			communeSectionLabel, addressLabel, dateOfBirthLabel, sexLabel,
			beneficiaryCategoryLabel, nInHouseLabel, healthCenterLabel,
			distributionLabel, childLabel, womanLabel, husbandLabel,
			fatherLabel, leaderLabel, visitLabel,
			agriQuestionLabel,
			// agr2Label,
			giveNameLabel, agriCategoryLabel, landLabel, seedTypesLabel,
			seedQuantityLabel, toolsLabel, organizationLabel, dossierLabel,
			healthLabel, giveNameLabel2, healthPerson;

	private JTextField dossier;

	/**
	 * Constructor of the form, calls the setup and stores the GUI it is
	 * connected to
	 * 
	 * @param gui
	 *            the gui to which this form will be added
	 */
	public DataEntryFormStatic(DataEntryGUI gui) {
		mGui = gui;
		setUpDataEntryPanel();
	}

	/**
	 * Fills in the Data Entry Form.
	 * 
	 * @param beneficiary
	 */
	public void fillInForm(Object o, DAO reader) {
		Beneficiary beneficiary = (Beneficiary) o;
		System.out.println("Filling in form with beneficiary = \n"
				+ beneficiary);

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
		radioCoffee.setSelected(false);
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
		this.communeSectionBox.setText(AttributeManager.mapToLong(true,
				beneficiary.getCommuneSection()));
		this.address.setText(beneficiary.getAddress());
		this.dateOfBirth.setText(beneficiary.getDob());
		this.peopleInHouse.setText(Integer.toString(beneficiary
				.getNumberInHome()));

		if (beneficiary.getBeneficiaryCategory().equals(
				AttributeManager.BeneficiaryCategory.EXPECTING))
			this.beneficiaryCategory.setText(LocaleManager.resources
					.getString(AttributeManager.FORM_MOTHER_EXPECTING));
		else if (beneficiary.getBeneficiaryCategory().equals(
				AttributeManager.BeneficiaryCategory.NURSING))
			this.beneficiaryCategory.setText(LocaleManager.resources
					.getString(AttributeManager.FORM_MOTHER_NURSING));
		else if (beneficiary.getBeneficiaryCategory().equals(
				AttributeManager.BeneficiaryCategory.MALNOURISHED))
			this.beneficiaryCategory.setText(LocaleManager.resources
					.getString(AttributeManager.FORM_INFANT_MAL));
		else if (beneficiary.getBeneficiaryCategory().equals(
				AttributeManager.BeneficiaryCategory.PREVENTION))
			this.beneficiaryCategory.setText(LocaleManager.resources
					.getString(AttributeManager.FORM_INFANT_PREVENTION));
		else if (beneficiary.getBeneficiaryCategory().equals(
				AttributeManager.BeneficiaryCategory.AGRI))
			this.beneficiaryCategory.setText(LocaleManager.resources
					.getString(AttributeManager.FORM_AGRI));

		if (beneficiary.getSex().equals(AttributeManager.Sex.M))
			this.sex.setText(AttributeManager.FORM_MALE);
		else
			this.sex.setText(AttributeManager.FORM_FEMALE);

		this.healthCenter.setText(beneficiary.getHealthCenter());
		this.dp.setText(beneficiary.getDistributionPost());
		this.guardianChild.setText(beneficiary.getGuardianChild());
		this.guardianWoman.setText(beneficiary.getGuardianWoman());
		// this.husband.setText(beneficiary.getHusband());
		// this.father.setText(beneficiary.getFather());
		this.agriPersonLabel.setText(beneficiary.getAgriPerson());
		this.motherLeader.setText(beneficiary.getIsMotherLeader().name());
		this.motherVisit.setText(beneficiary.getVisitMotherLeader().name());
		this.agriAnswerLabel.setText(beneficiary.getIsAgri().name());

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
		if (beneficiary.getGetsGrafting() == YnQuestion.Y)
			radioGrafting.setSelected(true);
		if (beneficiary.getGetsCoffee() == YnQuestion.Y)
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
		
		land.setText(String.valueOf(beneficiary.getAmountOfLand()));
		healthPerson.setText(beneficiary.getHealthPerson());
		this.health.setText(beneficiary.getIsHealth().name());

		// mGui.setSize(1200, 800);

	}

	/**
	 * Helper method to add an array of items ot a Combo Box.
	 * 
	 * @param combo
	 * @param arr
	 */
	// private void addItemsToComboBox (JComboBox combo, String arr[]) {
	// for (int k = 0; k < arr.length; k++)
	// combo.addItem(arr[k]);
	// }

	/**
	 * Called automatically whenever the Panel is repainted this method sets all
	 * locale dependent strings in the Panel.
	 * 
	 * @param g
	 *            the graphics to be painted
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		toDbButton.setText(LocaleManager.resources.getString(BUTTON_SAVE));
		this.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources
				.getString(BORDER_DATA_ENTRY)));
		buttonPanel.setBorder(BorderFactory
				.createTitledBorder(LocaleManager.resources
						.getString(BORDER_CONTROLS)));
		geninfoPanel.setBorder(BorderFactory
				.createTitledBorder(LocaleManager.resources
						.getString(BORDER_GEN_INFO)));
		firstNameLabel.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_FIRST_NAME) + " ");
		lastNameLabel.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_LAST_NAME) + " ");
		communeLabel.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_COMMUNE) + " ");
		communeSectionLabel.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_SECTION) + " ");
		addressLabel.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_ADDRESS) + " ");
		dateOfBirthLabel.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_DOB) + " ");
		sexLabel.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_SEX) + " ");

		beneficiaryCategoryLabel.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_BENEFICIARY_CATEGORY) + " ");
		nInHouseLabel.setText(LocaleManager.resources
				.getString(AttributeManager.LONG_NUMBER_IN_HOME) + " ");

		mchnPanel.setBorder(BorderFactory
				.createTitledBorder(LocaleManager.resources
						.getString(BORDER_MCHN_INFO)));
		healthCenterLabel.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_HEALTH_CENTER) + " ");
		distributionLabel.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_DISTRIBUTION_POST) + " ");
		childLabel.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_NAME_CHILD) + " ");
		womanLabel.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_NAME_WOMAN) + " ");
		leaderLabel.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_MOTHER_LEADER) + " ");
		visitLabel.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_VISIT_MOTHER) + " ");
		agriQuestionLabel.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_AGRICULTURE_1) + " ");
		giveNameLabel.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_GIVE_NAME) + " ");

		agriCategoryPanel
		.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources
				.getString(AttributeManager.FORM_AGRICULTURE_CATEGORY)));
		radioFarmer.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_FARMER));
		radioMuso.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_MUSO));
		radioCattleRancher.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_CATTLE_RANCHER));
		radioStoreOwner.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_STORE_OWNER));
		radioFisherman.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_FISHERMAN));
		radioOther.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_OTHER));

		landLabel.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_LAND) + " ");


		seedTypesPanel
		.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources
				.getString(AttributeManager.FORM_SEED_TYPE)));
		radioVegetables.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_VEGETABLES));
		radioCereal.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_CEREAL));
		radioTubers.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_TUBERS));
		radioTree.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_TREE));
		radioGrafting.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_GRAFTING));
		radioCoffee.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_COFFEE));

		toolsPanel
		.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources
				.getString(AttributeManager.FORM_TOOLS)));
		radioHoe.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_HOE));
		radioPickaxe.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_PICKAXE));
		radioWheelbarrow.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_WHEELBARROW));
		radioMachette.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_MACHETTE));
		radioPruningKnife.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_PRUNING_KNIFE));
		radioShovel.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_SHOVEL));
		radioCrowbar.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_CROWBAR));

		healthLabel.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_HEALTH) + " ");
		giveNameLabel2.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_GIVE_NAME) + " ");

		organizationPanel
		.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources
				.getString(AttributeManager.FORM_ORGANIZATIONS)));

		dossierLabel.setText(LocaleManager.resources.getString(LABEL_DOSSIER));
	}

	/**
	 * Sets up the panel used for Beneficiary registration.
	 */
	protected void setUpDataEntryPanel() {
		// Form panel
		formPanel = new JPanel();
		formPanel.setBorder(BorderFactory.createEmptyBorder());
		formPanel.setLayout(new BoxLayout(formPanel, BoxLayout.PAGE_AXIS));

		this.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources
				.getString(BORDER_DATA_ENTRY)));
		this.setLayout(new BorderLayout());

		// General Information Panel
		geninfoPanel = new JPanel();
		geninfoPanel.setBorder(BorderFactory
				.createTitledBorder(LocaleManager.resources
						.getString(BORDER_GEN_INFO)));
		geninfoPanel.setLayout(new GridLayout(0, 4));

		firstName = new JLabel();
		lastName = new JLabel();
		communeBox = new JLabel();
		communeSectionBox = new JLabel();
		address = new JLabel();
		dateOfBirth = new JLabel();
		sex = new JLabel();
		beneficiaryCategory = new JLabel();
		firstNameLabel = new JLabel(
				LocaleManager.resources
						.getString(AttributeManager.FORM_FIRST_NAME),
				JLabel.RIGHT);
		geninfoPanel.add(firstNameLabel);
		geninfoPanel.add(firstName);

		lastNameLabel = new JLabel(
				LocaleManager.resources
						.getString(AttributeManager.FORM_LAST_NAME),
				JLabel.RIGHT);
		geninfoPanel.add(lastNameLabel);
		geninfoPanel.add(lastName);

		communeLabel = new JLabel(
				LocaleManager.resources
						.getString(AttributeManager.FORM_COMMUNE),
				JLabel.RIGHT);
		geninfoPanel.add(communeLabel);
		geninfoPanel.add(communeBox);

		communeSectionLabel = new JLabel(
				LocaleManager.resources
						.getString(AttributeManager.FORM_SECTION),
				JLabel.RIGHT);
		geninfoPanel.add(communeSectionLabel);
		geninfoPanel.add(communeSectionBox);

		addressLabel = new JLabel(
				LocaleManager.resources
						.getString(AttributeManager.FORM_ADDRESS),
				JLabel.RIGHT);
		geninfoPanel.add(addressLabel);
		geninfoPanel.add(address);

		dateOfBirthLabel = new JLabel(
				LocaleManager.resources.getString(AttributeManager.FORM_AGE),
				JLabel.RIGHT);
		geninfoPanel.add(dateOfBirthLabel);
		geninfoPanel.add(dateOfBirth);

		sexLabel = new JLabel(
				LocaleManager.resources.getString(AttributeManager.FORM_SEX),
				JLabel.RIGHT);
		geninfoPanel.add(sexLabel);
		geninfoPanel.add(sex);

		beneficiaryCategoryLabel = new JLabel(
				LocaleManager.resources
						.getString(AttributeManager.FORM_BENEFICIARY_CATEGORY),
				JLabel.RIGHT);
		geninfoPanel.add(beneficiaryCategoryLabel);
		geninfoPanel.add(beneficiaryCategory);

		peopleInHouse = new JLabel();
		nInHouseLabel = new JLabel(
				LocaleManager.resources
						.getString(AttributeManager.LONG_NUMBER_IN_HOME),
				JLabel.RIGHT);
		geninfoPanel.add(nInHouseLabel);
		geninfoPanel.add(peopleInHouse);

		// MCHN panel
		mchnPanel = new JPanel();
		mchnPanel.setBorder(BorderFactory
				.createTitledBorder(LocaleManager.resources
						.getString(BORDER_GEN_INFO)));
		mchnPanel.setLayout(new BoxLayout(mchnPanel, BoxLayout.PAGE_AXIS));
		mchnPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		healthCenter = new JLabel();
		dp = new JLabel();
		guardianChild = new JLabel();
		guardianWoman = new JLabel();

		JPanel healthCenterPanel = new JPanel();
		healthCenterPanel.setLayout(new BoxLayout(healthCenterPanel,
				BoxLayout.LINE_AXIS));
		healthCenterPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		healthCenterLabel = new JLabel(
				LocaleManager.resources
						.getString(AttributeManager.FORM_HEALTH_CENTER));
		healthCenterPanel.add(healthCenterLabel);
		healthCenterPanel.add(healthCenter);
		// mchnPanel.add(healthCenterPanel); Removed for now.

		JPanel distributionPanel = new JPanel();
		distributionPanel.setLayout(new BoxLayout(distributionPanel,
				BoxLayout.LINE_AXIS));
		distributionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		distributionLabel = new JLabel(
				LocaleManager.resources
						.getString(AttributeManager.FORM_DISTRIBUTION_POST));
		distributionPanel.add(distributionLabel);
		distributionPanel.add(dp);
		mchnPanel.add(distributionPanel);

		JPanel childPanel = new JPanel();
		childPanel.setLayout(new BoxLayout(childPanel, BoxLayout.LINE_AXIS));
		childPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		childLabel = new JLabel(
				LocaleManager.resources
						.getString(AttributeManager.FORM_NAME_CHILD),
				JLabel.LEFT);
		childPanel.add(childLabel);
		childPanel.add(guardianChild);
		mchnPanel.add(childPanel);

		JPanel womanPanel = new JPanel();
		womanPanel.setLayout(new BoxLayout(womanPanel, BoxLayout.LINE_AXIS));
		womanPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		womanLabel = new JLabel(
				LocaleManager.resources
						.getString(AttributeManager.FORM_NAME_WOMAN),
				JLabel.LEFT);
		womanPanel.add(womanLabel);
		womanPanel.add(guardianWoman);
		mchnPanel.add(womanPanel);

		JPanel motherLeaderPanel = new JPanel();
		motherLeaderPanel.setLayout(new BoxLayout(motherLeaderPanel,
				BoxLayout.LINE_AXIS));
		motherLeaderPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		motherLeader = new JLabel();
		leaderLabel = new JLabel(
				LocaleManager.resources
						.getString(AttributeManager.FORM_MOTHER_LEADER),
				JLabel.LEFT);
		motherLeaderPanel.add(leaderLabel);
		motherLeaderPanel.add(motherLeader);
		mchnPanel.add(motherLeaderPanel);

		JPanel motherVisitPanel = new JPanel();
		motherVisitPanel.setLayout(new BoxLayout(motherVisitPanel,
				BoxLayout.LINE_AXIS));
		motherVisitPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		motherVisit = new JLabel();
		visitLabel = new JLabel(
				LocaleManager.resources
						.getString(AttributeManager.FORM_VISIT_MOTHER),
				JLabel.LEFT);
		motherVisitPanel.add(visitLabel);
		motherVisitPanel.add(motherVisit);
		mchnPanel.add(motherVisitPanel);

		JPanel agriQuestionPanel = new JPanel();
		agriQuestionPanel.setLayout(new BoxLayout(agriQuestionPanel,
				BoxLayout.LINE_AXIS));
		agriQuestionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		agriAnswerLabel = new JLabel();
		agriQuestionLabel = new JLabel(
				LocaleManager.resources
						.getString(AttributeManager.FORM_AGRICULTURE_1),
				JLabel.LEFT);
		agriQuestionPanel.add(agriQuestionLabel);
		agriQuestionPanel.add(agriAnswerLabel);
		mchnPanel.add(agriQuestionPanel);

		JPanel agriPersonPanel = new JPanel();
		agriPersonPanel.setLayout(new BoxLayout(agriPersonPanel,
				BoxLayout.LINE_AXIS));
		agriPersonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		agriPersonLabel = new JLabel();
		giveNameLabel = new JLabel(
				LocaleManager.resources
						.getString(AttributeManager.FORM_GIVE_NAME),
				JLabel.LEFT);
		agriPersonPanel.add(giveNameLabel);
		agriPersonPanel.add(agriPersonLabel);
		mchnPanel.add(agriPersonPanel);

		// Agri panel.
		agriPanel = new JPanel();
		agriPanel.setBorder(BorderFactory
				.createTitledBorder(LocaleManager.resources
						.getString(BORDER_AGRI_INFO)));
		agriPanel.setLayout(new BoxLayout(agriPanel, BoxLayout.PAGE_AXIS));

		JPanel radioButtonsPanel = new JPanel();
		radioButtonsPanel.setLayout(new BoxLayout(radioButtonsPanel,
				BoxLayout.LINE_AXIS));

		agriCategoryPanel = new JPanel();
		agriCategoryPanel.setLayout(new BoxLayout(agriCategoryPanel,
				BoxLayout.PAGE_AXIS));
		agriCategoryPanel
				.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources
						.getString(AttributeManager.FORM_AGRICULTURE_CATEGORY)));
		agriCategoryPanel.setAlignmentY(TOP_ALIGNMENT);
		radioFarmer = new JRadioButton(
				LocaleManager.resources.getString(AttributeManager.FORM_FARMER),
				false);
		radioMuso = new JRadioButton(
				LocaleManager.resources.getString(AttributeManager.FORM_MUSO),
				false);
		radioCattleRancher = new JRadioButton(
				LocaleManager.resources
						.getString(AttributeManager.FORM_CATTLE_RANCHER),
				false);
		radioStoreOwner = new JRadioButton(
				LocaleManager.resources
						.getString(AttributeManager.FORM_STORE_OWNER),
				false);
		radioFisherman = new JRadioButton(
				LocaleManager.resources
						.getString(AttributeManager.FORM_FISHERMAN),
				false);
		radioArtisan = new JRadioButton(
				LocaleManager.resources
						.getString(AttributeManager.FORM_ARTISAN),
				false);
		radioOther = new JRadioButton(
				LocaleManager.resources.getString(AttributeManager.FORM_OTHER),
				false);
		agriCategoryPanel.add(radioFarmer);
		agriCategoryPanel.add(radioMuso);
		agriCategoryPanel.add(radioCattleRancher);
		agriCategoryPanel.add(radioStoreOwner);
		agriCategoryPanel.add(radioFisherman);
		agriCategoryPanel.add(radioArtisan);
		agriCategoryPanel.add(radioOther);
		radioFarmer.setEnabled(false);
		radioMuso.setEnabled(false);
		radioCattleRancher.setEnabled(false);
		radioStoreOwner.setEnabled(false);
		radioFisherman.setEnabled(false);
		radioArtisan.setEnabled(false);
		radioOther.setEnabled(false);
		radioButtonsPanel.add(agriCategoryPanel);


		seedTypesPanel = new JPanel();
		seedTypesPanel.setBorder(BorderFactory.createEmptyBorder());
		seedTypesPanel.setLayout(new BoxLayout(seedTypesPanel,
				BoxLayout.PAGE_AXIS));
		seedTypesPanel.setBorder(BorderFactory
				.createTitledBorder(LocaleManager.resources
						.getString(AttributeManager.FORM_SEED_TYPE)));
		seedTypesPanel.setAlignmentY(TOP_ALIGNMENT);
		radioVegetables = new JRadioButton(
				LocaleManager.resources
						.getString(AttributeManager.FORM_VEGETABLES),
				false);
		radioCereal = new JRadioButton(
				LocaleManager.resources.getString(AttributeManager.FORM_CEREAL),
				false);
		radioTubers = new JRadioButton(
				LocaleManager.resources.getString(AttributeManager.FORM_TUBERS),
				false);
		radioTree = new JRadioButton(
				LocaleManager.resources.getString(AttributeManager.FORM_TREE),
				false);
		radioGrafting = new JRadioButton(
				LocaleManager.resources
						.getString(AttributeManager.FORM_GRAFTING),
				false);
		radioCoffee = new JRadioButton(
				LocaleManager.resources.getString(AttributeManager.FORM_COFFEE),
				false);
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
		radioButtonsPanel.add(seedTypesPanel);

		toolsPanel = new JPanel();
		toolsPanel.setBorder(BorderFactory.createEmptyBorder());
		toolsPanel.setBorder(BorderFactory
				.createTitledBorder(LocaleManager.resources
						.getString(AttributeManager.FORM_TOOLS)));
		toolsPanel.setLayout(new BoxLayout(toolsPanel, BoxLayout.PAGE_AXIS));
		toolsPanel.setAlignmentY(TOP_ALIGNMENT);
		radioHoe = new JRadioButton(
				LocaleManager.resources.getString(AttributeManager.FORM_HOE),
				false);
		radioPickaxe = new JRadioButton(
				LocaleManager.resources
						.getString(AttributeManager.FORM_PICKAXE),
				false);
		radioWheelbarrow = new JRadioButton(
				LocaleManager.resources
						.getString(AttributeManager.FORM_WHEELBARROW),
				false);
		radioMachette = new JRadioButton(
				LocaleManager.resources
						.getString(AttributeManager.FORM_MACHETTE),
				false);
		radioPruningKnife = new JRadioButton(
				LocaleManager.resources
						.getString(AttributeManager.FORM_PRUNING_KNIFE),
				false);
		radioShovel = new JRadioButton(
				LocaleManager.resources.getString(AttributeManager.FORM_SHOVEL),
				false);
		radioCrowbar = new JRadioButton(
				LocaleManager.resources
						.getString(AttributeManager.FORM_CROWBAR),
				false);
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
		radioButtonsPanel.add(toolsPanel);

		organizationPanel = new JPanel();
		organizationPanel.setBorder(BorderFactory
				.createTitledBorder(LocaleManager.resources
						.getString(AttributeManager.FORM_ORGANIZATIONS)));
		organizationPanel.setLayout(new BoxLayout(organizationPanel,
				BoxLayout.PAGE_AXIS));
		organizationPanel.setAlignmentY(TOP_ALIGNMENT);
		radioFAO = new JRadioButton(
				LocaleManager.resources.getString(AttributeManager.FORM_FAO),
				false);
		radioSAVE = new JRadioButton(
				LocaleManager.resources
						.getString(AttributeManager.FORM_SAVE_ORG),
				false);
		radioCROSE = new JRadioButton(
				LocaleManager.resources.getString(AttributeManager.FORM_CROSE),
				false);
		radioPLAN = new JRadioButton(
				LocaleManager.resources.getString(AttributeManager.FORM_PLAN),
				false);
		radioMARDNR = new JRadioButton(
				LocaleManager.resources.getString(AttributeManager.FORM_MARDNR),
				false);
		radioOrganizationOther = new JRadioButton(
				LocaleManager.resources
						.getString(AttributeManager.FORM_OTHER_ORG),
				false);
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
		radioButtonsPanel.add(organizationPanel);

		agriPanel.add(radioButtonsPanel);

		JPanel otherAgriQuestionsPanel = new JPanel();
		otherAgriQuestionsPanel.setLayout(new BoxLayout(
				otherAgriQuestionsPanel, BoxLayout.PAGE_AXIS));
		otherAgriQuestionsPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

		JPanel landPanel = new JPanel();
		landPanel.setLayout(new BoxLayout(landPanel, BoxLayout.LINE_AXIS));
		landPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		landLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_LAND),
				JLabel.LEFT);
		land = new JLabel();
		landPanel.add(landLabel);
		landPanel.add(land);
		otherAgriQuestionsPanel.add(landPanel);
		
		JPanel healthProgramQuestionPanel = new JPanel();
		healthProgramQuestionPanel.setLayout(new BoxLayout(
				healthProgramQuestionPanel, BoxLayout.LINE_AXIS));
		healthProgramQuestionPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		healthLabel = new JLabel(
				LocaleManager.resources.getString(AttributeManager.FORM_HEALTH),
				JLabel.LEFT);
		healthProgramQuestionPanel.add(healthLabel);
		health = new JLabel();
		healthProgramQuestionPanel.add(health);
		otherAgriQuestionsPanel.add(healthProgramQuestionPanel);

		JPanel healthPersonPanel = new JPanel();
		healthPersonPanel.setLayout(new BoxLayout(healthPersonPanel,
				BoxLayout.LINE_AXIS));
		healthPersonPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
		healthPerson = new JLabel();
		giveNameLabel2 = new JLabel(
				LocaleManager.resources
						.getString(AttributeManager.FORM_GIVE_NAME),
				JLabel.LEFT);
		healthPersonPanel.add(giveNameLabel2);
		healthPersonPanel.add(healthPerson);
		otherAgriQuestionsPanel.add(healthPersonPanel);
		agriPanel.add(otherAgriQuestionsPanel);

		buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory
				.createTitledBorder(LocaleManager.resources
						.getString(BORDER_CONTROLS)));

		dossierLabel = new JLabel();
		dossierLabel.setText(LocaleManager.resources.getString(LABEL_DOSSIER));
		dossier = new JTextField(10);
		dossier.addKeyListener(this);

		toDbButton = new JButton(LocaleManager.resources.getString(BUTTON_SAVE));
		toDbButton.setEnabled(false);
		toDbMgrButton = new JButton(
				LocaleManager.resources.getString(BUTTON_DB_MGR));
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
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			JButton button = (JButton) e.getSource();
			if (button.equals(toDbButton)) {

				mGui.postMessageToTBS(dossier.getText());
			} else if (button.equals(toDbMgrButton)) {
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