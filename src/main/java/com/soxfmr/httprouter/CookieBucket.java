package com.soxfmr.httprouter;

import java.net.HttpCookie;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class CookieBucket {

    private URI uri;
    private List<HttpCookie> httpCookieList;

    public CookieBucket(URI uri) {
        this.uri = uri;
        httpCookieList = new ArrayList<>();
    }

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public List<HttpCookie> getHttpCookieList() {
        return httpCookieList;
    }

    public void push(HttpCookie cookie) {
        httpCookieList.add(cookie);
    }

    public void pushAll(List<HttpCookie> cookies) {
        httpCookieList.addAll(cookies);
    }
}
