package com.soxfmr.httprouter;

import com.soxfmr.httprouter.utils.Args;

public class HttpGet extends HttpRequest {

    public HttpGet() {}

    public HttpGet(String uri) {
        super(uri);
    }

    @Override
    public String getURI() {
        if (mParams.size() == 0)
            return mUri;

        String payload = mParams.toString(mEncoding);
        if (Args.isEmpty(payload))
            return mUri;

        if (! mUri.endsWith("?")) mUri += "?";

        return String.format("%s%s", mUri, payload);
    }

    @Override
    public boolean isFullDuplex() {
        return false;
    }

    /**
     * @return For the get request it always null.
     */
    @Override
    public byte[] getPayload() {
        return null;
    }
}
