import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.PriorityQueue;

public class Node implements I_Node, Serializable{
	private static final long serialVersionUID = -6280648254081711979L;

	PriorityQueue<Message> queue; 
	
	I_Node nodes[];
	ArrayList<I_Node[]> reqlist;
	
	boolean granted;
	Message current_grant;
	
	int numberOfGranted;
		
	boolean postponed;
	
	boolean wait;
	boolean inquiring;
	
	private int id;

//	private MsgComparator msgComp;

	private int currentTargetCS;

	private boolean waitinquire;

	
	
	public Node(int id) {
		queue = new PriorityQueue<Message>(10);
		this.id = id;
		
//		msgComp = new MsgComparator();
	}
	
	
	@Override
	public void receive(Message msg) throws RemoteException {
		System.out.println("["+new Date().getTime()+"]"+id+" receive : "+msg);
		switch (msg.type) {
		case Message.TYPE_REQUEST:
			if (!granted) {
				granted = true;
				
				current_grant = msg;
				Message mGrnt = new Message();
				mGrnt.idSender = this.id; mGrnt.timestamp = new Date().getTime(); 
				mGrnt.idDest = msg.idSender; mGrnt.type = Message.TYPE_GRANT;
				send(mGrnt, nodes[msg.idSender]);
				
//				granted = true;
			} else {
				queue.add(msg);
				
				Message head = queue.peek();
				
				if ((current_grant.compareTo(msg) < 0) || (head.compareTo(msg) < 0)) {
					Message mPstpone = new Message();
					mPstpone.idSender = this.id; mPstpone.timestamp = new Date().getTime(); 
					mPstpone.idDest = msg.idSender; mPstpone.type = Message.TYPE_POSTPONED;
					
					send(mPstpone, nodes[msg.idSender]);
				} else {
					if (!inquiring) {
						inquiring = true;
						Message mInq = new Message();
						mInq.idSender = this.id; mInq.timestamp = new Date().getTime(); 
						mInq.idDest = current_grant.idSender; mInq.type = Message.TYPE_INQUIRE;
						
						send(mInq, nodes[current_grant.idSender]);
					}
				}
				
			}
			break;
		case Message.TYPE_GRANT:
			numberOfGranted++;
			
			if (numberOfGranted == reqlist.get(currentTargetCS).length) {
				postponed = false;
				
				nodes[currentTargetCS].enterCS(id);
				
				for (I_Node i_dest: reqlist.get(currentTargetCS)) {
					Message mRel = new Message();
					mRel.idSender = this.id; mRel.timestamp = new Date().getTime(); 
					mRel.idDest = i_dest.getId(); mRel.type = Message.TYPE_RELEASE;
					
					send(mRel, i_dest);
				}
			}
			
			break;
		case Message.TYPE_RELEASE:
			granted = false;
			inquiring = false;
			
			if (queue.size() != 0) {
				current_grant = queue.poll();
				
				Message mGrnt = new Message();
				mGrnt.idSender = this.id; mGrnt.timestamp = new Date().getTime(); 
				mGrnt.idDest = current_grant.idSender; mGrnt.type = Message.TYPE_GRANT;
				
				send(mGrnt, nodes[mGrnt.idDest]);
				granted = true;
			}
			break;
		case Message.TYPE_INQUIRE:
			waitinquire = true;
			break;
		case Message.TYPE_POSTPONED:
			postponed = true;
			break;
		case Message.TYPE_RELINQUISH:
			inquiring = false;
			granted = false;
			
			if (current_grant != null) {
				queue.add(current_grant);
				current_grant = queue.poll();
				
				granted = true;
				
				Message mGrnt = new Message();
				mGrnt.idSender = this.id; mGrnt.timestamp = new Date().getTime(); 
				mGrnt.idDest = current_grant.idSender ;mGrnt.type = Message.TYPE_GRANT;
				
				send(mGrnt, nodes[mGrnt.idDest]);
			}
			
			
			break;
		default: //ERROR
			break;
		}

		checkIfWait(msg);
	}
	
	private void checkIfWait(Message msg) throws RemoteException {
		if (wait) return;
		
		if (waitinquire) {
			if (postponed || numberOfGranted == reqlist.get(currentTargetCS).length) {
				if (postponed) {
					numberOfGranted--;
					Message mRelinquish = new Message();
					mRelinquish.idSender = this.id; mRelinquish.timestamp = new Date().getTime(); 
					mRelinquish.idDest = msg.idSender; mRelinquish.type = Message.TYPE_RELINQUISH;
					
					send(mRelinquish, nodes[msg.idSender]);
				}
			}
		}
		
	}
	

	@Override	
	public void send(Message msg, I_Node dest) throws RemoteException {
		if (msg.type == Message.TYPE_REQUEST) {
			numberOfGranted = 0;
			currentTargetCS = dest.getId();
			for (I_Node i_dest: reqlist.get(currentTargetCS)) {
				System.out.println("["+new Date().getTime()+"]"+id+" send : "+msg+" to "+dest.getId()+" - "+i_dest.getId());
				i_dest.receive(msg);
			}
		} else {
			try {
				Thread.sleep((long)(300));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("["+new Date().getTime()+"]"+id+" send : "+msg+" to "+dest.getId());
			dest.receive(msg);
		}
		
	}

	
	@Override
	public void setHashMap(List<I_Node[]> proc) throws RemoteException {
		this.reqlist = (ArrayList<I_Node[]>) proc;
	}


	@Override
	public void enterCS(int callee) throws RemoteException {
		try {
			System.out.println(callee+" entered CS "+id+" at "+(new Date().getTime()));
			Thread.sleep((long) Math.random() * 2000);
			System.out.println(callee+" want out CS "+id+" at "+(new Date().getTime()));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		// try another request
		
	}


	@Override
	public void setProcessNetwork(I_Node[] nodes) {
		this.nodes = nodes;
	}


	@Override
	public int getId() throws RemoteException {
		return id;
	}

}
