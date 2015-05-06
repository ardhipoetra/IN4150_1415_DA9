import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class OrdinaryProcess implements I_Node, Serializable {
	
	private static final long serialVersionUID = -3188433915760515434L;
	public int level;
	public int id;
	public int owner_id;
	public CandidateProcess potentialOwner;
	public CandidateProcess owner;
	
	public final static String TAG = "[OP]";
	
	public OrdinaryProcess(int id) {
		this.level = -1;
		this.id = id;
		this.owner_id = -1;//not sure about initialization value  (saw it in the slides) 
		this.owner = null;
		this.potentialOwner = null;
		
//		for (int i = 0; i < Main.NODES; i++) {
//			try {
//				runOP();
//			} catch (RemoteException e) {
//				e.printStackTrace();
//			}
//		}
		
		System.out.println(TAG+"OP "+this+" created");
	}
	
	// don't need runOP because OP is not active
	private void runOP() throws RemoteException{
		while(true) {
                // here i should call receive function but i was not sure how i can get the level, id , and link from the sending process
		}
		 
	}

	@Override
	public void send(Link link, int level, int fromId) throws RemoteException,
			MalformedURLException, NotBoundException {

		// don't just copy paste
//		I_Node node = (I_Node) Naming.lookup("rmi://localhost/AfganOP"+link.pointTarget);
		
		System.out.println(TAG+"OP "+this+" send ("+level+","+fromId+") to CP "+link.pointTarget);
		// OP always send to CP
		I_Node node = (I_Node) Naming.lookup("rmi://localhost/AfganCP"+link.pointTarget);
		node.receive(link, level, fromId);
		
	}

	@Override
	public void receive(Link link, int level, int fromId)
			throws RemoteException, MalformedURLException, NotBoundException {
		// same reason
//        I_Node nodeFrom = (I_Node) Naming.lookup("rmi://localhost/AfganOP"+link.pointMe);
		
        I_Node nodeFrom = (I_Node) Naming.lookup("rmi://localhost/AfganCP"+link.pointMe);
        
        System.out.println(TAG+"OP "+this+" recv ("+level+","+fromId+") from CP "+link.pointMe);
		
        if (isLargerTo(level,fromId)) {
        	System.out.println(TAG+" me larger, ignored");
        }
        if ( !(isLargerTo(level,fromId)) ) {
        	
//        	potentialOwner.id = fromId;//not sure for ( potential_owner <- link' )
        	potentialOwner = (CandidateProcess) nodeFrom;
        	this.level = level;
//        	this.id = fromId;
        	// add this because the algo says so
        	this.owner_id = fromId;
        	
        	if (owner == null){
        		owner = potentialOwner;
        		System.out.println(TAG+" CP larger owner null");
           	}
//        	else{
//        		owner.send(link, level, fromId);
        		
        		// change the perspective, me = OP, target = CP
        	System.out.println(TAG+"OP "+this+" try to kill, or ack owner");
    		Link ownerlink = link.switchPerspective();
    		this.send(ownerlink, level, fromId);
//        	}
        }
        if ( (level == this.level) && (fromId == this.owner_id) ){
        	
        	System.out.println(TAG+" OLD owner officially killed "+this+" change owner to "+potentialOwner.id);
        	
        	owner = potentialOwner;
        	Link ownerlink = link.switchPerspective();
        	this.send(ownerlink, level, fromId);
        }
        
    }

	// return true if this (lvl,id) larger than target
	// otherwise else
	public boolean isLargerTo(int level_, int id_) {
		if (this.level > level_) return true;
		else if (this.level == level_) return this.owner_id > id_;
		else return false; //means level target higher
	}
	
	@Override
	public String toString() {
		return "{lvl"+this.level+" | "+this.id+"+"+this.owner_id+"}";
	}
}