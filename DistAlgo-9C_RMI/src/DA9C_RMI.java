import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.PriorityQueue;


public interface DA9C_RMI extends Remote {
	
	public int broadcast(Messages msg, DA9C_RMI[] proc) throws RemoteException;
	public void receive(Messages msg, DA9C_RMI[] proc) throws RemoteException;
//	public void setProcessesNetwork(Object proc[]);
}
