package com.kalaha;

import io.restassured.response.Response;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
public class KalahaGameIntegrationTests {

    private final String baseUrl = "http://localhost:8082";

    @Test
    @DisplayName("get on root should successfully return the index page")
    void getIndexPage() throws IOException {
        String indexPage = IOUtils.toString(KalahaGameIntegrationTests.class.getResourceAsStream("/indexPage.html"), "UTF-8");

        Response response =
                when()
                        .get(baseUrl + "/")
                .then()
                        .assertThat()
                        .statusCode(200)
                .and()
                        .extract()
                        .response();

        assertThat(response.getBody().print()).isEqualToIgnoringWhitespace(indexPage);
    }

    @Test
    @DisplayName("get on /play should successfully return new board")
    void getPlay() throws IOException {
        String newBoard = IOUtils.toString(KalahaGameIntegrationTests.class.getResourceAsStream("/newBoard.html"), "UTF-8");

        Response response =
                when()
                        .get(baseUrl + "/play")
                .then()
                        .assertThat()
                        .statusCode(200)
                .and()
                        .extract()
                        .response();

        assertThat(response.getBody().print()).isEqualToIgnoringWhitespace(newBoard);
    }

    @Test
    @DisplayName("post on /play should successfully return a correctly updated board")
    void postPlay() throws IOException {
        String updatedBoard = IOUtils.toString(KalahaGameIntegrationTests.class.getResourceAsStream("/updatedBoard.html"), "UTF-8");

        Response response =
                given()
                        .queryParam("index", "0")
                        .queryParam("isSouthTurn", "true")
                .when()
                        .post(baseUrl + "/play")
                .then()
                        .assertThat()
                        .statusCode(200)
                .and()
                        .extract()
                        .response();

        assertThat(response.getBody().print()).isEqualToIgnoringWhitespace(updatedBoard);
    }
}
