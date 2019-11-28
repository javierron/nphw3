import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class UploadThread implements Runnable {

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

            String ethan_path = "/Users/fccc/Documents/";
            String javier_path = "/home/javier/hw3files-client/";

            File file = new File(javier_path + filename);
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
