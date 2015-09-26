package com.soxfmr.httprouter;

import java.util.HashMap;

public class HttpHeader extends NameValueCollection {

    public HttpHeader() {
        super();
    }

    public HttpHeader(HashMap<String, String> headerList) {
        super(headerList);
    }

    public void setHeader(String name, String value) {
        add(name, value);
    }

    public void addHeader(String name, String value) {
        add(name, value);
    }

    public String getHeaderValue(String key) {
        return get(key);
    }

    public void removeHeader(String key) {
        remove(key);
    }

}
