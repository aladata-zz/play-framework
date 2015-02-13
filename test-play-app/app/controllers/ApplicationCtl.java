package controllers;


import plank.models.*;
import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import security.ApiAuthenticator;


import java.util.ArrayList;

@Security.Authenticated(ApiAuthenticator.class)
public class ApplicationCtl extends Controller {

    public static Result getApplications() {
        Logger.debug("hello world");

        ArrayList<PlankApplication> list = new ArrayList<PlankApplication>();
        list.add(new PlankApplication(Business.getDummy(), BusinessOwner.getDummy()));
        list.add(new PlankApplication(Business.getDummy(), BusinessOwner.getDummy()));
        return ok(Json.toJson(list));
    }


}
