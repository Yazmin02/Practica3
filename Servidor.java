import java.io.*;
import java.net.*;
import java.util.*;

class Servidor {
    public static final int PUERTO = 8000;
    ServerSocket ss;

    public Servidor() throws Exception {
        System.out.println("Iniciando Servidor.......");
        this.ss = new ServerSocket(PUERTO);
        System.out.println("Servidor iniciado:---OK");
        System.out.println("Esperando por Cliente....");
        for (;;) {
            Socket accept = ss.accept();
            new Manejador(accept).start();
        }
    }

    class Manejador extends Thread {
        protected Socket socket;
        DataOutputStream dos;

        public Manejador(Socket _socket) throws Exception {
            this.socket = _socket;
            dos = new DataOutputStream(socket.getOutputStream());
        }

        public void run() {
            try {
                // Obtener la entrada de datos del socket
                DataInputStream dis = new DataInputStream(socket.getInputStream());

                // Leer la solicitud del cliente
                byte[] b = new byte[50000];
                int t = dis.read(b);
                String request = new String(b, 0, t);

                // Analizar la solicitud para determinar el método y la ruta
                String[] requestLines = request.split("\n");
                String firstLine = requestLines[0];
                String[] parts = firstLine.split(" ");
                String method = parts[0];
                String path = parts[1];

                // Procesar la solicitud según el método
                if (method.equals("GET")) {
                    handleGET(path, dos);
                } else if (method.equals("HEAD")) {
                    handleHEAD(path, dos);
                } else if (method.equals("PUT")) {
                    handlePUT(path, dis, dos);
                } else if (method.equals("POST")) {
                    handlePOST(path, request, dos);
                } else {
                    // Método no permitido
                    dos.writeBytes("HTTP/1.1 405 Method Not Allowed\r\n");
                    dos.writeBytes("\r\n");
                    dos.flush();
                }

                // Cerrar el socket
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void handleGET(String path, DataOutputStream dos) throws IOException {
            File file = new File("archivos" + path);

            if (file.exists() && !file.isDirectory()) {
                FileInputStream fis = new FileInputStream(file);
                byte[] buffer = new byte[1024];
                int bytesRead;

                dos.writeBytes("HTTP/1.1 200 OK\r\n");
                dos.writeBytes("Content-Length: " + file.length() + "\r\n");
                dos.writeBytes("\r\n");

                while ((bytesRead = fis.read(buffer)) != -1) {
                    dos.write(buffer, 0, bytesRead);
                }

                fis.close();
            } else {
                dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
                dos.writeBytes("\r\n");
            }

            dos.flush();
        }

        private void handleHEAD(String path, DataOutputStream dos) throws IOException {
            File file = new File("archivos" + path);

            if (file.exists() && !file.isDirectory()) {
                dos.writeBytes("HTTP/1.1 200 OK\r\n");
                dos.writeBytes("Content-Length: " + file.length() + "\r\n");
                dos.writeBytes("\r\n");
            } else {
                dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
                dos.writeBytes("\r\n");
            }

            dos.flush();
        }

        private void handlePUT(String path, DataInputStream dis, DataOutputStream dos) throws IOException {
            File file = new File("archivos" + path);
            file.getParentFile().mkdirs();

            FileOutputStream fos = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int bytesRead;

            while ((bytesRead = dis.read(buffer)) != -1) {
                fos.write(buffer, 0, bytesRead);
            }

            fos.close();

            dos.writeBytes("HTTP/1.1 200 OK\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        }

        private void handlePOST(String path, String request, DataOutputStream dos) throws IOException {
            // Aquí puedes procesar los datos del cuerpo de la solicitud
            // Por ejemplo, extraer parámetros de un formulario HTML

            String[] parts = request.split("\r\n\r\n", 2);
            String body = parts[1];
            String[] params = body.split("&");

            // Procesar los parámetros
            // Aquí puedes realizar cualquier lógica adicional con los parámetros recibidos

            dos.writeBytes("HTTP/1.1 200 OK\r\n");
            dos.writeBytes("\r\n");
            dos.flush();
        }
    }

    public static void main(String[] args) throws Exception {
        Servidor sWEB = new Servidor();
    }
}
