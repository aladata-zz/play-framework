package security;

import play.Logger;
import play.libs.Json;
import play.mvc.Http;
import play.mvc.Result;
import play.mvc.Security;
import util.ApiError;
import util.ApiException;

/**
 * Created by aabramov on 12/5/14.
 */
public class ApiAuthenticator extends Security.Authenticator {

    private final static String AUTH_TOKEN_HEADER = "X-AUTH-TOKEN";

    @Override
    public String getUsername(Http.Context ctx) {
        final String transactionId = java.util.UUID.randomUUID().toString();
        ctx.flash().put("transactionId", transactionId);

        String[] tokenHeader = ctx.request().headers().get(ApiAuthenticator.AUTH_TOKEN_HEADER);
        if (tokenHeader == null || tokenHeader.length != 1 || tokenHeader[0] == null) {
            throw new ApiException("Invalid Token Header", ApiError.ApiErrorCode.ACCESS_DENIED);
        }


        SecurityManager sm = new SecurityManager();
        Token token = sm.authenticate(tokenHeader[0]);
        ctx.flash().put("tokenId", token.tokenId);

        //log convo and transaction id
        Logger.info(transactionId + ":" + token.tokenId + ":" + ctx.request().method() + ":" + ctx.request().uri() + ":" + ctx.request().remoteAddress());

        return token.userid;
    }

    @Override
    public Result onUnauthorized(Http.Context ctx) {
        return unauthorized(Json.toJson(new ApiError(ApiError.ApiErrorCode.ACCESS_DENIED, "Unauthorized access")));
    }
}
