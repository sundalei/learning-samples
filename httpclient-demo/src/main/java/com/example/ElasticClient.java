package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import javax.net.ssl.SSLContext;
import org.apache.hc.client5.http.auth.AuthScope;
import org.apache.hc.client5.http.auth.CredentialsProvider;
import org.apache.hc.client5.http.auth.UsernamePasswordCredentials;
import org.apache.hc.client5.http.impl.auth.BasicCredentialsProvider;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.ClientTlsStrategyBuilder;
import org.apache.hc.client5.http.ssl.TlsSocketStrategy;
import org.apache.hc.core5.ssl.SSLContextBuilder;

public class ElasticClient {

  public CloseableHttpClient getClient() {
    return HttpClients.custom()
        .setConnectionManager(getConnectionManager())
        .setDefaultCredentialsProvider(getCredentialsProvider())
        .build();
  }

  private PoolingHttpClientConnectionManager getConnectionManager() {
    SSLContext sslContext;
    try {
      sslContext = buildSslContext();
    } catch (KeyManagementException
        | NoSuchAlgorithmException
        | KeyStoreException
        | CertificateException
        | IOException e) {
      throw new IllegalStateException("Failed to load Elasticsearch CA certificate", e);
    }

    TlsSocketStrategy tlsSocketStrategy =
        ClientTlsStrategyBuilder.create().setSslContext(sslContext).buildClassic();

    return PoolingHttpClientConnectionManagerBuilder.create()
        .setTlsSocketStrategy(tlsSocketStrategy)
        .build();
  }

  private CredentialsProvider getCredentialsProvider() {
    String password = System.getenv("ELASTIC_PASSWORD");
    String host = System.getenv("ELASTIC_HOST");
    if (password == null || password.isBlank()) {
      throw new IllegalStateException("ELASTIC_PASSWORD environment variable is required");
    }
    if (host == null || host.isBlank()) {
      throw new IllegalStateException("ELASTIC_HOST environment variable is required");
    }

    BasicCredentialsProvider credentialsProvider = new BasicCredentialsProvider();
    credentialsProvider.setCredentials(
        new AuthScope(host, 9200),
        new UsernamePasswordCredentials("elastic", password.toCharArray()));
    return credentialsProvider;
  }

  private SSLContext buildSslContext()
      throws CertificateException,
          KeyStoreException,
          IOException,
          NoSuchAlgorithmException,
          KeyManagementException {
    CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
    KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
    trustStore.load(null);

    try (InputStream certificateStream =
        ElasticClient.class.getClassLoader().getResourceAsStream("ca.crt")) {
      if (certificateStream == null) {
        throw new IOException("ca.crt not found on classpath");
      }

      trustStore.setCertificateEntry(
          "elastic-ca", certificateFactory.generateCertificate(certificateStream));
    }

    return SSLContextBuilder.create().loadTrustMaterial(trustStore, null).build();
  }
}
