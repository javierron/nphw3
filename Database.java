import java.security.acl.Owner;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;  

class Database{
    
    Connection connection;

    public Database(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/catalog","javier","password");
        }catch(Exception e){
            e.printStackTrace();
            //connection.close();
        }
    }


    public boolean exists(String username) throws SQLException {
        Statement sql = connection.createStatement();
        String query = "SELECT username FROM user WHERE username = '" + username + "'";
        ResultSet rs = sql.executeQuery(query);
        return rs.next();
        // return db.containsKey(username);
    }

    public void createUser(String username, String password) throws SQLException {
        Statement sql = connection.createStatement();
        String query = "INSERT INTO user(username, password) VALUES ('" + username + "','" + password + "');";
        sql.executeUpdate(query);
        // db.put(username, password);
    }

    public boolean checkLogin(String username, String password) throws SQLException {
        Statement sql = connection.createStatement();
        String query = "SELECT password FROM user WHERE username = '" + username + "';";
        
        ResultSet rs = sql.executeQuery(query);
        boolean hasNext = rs.next();
        
        if(!hasNext) {
            return false;
        }
        
        String storedPassword = rs.getString(1);
        return storedPassword.equals(password);
    }

    public boolean saveFile(String name, long size, String user) throws SQLException {

            int userId = getUserId(user);
            
            Statement sql = connection.createStatement();
            String query = "SELECT owner, permission FROM file WHERE name = '" + name + "';";
            
            ResultSet rs = sql.executeQuery(query);

            boolean fileExists = rs.next();
            if(!fileExists){
                saveFile(name, size, userId);
                return true;
            }else{
                if(rs.getInt(1) == userId){
                    updateFile(name, size);
                    return true;
                }else if(rs.getInt(2) == 1){
                    updateFile(name, size);
                    return true;
                }
            }
        
        return false;
    }

    public List<String> getFileNames() throws SQLException{
        
        List<String> res = new ArrayList<String>();
        
        Statement sql = connection.createStatement();
        String query = "SELECT name FROM file;";
        
        ResultSet rs = sql.executeQuery(query);
        
        while(rs.next()){
            res.add(rs.getString(1));
        }

        return res;
    }

    public List<String> getFileNames(String user) throws SQLException{
        List<String> res = new ArrayList<String>();
        
        int userId = getUserId(user);

        Statement sql = connection.createStatement();
        String query = "SELECT name FROM file WHERE owner = " + userId + ";";
        
        ResultSet rs = sql.executeQuery(query);
        
        while(rs.next()){
            res.add(rs.getString(1));
        }
        return res;
    }

    public Metadata getFile(String name) throws SQLException{
        Statement sql = connection.createStatement();
        String query = "SELECT name, size, owner FROM file WHERE name = '" + name + "';";
        System.out.println(query);
        ResultSet rs = sql.executeQuery(query);

        
        if(!rs.next()){
            return null;
        }
        String owner = getUsername(rs.getInt(3));

        return new Metadata(rs.getString(1), rs.getInt(2), owner, "path");
    }

    public boolean deleteFile(String name, String user) throws SQLException{
        int userId = getUserId(user);

        Statement sql = connection.createStatement();
        String query = "SELECT owner, permission FROM file WHERE name = '" + name + "';";
          
        ResultSet rs = sql.executeQuery(query);
        if(!rs.next()){
            return false;
        }

        if(rs.getInt(1) != userId && !rs.getBoolean(2)){
            return false;
        }

        Statement sql2 = connection.createStatement();
        String delete = "DELETE FROM file WHERE name = '" + name + "';";
        sql2.executeUpdate(delete);
        
        return true;
    }

    public boolean setPermission(String name, boolean write, String user) throws SQLException{
        int userId = getUserId(user);

        Statement sql = connection.createStatement();
        String query = "SELECT owner, permission FROM file WHERE name = '" + name + "';";

        ResultSet rs = sql.executeQuery(query);
        if(!rs.next()){
            return false;
        }


        if(rs.getInt(1) != userId){
            return false;
        }

        Statement sql2 = connection.createStatement();
        String update = "UPDATE file SET permission = " + write + " WHERE name = '" + name + "';";
        sql2.executeUpdate(update);

        return true;
    }

    void saveFile(String name, long size, int userId) throws SQLException {
        Statement sql = connection.createStatement();
        String insert = "INSERT INTO file(name, size, owner, permission) VALUES ('" + name + "'," + size + "," + userId + ", 0);";
        sql.executeUpdate(insert);
    }

    void updateFile(String name, long size) throws SQLException {
        Statement sql = connection.createStatement();
        String update = "UPDATE file SET size = " + size + " WHERE name = '" + name + "';";
        sql.executeUpdate(update);
    }

    int getUserId(String username) throws SQLException {
        Statement sql = connection.createStatement();
        String query = "SELECT id FROM user WHERE username = '" + username + "';";
        ResultSet rs = sql.executeQuery(query);
        if(!rs.next()){
            return -1;
        } 
        return rs.getInt(1);
    }

    String getUsername(int id) throws SQLException {
        Statement sql = connection.createStatement();
        String query = "SELECT username FROM user WHERE id = '" + id + "';";
        ResultSet rs = sql.executeQuery(query);
        if(!rs.next()){
            return "";
        } 
        return rs.getString(1);
    }
}  