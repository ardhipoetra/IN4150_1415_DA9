import java.io.PrintWriter;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.util.Date;

public class DATotalOrderingmain {

	// total proc in system
	public static int TOTAL_PROCESS_NUMBER = 3;
	// total msg per proc 
	public static int MESSAGES_NUMBER = 2;
	// total thread per java-proc
	public static int THREAD_NUMBER = 1;
	// the id of java-proc
	public static int JAVAPROC_ID = -1;
	
	public static void main(String[] args) throws Exception{
		DATotalOrdering cls;
//		System.setSecurityManager(new RMISecurityManager());

//		try {
//			java.rmi.registry.LocateRegistry.createRegistry(1099);
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
		
		JAVAPROC_ID = Integer.parseInt(args[0]);
		TOTAL_PROCESS_NUMBER= Integer.parseInt(args[1]);
		THREAD_NUMBER= Integer.parseInt(args[2]);
		MESSAGES_NUMBER = Integer.parseInt(args[3]);
		
		
		
		
		
		for (int i = 0; i < THREAD_NUMBER; i++) {
			int globalId = JAVAPROC_ID * THREAD_NUMBER + i;
			cls = new DATotalOrdering(globalId);
			Naming.rebind("rmi://localhost/RD"+globalId, cls);
			
			new PrintWriter("send-"+globalId+".txt").close();
			new PrintWriter("rcvMsg-"+globalId+".txt").close();
			new PrintWriter("rcvAck-"+globalId+".txt").close();
			new PrintWriter("dlvrMsg-"+globalId+".txt").close();
		}
		
		DATotalOrdering_RMI proc[] = new DATotalOrdering_RMI[TOTAL_PROCESS_NUMBER];
		
		
		boolean ready = false;
		while(!ready) {
			try {
				for (int i = 0; i < TOTAL_PROCESS_NUMBER; i++) {
					proc[i] = (DATotalOrdering_RMI) Naming.lookup("rmi://localhost/RD"+i);
				}
				ready = true;
			} catch(NotBoundException nbe) {
				System.out.println("not ready, wait for others");
			}
		}
		
		long timeStart = new Date().getTime();
		
		// everyone set this locally
		for (int i = 0; i < THREAD_NUMBER; i++) {
			proc[JAVAPROC_ID * THREAD_NUMBER + i].setProcessesNetwork(proc);
			proc[JAVAPROC_ID * THREAD_NUMBER + i].setStartTime(timeStart);
		}
		
		for (int i = 0; i < THREAD_NUMBER; i++) {
			
				final int countP = JAVAPROC_ID * THREAD_NUMBER + i;
				
				// this object will act
				final DATotalOrdering_RMI daObj = proc[countP];
				
				Thread tr = new Thread("MAIN_"+countP){
					@Override
					public void run() {
						for (int j = 0; j < MESSAGES_NUMBER; j++) {
							
							try {
								Thread.sleep((long)(Math.random() * 500));
								
								Messages testmsg = new Messages();
								testmsg.msg = "The Message P"+countP+"x"+j;
								testmsg.idSender = countP;
								testmsg.timestamp = new Date().getTime();
								daObj.broadcast(testmsg);
								
								
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				};
				tr.start();
		}
	}

}
