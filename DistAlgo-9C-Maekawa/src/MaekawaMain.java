import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;


public class MaekawaMain {
	
	public static final int NODE = 3;
	public static final int K = 2;
	
	static I_Node[] nodes;
	
	public static final int[][] REQ_S = 
	{
		{0,1},
		{1,2},
		{0,2}
	};
	

	public static void main(String[] args) throws RemoteException, MalformedURLException, NotBoundException {
		Node n;
		ArrayList<I_Node[]> reqsets = new ArrayList<I_Node[]>();
		nodes = new I_Node[NODE];
		
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
		
		for (int i = 1; i <= 2; i++) {
			final int countP = i;
			Thread tr = new Thread("MAIN_"+countP){
				@Override
				public void run() {
					try {
						Thread.sleep((long)(Math.random() * 200));
						
						Message testmsg = new Message();
						testmsg.idSender = countP;
						testmsg.idDest = 0;
						testmsg.timestamp = new Date().getTime();
						testmsg.type = Message.TYPE_REQUEST;
						
						nodes[countP].send(testmsg, nodes[0]);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			
			tr.start();
		}
	}

}
