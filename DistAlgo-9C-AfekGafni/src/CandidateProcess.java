import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;


public class CandidateProcess extends OrdinaryProcess implements Serializable{
	
	private static final long serialVersionUID = -8539885619007756603L;
	public boolean killed;
	public Queue<Link> untraversed;
	public boolean elected;
	
	public final static String TAG = "[CP]";
	
	public CandidateProcess(int id) throws RemoteException, MalformedURLException {
		super(id);
//		this.id = id;
		this.level = new AtomicInteger(0);
		this.killed = false;
		this.elected = false;
		untraversed = new ArrayBlockingQueue<Link>(Main.NODES);
		
		
		for (int i = 0; i < Main.NODES; i++) {
			if (i == this.getid()) continue;
			untraversed.add(new Link(this.getid(), i));
		}
		
		
		System.out.println(TAG+" CP Created");
		Naming.rebind("rmi://localhost/AfganCP"+this.id, this);
		try {
			runCP();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private void runCP() throws RemoteException{
		if(!untraversed.isEmpty()) {
			System.out.println("size link "+untraversed.size());
			final Link link = chooseLink();
			
			try {
				System.out.println("level "+this.level.get());
				System.out.println(TAG+"CP "+this+" find link "+link);
				
				this.send(link, this.level.get(), getid());
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			if (!killed) elected = true;
			System.out.println(TAG+"CP "+this+"elected? "+elected);
		}
	}
	
	@Override
	public void send(Link link, int level, int fromId) throws RemoteException, MalformedURLException, NotBoundException {
		// put delay here
		try {
			Thread.sleep(Math.round(Math.random() * 1000));
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		System.out.println(TAG+"CP "+this+" send ("+level+","+fromId+") to OP "+link.pointTarget);
		final I_Node target = (I_Node) Naming.lookup("rmi://localhost/AfganOP"+link.pointTarget);
		//CP always send to OP
		target.receive(link, level, fromId);
	}
	
	@Override
	public void receive(Link link, int level, int fromId) throws RemoteException, MalformedURLException, NotBoundException {
		// also, CP always receive message from OP
//		OrdinaryProcess nodeFrom = (OrdinaryProcess) Naming.lookup("rmi://localhost/AfganOP"+link.pointMe);
		
		System.out.println(TAG+"CP "+this+" recv ("+level+","+fromId+") from OP "+link.pointMe);
		
		if (this.getid() == fromId && !killed) {
			System.out.println(TAG+"CP "+this+" got ACK, increase lvel "+this.level);
			int incrementAndGet = this.level.incrementAndGet();
			
			System.out.println(link+" try remove");
			System.out.println(untraversed.remove(link));
			
			
			System.out.println("le level : "+incrementAndGet);
			runCP();
		} else {
			if (isLargerTo(level,fromId)) {
				//ignored, lol
				System.out.println(TAG+" this request was ignored by OP");
			} else {
				System.out.println(TAG+" OP has found new owner which is CP "+fromId+" with lvl "+level);
				Link oplinkDead = link.switchPerspective();
				this.send(oplinkDead, level, fromId);
				killed = true;
				
				System.out.println(TAG+"CP "+this+" kill me already");
			}
		}
			
	}
	
	public boolean isLargerTo(int level_, int id_) throws RemoteException {
		if (this.level.get() > level_) return true;
		else if (this.level.get() == level_) return this.getid() > id_;
		else return false; //means level target higher
	}
	
	private Link chooseLink() {
		return (Link) untraversed.toArray()[new Random().nextInt(untraversed.size())];
	}

	@Override
	public int dummy() throws RemoteException {
		System.out.println(level.get());
		return level.get();
	}

	@Override
	public String toString() {
		return "{lvl"+this.level.get()+" | "+this.id+"}";
	}
}
