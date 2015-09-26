package com.soxfmr.httprouter;

import com.soxfmr.httprouter.utils.Args;
import com.soxfmr.httprouter.utils.EncodingUtils;

import java.net.HttpCookie;
import java.net.URI;

public abstract class HttpRequest {

    protected RequestParam mParams;
    protected HttpHeader mHttpHeader;

    protected String mUri;
    protected String mEncoding;

    private HttpConfig mHttpConfig = null;

    public HttpRequest() {
        this(null);
    }

    public HttpRequest(String uri) {
        if (! Args.isEmpty(uri)) {
            mUri = uri;
        }

        mParams = new RequestParam();
        mHttpHeader = new HttpHeader();
        mEncoding = EncodingUtils.UTF8;
    }

    public abstract String getURI();

    public abstract byte[] getPayload();

    public abstract boolean isFullDuplex();

    public HttpRequest setURI(String uri) {
        if (! Args.isEmpty(uri))
            mUri = uri;

        return this;
    }

    public HttpRequest setEncoding(String encoding) {
        if (! Args.isEmpty(encoding))
            mEncoding = encoding;

        return this;
    }

    public String getEncoding() {
        return mEncoding;
    }

    public HttpConfig getHttpConfig() {
        return mHttpConfig;
    }

    public void setHttpConfig(HttpConfig httpConfig) {
        this.mHttpConfig = httpConfig;
    }

    public HttpRequest attachHeaders(HttpHeader headers) {
        if (headers != null)
            mHttpHeader = headers;

        return this;
    }

    public HttpRequest addHeader(String name, String value) {
        mHttpHeader.addHeader(name, value);

        return this;
    }

    public HttpRequest setHeader(String name, String value) {
        mHttpHeader.setHeader(name, value);

        return this;
    }

    public HttpRequest attachParams(RequestParam params) {
        if (params != null)
            mParams = params;

        return this;
    }

    public HttpRequest setParam(String key, String value) {
        mParams.setParam(key, value);

        return this;
    }

    public HttpRequest addParam(String key, String value) {
        mParams.addParam(key, value);

        return this;
    }

    public HttpRequest addCookie(URI uri, HttpCookie httpCookie) {
        CookieContainer.addCookie(uri, httpCookie);

        return this;
    }

    public boolean isAutoRedirect() {
        if (mHttpConfig == null)
            return false;

        return mHttpConfig.isAutoRedirect();
    }

    public HttpRequest setAutoRedirect(boolean autoRedirect) {
        initConfigInstance();
        mHttpConfig.setAutoRedirect(autoRedirect);

        return this;
    }

    public HttpRequest restoreCookies() {
        initConfigInstance();
        mHttpConfig.setRestore(true);

        return this;
    }

    public HttpRequest disableCookies() {
        initConfigInstance();
        mHttpConfig.setEnableCookies(false);

        return this;
    }

    public HttpRequest enableCookies() {
        initConfigInstance();
        mHttpConfig.setEnableCookies(true);

        return this;
    }

    public HttpHeader getHttpHeader() {
        return mHttpHeader;
    }

    public void initConfigInstance() {
        if (mHttpConfig == null)
            mHttpConfig = new HttpConfig();
    }

}
