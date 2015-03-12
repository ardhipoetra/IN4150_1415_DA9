import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
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
	
	long timeStart;
	
	private PrintWriter outSend, outRcvMsg, outRcvAck, outDlvrMsg;
	
	protected DATotalOrdering(int id) throws RemoteException {
		super();
		this.id = id;
		queue = new PriorityQueue<Messages>(10, new MsgComparator());
		ackQueue = new ArrayList<Messages>();
		try {
			outSend = new PrintWriter(new BufferedWriter(new FileWriter("send-"+id+".txt", true)));
			outRcvMsg = new PrintWriter(new BufferedWriter(new FileWriter("rcvMsg-"+id+".txt", true)));
			outRcvAck= new PrintWriter(new BufferedWriter(new FileWriter("rcvAck-"+id+".txt", true)));
			outDlvrMsg = new PrintWriter(new BufferedWriter(new FileWriter("dlvrMsg-"+id+".txt", true)));
			
//			outSend = outRcvAck = outRcvMsg = outDlvrMsg = System.out;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	@Override
	public int broadcast(final Messages msg) throws RemoteException{
		
		outSend.println("Process ["+id+"] SEND : "+ msg + " at "+(new Date().getTime() - timeStart));
		int i = 0;
		
		// TODO: instead of using proc (array of object), retrieve again lookup
		// but still need id 
		
		
		
		
		
		for (final DATotalOrdering_RMI totOrd_I : proc) {
			
			Thread tr = new Thread("t_"+(i++)){
				@Override
				public void run() {
					try {
						//random delay
						Thread.sleep((500));
						
						totOrd_I.receive(msg);
					} 
					catch (InterruptedException e) {
						e.printStackTrace();
					} 
					catch (RemoteException e) {
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
		
//		try {
//			Thread.sleep((long)(Math.random() * 500));
//		} catch (InterruptedException e1) {
//			e1.printStackTrace();
//		}
		
		if (msg.type == 0) { //message
			outRcvMsg.println("Process ["+id+"]"+msg+" is RECEIVED at "+ (new Date().getTime() - timeStart));
			queue.add(msg); //put that into buffer
			
			Messages ack = new Messages();
			ack.idSender = id ; ack.msg = msg.msg; ack.timestamp = msg.timestamp;
			ack.type = 1;
			
			broadcast(ack); //broadcast ack
		} else if (msg.type == 1) { //ack
			outRcvAck.println("Process ["+id+"]"+msg+" is RECEIVED at "+ (new Date().getTime() - timeStart));
			ackQueue.add(msg);
			int count = 0;
			
			if (queue.size() == 0 && ackQueue.size() != 0) { //if msg not yet arrived, ignore ack for now
				return;
			}
			
			for (int i = 0; i < ackQueue.size(); i++) {
				if (ackQueue.get(i).equals(queue.peek())) { //compare ack with head of queue
					count++; 
				}
			}
			
			if (count == proc.length) {
				// msg is delivered
				Messages m = queue.poll();
				outDlvrMsg.println("Process ["+id+"]"+m+" is DELIVERED at "+ (new Date().getTime() - timeStart));
				
				//remove acks from ackqueue
				for (Iterator<Messages> iterator = ackQueue.iterator(); iterator.hasNext();) {
					Messages gs = (Messages) iterator.next();
					if (gs.equals(m)) {
						iterator.remove();
					}
				}
			}
		}
		
		//further check
		int countFinal = 0;
		int ackQ = 0;
		
		while(ackQ != ackQueue.size() && ackQueue.size() != 0) {
			countFinal = 0;
			try {
				Thread.sleep((long)(Math.random() * 100));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("q "+queue.size() + " ack : "+ackQueue.size());

			ackQ = ackQueue.size();
			for (int i = 0; i < ackQueue.size(); i++) {
				if (ackQueue.get(i).equals(queue.peek())) { //compare ack with head of queue
					countFinal++; 
				}
			}
			
			if (countFinal == proc.length) {
				// msg is delivered
				Messages m = queue.poll();
				outDlvrMsg.println("Process ["+id+"]"+m+" is DELIVERED at "+ (new Date().getTime() - timeStart));
				
				//remove acks from ackqueue
				for (Iterator<Messages> iterator = ackQueue.iterator(); iterator.hasNext();) {
					Messages gs = (Messages) iterator.next();
					if (gs.equals(m)) {
						iterator.remove();
					}
				}
			}
			
			System.out.println("aq' "+ackQ + " aq : "+ackQueue.size() + " m "+queue.peek());
			
			for (int i = 0; i < ackQueue.size(); i++) {
				System.out.print(ackQueue.get(i)+",");
			}
		}
		
		
		
		if (queue.size() == 0 && ackQueue.size() == 0) { 
			outDlvrMsg.close();
			outRcvAck.close();
			outSend.close();
			outRcvMsg.close();
			
			System.out.println("FINISHED");
		}
	}
	
	class MsgComparator implements Comparator<Messages> {

		@Override
		public int compare(Messages o1, Messages o2) {
			if (o1.timestamp == o2.timestamp) {
				return o1.idSender - o2.idSender;
			}
			return (int) (o1.timestamp - o2.timestamp);
//			if (o1.timestamp < o2.timestamp) return -1;
//			else return 1;
		}
		
	}

	@Override
	public void setProcessesNetwork(DATotalOrdering_RMI[] proc) throws RemoteException {
		this.proc = proc;
	}


	@Override
	public void setStartTime(long t) throws RemoteException {
		timeStart = t;
		
	}
}
