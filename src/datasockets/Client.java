package datasockets;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import javax.swing.JOptionPane;

public class Client {

    private DataOutputStream output; // output stream to client
    private DataInputStream input; // input stream from client
    private Socket client; // connection to client
    private final String HOST = "127.0.0.1";
    private final int PORT = 12345;

    // connect to server and process messages from server
    public void runClient() {
        try {// connect to server, get streams, process connection
            connectToServer(); // create a Socket to make connection
            getStreams(); // get the input and output streams
            processConnection(); // process connection
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            closeConnection(); // close connection
        }
    }

    // connect to server
    private void connectToServer() throws IOException {
        System.out.println("Attempting connection\n");
        // create Socket to make connection to server
        client = new Socket(HOST, PORT);
        // display connection information
        System.out.println("Connected to: " + client.getInetAddress().getHostName());
    }

    // get streams to send and receive data
    private void getStreams() throws IOException {
        // set up output stream for data
        output = new DataOutputStream(client.getOutputStream());
        output.flush(); // flush output buffer to send header information   
        // set up input stream for data
        input = new DataInputStream(client.getInputStream());
    }

    // process connection with server
    private void processConnection() throws IOException {
        System.out.println("Waiting for server message ...");
        String stringMessage = input.readUTF();
        System.out.println(stringMessage);

        System.out.println("Waiting for server message ...");
        int intMessage = input.readInt();
        System.out.println("The message: \"" + intMessage + "\" was received");

        intMessage = 456;
        JOptionPane.showMessageDialog(null, "Stop!", "Message", JOptionPane.INFORMATION_MESSAGE);
        output.writeInt(intMessage);
        System.out.println("The message: \"" + intMessage + "\" was sent");

        stringMessage = "PING";
        output.writeUTF(stringMessage);
        System.out.println("The message: \"" + stringMessage + "\" was sent");

        System.out.println("Waiting for server message ...");
        stringMessage = input.readUTF();
        System.out.println("The message: \"" + stringMessage + "\" was received");

        //stringMessage = input.readUTF();        
    }

    private void closeConnection() {
        System.out.println("\nClosing connection");
        try {
            output.close(); // close output stream
            input.close(); // close input stream
            client.close(); // close socket
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Client().runClient();
    }
}
