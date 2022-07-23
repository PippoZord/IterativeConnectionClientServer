import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;

import javax.swing.InputMap;

public class Server {
    public static void main(String[] args) {
        ServerSocket srv;
        Socket sClient;
        String msg;
        
        sClient = new Socket();
        
        try {
            srv = new ServerSocket(0);
            System.out.println("INDIRIZZO: " + srv.getLocalPort() + "\tPORTA: " + srv.getLocalPort());
            
            while (true) {
                sClient = srv.accept();
                boolean l=true;
                do {
                
                    sendToClient("SCEGLI LA FIGURA: ", sClient);
                    msg = readFromClient(sClient);

                    switch (msg) {
                        case "quadrato":
                            System.out.println(msg);
                            sendToClient("INSERISCI IL LATO DEL QUADRATO", sClient);
                            msg = readFromClient(sClient);
                            double tmp = Double.parseDouble(msg);
                            sendToClient("QUADRATO\tAREA = " + tmp*tmp +"\tPERIMETRO = " + tmp*4, sClient);
                            break;
                        case "cerchio":
                            System.out.println(msg);
                            sendToClient("INSERISCI IL RAGGIO DEL CERCHIO", sClient);
                            msg = readFromClient(sClient);
                            tmp = Double.parseDouble(msg);
                            System.out.println(tmp);
                            sendToClient("CERCHIO\tAREA = " + Math.PI * (tmp*tmp) + "\tCIRCONFERENZA = " + 2*Math.PI*tmp, sClient);
                            break;
                        case "rettangolo":
                            System.out.println(msg);
                            sendToClient("INSERISCI I LATI DEL RETTANGOLO", sClient);
                            msg = readFromClient(sClient);
                            StringTokenizer tokenString = new StringTokenizer(msg);
                            tmp = Double.parseDouble(tokenString.nextToken());
                            double tmp2 = Double.parseDouble(tokenString.nextToken());
                            System.out.println(tmp  +" " + tmp2);
                            sendToClient("RETTANGOLO\tAREA = " + tmp * tmp2 + "\tPERIMETRO = "
                                    + (tmp + tmp + tmp2 + tmp2), sClient);
                            break;
                        case "triangolo":
                            System.out.println(msg);
                            sendToClient("INSERISCI I CATETI DEL RETTANGOLO", sClient);
                            msg = readFromClient(sClient);
                            tokenString = new StringTokenizer(msg);
                            tmp = Double.parseDouble(tokenString.nextToken());
                            tmp2 = Double.parseDouble(tokenString.nextToken());
                            double ipo = Math.sqrt((tmp*tmp + tmp2*tmp2));
                            System.out.println(tmp + " " + tmp2 + " " + ipo);
                            sendToClient("TRIANGOLO RETTANGOLO\tAREA = " + tmp * tmp2 + "\tPERIMETRO = " + (tmp + tmp2 + ipo), sClient);
                            break;
                        default:
                            sendToClient("OPZIONE NON VALIDA", sClient);
                            break;
                    }
                    
                    readFromClient(sClient);

                    do {
                        sendToClient("VUOI CONTINUARE? (Y/N)", sClient);
                        msg = readFromClient(sClient);
                    } while (!msg.equals("N") && !msg.equals("Y"));

                    if (msg.equals("N")){
                        l = false;
                    }
                } while (l);
            }



            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Invia msg al Client
     * 
     * @throws IOException
     */
    private static void sendToClient(String msg, Socket sClient) throws IOException{
        OutputStream ou = sClient.getOutputStream();
        ou.write(msg.getBytes(), 0, msg.getBytes().length);
    }

    /**
     * @return Legge dal Client
     * @throws IOException
     */
    private static String readFromClient(Socket sClient) throws IOException{        
        InputStream in = sClient.getInputStream();
        byte[] buf= new byte[128];
        int l = in.read(buf);
        return new String(buf, 0, l);
    }
}
