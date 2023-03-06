package com.example.graphqlclient01;

import com.example.graphqlclient01.graphqlapi.GraphQlApiClient;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
class DetailsClientGraphQlTests {

    @SneakyThrows
    @Test
    void calls_graphql_api() {

        try (var server = new MockWebServer()) {
            // given
            server.enqueue(mockResponse());
            server.start();

            String baseUrl = server.url("").toString();

            WebClient webClient = createWebClient(baseUrl);
            var client = new GraphQlApiClient(webClient);

            // when
            String actualTeamName = client.getTeamName(333);

            // then
            assertThat(server.getRequestCount()).isEqualTo(1);

            RecordedRequest request = server.takeRequest();

            assertThat(request.getBody().readUtf8()).contains("query").contains("variables").contains("333");
            assertThat(request.getHeader("Content-Type")).isEqualTo("application/json");
            assertThat(request.getRequestUrl() == null ? "" : request.getRequestUrl().toString()).isEqualTo(String.format("%sgraphql", baseUrl));
            assertThat(request.getMethod()).isEqualTo("POST");

            assertThat(actualTeamName).isEqualTo("--SOME TEAM NAME 333--");

            server.shutdown();
        }
    }

    private static WebClient createWebClient(String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }

    private MockResponse mockResponse() {

        String body = readFileToString(this.getClass())
                .replace("${name}", "--SOME TEAM NAME 333--");

        return new MockResponse()
                .setChunkedBody(body, 32)
                .addHeader("Content-Type", "application/json");
    }

    private String readFileToString(Class<?> clazz) {

        InputStream stream = clazz.getClassLoader().getResourceAsStream("GetTeamResponse.json");

        if (null == stream) {
            return "";
        }

        try (InputStreamReader reader = new InputStreamReader(stream, Charset.defaultCharset())) {
            return FileCopyUtils.copyToString(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
