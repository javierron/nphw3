import java.sql.SQLException;
import java.util.List;

/**
 * Catalog
 */
public class Catalog implements RemoteCatalog {

    Database db;
    ClientDB clientDB;

    public Catalog() {
        db = new Database();
        clientDB = new ClientDB();
    }

    public boolean register(String username, String password) throws SQLException {



        if(db.exists(username)){
            return false;
        }

        db.createUser(username, password);
        System.out.println("created user: " + username + " " + password);
        return true;
    }

    public boolean login(String username, String password, RemoteClient client) throws SQLException {

        clientDB.createRemoteClient(username,client);
        return db.checkLogin(username, password);
    }

    public boolean upload(String name, long size, String user)throws SQLException{

        String uploader_name = null;
        Metadata metadata = db.getFile(name);
        if(metadata != null) {
             uploader_name = metadata.owner;
        }

        db.saveFile(name, size, user);

        if(uploader_name !=null) {


            RemoteClient uploader = clientDB.getRemoteClient(uploader_name);

            try {
                uploader.notify(user, Client.State.UPLOAD_FILE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        System.out.println("Saved: " + name + " " + size );
        return true;
    }

    public List<String> getAvailableFiles()throws SQLException{

        return db.getFileNames();
    }

    public Metadata download(String name, String downloader_name) throws SQLException{
        Metadata metadata = db.getFile(name);
        String uploader_name = metadata.owner;
        RemoteClient uploader = clientDB.getRemoteClient(uploader_name);


        try{
            uploader.notify(downloader_name, Client.State.DOWNLOAD_FILE);
        }catch (Exception e){
            e.printStackTrace();
        }

        return metadata;

    }

    public boolean delete(String name, String user)throws SQLException{

        Metadata metadata = db.getFile(name);
        String uploader_name = metadata.owner;
        RemoteClient uploader = clientDB.getRemoteClient(uploader_name);

        boolean status = db.deleteFile(name, user);

        try{
            uploader.notify(user, Client.State.DELETE_FILE );
        }catch (Exception e){
            e.printStackTrace();
        }


        return status;
    }

    public List<String> getUserFiles(String user)throws SQLException{
        return db.getFileNames(user);
    }

    public boolean setPermissions(String name, boolean write, String user)throws SQLException{

        Metadata metadata = db.getFile(name);
        String uploader_name = metadata.owner;
        RemoteClient uploader = clientDB.getRemoteClient(uploader_name);

        boolean status = db.setPermission(name, write, user);

        try{
            uploader.notify(user, Client.State.PERMISSIONS_FILE );
        }catch (Exception e){
            e.printStackTrace();
        }

        return status;
    }
    
}