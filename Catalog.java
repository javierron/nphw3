import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

/**
 * Catalog
 */
public class Catalog implements RemoteCatalog {

    Database db;
    ClientDB clientDB;
    TransferServer transfer;
    DownloadServer downloadServer;

    public Catalog() throws IOException {
        db = new Database();
        clientDB = new ClientDB();
        transfer = new TransferServer();

        Thread t = new Thread(transfer);
        t.start();

        downloadServer = new DownloadServer();
        Thread t1 = new Thread(downloadServer);
        t1.start();
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
        transfer.setFilename(name);

        if(!db.saveFile(name, size, user)){
            return false;
        }

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
        
        downloadServer.setDownloadName(name);
        
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
 
    
    class TransferServer implements Runnable {
        ServerSocket sSocket;
        String filename;

        public TransferServer(){
            try {
                sSocket = new ServerSocket(4321);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void setFilename(String filename) {
            this.filename = filename;
        }
        
        @Override
        public void run() {
            while(true){
                Socket socket = null;
                InputStream in = null;
                OutputStream out = null;

                try {
                    socket = sSocket.accept();
                    String javier_path = "/home/javier/hw3files-server/";
                    String ethan_path = "/Users/fccc/Downloads/";
                    in = socket.getInputStream();
                    out = new FileOutputStream(javier_path + filename);
                    
                    byte[] bytes = new byte[8 * 1024];
                    
                    int read;
                    while ((read = in.read(bytes)) > 0) {
                        out.write(bytes, 0, read);
                    }
                    
                    out.close();
                    in.close();
                    socket.close();
                    //sSocket.close();
                
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(0);
                }
            }
        }
    }


    class DownloadServer implements Runnable{
        ServerSocket ssock;
        String filename;
        public DownloadServer() throws IOException {

            ssock = new ServerSocket(4322);


        }

        public void setDownloadName(String name){
            this.filename = name;
        }

        @Override
        public void run() {

            while (true) {
                try {
                    Socket socket = ssock.accept();

                    String javier_path = "/home/javier/hw3files-server/";
                    String ethan_path = "/Users/fccc/Downloads/";

                    File file = new File(javier_path + filename);
                    FileInputStream fis = new FileInputStream(file);

                    OutputStream os = socket.getOutputStream();


                    byte[] bytes = new byte[8 * 1024];

                    int count;
                    while ((count = fis.read(bytes)) > 0) {
                        os.write(bytes, 0, count);
                    }

                    fis.close();
                    os.close();
                    socket.close();
                    //ssock.close();


                } catch (IOException e) {
                    e.printStackTrace();
                }


            }
        }
    }






}