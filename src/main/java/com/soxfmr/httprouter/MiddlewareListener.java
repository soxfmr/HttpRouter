package com.soxfmr.httprouter;

public interface MiddlewareListener {

    void handle(HttpRequest nextRequest, int requestCode, HttpResponse prevResponse);

}
