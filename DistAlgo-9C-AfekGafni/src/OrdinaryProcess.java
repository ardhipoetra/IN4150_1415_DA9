import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class OrdinaryProcess implements I_Node {
	
	public int level;
	public int id;
	public CandidateProcess potentialOwner;
	public CandidateProcess owner;
	
	public OrdinaryProcess(int id) {
		this.level = -1;
		this.id = id;
		this.owner = null;
		this.potentialOwner = null;
	}

	@Override
	public void send(Link link, int level, int fromId) throws RemoteException,
			MalformedURLException, NotBoundException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receive(Link link, int level, int fromId)
			throws RemoteException, MalformedURLException, NotBoundException {
		// TODO Auto-generated method stub
		
	}

}
