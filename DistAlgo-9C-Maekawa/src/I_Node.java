import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Comparator;
import java.util.List;



public interface I_Node extends Remote{
	public void receive(Message msg) throws RemoteException;
	public void send(Message msg, I_Node dest) throws RemoteException;
	public void setHashMap(List<I_Node[]> proc) throws RemoteException;
	public void setProcessNetwork(I_Node[] nodes) throws RemoteException;
	public void enterCS() throws RemoteException;
	
	public int getId() throws RemoteException;
	

	class MsgComparator implements Comparator<Message>, Serializable{

		private static final long serialVersionUID = -1982104430673196394L;

		@Override
		public int compare(Message o1, Message o2) {
			if (o1.timestamp == o2.timestamp) {
				return o1.idSender - o2.idSender;
			}
			return (int) (o1.timestamp - o2.timestamp);
		}
		
	}
}
