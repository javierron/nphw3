import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

/**
 * Server
 */
public class Server {

    public static void main(String[] args) {

        try {
            
            
            Catalog catalog = new Catalog();
            RemoteCatalog stub = (RemoteCatalog)UnicastRemoteObject.exportObject(catalog, 0);
            
            Registry registry = LocateRegistry.getRegistry();
            registry.bind("catalog", stub);
            
            System.out.println("catalog ready");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong");
        }
    }


}