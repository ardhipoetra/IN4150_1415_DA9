import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Date;


public class MaekawaMain {
	
	public static final int NODE = 3;
	public static final int K = 2; // size of request set
	
	static I_Node[] nodes;
	
	public static final int[][] REQ_S = 
	{
		{0,1},
		{1,2},
		{0,2}
	};
			
//	{
//		{0,1,2},
//		{1,4,6},
//		{2,3,4},
//		{0,3,6},
//		{0,4,5},
//		{1,3,5},
//		{2,5,6}	
//	};
	

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
		
		
//		Thread tr = new Thread("MAIN_02"){
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(Math.round(Math.random() * 1000));
//					
//					Message testmsg = new Message();
//					testmsg.idSender = 0;
//					testmsg.idDest = 2;
//					testmsg.timestamp = new Date().getTime();
//					testmsg.type = Message.TYPE_REQUEST;
//					
//					nodes[0].send(testmsg, nodes[2]);
//					
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		};
//		
//		Thread tr2 = new Thread("MAIN_01"){
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(Math.round(Math.random() * 1000));
//					
//					Message testmsg = new Message();
//					testmsg.idSender = 0;
//					testmsg.idDest = 1;
//					testmsg.timestamp = new Date().getTime();
//					testmsg.type = Message.TYPE_REQUEST;
//					
//					nodes[0].send(testmsg, nodes[1]);
//					
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		};
//		
//		Thread tr3 = new Thread("MAIN_12"){
//			@Override
//			public void run() {
//				try {
//					Thread.sleep(Math.round(Math.random() * 1000));
//					
//					Message testmsg = new Message();
//					testmsg.idSender = 1;
//					testmsg.idDest = 2;
//					testmsg.timestamp = new Date().getTime();
//					testmsg.type = Message.TYPE_REQUEST;
//					
//					nodes[1].send(testmsg, nodes[2]);
//					
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		};
//			
//		
//		tr.start(); tr2.start(); tr3.start();
		
		
		
		
		for (int i = 0; i < NODE; i++) {
			final int countP = i;
			for (int j = 0; j < REQ_S[countP].length ; j++) {
				final int countJ = j;
				Thread tr = new Thread("MAIN_"+countP){
					@Override
					public void run() {
						try {
							Thread.sleep(Math.round(Math.random() * 300));
							
							Message testmsg = new Message();
							testmsg.idSender = countP;
							testmsg.idDest = REQ_S[countP][countJ];
							testmsg.timestamp = new Date().getTime();
							testmsg.type = Message.TYPE_REQUEST;
							
							nodes[countP].send(testmsg, nodes[testmsg.idDest]);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				};
				tr.start();
			}
			
		}
	}

}
