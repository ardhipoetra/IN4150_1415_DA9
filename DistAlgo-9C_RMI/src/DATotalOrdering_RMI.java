import java.rmi.Remote;
import java.rmi.RemoteException;


public interface DATotalOrdering_RMI extends Remote {
	
	public int broadcast(Messages msg, DATotalOrdering_RMI[] proc) throws RemoteException;
	public void receive(Messages msg, DATotalOrdering_RMI[] proc) throws RemoteException;
//	public void send(Messages msg, DATotalOrdering_RMI[] proc) throws RemoteException;
//	public void setProcessesNetwork(Object proc[]);
}
