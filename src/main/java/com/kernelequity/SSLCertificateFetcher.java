package com.kernelequity;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;

public class SSLCertificateFetcher {

    public static void main(String[] args) {
        String domain = "www.google.com";  // Domain to get the SSL certificate from
        int port = 443;  // Default HTTPS port

        try {
            fetchSSLCertificate(domain, port);
        } catch (Exception e) {
            System.out.println("The final destination does not use HTTPS.");
            e.printStackTrace();
        }
    }

    public static void fetchSSLCertificate(String domain, int port) throws Exception {
        URL url = new URL("https://" + domain);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setInstanceFollowRedirects(true);  // Enable redirect following
        conn.connect();

        // Check if the final destination uses HTTPS
        if (conn.getURL().getProtocol().equals("https")) {

            // Create an SSL socket to connect to the domain on the given port
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSocket sslSocket = (SSLSocket) factory.createSocket(domain, port);

            // Start handshake to initiate the SSL connection
            sslSocket.startHandshake();

            // Retrieve the server certificates
            SSLSession session = sslSocket.getSession();
            Certificate[] certificates = session.getPeerCertificates();

            // Loop through the certificates and print details
            for (Certificate certificate : certificates) {
                if (certificate instanceof X509Certificate) {
                    X509Certificate x509Cert = (X509Certificate) certificate;
                    System.out.println("Certificate Type: " + x509Cert.getType());
                    System.out.println("Issuer: " + x509Cert.getIssuerDN());
                    System.out.println("Subject: " + x509Cert.getSubjectDN());
                    System.out.println("Valid From: " + x509Cert.getNotBefore());
                    System.out.println("Valid Until: " + x509Cert.getNotAfter());
                    System.out.println("Serial Number: " + x509Cert.getSerialNumber());
                    System.out.println();
                }
            }

            // Close the SSL socket
            sslSocket.close();
        } else {
            System.out.println("The final destination does not use HTTPS.");
        }
    }

}
