package integration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.routes;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import play.libs.Json;
import play.mvc.Result;
import play.twirl.api.Content;
import security.Token;

import static org.fest.assertions.Assertions.assertThat;
import static play.test.Helpers.*;
import static play.test.Helpers.contentAsString;
import static play.test.Helpers.contentType;


/**
 * Created by aabramov on 12/7/14.
 */
public class SecurityTests {

    @Test
    public void renderTemplate() {
        Content html = views.html.index.render("Your new application is ready.");
        assertThat(contentType(html)).isEqualTo("text/html");
        assertThat(contentAsString(html)).contains("Your new application is ready.");
    }

    @Test
    public void login() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                ObjectNode loginJson = Json.newObject();
                loginJson.put("emailAddress", "test@gmail.com");
                loginJson.put("password", "test");

                Result result = callAction(routes.ref.AuthCtl.login(), fakeRequest().withJsonBody(loginJson));

                assertThat(status(result)).isEqualTo(OK);

                JsonNode json = Json.parse(contentAsString(result));
                assertThat(json.get("authToken")).isNotNull();
            }
        });
    }

    @Test
    public void loginWithBadPassword() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                ObjectNode loginJson = Json.newObject();
                loginJson.put("emailAddress", "test@gmail.com");
                loginJson.put("password", "tasdfasdfasdfaasdfasdfasdfasdfasdf");

                Result result = callAction(routes.ref.AuthCtl.login(), fakeRequest().withJsonBody(loginJson));

                assertThat(status(result)).isEqualTo(UNAUTHORIZED);
            }
        });
    }

    @Test
    public void loginWithBadUsername() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                ObjectNode loginJson = Json.newObject();
                loginJson.put("emailAddress", "asdfasf@sdfasdf.com");
                loginJson.put("password", "test");

                Result result = callAction(routes.ref.AuthCtl.login(), fakeRequest().withJsonBody(loginJson));

                assertThat(status(result)).isEqualTo(UNAUTHORIZED);
            }
        });
    }

    @Test
    public void loginWithDifferentCaseUsername() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                ObjectNode loginJson = Json.newObject();
                loginJson.put("emailAddress", "test@gmail.com".toUpperCase());
                loginJson.put("password", "test");

                Result result = callAction(routes.ref.AuthCtl.login(), fakeRequest().withJsonBody(loginJson));

                assertThat(status(result)).isEqualTo(UNAUTHORIZED);
            }
        });
    }

    @Test
    public void loginWithNullPassword() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {

                ObjectNode loginJson = Json.newObject();
                loginJson.put("emailAddress", "test@gmail.com");

                Result result = callAction(routes.ref.AuthCtl.login(), fakeRequest().withJsonBody(loginJson));

                assertThat(status(result)).isEqualTo(BAD_REQUEST);
            }
        });
    }

    /*@Test
    public void logout() {
        running(fakeApplication(inMemoryDatabase()), new Runnable() {
            public void run() {
                DemoData.loadDemoData();

                String authToken = DemoData.user1.createToken();

                Result result = callAction(routes.ref.SecurityController.logout(), fakeRequest().withHeader(SecurityController.AUTH_TOKEN_HEADER, authToken));

                assertThat(status(result)).isEqualTo(SEE_OTHER);
            }
        });
    }*/
}
