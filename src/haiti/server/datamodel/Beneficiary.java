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

import java.util.Locale;
import java.util.ResourceBundle;

import java.util.Locale;
import java.util.ResourceBundle;

public class Beneficiary {
	
	public enum Sex {U, M, F};
	public enum YnQuestion { U, Y, N};
	
	public enum BeneficiaryCategory {
		UNKNOWN(-1), EXPECTING(0), NURSING(1), PREVENTION(2),  MALNOURISHED(3);
		
		private int code;
		private BeneficiaryCategory(int code) {
			this.code = code;
		}
		public int getCode() {
			return code;
		}
	}
	
	
	public enum Abbreviated {TRUE, FALSE};
	
	public enum Status {
		UNKNOWN(-1), NEW(0), UPDATED(1), PENDING(2), PROCESSED(3);
		private int code;
		private Status(int code) {
			this.code = code;
		}
		public int getCode() {
			return code;
		}
	};

	// Bookeeping attributes
	private int id = -1;
	private Status status = Status.UNKNOWN;
	
	// Attributes in the order of the form on the phone
	private String firstName = "";
	private String lastName = "";
	private String locality = "";
	private String dob = "";
	private Sex sex = Sex.U; 
	private int numberInHome = -1;
	private BeneficiaryCategory beneficiaryCategory = BeneficiaryCategory.UNKNOWN;
	
	// MCHN Information
	private String healthCenter = "";
	private String distributionPost = "";
	private String commune = "";
	private String communeSection = "";
	
	
	private YnQuestion isMotherLeader = YnQuestion.U;
	private YnQuestion visitMotherLeader  = YnQuestion.U;
	private YnQuestion isParticipatingAgri = YnQuestion.U;
	
	// Agriculture categories
	
	private double amountOfLand = -1;
	
	private YnQuestion isFarmer = YnQuestion.U;
	private YnQuestion isMuso = YnQuestion.U;
	private YnQuestion isRancher = YnQuestion.U;
	private YnQuestion isMerchant = YnQuestion.U;
	private YnQuestion isFisherman = YnQuestion.U;
	private YnQuestion isOther = YnQuestion.U;
	
	private YnQuestion getsVeggies = YnQuestion.U;
	private YnQuestion getsCereal = YnQuestion. U;
	private YnQuestion getsTubers = YnQuestion.U;
	private YnQuestion getsTrees = YnQuestion.U;
	
	private YnQuestion getsHoe = YnQuestion.U;
	private YnQuestion getsPickax = YnQuestion.U;
	private YnQuestion getsWheelbarrow = YnQuestion.U;  // Brouette
	private YnQuestion getsMachete = YnQuestion.U;
	private YnQuestion getsSerpette = YnQuestion.U;
	private YnQuestion getsPelle = YnQuestion.U;
	private YnQuestion getsBarreAMines = YnQuestion.U;

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
				else if (attr.equals(AttributeManager.ABBREV_FIRST)) 
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
				else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_CATEGORY))
					beneficiaryCategory = 
						BeneficiaryCategory.valueOf( AttributeManager.getMapping(val.toUpperCase()));
				else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_HEALTH_CENTER))
					healthCenter = val;
				else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_IS_MOTHERLEADER))
					isMotherLeader = YnQuestion.valueOf(val.toUpperCase());
				else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_VISIT_MOTHERLEADER))
					visitMotherLeader = YnQuestion.valueOf(val.toUpperCase());			
				else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_IS_AGRI))
					visitMotherLeader = YnQuestion.valueOf(val.toUpperCase());	
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
	 * For temporary development purposes only.
	 * @return
	 */
	public String getRandomDossierNumber(){
		return "" + (int)(Math.random() * 1000000);
	}

	
	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
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


	public YnQuestion getIsParticipatingAgri() {
		return isParticipatingAgri;
	}


	public void setIsParticipatingAgri(YnQuestion isParticipatingAgri) {
		this.isParticipatingAgri = isParticipatingAgri;
	}


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


	public YnQuestion getIsMerchant() {
		return isMerchant;
	}


	public void setIsMerchant(YnQuestion isMerchant) {
		this.isMerchant = isMerchant;
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


	public YnQuestion getGetsHoe() {
		return getsHoe;
	}


	public void setGetsHoe(YnQuestion getsHoe) {
		this.getsHoe = getsHoe;
	}


	public YnQuestion getGetsPickax() {
		return getsPickax;
	}


	public void setGetsPickax(YnQuestion getsPickax) {
		this.getsPickax = getsPickax;
	}


	public YnQuestion getGetsWheelbarrow() {
		return getsWheelbarrow;
	}


	public void setGetsWheelbarrow(YnQuestion getsWheelbarrow) {
		this.getsWheelbarrow = getsWheelbarrow;
	}


	public YnQuestion getGetsMachete() {
		return getsMachete;
	}


	public void setGetsMachete(YnQuestion getsMachete) {
		this.getsMachete = getsMachete;
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
				+ isParticipatingAgri + ", amountOfLand=" + amountOfLand
				+ ", isFarmer=" + isFarmer + ", isMuso=" + isMuso
				+ ", isRancher=" + isRancher + ", isMerchant=" + isMerchant
				+ ", isFisherman=" + isFisherman + ", isOther=" + isOther
				+ ", getsVeggies=" + getsVeggies + ", getsCereal=" + getsCereal
				+ ", getsTubers=" + getsTubers + ", getsTrees=" + getsTrees
				+ ", getsHoe=" + getsHoe + ", getsPickax=" + getsPickax
				+ ", getsWheelbarrow=" + getsWheelbarrow + ", getsMachete="
				+ getsMachete + ", getsSerpette=" + getsSerpette
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
