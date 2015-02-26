import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Comparator;
import java.util.List;



public interface I_Node extends Remote{
	public void receive(Message msg) throws RemoteException;
	public void send(Message msg) throws RemoteException;
	public void setHashMap(List<I_Node[]> proc) throws RemoteException;
	

	class MsgComparator implements Comparator<Message> {

		@Override
		public int compare(Message o1, Message o2) {
			if (o1.timestamp == o2.timestamp) {
				return o1.idSender - o2.idSender;
			}
			return (int) (o1.timestamp - o2.timestamp);
//			if (o1.timestamp < o2.timestamp) return -1;
//			else return 1;
		}
		
	}
}