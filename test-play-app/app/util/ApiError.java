package util;

import play.Play;
import play.mvc.Http;


/**
 * Created by aabramov on 12/6/14.
 */
public class ApiError {

    public ApiErrorCode code;
    public String transactionId;
    public String message;
    public String stackTrace = ""; //intended for non-production environments

    public ApiError(ApiErrorCode code, String message, String stackTrace) {
        this.initialize(code, message, stackTrace);
    }

    public ApiError(ApiErrorCode code, Throwable t) {
        this.initialize(code, t.getMessage(), ApiException.toStringStackTrace(t));
    }

    public ApiError(ApiErrorCode code, String message) {
        this.initialize(code, message, "");
    }

    private void initialize(ApiErrorCode code, String message, String stackTrace){
        this.code = code;
        String transId = Http.Context.current().flash().get("transactionId"); //assuming we are under web app
        if(transId != null) this.transactionId = transId;
        this.message = message;
        if (!Play.isProd()) this.stackTrace = stackTrace;
    }

    @Override
    public String toString(){
        String conversationid = Http.Context.current().flash().get("tokenId"); //see if we have convo
        if(conversationid == null) conversationid = "";

        return transactionId+":"+ conversationid +":"+code.toString()+":"+message+":"+stackTrace;
    }

    public static enum ApiErrorCode {

        UNEXPECTED_ERROR,
        ACCESS_DENIED,
        INVALID_REQUEST,
        INVALID_CONFIGURATION
    }
}
