package com.kernelequity.util;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.Date;

public class DomainChecker {

    public static boolean isDomainUp(String domain) {
        // Create an HttpClient that uses the custom SSL socket factory
        CloseableHttpResponse response;

        try (CloseableHttpClient httpClient = HttpClients.custom()
                .setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE)
                .build()) {

            // Create an HTTP GET request
            HttpGet request = new HttpGet("https://" + domain);

            // Execute the request and get the response
            response = httpClient.execute(request);

            int responseCode = response.getStatusLine().getStatusCode();
            return (responseCode >= 200 && responseCode < 400);
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
        return false;
    }

    public static Date getCertificateExpirationDate(String domain) {
        X509Certificate certificate = getCertificate(domain);
        if (certificate != null) {
            // Extract expiration date
            return certificate.getNotAfter();
        } else {
            return null;
        }
    }

    private static X509Certificate getCertificate(String domain) {
        try {
            URL url = new URL("https://" + domain);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setInstanceFollowRedirects(true);  // Enable redirect following
            conn.connect();

            // Check if the final destination uses HTTPS
            String protocol = conn.getURL().getProtocol();
            if (!protocol.equals("https")) return null;

            // Create an SSL socket to connect to the domain on the given port
            SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            SSLSession session;
            try (SSLSocket sslSocket = (SSLSocket) factory.createSocket(domain, 443)) {

                // Start handshake to initiate the SSL connection
                sslSocket.startHandshake();

                // Retrieve the server certificates
                session = sslSocket.getSession();

                Certificate[] certificates = session.getPeerCertificates();

                if (certificates.length > 0) {
                    return (X509Certificate) certificates[0];
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to fetch SSL certificate for domain " + domain + ": " + e.getMessage());
        }
        return null;
    }

}
