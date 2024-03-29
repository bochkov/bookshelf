package sb.bookshelf.web;

import java.util.Collections;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import sb.bookshelf.common.messages.DeleteRequest;
import sb.bookshelf.common.messages.DeleteResponse;
import sb.bookshelf.common.messages.SearchQuery;
import sb.bookshelf.common.messages.TotalBooks;
import sb.bookshelf.common.model.VolumeInfo;
import sb.bookshelf.web.model.Volume;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import({TestMongoConfig.class, TestSecurityConfig.class})
class ApiAuthTest {

    private final TestRestTemplate restTemplate = new TestRestTemplate();
    private final TestRestTemplate authTemplate = new TestRestTemplate("user", "password");

    private final String url;

    public ApiAuthTest(@LocalServerPort int port) {
        this.url = "http://localhost:" + port;
    }

    @Test
    void testCount() {
        ResponseEntity<TotalBooks> response = restTemplate.getForEntity(url + "/api/count/", TotalBooks.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @ParameterizedTest
    @ValueSource(strings = {"/api/authors/", "/api/publishers/", "/api/latest/"})
    void testAuthors(String path) {
        ResponseEntity<Object> response = restTemplate.getForEntity(url + path, Object.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testSaveVolume() {
        VolumeInfo info = new VolumeInfo();
        info.setAuthor("Пушкин");
        info.setTitle("Капитанская дочка");
        ResponseEntity<Volume> response = restTemplate.postForEntity(url + "/api/save/", info, Volume.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void testSaveVolumeAuth() {
        VolumeInfo info = new VolumeInfo();
        info.setAuthor("Пушкин");
        info.setTitle("Капитанская дочка");
        ResponseEntity<Volume> response = authTemplate.postForEntity(url + "/api/save/", info, Volume.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testDeleteVolume() {
        DeleteRequest req = new DeleteRequest(Collections.singletonList("hehe"));
        ResponseEntity<DeleteResponse> response = restTemplate.postForEntity(url + "/api/delete/", req, DeleteResponse.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void testDeleteVolumeAuth() {
        DeleteRequest req = new DeleteRequest(Collections.singletonList("hehe"));
        ResponseEntity<DeleteResponse> response = authTemplate.postForEntity(url + "/api/delete/", req, DeleteResponse.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    void testSearchVolume() {
        SearchQuery query = new SearchQuery("author=Пушкин");
        ResponseEntity<Object> response = restTemplate.postForEntity(url + "/api/search/", query, Object.class);
        Assertions.assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }
}
