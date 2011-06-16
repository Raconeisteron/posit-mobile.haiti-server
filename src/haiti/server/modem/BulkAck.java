package haiti.server.modem;

import java.util.Iterator;
import java.util.List;

public class BulkAck {
	
	private List<Integer> ids;
	private String message = "AV=ACK,ids=";
	private String sender;
	
	public BulkAck(List<Integer> ids, String sender) {
		Iterator<Integer> it = ids.iterator();
		this.sender=sender;
		while (it.hasNext()){
			message+=it.next()+"&"; 
		}
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	public String getMessage() {
		return message;
	}
	public void setIds(List<Integer> ids) {
		this.ids = ids;
	}
	public List<Integer> getIds() {
		return ids;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getSender() {
		return sender;
	}
}
