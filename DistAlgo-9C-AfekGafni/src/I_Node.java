import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;


public interface I_Node extends Remote{
	public void send(Link link, int level, int fromId) throws RemoteException, MalformedURLException, NotBoundException;
	public void receive(Link link, int level, int fromId) throws RemoteException, MalformedURLException, NotBoundException;
}
