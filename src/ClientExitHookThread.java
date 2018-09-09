import java.io.IOException;
import java.net.Socket;

public class ClientExitHookThread extends Thread {
    Socket s = null;

    public ClientExitHookThread (Socket s) {
        this.s = s;
    }

    @Override
    public void run() {
        super.run();

        try {
            this.s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
