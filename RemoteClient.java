import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * RemoteCatalog
 */
public interface RemoteClient
 extends Remote {
    public void notify(int code) throws RemoteException;
}