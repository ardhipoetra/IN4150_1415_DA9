import java.rmi.Remote;
import java.rmi.RemoteException;


public interface DATotalOrdering_RMI extends Remote {
	
	public int broadcast(final Messages msg) throws RemoteException;
	public void receive(Messages msg) throws RemoteException;
//	public void send(Messages msg, DATotalOrdering_RMI[] proc) throws RemoteException;
	public void setProcessesNetwork(DATotalOrdering_RMI[] proc) throws RemoteException;
	public void setStartTime(long t) throws RemoteException;
}
