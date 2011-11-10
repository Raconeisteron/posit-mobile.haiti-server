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

public class BeneficiaryUpdateFormStatic extends FormPanel implements
		ActionListener {

	public static final String BUTTON_PROCESS = "ProcessUpdate";

	public static final String BORDER_BENEFICIARY_UPDATE = "BeneficiaryUpdateForm";
	public static final String BORDER_BENEFICIARY_INFO = "BeneficiaryInfo";
	public static final String BORDER_UPDATE_INFO = "UpdateInfo";
	public static final String BORDER_CONTROLS = "Controls";

	private Update mUpdate;

	private DataEntryGUI mGui;

	private JButton processButton;

	private JPanel
			buttonPanel,
			benifPanel, updatePanel;

	private JLabel dossier, firstName, lastName, dob,
			beneficiaryCategory, present, transferredWhy, modificationsWhy,
			suspendWhy, change, changeType, communeSection, locality, other;

	private JLabel dossierLabel, firstNameLabel,
			lastNameLabel,
			dobLabel,
			beneficiaryLabel, presentLabel, transferredLabel,
			modificationsLabel, suspendLabel, transferredWhyLabel,
			modificationsWhyLabel, suspendWhyLabel, changeLabel,
			changeTypeLabel, communeSectionLabel, localityLabel, otherLabel;

	public BeneficiaryUpdateFormStatic(DataEntryGUI gui) {
		mGui = gui;
		setUpDataEntryPanel();
	}

	/**
	 * Called from GUI to fill in data in the form.
	 * 
	 * @param update
	 */
	public void fillInForm(Object o, DAO reader) {
		Update update = (Update) o;
		mUpdate = update;
		this.dossier.setText(update.getDossier());

		this.firstName.setText(update.getFirstName());
		this.lastName.setText(update.getLastName());

		beneficiaryCategory.setText(update.getBeneficiaryCategory().name());
		dob.setText(update.getDob());
		present.setText(update.getPresent().name());
		change.setText(update.getChange().name());
		changeType.setText(LocaleManager.resources
				.getString(AttributeManager.transferTypes[Integer
						.parseInt(update.getChangeType())]));
		communeSection.setText(AttributeManager.mapToLong(true,update.getCommuneSection()));
		locality.setText(update.getLocality());
		other.setText(update.getOther());
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
	 */
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);

		processButton
				.setText(LocaleManager.resources.getString(BUTTON_PROCESS));
		this.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources
				.getString(BORDER_BENEFICIARY_UPDATE)));
		benifPanel.setBorder(BorderFactory
				.createTitledBorder(LocaleManager.resources
						.getString(BORDER_BENEFICIARY_INFO)));

		dossierLabel.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_DOSSIER));
		firstNameLabel.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_FIRST_NAME));
		lastNameLabel.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_LAST_NAME));
		dobLabel.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_DOB));
		beneficiaryLabel.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_BENEFICIARY_CATEGORY));
		communeSectionLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_SECTION));
		localityLabel.setText(LocaleManager.resources.getString(AttributeManager.FORM_ADDRESS));
		otherLabel.setText(LocaleManager.resources.getString(AttributeManager.OTHER_REASON));

		updatePanel.setBorder(BorderFactory
				.createTitledBorder(LocaleManager.resources
						.getString(BORDER_UPDATE_INFO)));
		presentLabel.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_PRESENT));

		changeLabel.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_CHANGE));
		changeTypeLabel.setText(LocaleManager.resources
				.getString(AttributeManager.FORM_CHANGE_TYPE));

	}

	/**
	 * Sets up the panel used for Beneficiary registration.
	 * 
	 * @return
	 */
	protected void setUpDataEntryPanel() {

		this.setBorder(BorderFactory.createTitledBorder(LocaleManager.resources
				.getString(BORDER_BENEFICIARY_UPDATE)));
		this.setLayout(new GridLayout(0, 1));


		// Beneficiary Information Panel
		benifPanel = new JPanel();
		benifPanel.setBorder(BorderFactory
				.createTitledBorder(LocaleManager.resources
						.getString(BORDER_BENEFICIARY_INFO)));
		benifPanel.setLayout(new GridLayout(0, 4));
		dossier = new JLabel();
		firstName = new JLabel();	
		lastName = new JLabel();
		dob = new JLabel();


		dossierLabel = new JLabel(
				LocaleManager.resources
						.getString(AttributeManager.FORM_DOSSIER));
		benifPanel.add(dossierLabel);
		benifPanel.add(dossier);

		firstNameLabel = new JLabel(
				LocaleManager.resources
						.getString(AttributeManager.FORM_FIRST_NAME));

		benifPanel.add(firstNameLabel);
		benifPanel.add(firstName);
		lastNameLabel = new JLabel(
				LocaleManager.resources
						.getString(AttributeManager.FORM_LAST_NAME));
		benifPanel.add(lastNameLabel);
		benifPanel.add(lastName);

		dobLabel = new JLabel(
				LocaleManager.resources.getString(AttributeManager.FORM_DOB));
		benifPanel.add(dobLabel);

		benifPanel.add(dob);

		// Beneficiary radio buttons
		beneficiaryLabel = new JLabel(
				LocaleManager.resources
						.getString(AttributeManager.FORM_BENEFICIARY_CATEGORY));
		benifPanel.add(beneficiaryLabel);
		beneficiaryCategory = new JLabel();
		benifPanel.add(beneficiaryCategory);

		// Update panel
		updatePanel = new JPanel();
		updatePanel.setBorder(BorderFactory
				.createTitledBorder(LocaleManager.resources
						.getString(BORDER_UPDATE_INFO)));
		updatePanel.setLayout(new GridLayout(0, 4));
		
		presentLabel = new JLabel(
				LocaleManager.resources
						.getString(AttributeManager.FORM_PRESENT));
		updatePanel.add(presentLabel);
		present = new JLabel();
		updatePanel.add(present);

		changeLabel = new JLabel(
				LocaleManager.resources.getString(AttributeManager.FORM_CHANGE));
		changeTypeLabel = new JLabel(
				LocaleManager.resources
						.getString(AttributeManager.FORM_CHANGE_TYPE));
		otherLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.OTHER_REASON));
		communeSectionLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_SECTION));
		localityLabel = new JLabel(LocaleManager.resources.getString(AttributeManager.FORM_ADDRESS));
		change = new JLabel();
		changeType = new JLabel();
		other = new JLabel();
		communeSection = new JLabel();
		locality = new JLabel();

		updatePanel.add(changeLabel);
		updatePanel.add(change);
		updatePanel.add(changeTypeLabel);
		updatePanel.add(changeType);
		updatePanel.add(otherLabel);
		updatePanel.add(other);
		updatePanel.add(communeSectionLabel);
		updatePanel.add(communeSection);
		updatePanel.add(localityLabel);
		updatePanel.add(locality);


		// Control panel
		buttonPanel = new JPanel();
		buttonPanel.setBorder(BorderFactory
				.createTitledBorder(LocaleManager.resources
						.getString(BORDER_CONTROLS)));
		processButton = new JButton(
				LocaleManager.resources.getString(BUTTON_PROCESS));
		this.add(processButton);

		processButton.addActionListener(this);
		this.add(benifPanel, "North");
		this.add(updatePanel, "Center");
		this.add(buttonPanel, "South");
		buttonPanel.add(processButton);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() instanceof JButton) {
			JButton button = (JButton) e.getSource();
			if (button.equals(processButton)) {
				mGui.setUpdateStatus(dossier.getText());

			}

		}
	}

}
