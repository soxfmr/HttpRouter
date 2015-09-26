package com.soxfmr.httprouter;

import com.soxfmr.httprouter.utils.Args;

import java.io.UnsupportedEncodingException;

public class HttpPost extends HttpRequest {

    public HttpPost() {}

    public HttpPost(String uri) {
        super(uri);
    }

    @Override
    public String getURI() {
        return mUri;
    }

    @Override
    public boolean isFullDuplex() {
        return true;
    }

    @Override
    public byte[] getPayload() {
        if (mParams.size() == 0)
            return null;

        String payload = mParams.toString(mEncoding);
        if (Args.isEmpty(payload))
            return null;

        byte[] bytes = null;
        try {
            bytes = payload.getBytes(mEncoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return bytes;
    }
}
