import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;


public class DA9C extends UnicastRemoteObject implements DA9C_RMI {
	private static final long serialVersionUID = 1L;
	
	PriorityQueue<Messages> queue;
	ArrayList<Messages> ackQueue;
	DA9C_RMI proc[];
	int id;
	
	protected DA9C(int id) throws RemoteException {
		super();
		this.id = id;
		queue = new PriorityQueue<Messages>(10, new MsgComparator());
		ackQueue = new ArrayList<Messages>();
	}
	

	@Override
	public int broadcast(Messages msg,DA9C_RMI[] proc) throws RemoteException {
		System.out.println(id + "send : "+ msg);
		for (DA9C_RMI da9c_RMI : proc) {
			da9c_RMI.receive(msg,proc);
		}
		return 0;
	}


	@Override
	public void receive(Messages msg,DA9C_RMI[] proc) throws RemoteException {
		if (msg.type == 0) { //message
			queue.add(msg);
			
			Messages ack = new Messages();
			ack.idSender = msg.idSender; ack.msg = msg.msg; ack.timestamp = msg.timestamp;
			ack.type = 1;
			
			broadcast(ack,proc);
		} else if (msg.type == 1) { //ack
			ackQueue.add(msg);
			int count = 0;
			
			if (queue.size() == 0) {
				return;
			}
			for (int i = 0; i < ackQueue.size(); i++) {
				if (ackQueue.get(i).timestamp == queue.peek().timestamp) { //compare ack with head of queue
					count++; 
				}
			}
			
			if (count == proc.length) {
				// msg is delivered
				Messages m = queue.poll();
				System.out.println(m+" is delivered");
				
				//remove acks from ackqueue
				for (Iterator<Messages> iterator = ackQueue.iterator(); iterator.hasNext();) {
					Messages gs = (Messages) iterator.next();
					if (gs.timestamp == m.timestamp) {
						iterator.remove();
					}
				}
			}
		}
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

//	public void setProcessesNetwork(Object[] proc) {
//		this.proc = proc;
//	}

}
