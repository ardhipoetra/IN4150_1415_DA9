import java.io.PrintWriter;
import java.rmi.Naming;
import java.util.Date;

public class DATotalOrderingmain {

	static final int PROCESS_NUMBER = 3;
	static final int MESSAGES_NUMBER = 2;
	
	public static void main(String[] args) throws Exception{
		DATotalOrdering_RMI dai;
		DATotalOrdering cls;
//		System.setSecurityManager(new RMISecurityManager());

//		try {
//			java.rmi.registry.LocateRegistry.createRegistry(1099);
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
		
		new PrintWriter("send.txt").close();
		new PrintWriter("rcvMsg.txt").close();
		new PrintWriter("rcvAck.txt").close();
		new PrintWriter("dlvrMsg.txt").close();
		
		for (int i = 0; i < PROCESS_NUMBER; i++) {
			cls = new DATotalOrdering(i);
			Naming.rebind("rmi://localhost/RD"+i, cls);
		}
		
		DATotalOrdering_RMI proc[] = new DATotalOrdering_RMI[PROCESS_NUMBER];
		for (int i = 0; i < PROCESS_NUMBER; i++) {
			proc[i] = (DATotalOrdering_RMI) Naming.lookup("rmi://localhost/RD"+i);
		}
		
		long timeStart = new Date().getTime();
		
		for (int i = 0; i < PROCESS_NUMBER; i++) {
			proc[i].setProcessesNetwork(proc);
			proc[i].setStartTime(timeStart);
		}
		
		for (int i = 0; i < PROCESS_NUMBER; i++) {
			
				final int countP = i;
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
