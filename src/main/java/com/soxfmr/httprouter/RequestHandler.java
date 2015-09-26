package com.soxfmr.httprouter;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.*;

public class RequestHandler {

    private static RequestHandler mInstance = null;

    private RequestHandler() {}

    public static RequestHandler getInstance() {
        if (mInstance == null) {
            mInstance = new RequestHandler();
        }

        return mInstance;
    }

    private HttpRequest mRequest;
    private HttpResponse mResponse;
    private HttpURLConnection mConn;
    private HttpConfig mConfig;

    public HttpResponse prevHttpResponse() {
        return mResponse;
    }

    public void init(HttpRequest request, HttpConfig config) {
        init(config);
        establishConnection(request);
        initConnectionConfigure(mConn, config);
    }

    private void init(HttpConfig config) {
        // Cookies management
        boolean enableCookies = config.isEnableCookies();
        if (enableCookies) {
            CookieContainer.enable(config.isRestore());
        } else {
            CookieContainer.disable();
        }
    }

    public HttpResponse makeRequest() {
        mResponse = new HttpResponse();

        OutputStream os = null;
        try {
            if (mConfig.isFullDuplex()) {
                os = new BufferedOutputStream(mConn.getOutputStream());
                writeStream(os, mRequest.getPayload());
            }
            // Retrieve the response
            ResponseHandler responseHandler = ResponseHandler.getInstance();
            mResponse = responseHandler.retrieve(mConn);
        } catch (IOException ex) {
            ex.printStackTrace();

            if (mResponse != null) {
                mResponse.setCorrupt(true);
            }
        } finally {
            try {
                if (os != null) os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            mConn.disconnect();
        }

        return mResponse;
    }

    private void establishConnection(HttpRequest request) {
        mRequest = request;
        try {
            URI uri = new URI(request.getURI());
            URL url = new URL(request.getURI());

            if (uri.getScheme() != null && uri.getScheme().equals("HTTPS")) {
                mConn = (HttpsURLConnection) url.openConnection();
            } else {
                mConn = (HttpURLConnection) url.openConnection();
            }
            // Append the header to the request
            attachProperties(mConn, request.getHttpHeader());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    private void initConnectionConfigure(HttpURLConnection conn, HttpConfig config) {
        mConfig = config;
        // Reset the wire status
        config.setFullDuplex(mRequest.isFullDuplex());
        // Timeout
        conn.setConnectTimeout(config.getConnectionTimeout());
        conn.setReadTimeout(config.getReadTimeout());
        // Connection status
        conn.setDoInput(true);
        conn.setDoOutput(config.isFullDuplex());
        // Redirect
        conn.setInstanceFollowRedirects(config.isAutoRedirect());
    }

    private void attachProperties(HttpURLConnection conn, HttpHeader headers) {
        if (headers.size() == 0)
            return ;

        for (NameValuePair header : headers) {
            if (!header.isValid()) continue;

            conn.addRequestProperty(header.getName(), header.getValue());
        }
    }

    private void writeStream(OutputStream os, byte[] data) throws IOException {
        if (data != null) {
            os.write(data, 0, data.length);
            os.flush();
        }
    }
}
