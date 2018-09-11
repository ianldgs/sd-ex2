import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerClientThread extends Thread {
    private Connection connection;

    public ServerClientThread(Connection c) {
        connection = c;
    }

    @Override
    public void run() {
        super.run();

        while (true) {
            String msg = connection.receive();

            if (msg.isEmpty()) {
                Server.disconnect(connection);
                return;
            }

            Date date = new Date();
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            String formattedDate = formatter.format(date);

            System.out.println(msg + " - Mensagem recebida às " + formattedDate);

            messageCommand(msg);
        }
    }

    private void messageCommand(String msg) {
        try {
            if (msg.startsWith("/say ")) {
                msg = msg.replace("/say ", "");

                Server.say(msg, connection);

                return;
            }

            if (msg.startsWith("/pm")) {
                String nick = msg.substring(msg.indexOf("(") + 1, msg.indexOf(")"));
                msg = msg.substring(msg.indexOf(")") + 1, msg.length());

                Server.pm(connection, nick, msg);

                return;
            }

            if (msg.startsWith("/list_users")) {
                Server.listClients(connection);

                return;
            }

            if (msg.startsWith("/exit")) {
                Server.disconnect(connection);
            }
        }
        catch (Exception ex) {
            System.out.println("Mensagem inválida - " + ex.getMessage());
        }
    }
}
