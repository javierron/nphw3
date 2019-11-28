import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.net.Socket;

public class DownloadThread implements Runnable {
    String filename;
    public DownloadThread(String name) {
        this.filename=name;
    }

    @Override
    public void run(){
        Socket socket = null;
        String host = "localhost";

        try {
            socket = new Socket(host, 4322);
            byte[] contents = new byte[10000];


            String ethan_path = "/Users/fccc/Documents/";
            String javier_path = "/home/javier/hw3files-client/";

            File file = new File(javier_path);
            file.createNewFile();

            FileOutputStream fos = new FileOutputStream(javier_path + filename);
            InputStream is = socket.getInputStream();

            int bytesRead = 0;

            while((bytesRead = is.read(contents)) != -1)
                fos.write(contents, 0, bytesRead);

            fos.close();
            socket.close();

            System.out.println("File downloaded successfully!");




        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
