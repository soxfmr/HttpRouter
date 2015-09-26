package com.soxfmr.httprouter;

import com.soxfmr.httprouter.thirdparty.HttpStatus;
import com.soxfmr.httprouter.utils.Args;
import com.soxfmr.httprouter.utils.EncodingUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ResponseHandler {

    private static ResponseHandler mInstance = null;

    private ResponseHandler() {}

    public static ResponseHandler getInstance() {
        if (mInstance == null) {
            mInstance = new ResponseHandler();
        }

        return mInstance;
    }

    public HttpResponse retrieve(HttpURLConnection conn) throws IOException {
        if (conn == null)
            return null;

        InputStream is = null;
        HttpResponse httpResponse = new HttpResponse();

        int statusCode;
        byte[] buffer;
        String encoding;

        HttpHeader headers;

        try {
            // Read the response data and handle the error stream
            statusCode = conn.getResponseCode();
            is = new BufferedInputStream(statusCode >= HttpStatus.SC_BAD_REQUEST ?
                    conn.getErrorStream() : conn.getInputStream());

            buffer = readStream(is);
            // Try to give the charset from the response content
            encoding = EncodingUtils.guessingEncoding(conn.getContentType(), buffer);

            headers = fetchResponseHeaders(conn.getHeaderFields());

            httpResponse.setStatusCode(statusCode);
            httpResponse.setData(buffer);
            httpResponse.setResponseHeaders(headers);
            httpResponse.setResponseEncoding(encoding);
        } finally {
            if (is != null) is.close();
        }

        return httpResponse;
    }

    private byte[] readStream(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        int len;
        byte[] bytes = new byte[512];
        while ((len = is.read(bytes)) != -1) {
            baos.write(bytes, 0, len);
        }

        return baos.toByteArray();
    }


    private HttpHeader fetchResponseHeaders(Map<String, List<String>> headerList) {
        if (headerList == null || headerList.size() == 0)
            return null;

        HttpHeader headers = new HttpHeader();
        Iterator<String> iterator = headerList.keySet().iterator();

        String name, value;
        List<String> fields;
        while (iterator.hasNext()) {
            name = iterator.next();
            // Skip after the status code line
            if (Args.isEmpty(name)) continue;

            value = "";
            fields = headerList.get(name);
            if (fields != null) {
                for (String field : fields) {
                    value += field + ";";
                }
                value = value.substring(0, value.length() - 1);
            }

            headers.addHeader(name, value);
        }

        return headers;
    }

}
