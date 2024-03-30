import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ChatServer {
    private static final int PORT = 4563;
    private static List<PrintWriter> clientWriters = new ArrayList<>();
    private static List<Socket> clients = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("serveur de chat en ecoute sur le port : " + PORT);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("nouveau client connecte: " + clientSocket);
                PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                clientWriters.add(out);
                clients.add(clientSocket);
                new ClientHandler(clientSocket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        public void run() {
            try {
                Scanner in = new Scanner(clientSocket.getInputStream());
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                while (true) {
                    if (!in.hasNextLine()) {
                        break;
                    }
                    String message = in.nextLine();
                    broadcast(getClientInfo(clientSocket) + ": " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (out != null) {
                    clientWriters.remove(out);
                    clients.remove(clientSocket);
                }
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("client déconnecte: " + clientSocket);
            }
        }

        private void broadcast(String message) {
            for (PrintWriter writer : clientWriters) {
                writer.println(message);
            }
        }

        private String getClientInfo(Socket clientSocket) {
            return "Client[" + clientSocket.getInetAddress().getHostAddress() + ":" + clientSocket.getPort() + "]";
        }
    }
}
