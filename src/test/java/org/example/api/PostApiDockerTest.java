package org.example.api;

import io.restassured.RestAssured;
import org.example.config.ApiConfig;
import org.example.utils.TestDataUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class PostApiDockerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PostApiDockerTest.class);

    @BeforeAll
    static void setupAll() {
        ApiConfig.init();
        LOGGER.info("BaseURI set to {}", RestAssured.baseURI);
    }

    // Test POST -> GET
    @ParameterizedTest
    @MethodSource("postDataProvider")
    void shouldCreateAndRetrievePost(Post post) {

        // ---------- WHEN (POST) ----------
        int postId = -1;
        try {
            LOGGER.info("Creating post with id {}", post.getTitle());

            postId =
                    given()
                            .contentType("application/json")
                            .body(post)
                            .when()
                            .post("/posts")

                            // ---------- THEN (POST ASSERTIONS) ----------
                            .then()
                            .statusCode(201)
                            .body("id", greaterThan(0))
                            .log().body()
                            .extract()
                            .path("id");

            LOGGER.info("Post created with id {}", postId);

            // ---------- WHEN (GET) ----------
            given()
                    .when()
                    .get("/posts/{id}", postId)

                    // ---------- THEN (GET ASSERTIONS) ----------
                    .then()
                    .statusCode(200)
                    .body("id", equalTo(postId))
                    .body("title", equalTo(post.getTitle()))
                    .body("body", equalTo(post.getBody()))
                    .body("userId", equalTo(post.getUserId()));

            LOGGER.info("Retrieving post with id {}", postId);

            // ---------- DELETE ----------
            given()
                    .when()
                    .delete("/posts/{id}", postId)
                    .then()
                    .statusCode(200);

            LOGGER.info("Deleting post with id {}", postId);

            // ---------- GET ----------
            given()
                    .when()
                    .get("/posts/{id}", postId)
                    .then()
                    .statusCode(404);

            LOGGER.info("Verify deletion post with id {}", postId);
            
        } catch (AssertionError | Exception e) {
            LOGGER.error("Test failed for post: {} (ID: {})", post.getTitle(), postId, e);
            throw e;
        }
    }

    static Stream<Post> postDataProvider() throws IOException {
        return TestDataUtils.loadPostsFromJson("src/test/resources/posts.json")
                .map(args -> (Post) args.get()[0]);
    }
}
