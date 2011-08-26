/*
 * File: Beneficiary.java
 * 
 * Copyright (C) 2011 Humanitarian FOSS Project (http://hfoss.org).
 * 
 * This file is part of POSIT-Haiti Server.
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
 *
 */

package haiti.server.datamodel;

import haiti.server.datamodel.AttributeManager.BeneficiaryCategory;
import haiti.server.datamodel.AttributeManager.BeneficiaryType;
import haiti.server.datamodel.AttributeManager.MessageStatus;
import haiti.server.datamodel.AttributeManager.Sex;
import haiti.server.datamodel.AttributeManager.YnQuestion;

import java.util.Locale;
import java.util.ResourceBundle;

import java.util.Locale;
import java.util.ResourceBundle;

public class Beneficiary {
	
//	public enum BeneficiaryType {
//		UNKNOWN(-1), MCHN(0), AGRI(1), BOTH(2);
//		
//		private int code;
//		private BeneficiaryType(int code) {
//			this.code = code;
//		}
//		public int getCode() {
//			return code;
//		}
//	}
//	
//	public enum Sex {U, M, F};
//	public enum YnQuestion { U, Y, N};
//	
//	public enum BeneficiaryCategory {
//		UNKNOWN(-1), AGRI(0), EXPECTING(1), NURSING(2), PREVENTION(3),  MALNOURISHED(4);
//		
//		private int code;
//		private BeneficiaryCategory(int code) {
//			this.code = code;
//		}
//		public int getCode() {
//			return code;
//		}
//	}
//	
//	
//	public enum Abbreviated {TRUE, FALSE};
//	
//	public enum MessageStatus {
//		UNKNOWN(-1), NEW(0), UPDATED(1), PENDING(2), PROCESSED(3);
//		private int code;
//		private MessageStatus(int code) {
//			this.code = code;
//		}
//		public int getCode() {
//			return code;
//		}
//	};

	// Bookkeeping attributes
	private int id = -1;
	private MessageStatus status = MessageStatus.UNKNOWN;
	private BeneficiaryType beneficiaryType = BeneficiaryType.UNKNOWN;

	// Attributes in the order of the form on the phone
	private String firstName = "";
	private String lastName = "";
	private String locality = "";
	private String dob = "";
	private Sex sex = Sex.U; 
	private int numberInHome = -1;
	private BeneficiaryCategory beneficiaryCategory = BeneficiaryCategory.UNKNOWN;
	private String dossier = "";
	
	// MCHN Information
	private String healthCenter = "";
	private String distributionPost = "";
	private String commune = "";
	private String communeSection = "";
	private String guardianChild = "";
	private String guardianWoman = "";
	private String husband = "";
	private String father = "";
	private String agriPerson = "";
	
	private YnQuestion isMotherLeader = YnQuestion.N;
	private YnQuestion visitMotherLeader  = YnQuestion.N;
	private YnQuestion isAgri = YnQuestion.N;
	private YnQuestion isRelAgri = YnQuestion.N;
	
	// Agriculture categories
	
	private double amountOfLand = -1;
	private double seedQuantity = -1;	
	private int unitOfMeasurement = -1;
	
	private YnQuestion isFarmer = YnQuestion.U;
	private YnQuestion isMuso = YnQuestion.U;
	private YnQuestion isRancher = YnQuestion.U;
	private YnQuestion isFisherman = YnQuestion.U;
	private YnQuestion isOther = YnQuestion.U;
	private YnQuestion isArtisan = YnQuestion.U;
	private YnQuestion isStoreOwner = YnQuestion.U;

	private YnQuestion isFAO = YnQuestion.U;
	private YnQuestion isSAVE = YnQuestion.U;
	private YnQuestion isCROSE = YnQuestion.U;
	private YnQuestion isPLAN = YnQuestion.U;
	private YnQuestion isMARDNR = YnQuestion.U;
	private YnQuestion isOrganizationOther = YnQuestion.U;
	
	private YnQuestion isHealth = YnQuestion.N;
	private YnQuestion isRelHealth = YnQuestion.N;

	private YnQuestion getsVeggies = YnQuestion.U;
	private YnQuestion getsCereal = YnQuestion. U;
	private YnQuestion getsTubers = YnQuestion.U;
	private YnQuestion getsTrees = YnQuestion.U;
	private YnQuestion getsGrafting = YnQuestion.U;
	private YnQuestion getsCoffee = YnQuestion.U;

	private YnQuestion getsHoe = YnQuestion.U;
	private YnQuestion getsPickaxe = YnQuestion.U;
	private YnQuestion getsWheelbarrow = YnQuestion.U;  // Brouette
	private YnQuestion getsMachette = YnQuestion.U;
	private YnQuestion getsSerpette = YnQuestion.U;
	private YnQuestion getsPelle = YnQuestion.U;
	private YnQuestion getsBarreAMines = YnQuestion.U;
	
	private String healthPerson = "";

//	private YnQuestion[] isStuff = {isFarmer, isFisherman, isMuso, isRancher, isMerchant, isOther};
//	private YnQuestion[] hsStuff = {getsVeggies, getsCereal, getsTubers, getsTrees, getsHoe, getsPickaxe, getsWheelbarrow, getsMachette, getsSerpette, getsPelle, getsBarreAMines};
	
	/**
	 * Default constructor
	 */
	public Beneficiary() { }

	
	/**
	 * Constructs an instance from an SMS string of the form:
	 *      attr1=value1&attr2=val2& ... &attrN=valueN
	 * @param smsString
	 */
	public Beneficiary (String smsString) {
		System.out.println("Creating instance from SMS: " + smsString);
		String attrvalPairs[] = smsString.split(AttributeManager.PAIRS_SEPARATOR);
		
		for (int k = 0; k < attrvalPairs.length; k++) {
			String attrval[] = attrvalPairs[k].split(AttributeManager.ATTR_VAL_SEPARATOR);
			String attr = "", val = "";
			if (attrval.length == 2) {
				attr = attrval[0];
				val = attrval[1];
			} else if (attrval.length == 1) {
				attr = attrval[0];
			}
			
			System.out.println("Attr= " + attr + " val= " + val);
			
			try {
				if (attr.equals(AttributeManager.ABBREV_ID)) 
					id = Integer.parseInt(val);
				else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_DOSSIER))
					dossier = val;
				else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_FIRST)) 
					firstName = val;
				else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_LAST)) 
					lastName = val;
				else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_LOCALITY))
					locality = val;
				else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_DOB))
					dob = val;		
				else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_SEX))
					sex = Sex.valueOf(val.toUpperCase());
				else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_NUMBER_IN_HOME))
					numberInHome = Integer.parseInt(val);
				else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_COMMUNE_SECTION))
					communeSection = val;
				else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_TYPE)) {
					if (Integer.parseInt(val) == BeneficiaryType.MCHN.getCode())
						beneficiaryType = BeneficiaryType.MCHN;
					else if (Integer.parseInt(val) == BeneficiaryType.AGRI.getCode()) {
						beneficiaryType = BeneficiaryType.AGRI;
						beneficiaryCategory = BeneficiaryCategory.AGRI;
					}
					else if (Integer.parseInt(val) == BeneficiaryType.BOTH.getCode())
						beneficiaryType = BeneficiaryType.BOTH;
				}
				if (beneficiaryType == BeneficiaryType.MCHN || beneficiaryType == BeneficiaryType.BOTH) {
					if (attr.equalsIgnoreCase(AttributeManager.ABBREV_HEALTH_CENTER))
						healthCenter = AttributeManager.mapToLong(true, val);
					else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_CATEGORY))
						beneficiaryCategory = BeneficiaryCategory.valueOf( AttributeManager.getMapping(val.toUpperCase()));
					else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_DISTRIBUTION_POST))
						distributionPost = AttributeManager.mapToLong(true, val);
					else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_RELATIVE_1) && (beneficiaryCategory == BeneficiaryCategory.MALNOURISHED || beneficiaryCategory == BeneficiaryCategory.PREVENTION))
						guardianChild = val;
					else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_RELATIVE_1) && (beneficiaryCategory == BeneficiaryCategory.EXPECTING || beneficiaryCategory == BeneficiaryCategory.NURSING))
						guardianWoman = val;
					else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_IS_MOTHERLEADER)){
						if (val.equals(AttributeManager.ABBREV_TRUE))
							isMotherLeader = YnQuestion.Y;
						else if (val.equals(AttributeManager.ABBREV_FALSE))
							isMotherLeader = YnQuestion.N;
						else
							isMotherLeader = YnQuestion.U;
					}
					else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_VISIT_MOTHERLEADER)){
						if (val.equals(AttributeManager.ABBREV_TRUE))
							visitMotherLeader = YnQuestion.Y;
						else if (val.equals(AttributeManager.ABBREV_FALSE))
							visitMotherLeader = YnQuestion.N;
						else
							visitMotherLeader = YnQuestion.U;	
					}
					else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_PARTICIPATING_BENE)){
						if (val.equals(AttributeManager.ABBREV_TRUE))
							isAgri = YnQuestion.Y;
						else if (val.equals(AttributeManager.ABBREV_FALSE))
							isAgri = YnQuestion.N;
						else
							isAgri = YnQuestion.U;	
					}
					else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_RELATIVE_2)) {
						agriPerson = val;					
					}
				}
				if (beneficiaryType == BeneficiaryType.AGRI || beneficiaryType == BeneficiaryType.BOTH) {
					if (attr.equalsIgnoreCase(AttributeManager.ABBREV_LAND_AMT))
						amountOfLand = Double.parseDouble(val);
					else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_ISA))
						split(AttributeManager.decodeBinaryFieldsInt(Integer.parseInt(val), AttributeManager.isAFields), AttributeManager.ABBREV_ISA);
					else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_HASA))
						split(AttributeManager.decodeBinaryFieldsInt(Integer.parseInt(val), AttributeManager.hasAFields), AttributeManager.ABBREV_HASA);
					else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_SEED_QUANTITY))
						seedQuantity = Double.parseDouble(val);
					else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_MEASUREMENT_UNIT))
						unitOfMeasurement = Integer.parseInt(val);
					else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_RELATIVE_2)) 
						healthPerson = val;
				}
			} catch (NumberFormatException e) {
				System.out.println("Number format exception");
				e.printStackTrace();
				continue;
			} catch (NullPointerException e) {
				System.out.println("Null pointer exception");
				e.printStackTrace();
				continue;				
			} catch (IllegalArgumentException e) {
				System.out.println("Illegal argument exception");
				e.printStackTrace();
				continue;				
			}
			
// Here are all the attributes that need assignments of the right type. 
//  There should be an ABBREV_   constant in AttributeManager for each one.
			
//				+ firstName + ", lastName=" + lastName + ", locality="
//				+ locality + ", dob=" + dob + ", sex=" + sex
//				+ ", numberInHome=" + numberInHome + ", beneficiaryCategory="
//				+ beneficiaryCategory + ", healthCenter=" + healthCenter
//				+ ", distributionPost=" + distributionPost + ", commune="
//				+ commune + ", communeSection=" + communeSection
//				+ ", isMotherLeader=" + isMotherLeader + ", visitMotherLeader="
//				+ visitMotherLeader + ", isParticipatingAgri="
//				+ isParticipatingAgri + ", amountOfLand=" + amountOfLand
//				+ ", isFarmer=" + isFarmer + ", isMuso=" + isMuso
//				+ ", isRancher=" + isRancher + ", isMerchant=" + isMerchant
//				+ ", isFisherman=" + isFisherman + ", isOther=" + isOther
//				+ ", getsVeggies=" + getsVeggies + ", getsCereal=" + getsCereal
//				+ ", getsTubers=" + getsTubers + ", getsTrees=" + getsTrees
//				+ ", getsHoe=" + getsHoe + ", getsPickax=" + getsPickax
//				+ ", getsWheelbarrow=" + getsWheelbarrow + ", getsMachete="
//				+ getsMachete + ", getsSerpette=" + getsSerpette
//				+ ", getsPelle=" + getsPelle + ", getsBarreAMines="
//				+ getsBarreAMines + "]";
		
				
				
				
		}

	}
		
	public Beneficiary(String firstName, String lastName, 
			String commune, String communeSection,
			int age,
			Sex sex, 
			BeneficiaryCategory beneCategory, 
			int numberInHome) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.commune = commune;
		this.communeSection = communeSection;
		this.sex = sex;
		this.beneficiaryCategory = beneCategory;
		this.numberInHome = numberInHome;
	}
	
	/**
	 * This method splits the is and hs fields according to the given
	 * binary string and sets the corresponding attributes as yes
	 * @param binary representation of is and hs fields to be split
	 * @param isORhs field, depending on which one, the respective array will be chosen
	 */
	private void split(String binary, String isORhs) {
		String fields[] = binary.split(AttributeManager.PAIRS_SEPARATOR);
		
		for (int k = 0; k < fields.length; k++) {
			String attrval[] = fields[k].split(AttributeManager.ATTR_VAL_SEPARATOR);
			String attr = "", val = "";
			if (attrval.length == 2) {
				attr = attrval[0];
				val = attrval[1];
			} else if (attrval.length == 1) {
				attr = attrval[0];
			}
			
			System.out.println("Attr= " + attr + " val= " + val);
			
			try {
				if (isORhs.equalsIgnoreCase(AttributeManager.ABBREV_ISA)) {
					if (attr.equalsIgnoreCase(AttributeManager.isAFields[0]))
						isFarmer = YnQuestion.Y;
					if (attr.equalsIgnoreCase(AttributeManager.isAFields[1]))
						isFisherman = YnQuestion.Y;		
					if (attr.equalsIgnoreCase(AttributeManager.isAFields[2]))
						isMuso = YnQuestion.Y;
					if (attr.equalsIgnoreCase(AttributeManager.isAFields[3]))
						isRancher = YnQuestion.Y;
					if (attr.equalsIgnoreCase(AttributeManager.isAFields[4]))
						isStoreOwner = YnQuestion.Y;
					if (attr.equalsIgnoreCase(AttributeManager.isAFields[5]))
						isOther = YnQuestion.Y;
					if (attr.equalsIgnoreCase(AttributeManager.isAFields[6]))
						isArtisan = YnQuestion.Y;
					if (attr.equalsIgnoreCase(AttributeManager.isAFields[7]))
						isFAO = YnQuestion.Y;
					if (attr.equalsIgnoreCase(AttributeManager.isAFields[8]))
						isSAVE = YnQuestion.Y;
					if (attr.equalsIgnoreCase(AttributeManager.isAFields[9]))
						isCROSE = YnQuestion.Y;
					if (attr.equalsIgnoreCase(AttributeManager.isAFields[10]))
						isPLAN = YnQuestion.Y;
					if (attr.equalsIgnoreCase(AttributeManager.isAFields[11]))
						isMARDNR = YnQuestion.Y;
					if (attr.equalsIgnoreCase(AttributeManager.isAFields[12]))
						isOrganizationOther = YnQuestion.Y;
					if (attr.equalsIgnoreCase(AttributeManager.isAFields[13]))
						isAgri = YnQuestion.Y;
					if (attr.equalsIgnoreCase(AttributeManager.isAFields[14]))
						isRelAgri = YnQuestion.Y;
					if (attr.equalsIgnoreCase(AttributeManager.isAFields[15]))
						isHealth = YnQuestion.Y;
					if (attr.equalsIgnoreCase(AttributeManager.isAFields[16]))
						isRelHealth = YnQuestion.Y;
				}
				else {
					if (attr.equalsIgnoreCase(AttributeManager.hasAFields[0]))
						getsVeggies = YnQuestion.Y;
					if (attr.equalsIgnoreCase(AttributeManager.hasAFields[1]))
						getsCereal = YnQuestion.Y;
					if (attr.equalsIgnoreCase(AttributeManager.hasAFields[2]))
						getsTubers = YnQuestion.Y;			
					if (attr.equalsIgnoreCase(AttributeManager.hasAFields[3]))
						getsTrees = YnQuestion.Y;
					if (attr.equalsIgnoreCase(AttributeManager.hasAFields[4]))
						getsHoe = YnQuestion.Y;
					if (attr.equalsIgnoreCase(AttributeManager.hasAFields[5]))
						getsPickaxe = YnQuestion.Y;		
					if (attr.equalsIgnoreCase(AttributeManager.hasAFields[6]))
						getsWheelbarrow = YnQuestion.Y;		
					if (attr.equalsIgnoreCase(AttributeManager.hasAFields[7]))
						getsMachette = YnQuestion.Y;	
					if (attr.equalsIgnoreCase(AttributeManager.hasAFields[8]))
						getsSerpette = YnQuestion.Y;
					if (attr.equalsIgnoreCase(AttributeManager.hasAFields[9]))
						getsPelle = YnQuestion.Y;
					if (attr.equalsIgnoreCase(AttributeManager.hasAFields[10]))
						getsBarreAMines = YnQuestion.Y;
					if (attr.equalsIgnoreCase(AttributeManager.hasAFields[11]))
						getsCoffee= YnQuestion.Y;
					if (attr.equalsIgnoreCase(AttributeManager.hasAFields[12]))
						getsGrafting = YnQuestion.Y;
				}
			} catch (NumberFormatException e) {
				System.out.println("Number format exception");
				e.printStackTrace();
				continue;
			} catch (NullPointerException e) {
				System.out.println("Null pointer exception");
				e.printStackTrace();
				continue;				
			} catch (IllegalArgumentException e) {
				System.out.println("Illegal argument exception");
				e.printStackTrace();
				continue;				
			}
		}
	}

	public BeneficiaryType getBeneficiaryType() {
		return beneficiaryType;
	}


	public void setBeneficiaryType(BeneficiaryType beneficiaryType) {
		this.beneficiaryType = beneficiaryType;
	}


	/**
	 * For temporary development purposes only.
	 * @return
	 */
	public String getRandomDossierNumber(){
		return "" + (int)(Math.random() * 1000000);
	}

	
	public MessageStatus getStatus() {
		return status;
	}

	public void setStatus(MessageStatus status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	
	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getDossier() {
		return dossier;
	}


	public void setDossier(String dossier) {
		this.dossier = dossier;
	}


	public String getAddress() {
		return locality;
	}

	public void setAddress(String address) {
		this.locality = address;
	}
	
	public String getCommune() {
		return commune;
	}

	public void setCommune(String commune) {
		this.commune = commune;
	}

	public String getCommuneSection() {
		return communeSection;
	}

	public void setCommuneSection(String communeSection) {
		this.communeSection = communeSection;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public int getNumberInHome() {
		return numberInHome;
	}

	public void setNumberInHome(int numberInHome) {
		this.numberInHome = numberInHome;
	}

	public BeneficiaryCategory getBeneficiaryCategory() {
		return beneficiaryCategory;
	}

	public void setBeneficiaryCategory(BeneficiaryCategory beneficiaryCategory) {
		this.beneficiaryCategory = beneficiaryCategory;
	}
	
	public String getLocality() {
		return locality;
	}


	public void setLocality(String locality) {
		this.locality = locality;
	}


	public String getDob() {
		return dob;
	}


	public void setDob(String dob) {
		this.dob = dob;
	}


	public String getHealthCenter() {
		return healthCenter;
	}


	public void setHealthCenter(String healthCenter) {
		this.healthCenter = healthCenter;
	}


	public String getDistributionPost() {
		return distributionPost;
	}


	public void setDistributionPost(String distributionPost) {
		this.distributionPost = distributionPost;
	}


	public String getGuardianChild() {
		return guardianChild;
	}


	public void setGuardianChild(String guardianChild) {
		this.guardianChild = guardianChild;
	}


	public String getGuardianWoman() {
		return guardianWoman;
	}


	public void setGuardianWoman(String guardianWoman) {
		this.guardianWoman = guardianWoman;
	}


	public String getHusband() {
		return husband;
	}


	public void setHusband(String husband) {
		this.husband = husband;
	}


	public String getFather() {
		return father;
	}


	public void setFather(String father) {
		this.father = father;
	}


	public String getAgriPerson() {
		return agriPerson;
	}


	public void setAgriPerson(String agriPerson) {
		this.agriPerson = agriPerson;
	}


	public String getHealthPerson() {
		return healthPerson;
	}


	public void setHealthPerson(String healthPerson) {
		this.healthPerson = healthPerson;
	}


	public YnQuestion getIsMotherLeader() {
		return isMotherLeader;
	}


	public void setIsMotherLeader(YnQuestion isMotherLeader) {
		this.isMotherLeader = isMotherLeader;
	}


	public YnQuestion getVisitMotherLeader() {
		return visitMotherLeader;
	}


	public void setVisitMotherLeader(YnQuestion visitMotherLeader) {
		this.visitMotherLeader = visitMotherLeader;
	}


//	public YnQuestion getIsParticipatingAgri() {
//		return isParticipatingAgri;
//	}
//
//
//	public void setIsParticipatingAgri(YnQuestion isParticipatingAgri) {
//		this.isParticipatingAgri = isParticipatingAgri;
//	}


	public double getAmountOfLand() {
		return amountOfLand;
	}


	public void setAmountOfLand(double amountOfLand) {
		this.amountOfLand = amountOfLand;
	}


	public YnQuestion getIsFarmer() {
		return isFarmer;
	}


	public void setIsFarmer(YnQuestion isFarmer) {
		this.isFarmer = isFarmer;
	}


	public YnQuestion getIsMuso() {
		return isMuso;
	}


	public void setIsMuso(YnQuestion isMuso) {
		this.isMuso = isMuso;
	}


	public YnQuestion getIsRancher() {
		return isRancher;
	}


	public void setIsRancher(YnQuestion isRancher) {
		this.isRancher = isRancher;
	}


	public YnQuestion getIsFisherman() {
		return isFisherman;
	}


	public void setIsFisherman(YnQuestion isFisherman) {
		this.isFisherman = isFisherman;
	}


	public YnQuestion getIsOther() {
		return isOther;
	}


	public void setIsOther(YnQuestion isOther) {
		this.isOther = isOther;
	}


	public YnQuestion getIsArtisan() {
		return isArtisan;
	}


	public void setIsArtisan(YnQuestion isArtisan) {
		this.isArtisan = isArtisan;
	}

	public YnQuestion getIsStoreOwner() {
		return isStoreOwner;
	}


	public void setIsStoreOwner(YnQuestion isStoreOwner) {
		this.isStoreOwner = isStoreOwner;
	}

	public YnQuestion getIsFAO() {
		return isFAO;
	}


	public void setIsFAO(YnQuestion isFAO) {
		this.isFAO = isFAO;
	}


	public YnQuestion getIsSAVE() {
		return isSAVE;
	}


	public void setIsSAVE(YnQuestion isSAVE) {
		this.isSAVE = isSAVE;
	}


	public YnQuestion getIsCROSE() {
		return isCROSE;
	}


	public void setIsCROSE(YnQuestion isCROSE) {
		this.isCROSE = isCROSE;
	}


	public YnQuestion getIsPLAN() {
		return isPLAN;
	}


	public void setIsPLAN(YnQuestion isPLAN) {
		this.isPLAN = isPLAN;
	}


	public YnQuestion getIsMARDNR() {
		return isMARDNR;
	}


	public void setIsMARDNR(YnQuestion isMARDNR) {
		this.isMARDNR = isMARDNR;
	}


	public YnQuestion getIsOrganizationOther() {
		return isOrganizationOther;
	}


	public void setIsOTHER(YnQuestion isOrganizationOther) {
		this.isOrganizationOther = isOrganizationOther;
	}


	public YnQuestion getIsAgri() {
		return isAgri;
	}


	public void setIsAgri(YnQuestion isAgri) {
		this.isAgri = isAgri;
	}


	public YnQuestion getIsRelAgri() {
		return isRelAgri;
	}


	public void setIsRelAgri(YnQuestion isRelAgri) {
		this.isRelAgri = isRelAgri;
	}


	public YnQuestion getIsHealth() {
		return isHealth;
	}


	public void setIsHealth(YnQuestion isHealth) {
		this.isHealth = isHealth;
	}


	public YnQuestion getIsRelHealth() {
		return isRelHealth;
	}


	public void setIsRelHealth(YnQuestion isRelHealth) {
		this.isRelHealth = isRelHealth;
	}


	public YnQuestion getGetsVeggies() {
		return getsVeggies;
	}


	public void setGetsVeggies(YnQuestion getsVeggies) {
		this.getsVeggies = getsVeggies;
	}


	public YnQuestion getGetsCereal() {
		return getsCereal;
	}


	public void setGetsCereal(YnQuestion getsCereal) {
		this.getsCereal = getsCereal;
	}


	public YnQuestion getGetsTubers() {
		return getsTubers;
	}


	public void setGetsTubers(YnQuestion getsTubers) {
		this.getsTubers = getsTubers;
	}


	public YnQuestion getGetsTrees() {
		return getsTrees;
	}


	public void setGetsTrees(YnQuestion getsTrees) {
		this.getsTrees = getsTrees;
	}

	public YnQuestion getGetsGrafting() {
		return getsGrafting;
	}


	public void setGetsGrafting(YnQuestion getsGrafting) {
		this.getsGrafting = getsGrafting;
	}

	
	public YnQuestion getGetsCoffee() {
		return getsCoffee;
	}


	public void setGetsCoffee(YnQuestion getsCoffee) {
		this.getsCoffee = getsCoffee;
	}
	
	public YnQuestion getGetsHoe() {
		return getsHoe;
	}


	public void setGetsHoe(YnQuestion getsHoe) {
		this.getsHoe = getsHoe;
	}


	public YnQuestion getGetsPickaxe() {
		return getsPickaxe;
	}


	public void setGetsPickaxe(YnQuestion getsPickaxe) {
		this.getsPickaxe = getsPickaxe;
	}


	public YnQuestion getGetsWheelbarrow() {
		return getsWheelbarrow;
	}


	public void setGetsWheelbarrow(YnQuestion getsWheelbarrow) {
		this.getsWheelbarrow = getsWheelbarrow;
	}


	public YnQuestion getGetsMachette() {
		return getsMachette;
	}


	public void setGetsMachete(YnQuestion getsMachette) {
		this.getsMachette = getsMachette;
	}


	public YnQuestion getGetsSerpette() {
		return getsSerpette;
	}


	public void setGetsSerpette(YnQuestion getsSerpette) {
		this.getsSerpette = getsSerpette;
	}


	public YnQuestion getGetsPelle() {
		return getsPelle;
	}


	public void setGetsPelle(YnQuestion getsPelle) {
		this.getsPelle = getsPelle;
	}


	public YnQuestion getGetsBarreAMines() {
		return getsBarreAMines;
	}


	public void setGetsBarreAMines(YnQuestion getsBarreAMines) {
		this.getsBarreAMines = getsBarreAMines;
	}


	public String toString(String separator) {
		return "id = " + id + separator + 
		"status = " + status + separator + 
		"firstName = " + firstName + separator +
		"lastName = " + lastName + separator +
		"commune = " + commune + separator +
		"communeSection = " + communeSection + separator +
		"numberInHome = " + numberInHome + separator +
		"beneficiaryCategory = " + beneficiaryCategory;
	}
	
	@Override
	public String toString() {
		return "Beneficiary [id=" + id + ", status=" + status + ", firstName="
				+ firstName + ", lastName=" + lastName + ", locality="
				+ locality + ", dob=" + dob + ", sex=" + sex
				+ ", numberInHome=" + numberInHome + ", beneficiaryCategory="
				+ beneficiaryCategory + ", healthCenter=" + healthCenter
				+ ", distributionPost=" + distributionPost + ", commune="
				+ commune + ", communeSection=" + communeSection
				+ ", isMotherLeader=" + isMotherLeader + ", visitMotherLeader="
				+ visitMotherLeader + ", isParticipatingAgri="
				+ isAgri + ", amountOfLand=" + amountOfLand
				+ ", isFarmer=" + isFarmer + ", isMuso=" + isMuso
				+ ", isRancher=" + isRancher + ", isStoreOwner=" + isStoreOwner
				+ ", isFisherman=" + isFisherman + ", isOther=" + isOther
				+ ", getsVeggies=" + getsVeggies + ", getsCereal=" + getsCereal
				+ ", getsTubers=" + getsTubers + ", getsTrees=" + getsTrees
				+ ", getsHoe=" + getsHoe + ", getsPickax=" + getsPickaxe
				+ ", getsWheelbarrow=" + getsWheelbarrow + ", getsMachete="
				+ getsMachette + ", getsSerpette=" + getsSerpette
				+ ", getsPelle=" + getsPelle + ", getsBarreAMines="
				+ getsBarreAMines + "]";
	}


	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		Beneficiary ben = new Beneficiary("AV=1,i=068MP-FAT,t=0,st=1,fn=Denisana,ln=Balthazar,a=Saint Michel,b=1947/11/31,s=F,c=P,d=24", Abbreviated.TRUE);
		// System.out.println(ben.toString("&"));
		// System.out.println(ben.toString());
//		System.out.println(ben.toString());
		
		String s = "=";
		System.out.println(s.split("=").length);
		
		// If you comment out the next line, it will generate a null pointer exception when
		// it calls map in AttributeManager.  
		//AttributeManager.init();
		// This string contains some number format exceptions.
		Beneficiary b = new Beneficiary("id=$153,c=M,h=Centre de sante une, sender=+18605022947,status=0,message_type=0,created_on=2011-06-10 21:38:47,modified_on=2011-06-10 21:38:47,AV=27,t=0,s=0,m=0,n=$4,f=Rachel,l=Foecking,a=25699 Yeoman Drive,b=1989/1/11,g=F,d=Repartition des postes une,");
	}

}
