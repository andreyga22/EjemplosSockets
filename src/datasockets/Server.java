package datasockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    private DataOutputStream output; // output stream to client
    private DataInputStream input; // input stream from client
    private ServerSocket server; // server socket
    private Socket connection; // connection to client
    private final int PORT = 12345;

    // set up and run server
    public void runServer() {
        try {
            server = new ServerSocket(PORT); // create ServerSocket
            waitForConnection(); // wait for a connection
            getStreams(); // get input & output streams
            processConnection(); // process connection
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(); // close connection            
        }
    }

    // wait for connection to arrive, then display connection info
    private void waitForConnection() throws IOException {
        System.out.println("Waiting for connection...\n");
        connection = server.accept(); // allow server to accept connection
        System.out.println("Connection received from: " + connection.getInetAddress().getHostName());
    }

    // get streams to send and receive data
    private void getStreams() throws IOException {
        // set up output stream for data
        output = new DataOutputStream(connection.getOutputStream());
        output.flush(); // flush output buffer to send header information   
        // set up input stream for data
        input = new DataInputStream(connection.getInputStream());
    }

    // process connection with client
    private void processConnection() throws IOException {
        String stringMessage = "Connection successful";
        output.writeUTF(stringMessage);
        System.out.println("The message: \"" + stringMessage + "\" was sent");

        int intMessage = 123;
        output.writeInt(intMessage);
        System.out.println("The message: \"" + intMessage + "\" was sent");

        System.out.println("Waiting for client message ...");
        intMessage = input.readInt();
        System.out.println("The message: \"" + intMessage + "\" was received");

        System.out.println("Waiting for client message ...");
        stringMessage = input.readUTF();
        System.out.println("The message: \"" + stringMessage + "\" was received");

        stringMessage = "PONG";
        output.writeUTF(stringMessage);
        System.out.println("The message: \"" + stringMessage + "\" was sent");
    }

    // close streams and socket
    private void closeConnection() {
        System.out.println("\nTerminating connection");
        try {
            output.close(); // close output stream
            input.close(); // close input stream
            connection.close(); // close socket  
            server.close(); // clse server socket
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Server().runServer();
    }
}
