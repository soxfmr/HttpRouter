package com.soxfmr.httprouter.utils;

import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EncodingUtils {

    private EncodingUtils() {}

    private static final String CHARSET = "charset";
    private static final String CONTENT_TYPE_HTML = "text/html";
    // "charset=".length()
    private static final int CHARSET_OFFSET = 8;

    private static final String ENCODING_PATTERN = "(.*)charset=(\"*)(.+)\"[\\s/]*>";

    public static final String UTF8 = "UTF-8";
    public static final String GB2312 = "GB2312";

    public static String decode(byte[] bytes, String encoding) {
        String Ret = null;
        try {
            Ret = new String(bytes, encoding);
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
        }
        return Ret;
    }

    public static String guessingEncoding(String contentType, byte[] data) {
        if (Args.isEmpty(contentType))
            return null;
        // Try to give the charset from content-type field
        String encoding = extractEncoding(contentType);
        if (encoding != null)
            return encoding;
        // Return null if the special content isn't html
        if (! contentType.contains(CONTENT_TYPE_HTML) || data == null) {
            return null;
        }
        try {
            // Encoding with default charset
            String buffer = new String(data, UTF8);

            Pattern pattern = Pattern.compile(ENCODING_PATTERN);
            Matcher matcher = pattern.matcher(buffer);
            if (matcher.find()) {
                String wrapper = matcher.group();
                encoding = extractEncoding(wrapper);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return encoding;
    }

    public static String extractEncoding(String contentType) {
        String Ret = null;

        // Remove all of noises
        contentType = contentType.replaceAll("\"", "");
        contentType = contentType.replaceAll(" ", "");
        contentType = contentType.replaceAll("/", "");

        int offset, endOffset;
        try {
            if ((endOffset = contentType.indexOf(">")) != -1 && (offset = contentType.indexOf(CHARSET)) != -1 ) {
                Ret = contentType.substring(offset + CHARSET_OFFSET, endOffset);
            } else if((offset = contentType.indexOf(CHARSET)) != -1) {
                Ret = contentType.substring(offset + CHARSET_OFFSET, contentType.length());
            }
        } catch (IndexOutOfBoundsException ex) {
            ex.printStackTrace();
        }
        return Ret;
    }

}
