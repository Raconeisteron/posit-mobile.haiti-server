package haiti.server.datamodel;

import haiti.server.datamodel.AttributeManager.BeneficiaryCategory;
import haiti.server.datamodel.AttributeManager.BeneficiaryType;
import haiti.server.datamodel.AttributeManager.MessageStatus;
import haiti.server.datamodel.AttributeManager.Sex;
import haiti.server.datamodel.AttributeManager.YnQuestion;

public class Update {
	
	private int id = -1;
	private MessageStatus status = MessageStatus.UNKNOWN;
	private BeneficiaryType beneficiaryType = BeneficiaryType.UNKNOWN;

	// Attributes in the order of the form on the phone
	private String firstName = "";
	private String lastName = "";
	private String dob = "";
	private Sex sex = Sex.U; 
	private BeneficiaryCategory beneficiaryCategory = BeneficiaryCategory.UNKNOWN;
	private String dossier = "";
	
	private YnQuestion present = YnQuestion.U;
	private YnQuestion change = YnQuestion.U;
	private String changeType = "";
	
	// MCHN Information
	public Update (String smsString) {
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
				else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_DOB))
					dob = val;		
				else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_SEX))
					sex = Sex.valueOf(val.toUpperCase());
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
				else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_CATEGORY))
					beneficiaryCategory = BeneficiaryCategory.valueOf( AttributeManager.getMapping(val.toUpperCase()));
				else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_Q_PRESENT)) {
					if (val.equalsIgnoreCase(AttributeManager.ABBREV_TRUE))
						present = YnQuestion.Y;
					else
						present = YnQuestion.N;
				}
				else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_Q_CHANGE)) {
					if (val.equalsIgnoreCase(AttributeManager.ABBREV_TRUE))
						change = YnQuestion.Y;
					else
						change = YnQuestion.N;
				}
				else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_CHANGE_TYPE))
					changeType = val;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public MessageStatus getStatus() {
		return status;
	}

	public void setStatus(MessageStatus status) {
		this.status = status;
	}

	public BeneficiaryType getBeneficiaryType() {
		return beneficiaryType;
	}

	public void setBeneficiaryType(BeneficiaryType beneficiaryType) {
		this.beneficiaryType = beneficiaryType;
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

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
	}

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

	public BeneficiaryCategory getBeneficiaryCategory() {
		return beneficiaryCategory;
	}

	public void setBeneficiaryCategory(BeneficiaryCategory beneficiaryCategory) {
		this.beneficiaryCategory = beneficiaryCategory;
	}

	public String getDossier() {
		return dossier;
	}

	public void setDossier(String dossier) {
		this.dossier = dossier;
	}

	public YnQuestion getPresent() {
		return present;
	}

	public void setPresent(YnQuestion present) {
		this.present = present;
	}

	public YnQuestion getChange() {
		return change;
	}

	public void setChange(YnQuestion change) {
		this.change = change;
	}

	public String getChangeType() {
		return changeType;
	}

	public void setChangeType(String changeType) {
		this.changeType = changeType;
	}
	
}
