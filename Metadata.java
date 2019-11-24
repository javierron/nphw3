import java.io.Serializable;

/**
 * Metadata
 */
public class Metadata implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public String name;
    public long size;
    public String path;
    public String owner;
    public boolean write;

    public Metadata(String name, long size, String owner, String path) {
        this.name = name;
        this.path = path;
        this.size = size;
        this.owner = owner;
        this.write = false;
    }
}