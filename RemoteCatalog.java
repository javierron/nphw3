import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

/**
 * RemoteCatalog
 */
public interface RemoteCatalog extends Remote {
    public boolean register(String username, String password)throws RemoteException, SQLException;
    public boolean login(String username, String password, RemoteClient client) throws RemoteException, SQLException;
    public boolean upload(String name, long size, String user) throws RemoteException, SQLException;
    public List<String> getAvailableFiles() throws RemoteException, SQLException;
    public Metadata download(String name, String username) throws RemoteException, SQLException;
    public boolean delete(String name, String user) throws RemoteException, SQLException;
    public List<String> getUserFiles(String user) throws RemoteException, SQLException;
    public boolean setPermissions(String name, boolean write, String user) throws RemoteException, SQLException;
}