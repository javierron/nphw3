import java.util.HashMap;

public class ClientDB {

    HashMap<String, RemoteClient> clientDB;

    public ClientDB(){

        clientDB = new HashMap<>();
    }

    public RemoteClient getRemoteClient(String username){
        return clientDB.get(username);
    }

    public void createRemoteClient(String username, RemoteClient client){
            clientDB.put(username, client);
    }


}
