package ordina.api;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
class WordCounterApiTest {

    @Test
    void testHighestFrequencyEndpoint_badRequest() {
        given()
            .when()
            .get("/v1/highest-frequency")
            .then()
            .statusCode(400);
    }

    @Test
    void testHighestFrequencyEndpoint() {
        given()
            .when()
            .body("The sun shines over the lake")
            .get("/v1/highest-frequency")
            .then()
            .statusCode(200)
            .body(is("{\"frequency\":2}"));
    }

    @Test
    void testWordFrequencyEndpoint_badRequest() {
        given()
            .when()
            .get("/v1/words/lake/frequency")
            .then()
            .statusCode(400);
    }

    @Test
    void testWordFrequencyEndpoint() {
        given()
            .when()
            .body("The sun shines over the lake")
            .get("/v1/words/lake/frequency")
            .then()
            .statusCode(200)
            .body(is("{\"frequency\":1}"));
    }

    @Test
    void testMostFrequentNWordsEndpoint_badRequest() {
        given()
            .when()
            .param("limit", 3)
            .get("/v1/words/frequency")
            .then()
            .statusCode(400);
    }

    @Test
    void testMostFrequentNWordsEndpoint() {
        given()
            .when()
            .body("The sun shines over the lake")
            .param("limit", 3)
            .get("/v1/words/frequency")
            .then()
            .statusCode(200)
            .body(is("[{\"word\":\"the\",\"frequency\":2},{\"word\":\"lake\",\"frequency\":1},{\"word\":\"over\",\"frequency\":1}]"));
    }
}