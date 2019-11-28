import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;
import java.io.BufferedReader;

public class Menu {

    static String print_login_menu(String forward) throws IOException{
        System.out.print("\033[H\033[2J");  
        System.out.flush(); 
        if(forward != null) System.out.println(forward);
        System.out.println("MAIN MENU:");
        System.out.println("1. Login");
        System.out.println("2. Register");
        System.out.println("3. Exit");
        System.out.println("select an option:");
        return readInput();
    }

    static String print_login_username_menu(String forward) throws IOException{
        System.out.print("\033[H\033[2J");  
        System.out.flush(); 
        if(forward != null) System.out.println(forward);
        System.out.println("LOGIN");
        System.out.println("Enter your username or 'back' to go to the Main Menu:");
        return readInput();
    }
    
    static String print_login_password_menu() throws IOException{
        System.out.println("Enter your password:");
        return readInput();
    }

    static String print_register_username_menu(String forward) throws IOException{
        System.out.print("\033[H\033[2J");  
        System.out.flush(); 
        if(forward != null) System.out.println(forward);
        System.out.println("REGISTER");
        System.out.println("Enter your username or 'back' to go to the Main Menu:");
        return readInput();
    }

    static String print_register_password_menu() throws IOException{
        System.out.println("Enter your password:");
        return readInput();
    }

    static String print_main_menu(String forward) throws IOException{
        System.out.print("\033[H\033[2J");  
        System.out.flush();
        if(forward != null) System.out.println(forward);
        System.out.println("MAIN MENU");
        System.out.println("1. Upload file");
        System.out.println("2. Download file");
        System.out.println("3. Delete file");
        System.out.println("4. Change file permissions");
        System.out.println("5. Logout");
        System.out.println("select an option:");
        return readInput();
    }

    static String print_upload_menu(String forward, List<Path> files) throws IOException{
        System.out.print("\033[H\033[2J");  
        System.out.flush();
        if(forward != null) System.out.println(forward);
        System.out.println("UPLOAD FILE");
        System.out.println("Select the file to upload:");

        for (int i = 0; i < files.size(); i++) {
            System.out.println(i + ". " + files.get(i).getFileName());
        }
        return readInput();
    }

    static String print_download_menu(String forward, List<String> files) throws IOException{
        System.out.print("\033[H\033[2J");  
        System.out.flush();
        if(forward != null) System.out.println(forward);
        System.out.println("DOWNLOAD FILE");
        System.out.println("Select the file to download:");
        for (int i = 0; i < files.size(); i++) {
            System.out.println(i + ". " + files.get(i));
        }

        return readInput();
    }

    static String print_delete_menu(String forward, List<String> files) throws IOException{
        System.out.print("\033[H\033[2J");  
        System.out.flush();
        if(forward != null) System.out.println(forward);
        System.out.println("DELETE FILE");
        System.out.println("Select the file to delete:");
        for (int i = 0; i < files.size(); i++) {
            System.out.println(i + ". " + files.get(i));
        }

        return readInput();
    }

    static String print_permission_menu(String forward, List<String> files) throws IOException{
        System.out.print("\033[H\033[2J");  
        System.out.flush();
        if(forward != null) System.out.println(forward);
        System.out.println("SET FILE PERMISSION");
        System.out.println("Select the file to set permission:");
        for (int i = 0; i < files.size(); i++) {
            System.out.println(i + ". " + files.get(i));
        }

        return readInput();
    }

    static String print_permission_option_menu(String forward) throws IOException{
        System.out.print("\033[H\033[2J");  
        System.out.flush();
        if(forward != null) System.out.println(forward);
        System.out.println("SET FILE PERMISSION");
        System.out.println("1. Read");
        System.out.println("2. Write");
        return readInput();
    }

    static String readInput() throws IOException {
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        return consoleReader.readLine().toLowerCase();
    }

}