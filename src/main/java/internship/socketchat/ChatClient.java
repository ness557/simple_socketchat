package internship.socketchat;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

    public static void main(String[] args) throws Exception{
        new ChatClient().start("127.0.0.1", "6666");

//        new ChatClient().start(args[0], args[1]);
    }

    private void start(String host, String port) throws Exception {

        Socket socket = null;

        int portNumber = Integer.valueOf(port);
        try {
            InetAddress ipAddress = InetAddress.getByName(host);

            System.out.println("Trying to connect to " + host + " with port " + port);

            socket = new Socket(ipAddress, portNumber);
        } catch (IOException e) {
            System.out.println("Could not connect!");

            ServerSocket ss = null;
            try {
                ss = new ServerSocket(portNumber);
            } catch (IOException e1) {
                System.err.println("Could not open socket!");
                return;
            }

            System.out.println("Waiting for connection...");
            socket = ss.accept();
        }

        System.out.println("Connected!");
        startChat(socket);
    }

    private void startChat(Socket socket) throws IOException {

        DataInputStream inputStream = new DataInputStream(socket.getInputStream());
        DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

        new Thread(()->{
            while (true) {
                try {
                    String msg = inputStream.readUTF();
                    System.err.println("got: " + msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String msg = keyboard.readLine();
            outputStream.writeUTF(msg);
            outputStream.flush();
            System.err.println("sent: " + msg);
        }

    }


}
