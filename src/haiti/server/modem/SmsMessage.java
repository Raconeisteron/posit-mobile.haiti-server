package haiti.server.modem;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import haiti.server.datamodel.AttributeManager;
import haiti.server.gui.*;
import haiti.server.datamodel.AttributeManager.MessageStatus;
import haiti.server.datamodel.AttributeManager.MessageType;

public class SmsMessage {

	public enum Abbreviated {
		TRUE, FALSE
	};

	private String AVnum = null;
	private int msgNumber = 0;
	private int msgTotal = 0;
	private AttributeManager.MessageStatus status = AttributeManager.MessageStatus.UNKNOWN;
	private AttributeManager.MessageType type = AttributeManager.MessageType.REGISTRATION;
	private String message = "";
	private String sender = "";
	private String distributionId = "";

	public SmsMessage(String aVnum, MessageStatus status, MessageType type, String message, String sender) {
		super();
		AVnum = aVnum;
		this.status = status;
		this.type = type;
		this.message = message;
		this.sender = sender;
	}

	public SmsMessage(String rawMsg, String rawSender) {
		message = rawMsg;
		sender = rawSender;

		// try {
		// message = URLDecoder.decode(message, "UTF-8");
		// sender = URLDecoder.decode(sender, "UTF-8");
		// sender = sender.replace("+", "");
		// } catch (UnsupportedEncodingException e) {
		// DbWriter.log(e.getMessage());
		// e.printStackTrace();
		// }
		split(message, AttributeManager.OUTER_DELIM, AttributeManager.INNER_DELIM, true);
	}

	public String getAVnum() {
		return AVnum;
	}

	public void setAVnum(String aVnum) {
		AVnum = aVnum;
	}

	public int getMsgNumber() {
		return msgNumber;
	}

	public void setMsgNumber(int msgNumber) {
		this.msgNumber = msgNumber;
	}

	public int getMsgTotal() {
		return msgTotal;
	}

	public void setMsgTotal(int msgTotal) {
		this.msgTotal = msgTotal;
	}

	public String getDistributionId() {
		return distributionId;
	}

	public void setDistributionId(String distributionId) {
		this.distributionId = distributionId;
	}

	private void split(String s, String outerDelim, String innerDelim, boolean abbreviated) {
		try {
			String attrvalPairs[] = s.split(outerDelim); // Pairs like
															// attr1=val1
			for (int k = 0; k < attrvalPairs.length; k++) {
				// Puts attribute in 0 and value in 1
				String attrval[] = attrvalPairs[k].split(innerDelim);
				AttributeManager am = AttributeManager.getInstance();
				String longAttr = am.mapToLong(abbreviated, attrval[0]);

				if (longAttr.equals(AttributeManager.LONG_AV))
					AVnum = attrval[1];
				else if (longAttr.equals(AttributeManager.LONG_MESSAGE_NUMBER)) {
					String parts[] = attrval[1].split(AttributeManager.MSG_NUMBER_SEPARATOR);
					msgNumber = Integer.parseInt(parts[0]);
					msgTotal = Integer.parseInt(parts[1]);
				} else if (longAttr.equals(AttributeManager.LONG_STATUS)) {
					int i = Integer.parseInt(attrval[1]);
					switch (i) {
					case 0:
						status = AttributeManager.MessageStatus.NEW;
						break;
					case 1:
						status = AttributeManager.MessageStatus.UPDATED;
						break;
					case 2:
						status = AttributeManager.MessageStatus.PENDING;
						break;
					case 3:
						status = AttributeManager.MessageStatus.PROCESSED;
						break;
					case 4:
						status = AttributeManager.MessageStatus.ALL;
						break;
					}
				} else if (longAttr.equals(AttributeManager.DISTRIBUTION_ID)) {
					distributionId = attrval[1];
				}
				// } else if (longAttr.equals(AttributeManager.LONG_TYPE)) {
				// switch (Integer.parseInt(attrval[1])) {
				// case 0:
				// type = AttributeManager.MessageType.REGISTRATION;
				// break;
				// case 1:
				// type = AttributeManager.MessageType.UPDATE;
				// break;
				// case 2:
				// type = AttributeManager.MessageType.ATTENDANCE;
				// break;
				// case 3:
				// type = AttributeManager.MessageType.ALL;
				// break;
				// }
			}

		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			DbWriter.log("Invalid message format: " + s);
		}
	}

	public int getMessageStatus() {
		return status.getCode();
	}

	public void setStatus(AttributeManager.MessageStatus status) {
		this.status = status;
	}

	public int getMessageType() {
		return type.getCode();
	}

	public void setType(AttributeManager.MessageType type) {
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

	@Override
	public String toString() {
		return "SmsMessage [AVnum=" + AVnum + ", msgNumber=" + msgNumber + ", msgTotal=" + msgTotal + ", status="
				+ status + ", type=" + type + ", message=" + message + ", sender=" + sender + ", distributionId="
				+ distributionId + "]";
	}

	public static void main(String args[]) {
		SmsMessage sms = new SmsMessage("AV=4,mn=1:10,i=fafa,ms=0,t=0", "111111");
		System.out.println(sms);

	}
}
