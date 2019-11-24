import java.util.HashMap;

public class UserDB {

    HashMap<String, String> db;

    public UserDB(){
        db = new HashMap<>();
    }

    public boolean exists(String username){
        return db.containsKey(username);
    }

    public void createUser(String username, String password){
        db.put(username, password);
    }

    public boolean checkLogin(String username, String password){
        String storedPassword = db.get(username);
        if (storedPassword == null || !storedPassword.equals(password)){
            return false;
        }
        return true;
    }
}