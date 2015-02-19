import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.PriorityQueue;


public class DATotalOrdering extends UnicastRemoteObject implements DATotalOrdering_RMI {
	private static final long serialVersionUID = 1L;
	
	PriorityQueue<Messages> queue;
	ArrayList<Messages> ackQueue;
	DATotalOrdering_RMI proc[];
	int id;
	
	protected DATotalOrdering(int id) throws RemoteException {
		super();
		this.id = id;
		queue = new PriorityQueue<Messages>(10, new MsgComparator());
		ackQueue = new ArrayList<Messages>();
	}
	

	@Override
	public int broadcast(final Messages msg) throws RemoteException{
		System.out.println("Process ["+id+"] SEND : "+ msg + " at "+(new Date().getTime() - msg.timestamp));
		int i = 0;
		
		
		for (final DATotalOrdering_RMI totOrd_I : proc) {//added it here as broadcast is same as send
			
			Thread tr = new Thread("t_"+(i++)){
				@Override
				public void run() {
					try {
						//random delay
						Thread.sleep((long)(Math.random() * 500));
						
						totOrd_I.receive(msg);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			};
			
			tr.start();
			
		}
		return 0;
	}


	@Override
	public void receive(Messages msg) throws RemoteException {
		
		System.out.println("Process ["+id+"]"+msg+" is RECEIVED at "+ (new Date().getTime() - msg.timestamp));
		
		if (msg.type == 0) { //message
			queue.add(msg); //put that into buffer
			
			Messages ack = new Messages();
			ack.idSender = id ; ack.msg = msg.msg; ack.timestamp = msg.timestamp;
			ack.type = 1;
			
			broadcast(ack); //broadcast ack
		} else if (msg.type == 1) { //ack
			ackQueue.add(msg);
			int count = 0;
			
			if (queue.size() == 0) { //if msg not yet arrived, ignore ack for now
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
				System.out.println("Process ["+id+"]"+m+" is DELIVERED at "+ (new Date().getTime() - m.timestamp));
				
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

	@Override
	public void setProcessesNetwork(DATotalOrdering_RMI[] proc) throws RemoteException {
		this.proc = proc;
	}

//	@Override
//	public void send(Messages msg, DATotalOrdering_RMI[] proc) throws RemoteException {
//		if (msg.type == 0) { //message
//			queue.add(msg);
//			
//			Messages ack = new Messages();
//			ack.idSender = msg.idSender; ack.msg = msg.msg; ack.timestamp = msg.timestamp;
//			ack.type = 1;
//			
//			broadcast(ack,proc);
//		} else 
//			
//		
//	}

//	public void setProcessesNetwork(Object[] proc) {
//		this.proc = proc;
//	}

}
