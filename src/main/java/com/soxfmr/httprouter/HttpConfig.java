package com.soxfmr.httprouter;

public class HttpConfig {

    public static final int DEFAULT_TIMEOUT = 10000;

    private int connectionTimeout;

    private int readTimeout;

    private boolean autoRedirect;

    private boolean fullDuplex;

    private boolean enableCookies;
    private boolean restore;

    public HttpConfig() {
        connectionTimeout = DEFAULT_TIMEOUT;

        readTimeout = DEFAULT_TIMEOUT;

        autoRedirect = false;

        fullDuplex = false;

        enableCookies = true;

        restore = false;
    }

    public boolean isRestore() {
        return restore;
    }

    public void setRestore(boolean restore) {
        this.restore = restore;
    }

    public boolean isEnableCookies() {
        return enableCookies;
    }

    public void setEnableCookies(boolean enableCookies) {
        this.enableCookies = enableCookies;
    }

    public int getConnectionTimeout() {
        return connectionTimeout;
    }

    public void setConnectionTimeout(int connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public int getReadTimeout() {
        return readTimeout;
    }

    public void setReadTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
    }

    public boolean isAutoRedirect() {
        return autoRedirect;
    }

    public void setAutoRedirect(boolean autoRedirect) {
        this.autoRedirect = autoRedirect;
    }

    public boolean isFullDuplex() {
        return fullDuplex;
    }

    public void setFullDuplex(boolean fullDuplex) {
        this.fullDuplex = fullDuplex;
    }
}
