import java.rmi.RemoteException;
import java.util.List;
import java.util.PriorityQueue;


public class Node implements I_Node{

	PriorityQueue<Message> queue;
	
	boolean granted;
	Message current_grant;
	int numberOfGranted;
		
	boolean postponed;
	
	public Node() {
		queue = new PriorityQueue<Message>(10, new MsgComparator());
	}
	
	
	@Override
	public void receive(Message msg) throws RemoteException {
		switch (msg.type) {
		case Message.TYPE_REQUEST:
			
			break;
		case Message.TYPE_GRANT:
			
			break;
		case Message.TYPE_RELEASE:
			
			break;
		case Message.TYPE_INQUIRE:
			
			break;
		case Message.TYPE_POSTPONED:
			
			break;
		case Message.TYPE_RELINQUISH:
			
			break;
		default: //ERROR
			break;
		}
	}

	@Override
	public void send(Message msg) throws RemoteException {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public void setHashMap(List<I_Node[]> proc) throws RemoteException {
		
	}

}
