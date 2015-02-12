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
		return "["+type+"]"+msg + "("+idSender+") ["+timestamp+"]";
	}
}
