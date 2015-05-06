import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;


public class Main {

	public final static int NODES = 5;
	
	public final static String TAG = "[MAIN]";
	
	public static void main(String[] args) throws RemoteException, MalformedURLException, InterruptedException {
		OrdinaryProcess opinit;
		
		for (int i = 0; i < NODES; i++) {
			opinit = new OrdinaryProcess(i);
			Naming.rebind("rmi://localhost/AfganOP"+i, opinit);
		}
		
		System.out.println(TAG+"sleep for 1 sec");
		Thread.sleep(1000);
		
		CandidateProcess cp = new CandidateProcess(1);
		Naming.rebind("rmi://localhost/AfganCP"+1, cp);
		
		cp.runCP();
	}

}
