import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.CountDownLatch;

public class Registry {
    public static void main(String[] args) throws InterruptedException{


        try {
            LocateRegistry.createRegistry(8080);
        } catch (RemoteException e) {
            e.printStackTrace();
        }

    }
}
