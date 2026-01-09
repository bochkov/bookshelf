package sb.bookshelf.web;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.client.RestTestClient;
import sb.bookshelf.common.messages.DeleteRequest;
import sb.bookshelf.common.messages.SearchQuery;
import sb.bookshelf.common.model.VolumeInfo;

import java.util.Collections;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({TestMongoConfig.class, TestSecurityConfig.class})
class ApiAuthTest {

    private final RestTestClient restClient;

    public ApiAuthTest(@LocalServerPort int port) {
        restClient = RestTestClient.bindToServer()
                .baseUrl("http://localhost:" + port)
                .build();
    }

    @Test
    void testCount() {
        restClient.get().uri("/api/count/")
                .exchange()
                .expectStatus().isOk();
    }

    @ParameterizedTest
    @ValueSource(strings = {"/api/authors/", "/api/publishers/", "/api/latest/"})
    void testAuthors(String path) {
        restClient.get().uri(path)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testSaveVolume() {
        VolumeInfo info = new VolumeInfo();
        info.setAuthor("Пушкин");
        info.setTitle("Капитанская дочка");
        restClient.post()
                .uri("/api/save/").body(info)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void testSaveVolumeAuth() {
        VolumeInfo info = new VolumeInfo();
        info.setAuthor("Пушкин");
        info.setTitle("Капитанская дочка");
        restClient.post()
                .uri("/api/save/").body(info)
                .headers(h -> h.setBasicAuth("user", "password"))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testDeleteVolume() {
        DeleteRequest req = new DeleteRequest(Collections.singletonList("hehe"));
        restClient.post()
                .uri("/api/delete/").body(req)
                .exchange()
                .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void testDeleteVolumeAuth() {
        DeleteRequest req = new DeleteRequest(Collections.singletonList("hehe"));
        restClient.post()
                .uri("/api/delete/").body(req)
                .headers(h -> h.setBasicAuth("user", "password"))
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void testSearchVolume() {
        SearchQuery query = new SearchQuery("author=Пушкин");
        restClient.post()
                .uri("/api/search/").body(query)
                .exchange()
                .expectStatus().isOk();
    }
}
