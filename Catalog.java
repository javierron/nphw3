import java.util.List;

/**
 * Catalog
 */
public class Catalog implements RemoteCatalog {

    UserDB userDB;
    MetadataDB metadataDB;
    ClientDB clientDB;

    public Catalog() {
        userDB = new UserDB();
        metadataDB = new MetadataDB();
        clientDB = new ClientDB();
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

        clientDB.createRemoteClient(username,client);
        return userDB.checkLogin(username, password);
    }

    public boolean upload(String name, long size, String user){

        String uploader_name = null;
        Metadata metadata = metadataDB.getFile(name);
        if(metadata != null) {
             uploader_name = metadata.owner;
        }

        MetadataDB.FileStaus fileStaus = metadataDB.saveFile(name, size, user);

        if(fileStaus == MetadataDB.FileStaus.UPDATE || uploader_name !=null) {


            RemoteClient uploader = clientDB.getRemoteClient(uploader_name);

            try {
                uploader.notify(user, Client.State.DOWNLOAD_FILE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        System.out.println("Saved: " + name + " " + size );
        return true;
    }

    public List<String> getAvailableFiles(){

        return metadataDB.getFileNames();
    }

    public Metadata download(String name, String downloader_name){
        Metadata metadata = metadataDB.getFile(name);
        String uploader_name = metadata.owner;
        RemoteClient uploader = clientDB.getRemoteClient(uploader_name);


        try{
            uploader.notify(downloader_name, Client.State.DOWNLOAD_FILE);
        }catch (Exception e){
            e.printStackTrace();
        }

        return metadata;

    }

    public boolean delete(String name, String user){

        Metadata metadata = metadataDB.getFile(name);
        String uploader_name = metadata.owner;
        RemoteClient uploader = clientDB.getRemoteClient(uploader_name);

        boolean status = metadataDB.deleteFile(name, user);

        try{
            uploader.notify(user, Client.State.DELETE_FILE );
        }catch (Exception e){
            e.printStackTrace();
        }


        return status;
    }

    public List<String> getUserFiles(String user){
        return metadataDB.getFileNames(user);
    }

    public boolean setPermissions(String name, boolean write, String user){

        Metadata metadata = metadataDB.getFile(name);
        String uploader_name = metadata.owner;
        RemoteClient uploader = clientDB.getRemoteClient(uploader_name);

        boolean status = metadataDB.setPermission(name, write, user);

        try{
            uploader.notify(user, Client.State.PERMISSIONS_FILE );
        }catch (Exception e){
            e.printStackTrace();
        }

        return status;
    }
    
}