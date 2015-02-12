
public class Messages {
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
