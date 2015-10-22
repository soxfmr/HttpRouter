package com.soxfmr.httprouter.service;

import com.soxfmr.httprouter.HttpRequest;
import com.soxfmr.httprouter.HttpRouter;
import com.soxfmr.httprouter.MiddlewareListener;
import com.soxfmr.httprouter.RequestHandler;

public class DispatcherImpl {

    private HttpRouter httpRouter;
    private RequestHandler requestHandler;

    private MiddlewareListener middlewareListener;

    public DispatcherImpl(HttpRouter httpRouter) {
        this.httpRouter = httpRouter;
        // Obtain the response object previously
        requestHandler = RequestHandler.getInstance();
    }

    public void setMiddlewareListener(MiddlewareListener middlewareListener) {
        this.middlewareListener = middlewareListener;
    }

    public DispatchRoute newDispatchRoute(final HttpRequest httpRequest, final int requestCode) {
        return new DispatchRoute() {
            @Override
            public void dispatch() {
                // Interrupt the current request for modify
                if (middlewareListener != null) {
                    middlewareListener.handle(httpRequest, requestCode, requestHandler.prevHttpResponse());
                }
                // Forward the package
                httpRouter.blockingForward(httpRequest);
            }
        };
    }

}
