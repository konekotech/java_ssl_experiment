package nonssl.client;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * This is a simple client program that connects to a server and sends a message
 * This client does not use SSL
 */
public class NonSslClient {
    public static void main(String[] args) { 
        try(
            Socket socket = new Socket("127.0.0.1", 3838); 
            PrintWriter output = new PrintWriter(socket.getOutputStream(),true);
        ){
            output.println(args[0]);
            try (Scanner input = new Scanner(socket.getInputStream())) {
                System.out.println("Message : " + input.nextLine());
            }
        } catch (IOException e){
            System.out.println(e);
        }
    }
}
