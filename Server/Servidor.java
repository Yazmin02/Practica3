package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Servidor {
    private static final int PORT = 8000; // Cambiado a otro puerto
    private static final int THREAD_POOL_SIZE = 10;
    private ServerSocket serverSocket;
    private final ExecutorService threadPool;

    public Servidor() {
        threadPool = Executors.newFixedThreadPool(THREAD_POOL_SIZE);
    }

    public void start() {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("Server is listening on port " + PORT);

            while (true) {
                try {
                    Socket clientSocket = serverSocket.accept();
                    threadPool.submit(new RequestHandler(clientSocket));
                } catch (IOException e) {
                    System.out.println("Error accepting client connection");
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            System.out.println("Could not listen on port " + PORT);
            e.printStackTrace();
        } finally {
            stop();
        }
    }

    public void stop() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            threadPool.shutdown();
        } catch (IOException e) {
            System.out.println("Error closing the server");
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Servidor server = new Servidor();
        server.start();
    }
}