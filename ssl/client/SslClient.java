package ssl.client;

import java.io.FileInputStream;
import java.io.PrintWriter;
import java.security.KeyStore;
import java.util.Scanner;
import javax.net.ssl.*;

public class SslClient {

    public static void main(String[] args) {
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            KeyStore keyStore = KeyStore.getInstance("JKS");
            KeyStore trustStore = KeyStore.getInstance("JKS");

            char[] password = "hogehoge".toCharArray();

            keyStore.load(new FileInputStream("./java_ssl_experiment/ssl/client/keystore.jks"), password);
            trustStore.load(new FileInputStream("./java_ssl_experiment/ssl/client/truststore.jks"), password);

            keyManagerFactory.init(keyStore, password);
            trustManagerFactory.init(trustStore);

            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket("127.0.0.1", 3839);

            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            output.println(args[0]);

            Scanner input = new Scanner(socket.getInputStream());
            System.out.println("Message : " + input.nextLine());

            output.close();
            input.close();
            socket.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
