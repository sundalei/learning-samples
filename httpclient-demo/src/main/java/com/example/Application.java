package com.example;

import java.io.IOException;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.HttpEntity;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.io.support.ClassicRequestBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application {
  private static final Logger logger = LoggerFactory.getLogger(Application.class);
  private static final int ELASTIC_PORT = 9200;

  public static void main(String[] args) {
    String host = System.getenv("ELASTIC_HOST");
    if (host == null || host.isBlank()) {
      throw new IllegalStateException("ELASTIC_HOST environment variable is required");
    }

    String elasticBaseUrl = "https://" + host + ":" + ELASTIC_PORT;
    ElasticClient client = new ElasticClient();

    try (CloseableHttpClient httpClient = client.getClient()) {
      ClassicHttpRequest httpGet = ClassicRequestBuilder.get(elasticBaseUrl).build();

      httpClient.execute(
          httpGet,
          response -> {
            logger.info("{} {}", response.getCode(), response.getReasonPhrase());
            final HttpEntity entity = response.getEntity();
            logger.info("Response");
            logger.info("{}", EntityUtils.toString(entity));
            return null;
          });
    } catch (IOException e) {
      logger.error("Request failed", e);
    }
  }
}
