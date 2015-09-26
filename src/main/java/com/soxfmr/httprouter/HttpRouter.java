package com.soxfmr.httprouter;

import com.soxfmr.httprouter.service.DispatcherImpl;
import com.soxfmr.httprouter.service.RouterService;
import com.soxfmr.httprouter.utils.Args;

public class HttpRouter {

    private HttpConfig httpConfig;

    private RouterService routerService;
    private DispatcherImpl dispatcher;

    private RequestHandler requestHandler;

    private ResponseListener responseListener;
    private ResponseLiteListener responseLiteListener;

    public void setHttpConfig(HttpConfig config) {
        if (config == null)
            return ;

        httpConfig = config;
    }

    public void setResponseListener(ResponseListener handler) {
        responseListener = handler;
    }

    public void setResponseLiteListener(ResponseLiteListener handler) {
        this.responseLiteListener = handler;
    }

    public void registerMiddlewareListener(MiddlewareListener middlewareListener) {
        dispatcher.setMiddlewareListener(middlewareListener);
    }

    public HttpRouter() {
        dispatcher = new DispatcherImpl(this);
        // Route service to forward the package in thread pool
        routerService = new RouterService(this);
        routerService.register(dispatcher);
        // Global setting
        httpConfig = new HttpConfig();
        // Initialize request handler
        requestHandler = RequestHandler.getInstance();
    }

    /**
     * To forward all of request
     */
    public void forward() {
        routerService.execute();
    }

    public void forward(HttpRequest request) {
        forward(request, 0);
    }

    /**
     * Forward a request immediately
     * @param request HttpRequest to be forward
     */
    public void forward(HttpRequest request, int requestCode) {
        addRoute(request, requestCode);
        routerService.execute();
    }

    public void blockingForward(HttpRequest request) {
        if (request == null)
            throw new IllegalArgumentException("Cannot forward a empty request.");

        if (Args.isEmpty(request.getURI()))
            throw new IllegalArgumentException("The remote server is unreachable.");

        if (request.getHttpHeader().size() == 0) {
            request.attachHeaders(new BasicHttpHeader());
        }
        // Set up the global settings default
        HttpConfig config = request.getHttpConfig();
        if (config == null) config = this.httpConfig;

        requestHandler.init(request,  config);
        HttpResponse httpResponse = requestHandler.makeRequest();

        triggerResponseHandler(httpResponse);
    }

    public void shutdown() {
        routerService.release();
    }

    private void triggerResponseHandler(HttpResponse httpResponse) {
        // For more simplify interface to handle the response
        if (responseLiteListener != null)
            responseLiteListener.onFinished(httpResponse);

        if (responseListener == null)
            return ;

        // Invalid request
        if (httpResponse.isCorrupt()) {
            responseListener.onCorrupt();
            return ;
        }

        int status = httpResponse.getStatusCode();
        if (status >= 500) {
            responseListener.onInternalError(httpResponse);
        } else if (status >= 400) {
            responseListener.onFailed(httpResponse);
        } else if (status >= 300) {
            responseListener.onRedirect(httpResponse);
        } else if (status >= 200) {
            responseListener.onSuccess(httpResponse);
        }
    }

    public void addRoute(HttpRequest request) {
        routerService.addRoute(request, 0);
    }

    public void addRoute(HttpRequest request, int requestCode) {
        routerService.addRoute(request, requestCode);
    }

}
