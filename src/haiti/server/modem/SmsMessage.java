package haiti.server.modem;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import haiti.server.datamodel.AttributeManager;
import haiti.server.gui.*;
import haiti.server.gui.DAO.MessageStatus;
import haiti.server.gui.DAO.MessageType;

public class SmsMessage {

	public enum Abbreviated {
		TRUE, FALSE
	};

	private String AVnum = null;
	private int msgNumber = 0;
	private int msgTotal = 0;
	private DAO.MessageStatus status = DAO.MessageStatus.UNKNOWN;
	private DAO.MessageType type = DAO.MessageType.UNKNOWN;
	private String message = "";
	private String sender = "";

	public SmsMessage(String aVnum, MessageStatus status, MessageType type,
			String message, String sender) {
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

		try {
			message = URLDecoder.decode(message, "UTF-8");
			sender = URLDecoder.decode(sender, "UTF-8");
			sender = sender.replace("+", "");
		} catch (UnsupportedEncodingException e) {
			DbWriter.log(e.getMessage());
			e.printStackTrace();
		}
		split(message, AttributeManager.OUTER_DELIM,
				AttributeManager.INNER_DELIM, true);
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

	private void split(String s, String outerDelim, String innerDelim,
			boolean abbreviated) {
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
				else if (longAttr.equals(AttributeManager.LONG_MESSAGE_NUMBER)){
					String parts[] = attrval[1].split(AttributeManager.MSG_NUMBER_SEPARATOR);
					msgNumber=Integer.parseInt(parts[0]);
					msgTotal=Integer.parseInt(parts[1]);
				}
				else if (longAttr.equals(AttributeManager.LONG_MESSAGE_STATUS)) {
					int i = Integer.parseInt(attrval[1]);
					switch (i) {
					case 0:
						status = DAO.MessageStatus.NEW;
						break;
					case 1:
						status = DAO.MessageStatus.PENDING;
						break;
					case 2:
						status = DAO.MessageStatus.PROCESSED;
						break;
					}
				} else if (longAttr.equals(AttributeManager.LONG_MESSAGE_TYPE)) {
					switch (Integer.parseInt(attrval[1])) {
					case 0:
						type = DAO.MessageType.REGISTRATION;
						break;
					case 1:
						type = DAO.MessageType.UPDATE;
						break;
					}
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
			DbWriter.log("Invalid message format: " + s);
		}
	}

	public int getMessageStatus() {
		return status.getCode();
	}

	public void setStatus(DAO.MessageStatus status) {
		this.status = status;
	}

	public int getMessageType() {
		return type.getCode();
	}

	public void setType(DAO.MessageType type) {
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
		return "Message:\n" + message + "\nSender:\n" + sender + "\nStatus:\n"
				+ status + "\nType:\n" + type + "\nAV Number: " + AVnum
				+ "\nMessage " + msgNumber + " of " + msgTotal;
	}
	
	public static void main(String args[]){
		SmsMessage sms = new SmsMessage("AV=4,mn=1:10,i=fafa,ms=0,t=0","111111");
		System.out.println(sms);
		
	}
}
