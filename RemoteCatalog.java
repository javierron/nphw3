import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

/**
 * RemoteCatalog
 */
public interface RemoteCatalog extends Remote {
    public boolean register(String username, String password)throws RemoteException;
    public boolean login(String username, String password, RemoteClient client) throws RemoteException;
    public boolean upload(String name, long size, String user) throws RemoteException;
    public List<String> getAvailableFiles() throws RemoteException;
    public Metadata download(String name) throws RemoteException;
    public boolean delete(String name, String user) throws RemoteException;
    public List<String> getUserFiles(String user) throws RemoteException;
    public boolean setPermissions(String name, boolean write, String user) throws RemoteException;
}