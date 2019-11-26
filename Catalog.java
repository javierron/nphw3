import java.sql.SQLException;
import java.util.List;

/**
 * Catalog
 */
public class Catalog implements RemoteCatalog {

    Database db;

    public Catalog() {
        db = new Database();
    }

    public boolean register(String username, String password) throws SQLException{
        if(db.exists(username)){
            return false;
        }

        db.createUser(username, password);
        System.out.println("created user: " + username + " " + password);
        return true;
    }

    public boolean login(String username, String password, RemoteClient client) throws SQLException {
        return db.checkLogin(username, password);
    }

    public boolean upload(String name, long size, String user) throws SQLException {
        return db.saveFile(name, size, user);
    }

    public List<String> getAvailableFiles()throws SQLException{
        return db.getFileNames();
    }

    public Metadata download(String name)throws SQLException{
        return db.getFile(name);
    }

    public boolean delete(String name, String user)throws SQLException{
        return db.deleteFile(name, user);
    }

    public List<String> getUserFiles(String user)throws SQLException{
        return db.getFileNames(user);
    }

    public boolean setPermissions(String name, boolean write, String user) throws SQLException{
        return db.setPermission(name, write, user);
    }
    
}