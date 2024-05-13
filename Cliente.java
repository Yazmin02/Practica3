import java.io.*;
import java.net.*;

public class Cliente {
    public static void main(String[] args) {
        try {
            // Crear objeto URL
            String urlString ="http://148.204.58.221/axel/aplicaciones/sockets/java/";
            URL url = new URL(urlString);

            // Abrir conexión HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Solicitar al usuario que ingrese el método HTTP deseado
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Ingrese el método HTTP (GET o HEAD): ");
            String method = reader.readLine().toUpperCase();

            // Validar el método HTTP ingresado
            if (!(method.equals("GET") || method.equals("HEAD"))) {
                System.out.println("Método HTTP inválido. Por favor, ingrese GET o HEAD.");
                return;
            }

            // Establecer el método HTTP en la conexión
            connection.setRequestMethod(method);

            // Obtener la respuesta del servidor
            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Leer la respuesta del servidor
                BufferedReader responseReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String line;
                StringBuilder response = new StringBuilder();

                while ((line = responseReader.readLine()) != null) {
                    response.append(line);
                    response.append("\n");
                }

                responseReader.close();

                // Mostrar la respuesta del servidor
                System.out.println("Respuesta del servidor:");
                System.out.println(response.toString());
            } else {
                System.out.println("La solicitud no fue exitosa. Código de respuesta: " + responseCode);
            }

            // Cerrar la conexión
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }//main
}
