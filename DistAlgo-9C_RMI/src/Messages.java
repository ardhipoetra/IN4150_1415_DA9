import java.io.Serializable;


public class Messages implements Serializable{
	private static final long serialVersionUID = 549228823969035011L;
	public int type;
	public String msg;
	public int idSender;
	public long timestamp;
	
	public Messages() {
		type = 0;
	}
	
	@Override
	public String toString() {
		String s = "ACK";
		if (type == 0) s = "MESSAGE";
			
		
		return "{"+s+"}"+msg + " from Process "+idSender+" at ["+timestamp+"]";
	}
	
	@Override
	public boolean equals(Object obj) {
		Messages m = (Messages) obj;
		if (timestamp == m.timestamp && msg.equals(m.msg)) return true;
		
		return false;
	}
}
