package com.soxfmr.httprouter;

import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class CookieContainer {

    private static CookieManager cookieManager;

    private static CookieStore cookieStore;
    private static List<CookieBucket> cookiesBackup;

    static {
        cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);

        cookieStore = cookieManager.getCookieStore();
        // Initialized the backup for cookies exist
        cookiesBackup = new ArrayList<>();
    }

    public static void addCookie(URI uri, HttpCookie httpCookie) {
        cookieStore.add(uri, httpCookie);
    }

    public static void enable() {
        enable(false);
    }

    public static void disable() {
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_NONE);
        CookieHandler.setDefault(cookieManager);
        // Backup all of cookies before remove
        removeAll(true);
    }

    public static void removeAll() {
        removeAll(false);
    }

    public static List<HttpCookie> getCookies() {
        return cookieStore.getCookies();
    }

    public static List<HttpCookie> getCookies(URI uri) {
        return cookieStore.get(uri);
    }

    public static HttpCookie getCookie(String name) {
        List<HttpCookie> cookies = cookieStore.getCookies();
        if (cookies == null || cookies.isEmpty())
            return null;

        HttpCookie httpCookie = null;
        for (HttpCookie cookie : cookies) {
            if (cookie.getName().equals( name )) {
                httpCookie = cookie;
                break;
            }
        }

        return httpCookie;
    }

    public static void enable(boolean restore) {
        if (restore) {
            restore();
        }
        // Accept cookies store
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
    }

    private static void restore() {
        if (cookiesBackup.size() == 0)
            return ;
        // Restore the cookies from backup instance
        for (CookieBucket cookieBucket : cookiesBackup) {
            List<HttpCookie> cookieList = cookieBucket.getHttpCookieList();
            for (HttpCookie cookie : cookieList) {
                cookieStore.add(cookieBucket.getUri(), cookie);
            }
        }
    }

    /**
     * Store all of store which is exist
     */
    private static void store() {
        cookiesBackup.clear();

        CookieBucket cookieBucket;
        List<HttpCookie> cookieList;
        List<URI> uriList = cookieStore.getURIs();

        for (URI uri : uriList) {
            cookieList = cookieStore.get(uri);
            if (cookieList == null || cookieList.size() == 0)
                continue;

            cookieBucket = new CookieBucket(uri);
            cookieBucket.pushAll(cookieList);
            // Backup
            cookiesBackup.add(cookieBucket);
        }
    }

    private static void removeAll(boolean bBackup) {
        if (bBackup) {
            store();
        }

        cookieStore.removeAll();
    }
}
