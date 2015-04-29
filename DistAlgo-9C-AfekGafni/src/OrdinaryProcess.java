import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class OrdinaryProcess implements I_Node {
	
	public int level;
	public int id;
	public int owner_id;
	public CandidateProcess potentialOwner;
	public CandidateProcess owner;
	
	public OrdinaryProcess(int id) {
		this.level = -1;
		this.id = id;
		this.owner_id = 0;//not sure about initialization value  (saw it in the slides) 
		this.owner = null;
		this.potentialOwner = null;
		
		for (int i = 0; i < Main.NODES; i++) {
			try {
				runOP();
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void runOP() throws RemoteException{
		while(true) {
                // here i should call receive function but i was not sure how i can get the level, id , and link from the sending process
		}
		 
	}

	@Override
	public void send(Link link, int level, int fromId) throws RemoteException,
			MalformedURLException, NotBoundException {

		I_Node node = (I_Node) Naming.lookup("rmi://localhost/AfganOP"+link.pointTarget);
		node.receive(link, level, fromId);
		
	}

	@Override
	public void receive(Link link, int level, int fromId)
			throws RemoteException, MalformedURLException, NotBoundException {
        I_Node nodeFrom = (I_Node) Naming.lookup("rmi://localhost/AfganOP"+link.pointMe);
		
        if (isLargerTo(level,fromId)) {}
        if ( !(isLargerTo(level,fromId)) ) {
        	potentialOwner.id = fromId;//not sure for ( potential_owner <- link' ) 
        	this.level = level;
        	this.id = fromId;
        	if (owner == null){
        		owner = potentialOwner;
           	}
        	else{
        		owner.send(link, level, fromId);
        	}
        }
        if ( (level == this.level) && (fromId == this.owner_id) ){
        	owner = potentialOwner;
        	owner.send(link, level, fromId);
        }
        
    }

	public boolean isLargerTo(int level_, int id_) {
		if (this.level > level_) return true;
		else if (this.level == level_) return this.id > id_;
		else return false; 
	}
}