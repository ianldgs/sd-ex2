import java.text.SimpleDateFormat;
import java.util.Date;

public class ServerClientThread extends Thread {
    Connection connect = null;

    public ServerClientThread(Connection c) {
        this.connect = c;
    }

    @Override
    public void run() {
        super.run();

        while (true) {
            String msg = this.connect.receive();

            if (!msg.isEmpty()) {
                Date date = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
                String formattedDate = formatter.format(date);

                System.out.println(msg + " - Mensagem recebida às " + formattedDate);

                if (msg.contains("/yell ")) {
                    msg.replace("/yell ", "");
                    Server.yell(msg, this.connect);
                }
            }
        }
    }
}
