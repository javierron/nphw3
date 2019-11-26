import java.security.Permission;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class MetadataDB {

    String pathDB = "~/hw3files/";
    HashMap<String, Metadata> db;

    enum FileStaus{
        UPLOAD,
        UPDATE,
        UPLOAD_FAIL
    }

    public MetadataDB(){
        db = new HashMap<>();
    }

    public FileStaus saveFile(String name, long size, String user){
        Metadata metadata = db.get(name);        
        
        if(metadata == null){
            db.put(name, new Metadata(name, size, user, pathDB + name));
            return FileStaus.UPLOAD;
        }else {
            if(metadata.owner == user){
                db.put(name, new Metadata(name, size, user, pathDB + name));
                return FileStaus.UPDATE;
            }else if(metadata.write){
                db.put(name, new Metadata(name, size, metadata.owner, pathDB + name));
                return FileStaus.UPDATE;
            }
        }
        return FileStaus.UPLOAD_FAIL;
    }

    public List<String> getFileNames(){
        List<String> list = new ArrayList<String>(); 
        list.addAll(db.keySet());
        return list;
    }

    public List<String> getFileNames(String user){
        List<String> list = db.values().stream().filter(m -> m.owner.equals(user)).map(m -> m.name).collect(Collectors.toList());
        return list;
    }

    public Metadata getFile(String name){

        return db.get(name);
    }

    public boolean deleteFile(String name, String user){
        Metadata metadata = db.get(name); 
        if(!metadata.owner.equals(user) && !metadata.write){
            return false;
        }

        db.remove(name);
        return true;
    }

    public boolean setPermission(String name, boolean write, String user){
        Metadata metadata = db.get(name); 
        
        if(!metadata.owner.equals(user)){
            return false;
        }

        metadata.write = write;
        return true;
    }




}