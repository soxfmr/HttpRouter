package com.soxfmr.httprouter;

public interface ResponseListener {
    void onCorrupt();
    void onSuccess(HttpResponse response);
    void onFailed(HttpResponse response);
    void onRedirect(HttpResponse response);
    void onInternalError(HttpResponse response);
}