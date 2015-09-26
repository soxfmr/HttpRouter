package com.soxfmr.httprouter;

import com.soxfmr.httprouter.thirdparty.HttpHeaders;
import com.soxfmr.httprouter.utils.EncodingUtils;

public class BasicHttpHeader extends HttpHeader {

    public BasicHttpHeader() {
        init();
    }

    private void init() {
        addHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
        addHeader(HttpHeaders.ACCEPT_CHARSET, EncodingUtils.UTF8);
        addHeader(HttpHeaders.ACCEPT_LANGUAGE, "zh-CN,zh;q=0.8,ja;q=0.6,en;q=0.4");
        addHeader(HttpHeaders.CACHE_CONTROL, "no-cache");
        addHeader(HttpHeaders.PRAGMA, "no-cache");
        addHeader(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded");
        addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.85 Safari/537.36");
    }

}
