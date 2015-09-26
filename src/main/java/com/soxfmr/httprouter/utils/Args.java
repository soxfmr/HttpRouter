package com.soxfmr.httprouter.utils;

public class Args {

    private Args() {}

    public static boolean isEmpty(String arg) {
        return arg == null || arg.length() == 0;
    }

}
