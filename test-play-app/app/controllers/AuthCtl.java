package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import play.data.Form;
import play.data.validation.Constraints;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import security.*;
import security.SecurityManager;
import util.ApiError;

import static play.data.Form.form;

public class AuthCtl extends Controller {

    private static final String AUTH_TOKEN_LABEL = "authToken";

    /**
     * Handle login form submission.
     *
     * @return Dashboard if auth OK or login form if auth KO
     */
    public static Result login() {
        Form<Login> loginForm = form(Login.class).bindFromRequest();

        if (loginForm.hasErrors()) {
            return badRequest(Json.toJson(new ApiError(ApiError.ApiErrorCode.INVALID_REQUEST, "Invalid Request: "+ loginForm.errorsAsJson().toString())));
        }

        Login login = loginForm.get();

        security.SecurityManager secman = new SecurityManager();
        Token token = secman.login(login.emailAddress, login.password, request().remoteAddress());

        if (token == null) {
            return unauthorized(Json.toJson(new ApiError(ApiError.ApiErrorCode.ACCESS_DENIED, "Invalid username/password.")));
        }

        String authToken = token.toString();
        ObjectNode authTokenJson = Json.newObject();
        authTokenJson.put(AUTH_TOKEN_LABEL, authToken);
        response().setCookie(AUTH_TOKEN_LABEL, authToken);
        return ok(authTokenJson);

    }

    @Security.Authenticated(ApiAuthenticator.class)
    public static Result logout() {
        response().discardCookie(AUTH_TOKEN_LABEL);
        return ok(Json.newObject());
    }


    public static class Login {

        @Constraints.Required
        @Constraints.Email
        public String emailAddress;

        @Constraints.Required
        public String password;

    }


}
