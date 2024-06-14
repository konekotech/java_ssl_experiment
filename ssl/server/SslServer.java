package ssl.server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Scanner;
import javax.net.ssl.*;

public class SslServer {
    public static void main(String[] args) {
        // System.setProperty("javax.net.debug", "all");
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            KeyStore keyStore = KeyStore.getInstance("JKS");
            char[] keystorePassword = "password".toCharArray();
            keyStore.load(new FileInputStream("./ssl/server/keystore.jks"), keystorePassword);
            keyManagerFactory.init(keyStore, keystorePassword);
            sslContext.init(keyManagerFactory.getKeyManagers(), null, new SecureRandom());
            SSLServerSocketFactory sslServerSocketFactory = sslContext.getServerSocketFactory();
            SSLServerSocket server = (SSLServerSocket) sslServerSocketFactory.createServerSocket(3839);
            System.out.println("Now Server is running with SSL...");
            while (true) {
                System.out.println("Server is waiting for message...");
                try (
                var socket = (SSLSocket) server.accept(); 
                Scanner input = new Scanner(socket.getInputStream()); 
                PrintWriter output = new PrintWriter(socket.getOutputStream())
                ) {
                    
                    String message = input.nextLine();
                    System.out.println(message);
                    if (message.equals("exit")) {
                        break;
                    }
                    output.println("Ack!!");
                    output.flush();
                }
            }
        } catch (IOException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException | CertificateException e) {
            System.out.println(e);
        }
    }
}
