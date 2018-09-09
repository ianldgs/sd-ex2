
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

    static Socket client_socket;

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

    public static void pm(Connection origin, String nick, String msg) {
        Optional<Connection> client = clients.stream()
                .filter(connection -> connection.getNick() == nick)
                .findAny();

        if (client.isPresent()) {
            client.get().send("De " + origin.getNick() + " para você:" + msg);
        } else {
            origin.send("Não foi encontrada nenhum usuário com esse nick");
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
        int operacao;

        // inicializa o banco central
        new Server();

        // inicializa a estrutura de armazenamento dos bancos
        while (true) {
            Connection client = new Connection(serversocket.accept());
            client.send("Digite seu nome:");

            String nick = client.receive();
            client.setNick(nick);

            clients.add(client);
            yell(nick + " conectado.");

            //TODO: new thread with the above code

            if (connect()) {
                c = new Connection(client_socket);

                // fica num loop de 5 mensagens
                while (true) {
                    msg = c.receive();

                    System.out.println("Mensagem recebida: " + msg);

                    c.send("/msgok");
                }

                try {
                    client_socket.close();
                } catch (Exception e) {
                    System.out.println("N�o desconectei...");
                }

            } else {
                try {
                    serversocket.close();
                    break;
                } catch (Exception e) {
                    System.out.println("N�o desconectei...");
                }
            }
        }

    }

    static boolean connect() {
        boolean ret;
        try {
            client_socket = serversocket.accept();
            System.out.println("Porta: " + client_socket.getLocalPort() + " "
                    + client_socket.getPort());
            ret = true;
        } catch (Exception e) {
            System.out.println("Erro de connect..." + e.getMessage());
            ret = false;
        }
        return ret;
    }
}
