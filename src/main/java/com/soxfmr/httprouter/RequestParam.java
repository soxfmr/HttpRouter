package com.soxfmr.httprouter;

import com.soxfmr.httprouter.utils.Args;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;


public class RequestParam extends NameValueCollection {

    public RequestParam() {
        super();
    }

    public RequestParam(HashMap<String, String> paramList) {
        super(paramList);
    }

    public void setParam(String key, String value) {
        add(key, value);
    }

    public void addParam(String key, String value) {
        add(key, value);
    }

    public String getParamValue(String key) {
        return get(key);
    }

    public void removeParam(String key) {
        remove(key);
    }

    public String toString(String encoding) {
        if (isEmpty())
            return null;

        StringBuilder sBuilder = new StringBuilder();
        String k, v;
        try {
            for (NameValuePair pair : this) {
                k = pair.getName();
                if (Args.isEmpty(k)) continue;

                v = pair.getValue();
                if (Args.isEmpty(v)) v = "";

                k = URLEncoder.encode(k, encoding);
                v = URLEncoder.encode(v, encoding);

                sBuilder.append(String.format("%s=%s&", k, v));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        // Remove the & symbol on the end of string
        sBuilder.deleteCharAt( sBuilder.length() - 1 );

        return sBuilder.toString();
    }
}
