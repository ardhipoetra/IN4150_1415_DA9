import java.io.Serializable;


public class Message implements Serializable, Comparable<Message>{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6974203889493409480L;
	public static final int TYPE_REQUEST = 1;
	public static final int TYPE_GRANT = 2;
	public static final int TYPE_RELEASE = 3;
	public static final int TYPE_INQUIRE = 4;
	public static final int TYPE_POSTPONED = 5;
	public static final int TYPE_RELINQUISH = 6;
	
	public int type;
	public int idSender;
	public int idDest;
	public long timestamp;
	
	public Message() {
		type = 0;
	}
	
	@Override
	public String toString() {
		String ss = "REQUEST";
		switch (type) {
		case TYPE_GRANT:
			ss = "GRANT";
			break;
		case TYPE_RELEASE:
			ss = "RELEASE";
			break;
		case TYPE_INQUIRE:
			ss = "INQUIRE";
			break;
		case TYPE_POSTPONED:
			ss = "POSTPONED";
			break;
		case TYPE_RELINQUISH:
			ss = "RELINQUISH";
			break;
		default:
			break;
		}
		return ss+"["+idSender+" -> "+idDest+"] "+timestamp;
	}
	
	@Override
	public boolean equals(Object obj) {
		return true;
	}

	@Override
	public int compareTo(Message o2) {
		if (this.timestamp == o2.timestamp) {
			return this.idSender - o2.idSender;
		}
		return (int) (this.timestamp - o2.timestamp);
	}
}
