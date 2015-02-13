package util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by aabramov on 12/4/14.
 */
public class ApiException extends RuntimeException {


    private ApiError error_ = new ApiError(ApiError.ApiErrorCode.UNEXPECTED_ERROR, "", "");


    public ApiException(String message) {
        super(message);
        this.error_ = new ApiError(ApiError.ApiErrorCode.UNEXPECTED_ERROR, message, "");
    }

    public ApiException(String message, ApiError.ApiErrorCode code) {
        super(message);
        this.error_ = new ApiError(code, message, "");
    }

    public ApiException(String message, Throwable cause) {
        super(message, cause);
        this.error_ = new ApiError(ApiError.ApiErrorCode.UNEXPECTED_ERROR, message + ": " + cause.getMessage(), ApiException.toStringStackTrace(cause));
    }

    public ApiException(String message, ApiError.ApiErrorCode code, Throwable cause) {
        super(message, cause);
        this.error_ = new ApiError(ApiError.ApiErrorCode.UNEXPECTED_ERROR, message + ": " + cause.getMessage(), ApiException.toStringStackTrace(cause));
    }

    public ApiError getApiError() {
        return this.error_;
    }

    public static String toStringStackTrace(Throwable t) {
        StringWriter sw = new StringWriter();

        PrintWriter pw = new PrintWriter(sw);
        t.printStackTrace(pw);
        return sw.toString();
    }

}



