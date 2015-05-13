import java.net.MalformedURLException;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.concurrent.CountDownLatch;


public class Main {

	public final static int NODES = 50;
	
	public final static String TAG = "[MAIN]";
	
	private final static CountDownLatch countDownLatch  = new CountDownLatch(1);
	
	public static void main(String[] args) throws RemoteException, MalformedURLException, InterruptedException, NotBoundException, AlreadyBoundException {
		OrdinaryProcess opinit;
		
		for (int i = 0; i < NODES; i++) {
			opinit = new OrdinaryProcess(i);
//			System.out.println(i+" > "+opinit);
//			Naming.rebind("rmi://localhost/AfganOP"+i, opinit);
		}
		
		Thread t1 = new Thread() {
			@Override
			public void run() {
				try {
					countDownLatch.await();
					new CandidateProcess(21);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		Thread t2 = new Thread() {
			@Override
			public void run() {
				try {
					countDownLatch.await();
					new CandidateProcess(0);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		};
		
		t1.start();
		t2.start();
		countDownLatch.countDown();
		
		
	}

}
