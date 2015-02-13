package security;


import util.ApiError;
import util.ApiException;
import util.ConfigHelper;

/**
 * Created by aabramov on 12/6/14.
 * Encapsullates business logic associated with authentication/authorization
 */
public class SecurityManager {


    public SecurityManager() {

    }


    /*
     * Locates and validates auth token
     */
    public Token authenticate(String token){

        try {

            Token t = new Token(token, (new ConfigHelper()).getInt("AUTH_TOKEN_EXPIRATION_HOURS"), (new ConfigHelper()).get("application.secret"));
            //todo: db: verify that user is still active (get user info and perms from cache)
            return t;
        }
        catch(IllegalArgumentException e) {
            throw new ApiException(e.getMessage(), ApiError.ApiErrorCode.ACCESS_DENIED, e);
        }




    }


    public Token login(String username, String password, String userIP){

        //todo: db: find user in db, find other data (e.g. shardid)
        if(!username.equals("test@gmail.com") || !password.equals("test")) return null;

        Token token = new Token(username, userIP, "shardId", (new ConfigHelper()).get("application.secret"));
        return token;

    }

}
