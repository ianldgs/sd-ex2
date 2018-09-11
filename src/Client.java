import java.net.*;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Client {

    static Connection c;
    static Socket socket;

    public Client() {
        try {
            socket = new Socket("localhost",9600);
        }
        catch (Exception e)
        {
            System.out.println("Nao consegui resolver o host...");
        }
    }

    public static void main(String args[])
    {
        new Client();

        c = new Connection(socket);

        String initialMessage = c.receive();
        System.out.println(initialMessage);

        // DataInputStream in = new DataInputStream(System.in);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        ServerClientThread serverClientThread = new ServerClientThread(c);
        serverClientThread.start();

        while (true) {
            try {
                String msg = in.readLine();

                if (msg.isEmpty()) {
                    continue;
                }

                c.send(msg);

                if (msg.equals("/exit")) {
                    //TODO: is this working?
                    System.exit(0);
                }
            } catch(Exception e) {
                System.out.println("Erro na leitura " +e.getMessage());
                break;
            }
        }

        try {
            socket.close();
        } catch (Exception e) {
            System.out.println("Nao desconectei..."+e);
        }
    }
}
