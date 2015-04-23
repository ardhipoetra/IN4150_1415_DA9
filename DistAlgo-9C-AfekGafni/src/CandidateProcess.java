import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;


public class CandidateProcess extends OrdinaryProcess implements I_Node {
	
	public boolean killed;
	public Set<Link> untraversed;
	public boolean elected;
	

	public CandidateProcess(int id) {
		super(id);
		this.level = 0;
		this.killed = false;
		this.elected = false;
		untraversed = Collections.synchronizedSet(new TreeSet<Link>());
		
		for (int i = 0; i < Main.NODES; i++) {
			if (i == this.id) continue;
			untraversed.add(new Link(this.id, i));
		}
		
		try {
			runCP();
		} catch (RemoteException e) {
			e.printStackTrace();
		}
	}
	
	private void runCP() throws RemoteException{
		while(!untraversed.isEmpty()) {
			Link link = chooseLink();
			
			try {
				this.send(link, this.level, this.id);
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (NotBoundException e) {
				e.printStackTrace();
			}
		}
		
		if (!killed) elected = true; 
	}
	
	@Override
	public void send(Link link, int level, int fromId) throws RemoteException, MalformedURLException, NotBoundException {
		// put delay here
		
		//CP always send to OP
		I_Node target = (I_Node) Naming.lookup("rmi://localhost/AfganOP"+link.pointTarget);
		target.receive(link, level, fromId);
	}
	
	@Override
	public void receive(Link link, int level, int fromId) throws RemoteException, MalformedURLException, NotBoundException {
		// also, CP always receive message from OP
		I_Node nodeFrom = (I_Node) Naming.lookup("rmi://localhost/AfganOP"+link.pointMe);
		
		if (this.id == fromId && !killed) {
			this.level++;
			untraversed.remove(link);
		} else {
			if (isLargerTo(level,fromId)) {
				//ignored, lol
			} else {
				this.send(link, level, fromId);
				killed = true;
			}
		}
			
	}
	
	// return true if this (lvl,id) larger than target
	// otherwise else
	public boolean isLargerTo(int level_, int id_) {
		if (this.level > level_) return true;
		else if (this.level == level_) return this.id > id_;
		else return false; //means level target higher
	}
	
	public Link chooseLink() {
		return (Link) untraversed.toArray()[new Random().nextInt(untraversed.size())];
	}
}
