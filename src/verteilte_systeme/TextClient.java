package verteilte_systeme;

import java.io.*;
import java.net.Socket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Scanner;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TextClient {
    private final Socket clientSocket;
    private final Scanner userScanner;
    private final BufferedReader socketReader;
    private final InputStream in;
    private final OutputStream out;
    private ExecutorService executor;

    public TextClient(String host, int port) throws IOException {
        clientSocket = new Socket(host, port);
        in = clientSocket.getInputStream();
        out = clientSocket.getOutputStream();
        userScanner = new Scanner(System.in);
        socketReader = new BufferedReader(new InputStreamReader(in));
        executor = Executors.newFixedThreadPool(2);
        executor.submit(() -> {
            recvLoop();
            System.out.println("recvLoop left.");
        });

        executor.submit(() -> {
            sendLoop();
            System.out.println("sendLoop left.");
        });
    }

    private void recvLoop() {
        while (!clientSocket.isClosed()) {
            /* receive */
            try {
                String recv = socketReader.readLine();
                if (recv == null) {
                    /* end of stream has been reached */
                    System.out.println("End of stream has been reached.");
                    clientSocket.close();
                    break;
                }
                System.out.printf("%s: %s\n", LocalDateTime.now().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)), recv);
            } catch (IOException e) {
                System.out.printf("I/O exception when trying to receive: %s\n", e);
                break;
            }
        }

        System.out.println("Leaving recvLoop...");
    }

    private void sendLoop() {
        while (!clientSocket.isClosed()) {
            /* send to server */
            String input = "";
            while (input.isEmpty())
                input = userScanner.nextLine();

            try {
                out.write(input.getBytes());
                out.write('\n');
                out.flush();
            } catch (IOException e) {
                System.out.printf("I/O exception when trying to send '%s': %s.\n", input, e);
            }
        }

        System.out.println("Leaving sendLoop...");
    }

    public static void main(String[] args) throws IOException {
        //final String host = "dsgw.mathematik.uni-marburg.de";
        //final int port = 32823;
        final String host = "localhost";
        final int port = 5712;

        TextClient client = new TextClient(host, port);
    }
}