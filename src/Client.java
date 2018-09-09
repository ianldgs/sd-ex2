import java.net.*;
import java.io.*;

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
        String msg;
        String texto;
        new Client();

        c = new Connection(socket);

        String initialMessage = c.receive();
        System.out.println(initialMessage);

        // DataInputStream in = new DataInputStream(System.in);
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        // fica num loop de 5 mensagens
        while (true) {
            try {
                msg = in.readLine();

                if (msg.equals("/sair")) {
                    System.exit(0);
                }

                c.send(msg);

                texto = c.receive();
                System.out.println(texto);
            } catch(Exception e) {
                System.out.println("Erro na leitura "+e.getMessage());
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
