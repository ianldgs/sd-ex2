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
                System.out.println("Mensagem recebida: " + msg);
                this.connect.send(MessagesEnum.MESSAGE_OK.toString());
            }
        }
    }
}
