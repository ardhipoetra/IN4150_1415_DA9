import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Comparator;
import java.util.PriorityQueue;


public class DA9C extends UnicastRemoteObject implements DA9C_RMI {
	private static final long serialVersionUID = 1L;
	
	PriorityQueue<Messages> queue;
	DA9C_RMI proc[];
	int id;
	
	protected DA9C(int id) throws RemoteException {
		super();
		this.id = id;
		queue = new PriorityQueue<Messages>(10, new MsgComparator());
	}
	

	@Override
	public int broadcast(Messages msg) throws RemoteException {
		System.out.println(id + "send : "+ msg);
		for (DA9C_RMI da9c_RMI : proc) {
			da9c_RMI.receive(msg);
		}
		return 0;
	}


	@Override
	public void receive(Messages msg) throws RemoteException {
		if (msg.type == 0) { //message
			queue.add(msg);
		} else if (msg.type == 1) { //ack
			
		}
		
		
		Messages ack = new Messages();
		ack.idSender = msg.idSender; ack.msg = msg.msg; ack.timestamp = msg.timestamp;
		ack.type = 1;
		
		broadcast(ack);
	}
	
	class MsgComparator implements Comparator<Messages> {

		@Override
		public int compare(Messages o1, Messages o2) {
			if (o1.timestamp == o2.timestamp && 
					o1.idSender < o2.idSender) {
				return o1.idSender - o2.idSender;
			}
			if (o1.timestamp < o2.timestamp) return -1;
			
			return 1;
		}
		
	}

	@Override
	public void setProcessesNetwork(DA9C_RMI[] proc) {
		this.proc = proc;
	}

}
