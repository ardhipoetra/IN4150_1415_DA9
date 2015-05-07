import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;


public class Main {

	public final static int NODES = 5;
	
	public final static String TAG = "[MAIN]";
	
	public static void main(String[] args) throws RemoteException, MalformedURLException, InterruptedException, NotBoundException, AlreadyBoundException {
		OrdinaryProcess opinit;
		
		for (int i = 0; i < NODES; i++) {
			opinit = new OrdinaryProcess(i);
//			System.out.println(i+" > "+opinit);
//			Naming.rebind("rmi://localhost/AfganOP"+i, opinit);
		}
		
		new CandidateProcess(2);
		new CandidateProcess(0);
		
		
		
	}

}
