package nonssl.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * This is a simple server program that listens to a port and accepts a message
 * This server does not use SSL
 */
public class NonSslServer {
    public static void main(String[] args) {
        try(ServerSocket server = new ServerSocket(3838)) {
            System.out.println("Now Server is running...");
            while (true) {
                System.out.println("Server is waiting for message...");
                try (
                    Socket socket = server.accept();
                    Scanner input = new Scanner(socket.getInputStream());
                    PrintWriter output = new PrintWriter(socket.getOutputStream());
                ) {
                    String message = input.nextLine();
                    System.out.println(message);
                    output.println("Ack!!");
                    if(message.equals("exit")){
                        break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println(e);
        }
    }
}

