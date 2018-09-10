
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Server {

    /**
     * @param args
     */
    static ServerSocket serversocket;

    static Connection c;

    static String msg;

    static List<Connection> clients = new ArrayList<>();

    public Server() {
        try {
            serversocket = new ServerSocket(9600, 5);
            System.out.println("Criei o Server Socket");
        } catch (Exception e) {
            System.out.println("Nao criei o server socket...");
        }
    }

    public static void yell(String msg) {
        for (Connection client : clients) {
            client.send(msg);
        }
    }

    public static void yell(String msg, Connection c) {
        for (Connection client : clients) {
            if (client.getNick() != c.getNick())
                client.send(msg);
        }
    }

    public static void pm(Connection origin, String nick, String msg) {
        Optional<Connection> client = clients.stream()
                .filter(connection -> connection.getNick().equals(nick))
                .findAny();

        if (client.isPresent()) {
            client.get().send("De " + origin.getNick() + " para você:" + msg);
        } else {
            origin.send("Não foi encontrado nenhum usuário com esse nick");
        }
    }

    public static void listClients(Connection c) {
        String connectedClients = "";

        for (Connection client : clients) {
            connectedClients += client.getNick() + "\n";
        }

        c.send(connectedClients);
    }

    public static void main(String args[]) throws IOException {
        String texto, resposta;

        // inicializa o banco central
        new Server();

        // inicializa a estrutura de armazenamento dos bancos
        while (true) {
            Connection client = new Connection(serversocket.accept());
            client.send("Digite seu nome:");

            String nick = client.receive();
            client.setNick(nick);

            clients.add(client);

            ServerClientThread serverClientThread = new ServerClientThread(client);
            serverClientThread.start();

            String connectedMessage = nick + " conectado";
            yell(connectedMessage);
        }
    }
}
