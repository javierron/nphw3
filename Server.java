import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.CountDownLatch;

/**
 * Server
 */
public class Server {

    public static void main(String[] args) throws InterruptedException, IOException {

        try{
            LocateRegistry.getRegistry().list();
        }catch (RemoteException noRegistryRunning){
            LocateRegistry.createRegistry(Registry.REGISTRY_PORT);
        }


        Catalog catalog = new Catalog();
            RemoteCatalog stub = (RemoteCatalog)UnicastRemoteObject.exportObject(catalog, 0);
            
        //Registry registry = LocateRegistry.getRegistry();
            //registry.unbind("catalog");
            //registry.bind("catalog", stub);
        Naming.rebind("catalog", stub);
        System.out.println("catalog ready");

    }


}