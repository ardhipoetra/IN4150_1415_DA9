import java.io.Serializable;


public class Message implements Serializable{
	
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
		return "";
	}
	
	@Override
	public boolean equals(Object obj) {
		return true;
	}
}
