import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.atomic.AtomicInteger;

public class OrdinaryProcess extends UnicastRemoteObject implements I_Node, Serializable{
	
	private static final long serialVersionUID = -1787817701524131768L;
	public AtomicInteger level;
	public AtomicInteger  id;
	public AtomicInteger  owner_id;
	public I_Node potentialOwner;
	public I_Node owner;
	
	public final static String TAG = "[OP]";
	
	public OrdinaryProcess(int id)  throws RemoteException, MalformedURLException {
		this.level = new AtomicInteger(-1);
		this.id = new AtomicInteger(id);
		this.owner_id = new AtomicInteger(-1);//not sure about initialization value  (saw it in the slides) 
		this.owner = null;
		this.potentialOwner = null;
		
		Naming.rebind("rmi://localhost/AfganOP"+id, this);
		System.out.println(TAG+"OP "+this+" created");
	}
	
	@Override
	public void send(Link link, int level, int fromId) throws RemoteException,
			MalformedURLException, NotBoundException {
		try {
			Thread.sleep(Math.round(Math.random() * 1000));
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		
		System.out.println(TAG+"OP "+this+" send ("+level+","+fromId+") to CP "+link.pointTarget);
		// OP always send to CP
		I_Node node = (I_Node) Naming.lookup("rmi://localhost/AfganCP"+link.pointTarget);
		node.receive(link, level, fromId);
		
	}

	@Override
	public void receive(Link link, int level, int fromId)
			throws RemoteException, MalformedURLException, NotBoundException {
		
		I_Node nodeFrom = (I_Node) Naming.lookup("rmi://localhost/AfganCP"+link.pointMe);
        
        System.out.println(TAG+"OP "+this+" recv ("+level+","+fromId+") from CP "+link.pointMe);
		
        if (isLargerTo(level,fromId)) {
        	System.out.println(TAG+" me larger, ignored");
        }
        else if ( !(isLargerTo(level,fromId)) ) {
        	potentialOwner = (I_Node) nodeFrom;
        	
        	this.level.set(level);
        	// add this because the algo says so
        	this.owner_id.set(fromId);
        	
        	if (owner == null){
        		owner = potentialOwner;
        		System.out.println(TAG+" CP larger owner null");
           	}
        		
    		// change the perspective, me = OP, target = CP
        	System.out.println(TAG+"OP "+this+" try to kill, or ack owner");
    		Link ownerlink = link.switchPerspective();
    		this.send(ownerlink, level, fromId);
//        	}
        }
        else if ( (level == this.level.get()) && (fromId == this.owner_id.get()) ){
        	
        	System.out.println(TAG+" OLD owner officially killed "+this+" change owner to "+potentialOwner.getid());
        	
        	owner = potentialOwner;
        	Link ownerlink = link.switchPerspective();
        	this.send(ownerlink, level, fromId);
        }
        
    }

	// return true if this (lvl,id) larger than target
	// otherwise else
	public boolean isLargerTo(int level_, int id_) throws RemoteException {
		if (this.level.get() > level_) return true;
		else if (this.level.get() == level_) return this.owner_id.get() > id_;
		else return false; //means level target higher
	}
	
	@Override
	public String toString() {
		return "{lvl"+this.level.get()+" | "+this.id+"+"+this.owner_id+"}";
	}

	@Override
	public int dummy() throws RemoteException {
		System.out.println(level.get());
		return level.get();
	}

	@Override
	public int getid() throws RemoteException {
		return this.id.get();
	}
}