package Server;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Cliente {
    private static final String SERVER_HOST = "localhost";
    private static final int SERVER_PORT = 9999;

    public static void main(String[] args) {
        Cliente client = new Cliente();
        try {
            // Ejemplo de solicitud GET
            client.sendGetRequest("/index.html");

            // Ejemplo de solicitud POST
            client.sendPostRequest("file.hmtl");

            // Ejemplo de solicitud PUT
            client.sendPutRequest("/file.txt", "Este es el contenido del archivo");

            // Ejemplo de solicitud HEAD
            client.sendHeadRequest("/index.html");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendGetRequest(String resource) throws IOException {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("GET " + resource + " HTTP/1.1");
            out.println("Host: " + SERVER_HOST);
            out.println("Connection: close");
            out.println();

            String responseLine;
            while ((responseLine = in.readLine()) != null) {
                System.out.println(responseLine);
            }
        }
    }

    public void sendPostRequest(String fileName) throws IOException {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            Path filePath = Paths.get("/home/alan232/Documentos/GitHub/Practica3/", fileName);
            String content = Files.readString(filePath);

            out.println("POST / HTTP/1.1");
            out.println("Host: " + SERVER_HOST);
            out.println("Content-Length: " + content.length());
            out.println("Content-Type: text/plain");
            out.println();
            out.println(content);
            out.flush();

            String responseLine;
            while ((responseLine = in.readLine()) != null) {
                System.out.println(responseLine);
            }
        }
    }

    public void sendPutRequest(String resource, String content) throws IOException {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);

            out.println("PUT " + resource + " HTTP/1.1");
            out.println("Host: " + SERVER_HOST);
            out.println("Content-Length: " + contentBytes.length);
            out.println("Content-Type: text/plain");
            out.println();
            out.println(content);
            out.flush();

            String responseLine;
            while ((responseLine = in.readLine()) != null) {
                System.out.println(responseLine);
            }
        }
    }

    public void sendHeadRequest(String resource) throws IOException {
        try (Socket socket = new Socket(SERVER_HOST, SERVER_PORT);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("HEAD " + resource + " HTTP/1.1");
            out.println("Host: " + SERVER_HOST);
            out.println("Connection: close");
            out.println();

            String responseLine;
            while ((responseLine = in.readLine()) != null) {
                System.out.println(responseLine);
            }
        }
    }
}
