package controllers;

import play.Logger;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import security.ApiAuthenticator;
import views.html.index;

import java.util.ArrayList;

/**
 * Created by aabramov on 12/7/14.
 */
@Security.Authenticated(ApiAuthenticator.class)
public class VendorsCtl extends Controller {

    public static Result getTransactions() {

        ArrayList<Transaction> list = new ArrayList<Transaction>();
        list.add(new Transaction("trans1", 123.12));
        list.add(new Transaction("trans2", 456.78));
        return ok(Json.toJson(list));
    }

    public static class Transaction {
        public String description;
        public double amount;

        public Transaction(String description, double amount) {
            this.description = description;
            this.amount = amount;
        }


    }



}