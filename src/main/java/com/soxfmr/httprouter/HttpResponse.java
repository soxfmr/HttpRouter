package com.soxfmr.httprouter;

import com.soxfmr.httprouter.utils.Args;
import com.soxfmr.httprouter.utils.EncodingUtils;

import java.io.UnsupportedEncodingException;

public class HttpResponse {

    private int statusCode = -1;
    private byte[] data = null;

    private HttpHeader responseHeaders = null;
    private String responseEncoding = null;

    private boolean corrupt = false;

    public HttpResponse() {
        // Default encoding
        responseEncoding = EncodingUtils.UTF8;
    }

    public boolean isCorrupt() {
        return corrupt;
    }

    public void setCorrupt(boolean corrupt) {
        this.corrupt = corrupt;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public HttpHeader getResponseHeaders() {
        return responseHeaders;
    }

    public void setResponseHeaders(HttpHeader responseHeaders) {
        this.responseHeaders = responseHeaders;
    }

    public String getResponseEncoding() {
        return responseEncoding;
    }

    public void setResponseEncoding(String responseEncoding) {
        if (Args.isEmpty(responseEncoding))
            responseEncoding = EncodingUtils.UTF8;

        this.responseEncoding = responseEncoding;
    }

    @Override
    public String toString() {
        if (corrupt || data == null) {
            return null;
        }

        return EncodingUtils.decode(data, responseEncoding);
    }
}
