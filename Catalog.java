import java.util.List;

/**
 * Catalog
 */
public class Catalog implements RemoteCatalog {

    UserDB userDB;
    MetadataDB metadataDB;

    public Catalog() {
        userDB = new UserDB();
        metadataDB = new MetadataDB();
    }

    public boolean register(String username, String password){
        if(userDB.exists(username)){
            return false;
        }

        userDB.createUser(username, password);
        System.out.println("created user: " + username + " " + password);
        return true;
    }

    public boolean login(String username, String password, RemoteClient client){
        try{
            client.notify(100);
        }catch (Exception e){
            e.printStackTrace();
        }
        return userDB.checkLogin(username, password);
    }

    public boolean upload(String name, long size, String user){
        metadataDB.saveFile(name, size, user);
        System.out.println("Saved: " + name + " " + size );
        return true;
    }

    public List<String> getAvailableFiles(){
        return metadataDB.getFileNames();
    }

    public Metadata download(String name){
        return metadataDB.getFile(name);
    }

    public boolean delete(String name, String user){
        return metadataDB.deleteFile(name, user);
    }

    public List<String> getUserFiles(String user){
        return metadataDB.getFileNames(user);
    }

    public boolean setPermissions(String name, boolean write, String user){
        return metadataDB.setPermission(name, write, user);
    }
    
}