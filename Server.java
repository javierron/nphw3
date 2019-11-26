import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.CountDownLatch;

/**
 * Server
 */
public class Server {

    public static void main(String[] args) throws InterruptedException {



        try {


            Catalog catalog = new Catalog();
            RemoteCatalog stub = (RemoteCatalog)UnicastRemoteObject.exportObject(catalog, 0);
            
            Registry registry = LocateRegistry.getRegistry();
            //registry.unbind("catalog");
            registry.bind("catalog", stub);

            System.out.println("catalog ready");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Something went wrong in server");
        }
    }


}