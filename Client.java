import javax.swing.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Client implements RemoteClient, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    public static String forward = null;

    enum State {
        LOGIN_MENU,
        REGISTER_USERNAME,
        REGISTER_PASSWORD,
        LOGIN_USERNAME,
        LOGIN_PASSWORD,
        MAIN_MENU,
        UPLOAD_FILE,
        DOWNLOAD_FILE,
        DELETE_FILE,
        PERMISSIONS_FILE,
        PERMISSIONS_OPTION,

    }

    public void notify(String downloader, State state){

        switch (state){
            case DELETE_FILE:
                System.out.println("Your file is deleted by " + downloader);
                break;
            case DOWNLOAD_FILE:
                System.out.println("Your file is downloaded by " + downloader);
                break;
            case UPLOAD_FILE:

            default:
                break;


        }


    }

    public static void main(String[] args) {
        
        String filesPath = "/home/javier/hw3files-client";
        State state = State.LOGIN_MENU;



        String username = "";
        String filename = "";
        
        try{

            Registry registry = LocateRegistry.getRegistry();
            RemoteCatalog catalog = (RemoteCatalog)registry.lookup("catalog");
            


            while (true) {
                String pick = "";
            
                switch (state) {
                    case LOGIN_MENU:
                        pick = Menu.print_login_menu(forward);

                        if(pick.equals("1")){
                            forward = null;
                            state = State.LOGIN_USERNAME;
                        }else if(pick.equals("2")){
                            forward = null;
                            state = State.REGISTER_USERNAME;
                        }else if(pick.equals("3")){
                            System.exit(0);
                        }
                        break;
                    case REGISTER_USERNAME:
                        username = Menu.print_register_username_menu(forward);
                        if(username.equals("back")){
                            forward = null;
                            state = State.LOGIN_MENU;
                            continue;
                        }
                        state = State.REGISTER_PASSWORD;
                        break;
                    case REGISTER_PASSWORD:
                        String rPassword = Menu.print_register_password_menu();


                        boolean rSuccess = catalog.register(username, rPassword);
                        if(rSuccess){
                            forward = null;
                            state = State.LOGIN_MENU;
                        }else{
                            forward = "The username already in use, pick a different username.";
                            state = State.REGISTER_USERNAME;
                        }
                        break;
                    case LOGIN_USERNAME:
                        username = Menu.print_login_username_menu(forward);
                        if(username.equals("back")){
                            forward = null;
                            state = State.LOGIN_MENU;
                            continue;
                        }
                        state = State.LOGIN_PASSWORD;
                        break;
                    case LOGIN_PASSWORD:
                        String lPassword = Menu.print_login_password_menu();


                        RemoteClient stub = (RemoteClient)UnicastRemoteObject.exportObject(new Client(), 0);
                        boolean lSuccess = catalog.login(username, lPassword, stub);

                        System.out.println(lSuccess);

                        if(lSuccess){
                            forward = null;
                            state = State.MAIN_MENU;
                        }else{
                            forward = "username/password incorrect, please try again.";
                            state = State.LOGIN_USERNAME;
                        }
                        break;
                    case MAIN_MENU:
                        pick = Menu.print_main_menu(forward);
                       
                        if(pick.equals("1")){
                            forward = null;
                            state = State.UPLOAD_FILE;
                        }else if(pick.equals("2")){
                            forward = null;
                            state = State.DOWNLOAD_FILE;
                        }else if(pick.equals("3")){
                            forward = null;
                            state = State.DELETE_FILE;
                        }
                        else if(pick.equals("4")){
                            forward = null;
                            state = State.PERMISSIONS_FILE;
                        }
                        break;
                    case UPLOAD_FILE:
                    
                        try (Stream<Path> paths = Files.walk(Paths.get(filesPath))) {

                            List<Path> pathList = paths.filter(Files::isRegularFile).collect(Collectors.toList());

                            pick = Menu.print_upload_menu(forward, pathList);

                            if(pick.equals("back")){
                                forward = null;
                                state = State.MAIN_MENU;
                                continue;
                            }
                        
                            int index = Integer.parseInt(pick);
                            Path path = pathList.get(index);
                            String f = path.getFileName().toString();
                            boolean up = catalog.upload(f, path.toFile().length() , username);
                            
                            if(up){
                                forward = "File " +  path.getFileName().toString() + "was uploaded";
                                Thread t = new Thread(new UploadThread(f));
                                t.start();
                            }else {
                                forward = "File " +  path.getFileName().toString() + "could not be updated. Permission denied";
                            }

                            state = State.MAIN_MENU;

                        } catch(Exception e){
                            e.printStackTrace();
                            System.exit(1);
                        }
                        break;

                    case DOWNLOAD_FILE:
                        try {
                            
                            List<String> files = catalog.getAvailableFiles();
                            pick = Menu.print_download_menu(forward, files);

                            if(pick.equals("back")){
                                forward = null;
                                state = State.MAIN_MENU;
                                continue;
                            }
                            
                            int index = Integer.parseInt(pick);
                            String name = files.get(index);
                            Metadata metadata = catalog.download(name,username);
                            forward = "Downloaded: " + metadata.name + " " + metadata.size;
                            state = State.MAIN_MENU;
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.exit(1);
                        }
                            
                        break;

                    case DELETE_FILE:
                        try {
                            List<String> files = catalog.getAvailableFiles();
                            pick = Menu.print_download_menu(forward, files);

                            if(pick.equals("back")){
                                forward = null;
                                state = State.MAIN_MENU;
                                continue;
                            }
                            
                            int index = Integer.parseInt(pick);
                            String name = files.get(index);
                            boolean del = catalog.delete(name, username);
                            if(del){
                                forward = "Deleted: " + name + " from catalog";
                            }else{
                                forward = "Could not delete: " + name + ". Permission denied";
                            }
                            state = State.MAIN_MENU;
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.exit(1);
                        }
                            
                        break;

                        //check permission of the file

                    case PERMISSIONS_FILE:
                        try {
                            List<String> files = catalog.getUserFiles(username);
                            pick = Menu.print_permission_menu(forward, files);

                            if(pick.equals("back")){
                                forward = null;
                                state = State.MAIN_MENU;
                                continue;
                            }
                            
                            int index = Integer.parseInt(pick);
                            filename = files.get(index);
                            state = State.PERMISSIONS_FILE;
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.exit(1);
                        }

                        //offer permission to the file
                    case PERMISSIONS_OPTION:
                        pick = Menu.print_permission_option_menu(forward);
                        
                        boolean write;
                        
                        if(pick.equals("1")){
                            write = false;
                        }else if (pick.equals("2")){
                            write = true;
                        }else{
                            forward = "Invalid option.";
                            continue;
                        }

                        boolean perm = catalog.setPermissions(filename, write, username);

                        if(perm){
                            forward = "Correctly set permission.";
                        }else {
                            forward = "Could not set permission.";
                        }
                        state = State.MAIN_MENU;
                    break;
                    default:
                        if(forward != null) System.out.println(forward);
                        break;
                }
                
            }


        }catch (Exception e ){
            System.out.println(e);

        }
    }
}

class UploadThread implements Runnable {

    String filename;

    public UploadThread(String filename){
        this.filename = filename;
    }

    @Override
    public void run() {
        try {
            Socket socket = null;
            String host = "localhost";

            socket = new Socket(host, 4321);

            File file = new File("/home/javier/hw3files-client/" + filename);
            //long length = file.length();
            byte[] bytes = new byte[8 * 1024];
            InputStream in = new FileInputStream(file);
            OutputStream out = socket.getOutputStream();

            int count;
            while ((count = in.read(bytes)) > 0) {
                out.write(bytes, 0, count);
            }

            out.close();
            in.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}