package ssl.client;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.util.Scanner;
import javax.net.ssl.*;

public class SslClient {

    public static void main(String[] args) {
        // System.setProperty("javax.net.debug", "all");
        try {
            SSLContext sslContext = SSLContext.getInstance("TLS");
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            KeyStore trustStore = KeyStore.getInstance("JKS");
            char[] truststorePassword = "password".toCharArray();
            trustStore.load(new FileInputStream("./ssl/client/truststore.jks"), truststorePassword);
            trustManagerFactory.init(trustStore);
            sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            try (SSLSocket socket = (SSLSocket) sslSocketFactory.createSocket("127.0.0.1", 3839)) {
                try (PrintWriter output = new PrintWriter(socket.getOutputStream(), true)) {
                    output.println(args[0]);
                    output.flush();
                    try (Scanner input = new Scanner(socket.getInputStream())) {
                        System.out.println("Message : " + input.nextLine());
                        socket.shutdownOutput();
                    }
                }
            }
        } catch (IOException | KeyManagementException | KeyStoreException | NoSuchAlgorithmException | CertificateException e) {
            System.out.println(e);
        }
    }
}
