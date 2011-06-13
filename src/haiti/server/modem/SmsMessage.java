package haiti.server.modem;

import haiti.server.datamodel.AttributeManager;
import haiti.server.gui.*;

public class SmsMessage {

//	public enum MessageStatus {NEW, PENDING, PROCESSED, UNKNOWN};
//	public enum MessageType {BENEFICIARY, UPDATE, UNKNOWN};
	public enum Abbreviated {TRUE, FALSE};
	
	private int AVnum = -1;
	private SmsReader.MessageStatus status = SmsReader.MessageStatus.UNKNOWN;
	private SmsReader.MessageType type = SmsReader.MessageType.UNKNOWN;
	private String message = "";
	private String sender = ""; 
	
	public SmsMessage(String rawMsg, String rawSender) {
		message = rawMsg;
		sender = rawSender;
		
		message = decodeUrl(message, AttributeManager.URL_INNER_DELIM, AttributeManager.INNER_DELIM);
		message = decodeUrl(message, AttributeManager.URL_OUTER_DELIM, AttributeManager.OUTER_DELIM);
		sender = decodeUrl(sender, AttributeManager.URL_PLUS, AttributeManager.PLUS);
		
		split(message, AttributeManager.OUTER_DELIM, AttributeManager.INNER_DELIM, true);
	}
	
	private String decodeUrl(String s, String urlSym, String regSym) {
		s = s.replaceAll(urlSym, regSym);
		return s;
	}
	
	//private void split(String s, String outerDelim, String innerDelim, Abbreviated abbreviated) {
	private void split(String s, String outerDelim, String innerDelim, boolean abbreviated) {
		String attrvalPairs[] = s.split(outerDelim);				// Pairs like attr1=val1
		for (int k = 0; k < attrvalPairs.length; k++) {
			String attrval[] = attrvalPairs[k].split(innerDelim);	// Puts attr in 0 and val in 1
			
			AttributeManager am = AttributeManager.getInstance();
			String longAttr = am.mapToLong(abbreviated, attrval[0]);
			
			if (longAttr.equals(AttributeManager.LONG_AV))
				AVnum = Integer.parseInt(attrval[1]);
			else if (longAttr.equals(AttributeManager.LONG_MESSAGE_STATUS)) {
				int i = Integer.parseInt(attrval[1]);
				switch (i) {
					case 0: status = SmsReader.MessageStatus.NEW; break;
					case 1: status = SmsReader.MessageStatus.PENDING; break;
					case 2: status = SmsReader.MessageStatus.PROCESSED; break;
				}
			}
			else if (longAttr.equals(AttributeManager.LONG_MESSAGE_TYPE)) {
				switch (Integer.parseInt(attrval[1])) {
					case 0: type = SmsReader.MessageType.REGISTRATION; break;
					case 1: type = SmsReader.MessageType.UPDATE; break;
				}
			}
		}
	}

	public int getAVnum() {
		return AVnum;
	}

	public void setAVnum(int aVnum) {
		AVnum = aVnum;
	}

	public SmsReader.MessageStatus getStatus() {
		return status;
	}

	public void setStatus(SmsReader.MessageStatus status) {
		this.status = status;
	}

	public SmsReader.MessageType getType() {
		return type;
	}

	public void setType(SmsReader.MessageType type) {
		this.type = type;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}
	
	public String toString() {
		return "Message:\n" + message + "\nSender:\n" + sender + "\nStatus:\n" + status + "\nType:\n" + type + "\nAV Number:\n" + AVnum;
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		SmsMessage a = new SmsMessage("AV%3D1%2Ci%3D068MP-FAT%2Ct%3D0%2Cst%3D1%2Cf%3DDenisana%2Cl%3DBalthazar%2Ca%3DSaint+Michel%2Cb%3D1947%2F11%2F31%2Cs%3DF%2Cc%3DP%2Cd%3D28%2C", "%2B18608748128");
		System.out.println(a);
	}
}
