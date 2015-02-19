import java.rmi.Naming;
import java.rmi.RMISecurityManager;
import java.rmi.RemoteException;
import java.util.Date;

public class DATotalOrderingmain {

	static final int PROCESS_NUMBER = 10;
	
	public static void main(String[] args) throws Exception{
		DATotalOrdering_RMI dai;
		DATotalOrdering cls;
//		System.setSecurityManager(new RMISecurityManager());

//		try {
//			java.rmi.registry.LocateRegistry.createRegistry(1099);
//		} catch (RemoteException e) {
//			e.printStackTrace();
//		}
		
		for (int i = 0; i < PROCESS_NUMBER; i++) {
			cls = new DATotalOrdering(i);
			Naming.rebind("rmi://localhost/RD"+i, cls);
		}
		
		DATotalOrdering_RMI proc[] = new DATotalOrdering_RMI[PROCESS_NUMBER];
		for (int i = 0; i < PROCESS_NUMBER; i++) {
			proc[i] = (DATotalOrdering_RMI) Naming.lookup("rmi://localhost/RD"+i);
		}
		
		for (int i = 0; i < PROCESS_NUMBER; i++) {
			proc[i].setProcessesNetwork(proc);
		}
		
		for (int i = 0; i < PROCESS_NUMBER; i++) {
			
			final int count = i;
			final DATotalOrdering_RMI daObj = proc[count];
			Thread tr = new Thread("MAIN_"+count){
				@Override
				public void run() {
					Messages testmsg = new Messages();
					testmsg.msg = "The Message P"+count;
					testmsg.idSender = count;
					testmsg.timestamp = new Date().getTime();
					
					try {
						daObj.broadcast(testmsg);
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
			};
			
			tr.start();
		}
		
		
		
		
	}

}
