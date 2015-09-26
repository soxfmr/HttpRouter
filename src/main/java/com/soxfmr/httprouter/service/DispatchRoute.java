package com.soxfmr.httprouter.service;

public abstract class DispatchRoute implements Runnable {
    /**
     * To interrupt the package for modify and forward it to the special routing
     */
    public abstract void dispatch();

    @Override
    public void run() {
        dispatch();
    }
}
