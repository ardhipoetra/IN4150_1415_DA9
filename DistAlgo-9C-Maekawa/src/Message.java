import java.io.Serializable;


public class Message implements Serializable{
	
	private static final long serialVersionUID = -9158610547120610162L;
	public static final int TYPE_REQUEST = 1;
	public static final int TYPE_GRANT = 2;
	public static final int TYPE_RELEASE = 3;
	public static final int TYPE_INQUIRE = 4;
	public static final int TYPE_POSTPONED = 5;
	public static final int TYPE_RELINQUISH = 6;
	
	public int type;
	public int idSender;
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
		return ss+"["+idSender+"] "+timestamp;
	}
	
	@Override
	public boolean equals(Object obj) {
		return true;
	}
}
