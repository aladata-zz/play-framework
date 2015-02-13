package unit;

import static org.fest.assertions.Assertions.assertThat;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import controllers.routes;
import org.junit.*;
import org.junit.rules.ExpectedException;
import play.libs.Json;
import play.mvc.Result;
import security.Token;

import play.test.*;
import play.libs.F.*;

import static play.test.Helpers.*;
import static play.test.Helpers.callAction;
import static play.test.Helpers.contentAsString;


/**
 * Created by aabramov on 12/7/14.
 */
public class SecurityTests {



    @Test
    public void createTokenSuccess() {
        String key = "asd2@#$@g23#%GS_ASDF$#%";
        Token token = new Token("alex@gmail.com", "123.12.3.0", "shardId",key);
        String tokenStr = token.toString();
        //System.out.println(tokenStr);

        Token tokenParsed = new Token(tokenStr, 24, key);
        //String tokenStr2 = tokenParsed.toString();
        //System.out.println(tokenStr2);

        assertThat(token.createdDateTime).isEqualTo(tokenParsed.createdDateTime);
        assertThat(token.hash).isEqualTo(tokenParsed.hash);
        assertThat(token.shardId).isEqualTo(tokenParsed.shardId);
        assertThat(token.tokenId).isEqualTo(tokenParsed.tokenId);
        assertThat(token.userid).isEqualTo(tokenParsed.userid);
        assertThat(token.userIP).isEqualTo(tokenParsed.userIP);


    }

    @Rule
    public ExpectedException expectedEx = ExpectedException.none();

    @Test
    public void createTokenExpired() {


        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Access token expired");

        String key = "asd2@#$@g23#%GS_ASDF$#%";
        Token token = new Token("alex@gmail.com", "123.12.3.0", "shardId",key);
        String tokenStr = token.toString();

        Token tokenParsed = new Token(tokenStr, -1, key);

    }


    @Test
    public void createTokenHashCheckFailed() {


        expectedEx.expect(IllegalArgumentException.class);
        expectedEx.expectMessage("Token hash check failed");

        String key = "asd2@#$@g23#%GS_ASDF$#%";
        String tokenStr = "c1YyNFBxaktESE9MdlBjYQ==_Y/HOXDWu+5LbHrtjTtC18UiuD5snhG85Yk8xz8vqQhGaVMTrR9RDFc1+kOIsrw1HqlFxcx6Ud2/JZODAjTFsAEZOHBrhI56LbIVg8g09/lP0whAsL9e5eGY19Eo5Jenk591vwaongIX0+ijRCg6fjEaFAkdSq7zF1ajvVt3a6OyGufK3qyKpxIRyOw6WgiCrPFjiCEO77ZOtyNqTIRS/jr4e/Y4wBh7MvGFUnggQx45J1z+9DDYo5/q/1XdA1nmtV09c9USaI47pd5fOLfhfLeHwWU0SPh3YlhxelqefQjBUjdOm6QIDsQr4Xg+/W14NBpUhDyxeCs5fB9GnwlL9oBaHc0Oy2HU8byiObhDCe2oKIxt4IfYoZvsv/4vQXINX5Y1SRbh7F8hpJ/Q2MSphisLFrUcfMOpGDdDNYvNpC4A=";

        Token tokenParsed = new Token(tokenStr, 24, key);

    }




}
