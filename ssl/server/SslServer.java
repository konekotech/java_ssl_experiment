package ssl.server;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.security.KeyStore;
import java.util.Scanner;
import javax.net.ssl.*;

public class SslServer {

    public static void main(String[] args) {
        SSLServerSocket server;
        Scanner input;
        PrintWriter output;
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            KeyStore keyStore = KeyStore.getInstance("JKS");
            KeyStore trustStore = KeyStore.getInstance("JKS");

            char[] password = "password".toCharArray();

            keyStore.load(new FileInputStream("./java_ssl_experiment/ssl/server/keystore.jks"), password);
            trustStore.load(new FileInputStream("./java_ssl_experiment/ssl/server/truststore.jks"), password);

            keyManagerFactory.init(keyStore, password);
            trustManagerFactory.init(trustStore);

            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
            server = (SSLServerSocket) sslServerSocketFactory.createServerSocket(3839);

            System.out.println("Now Server is running with SSL...");

            while (true) {
                System.out.println("Server is waiting for message...");
                SSLSocket socket = (SSLSocket) server.accept();
                input = new Scanner(socket.getInputStream());
                output = new PrintWriter(socket.getOutputStream());
                String message = input.nextLine();
                System.out.println(message);
                if (message.equals("exit")) {
                    break;
                }
                output.println("Ack!!");
                output.close();
                socket.close();
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
