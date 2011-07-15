package haiti.server.datamodel;

import haiti.server.datamodel.AttributeManager.MessageStatus;

public class Bulk {

	private int id = -1;
	private MessageStatus status = MessageStatus.UNKNOWN;
	private String avNum = "";

	public Bulk (String smsString) {
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
				else if (attr.equalsIgnoreCase(AttributeManager.ABBREV_AV))
					avNum = val;
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

	public String getAvNum() {
		return avNum;
	}

	public void setAvNum(String avNum) {
		this.avNum = avNum;
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

}
