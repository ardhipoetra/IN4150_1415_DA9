import java.rmi.Naming;
import java.sql.Timestamp;
import java.util.Date;

public class DA9Cmain {

	static final int PROCESS_NUMBER = 5;
	
	public static void main(String[] args) throws Exception{
		DA9C_RMI dai;
		DA9C cls;
//		System.setSecurityManager(new RMISecurityManager());
		for (int i = 0; i < PROCESS_NUMBER; i++) {
			cls = new DA9C(i);
			Naming.rebind("rmi://localhost/RD"+i, cls);
		}
		
		DA9C_RMI proc[] = new DA9C_RMI[PROCESS_NUMBER];
		for (int i = 0; i < PROCESS_NUMBER; i++) {
			proc[i] = (DA9C_RMI) Naming.lookup("rmi://localhost/RD"+i);
//			proc[i].setProcessesNetwork(proc);
		}
		
		
		Messages testmsg = new Messages(	);
		testmsg.msg = "something";
		testmsg.idSender = 0;
		testmsg.timestamp = new Date().getTime();
		
		proc[0].broadcast(testmsg,proc);
	}

}
