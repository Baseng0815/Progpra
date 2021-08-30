package verteilte_systeme;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoServer {
    private final ServerSocket serverSocket;
    private final ExecutorService executor;

    public EchoServer(int port, int numberOfThreads) throws IOException {
        serverSocket = new ServerSocket(port);
        System.out.printf("Server socket listening at %s\n", serverSocket.getLocalSocketAddress());
        executor = Executors.newFixedThreadPool(numberOfThreads);
        while (serverSocket.isBound()) {
            try {
                Socket client = serverSocket.accept();
                System.out.println("Accepted client " + client.toString());
                executor.submit(() -> {
                    handleClient(client);
                });
            } catch (Exception e) {
                System.out.printf("Server socket forcibly closed: %s.", e);
            }
        }
    }

    void handleClient(Socket client) {
        try {
            BufferedReader socketReader = new BufferedReader(new InputStreamReader(client.getInputStream()));
            OutputStream out = client.getOutputStream();

            while (!client.isClosed()) {
                /* receive */
                try {
                    String recv = socketReader.readLine();
                    if (recv == null) {
                        /* end of stream has been reached */
                        System.out.printf("End of stream for client %s has been reached.\n", client.getInetAddress());
                        client.close();
                        break;
                    }
                    System.out.printf("%s: %s (from %s)\n", LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)), recv, client.getInetAddress());

                    /* send back */
                    out.write(recv.getBytes());
                    out.write('\n');
                    out.flush();
                } catch (Exception e) {
                    System.out.printf("Connection to client %s forcibly closed: %s.\n", client, e);
                    client.close();
                    break;
                }
            }
        } catch (IOException e) {
            System.out.printf("Failed to open input or output stream for client %s: %s\n", client.getInetAddress(), e);
        }

        System.out.printf("Connection to client %s closed.\n", client);
    }

    public static void main(String[] args) throws IOException {
        EchoServer server = new EchoServer(27015, 16);
    }
}
