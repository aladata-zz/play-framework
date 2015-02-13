
import java.lang.reflect.Method;

import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.Logger.ALogger;
import play.filters.gzip.GzipFilter;
import play.libs.F;
import play.libs.Json;
import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Http.Request;
import play.mvc.Result;
import play.mvc.Http.*;
import play.libs.F.*;
import play.api.mvc.EssentialFilter;
import play.filters.csrf.CSRFFilter;
import play.filters.headers.SecurityHeadersFilter;
import util.ApiError;
import util.ApiException;

/**
 * Created by aabramov on 12/2/14
 * One place to do access logging and processing of http errors
 */
public class Global extends GlobalSettings {

    //private final ALogger accessLogger = Logger.of("access");

    @Override
    @SuppressWarnings("rawtypes")
    public Action onRequest(Request request, Method method) {
        //todo: figure out where to create and cache transactionId. log transaction and convo ids. Perhaps need to create custom Filter
        //init id for play request
        final String transactionId = java.util.UUID.randomUUID().toString();
        Logger.info(transactionId+":" + request.method() + ":" + request.uri() + ":" + request.remoteAddress());
        /*return new Action.Simple() {
            public F.Promise<Result> call(Http.Context ctx) throws Throwable {

                ctx.flash().put("transactionId", transactionId);

                return delegate.call(ctx);
            }
        };*/

        return super.onRequest(request, method);
    }

    @Override
    public void onStart(Application app) {

        Logger.info("Plank App has started");
    }

    @Override
    public void onStop(Application app) {
        Logger.info("Plank App has stopped");

    }

    public Promise<Result> onError(RequestHeader request, Throwable t) {

        //todo: error case does not report proper error code. figure out why we never get inside if case
        if(t instanceof ApiException) {
            ApiError er = ((ApiException) t).getApiError();
            Logger.error(er.toString());
            return Promise.<Result>pure(play.mvc.Results.internalServerError(Json.toJson(er)));

        }
        else {

            return Promise.<Result>pure(play.mvc.Results.internalServerError(
                    Json.toJson(new ApiError(ApiError.ApiErrorCode.UNEXPECTED_ERROR, t))
            ));
        }
    }

    public Promise<Result> onHandlerNotFound(RequestHeader request) {
        return Promise.<Result>pure(play.mvc.Results.notFound(
                views.html.error.render("Page not found: " + request.uri())
        ));
    }

    public Promise<Result> onBadRequest(RequestHeader request, String error) {
        return Promise.<Result>pure(play.mvc.Results.badRequest("Don't try to hack the URI: " + request.uri()));
    }


    /*
    * Generic CSRF protection filter
    */
    @Override
    public <T extends EssentialFilter> Class<T>[] filters() {
        return new Class[]{CSRFFilter.class, SecurityHeadersFilter.class, GzipFilter.class};
    }

}