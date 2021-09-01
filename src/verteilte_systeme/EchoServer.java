package verteilte_systeme;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {
    public static void main(String[] args) throws IOException {
        ServerSocket ss = new ServerSocket(27015);
        ExecutorService threads =
                Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        System.out.printf("Started server with %d threads. Listening...\n",
                Runtime.getRuntime().availableProcessors());
        while (true) {
            Socket s = ss.accept();
            System.out.printf("Got connection from %s\n", s.getInetAddress());
            threads.execute(() -> {
                try (BufferedReader connectionIn =
                             new BufferedReader(new InputStreamReader(s.getInputStream()));
                     OutputStream out = s.getOutputStream()) {
                    while (!s.isClosed()) {
                        String s1 = connectionIn.readLine();
                        if (s1 == null) { // stream is over, thus s1 was null
                            s.close();
                            break;
                        }
                        System.out.printf("%s wrote: %s\n", s.getInetAddress(), s1);
                        out.write(s1.concat("\n").getBytes());
                        out.flush();
                    }
                } catch (IOException ignored) {
                }
                try {
                    s.close();
                } catch (IOException ignored) {
                }
                System.out.printf("Closed connection to %s\n", s.getInetAddress());
            });
        }
    }

}
