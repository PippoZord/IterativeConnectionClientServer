import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.Buffer;
import java.util.stream.Stream;

import javax.xml.stream.util.StreamReaderDelegate;

public class Client {
    public static void main(String[] args) {
        InetAddress ia;
        InetSocketAddress isa;
        InetSocketAddress Myisa;
        Socket sClient;
        String msg;
        sClient = new Socket();
        
        try {
            if (args.length != 2) throw new IllegalArgumentException("PARAMETRI ERRATI");
            ia = InetAddress.getLocalHost();
            Myisa = new InetSocketAddress(ia, 0);
            sClient.bind(Myisa);
            int port = Integer.parseInt(args[1]);
            isa = new InetSocketAddress(args[0], port);
            sClient.connect(isa);
            
            do {
                msg = readFromServer(sClient);
                System.out.println(msg);
                msg = read();
                sendToServer(msg, sClient);

                msg = readFromServer(sClient);
                System.out.println(msg);
                if (!msg.equals("OPZIONE NON VALIDA")){
                    msg = read();
                    sendToServer(msg, sClient);
                
                    msg = readFromServer(sClient);
                    System.out.println(msg);
                }
                
                sendToServer("Y", sClient);
            
                do {
                    msg = readFromServer(sClient);
                    System.out.println(msg);
                    msg = read();
                    sendToServer(msg, sClient);
                } while (!msg.equals("N") && !msg.equals("Y"));

            } while (msg.equals("Y"));
            
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            try {
                sClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 
     * @return Stringa letta tramite System.in
     * @throws IOException
     */
    private static String read() throws IOException {
        InputStreamReader in = new InputStreamReader(System.in);
        BufferedReader bu = new BufferedReader(in);
        String s = bu.readLine();
        return s;
    }

    /**
     * Invia msg al Server
     * 
     * @throws IOException
     */
    private static void sendToServer(String msg, Socket sClient) throws IOException {
        OutputStream ou = sClient.getOutputStream();
        ou.write(msg.getBytes(), 0, msg.getBytes().length);
    }

    /**
     * @return legge dal Server
     * @throws IOException
     */
    private static String readFromServer(Socket sClient) throws IOException {
        InputStream in = sClient.getInputStream();
        byte[] buf = new byte[128];
        int l = in.read(buf);
        return new String(buf, 0, l);
    }
}