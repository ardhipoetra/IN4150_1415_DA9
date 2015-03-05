import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;


public class MaekawaMain {
	
	public static final int NODE = 3;
	public static final int K = 2;
	
	public static final int[][] REQ_S = 
	{
		{0,1},
		{1,2},
		{0,2}
	};
	

	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		Node n;
		ArrayList<I_Node[]> reqsets = new ArrayList<I_Node[]>();
		
//		System.setSecurityManager(new RMISecurityManager());

//		try {
//			java.rmi.registry.LocateRegistry.createRegistry(1099);
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
		
		
		for (int i = 0; i < NODE; i++) {
			n = new Node(i);
			Naming.rebind("rmi://localhost/Mae"+i, n);
		}
		
		I_Node[] nodes = new I_Node[NODE];
		
		for (int i = 0; i < NODE; i++) {
			nodes[i] = (I_Node) Naming.lookup("rmi://localhost/Mae"+i);
		}
		
		for (int i = 0; i < NODE; i++) {
			nodes[i].setProcessNetwork(nodes);
		}
		
		for (int i = 0; i < NODE; i++) {
			I_Node[] reqElems = new I_Node[REQ_S[i].length];
			int count = 0;
			for (int ii : REQ_S[i]) {
				reqElems[count++] = nodes[ii];
			}
			reqsets.add(reqElems);
		}
		
		for (int i = 0; i < NODE; i++) {
			nodes[i].setHashMap(reqsets);
		}
		
		Message s = new Message();
		s.idSender = 0;
		s.timestamp = new Date().getTime();
		s.type = Message.TYPE_REQUEST;
		
		nodes[0].send(s, nodes[1]);
		
	}

}
