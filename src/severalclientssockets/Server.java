package severalclientssockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.*;

public class Server {

    private DataOutputStream output;
    private DataInputStream input;
    private ServerSocket server;
    private Socket connection;
    private final int PORT = 12345;
    private final int NUMBER_OF_REQUESTS = 3;

    public void runServer() {
        try {
            server = new ServerSocket(PORT);
            for (int numCliente = 1; numCliente <= NUMBER_OF_REQUESTS; numCliente++) {
                try {
                    waitForConnection();
                    getStreams();
                    processConnection(numCliente);
                    
                } catch (IOException ex) {
                    ex.printStackTrace();
                } finally {
                    closeConnection();
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            closeServer();
        }
    }

    private void waitForConnection() throws IOException {
        System.out.println("Waiting for connection...\n");
        connection = server.accept();
        System.out.println("Connection received from: " + connection.getInetAddress().getHostName());
    }

    private void getStreams() throws IOException {
        output = new DataOutputStream(connection.getOutputStream());
        output.flush();
        input = new DataInputStream(connection.getInputStream());
    }

    private void processConnection(int numCliente) throws IOException {
        String name = input.readUTF();
        System.out.println("Servicing the request: " + numCliente + ", name: " + name);
        output.writeUTF(name + ", you are de client " + numCliente);
    }

    private void closeConnection() {
        System.out.println("\nTerminating connection\n\n");
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    private void closeServer() {
        System.out.println("\nTerminating server");
        try {
            server.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server().runServer();
    }
}
