package com.soxfmr.httprouter.service;

import com.soxfmr.httprouter.HttpRequest;
import com.soxfmr.httprouter.HttpRouter;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RouterService {
    // To dispatch a new service
    private DispatcherImpl dispatcher;
    // Route list which will be forward
    private List<DispatchRoute> routeList = null;

    private ExecutorService executorService;

    public RouterService(HttpRouter httpRouter) {
        routeList = new ArrayList<>();

        executorService = Executors.newSingleThreadExecutor();
    }

    /**
     * Register a dispatcher with a middleware listener
     * @param dispatcher A dispatcher to handle the routes
     */
    public void register(DispatcherImpl dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void addRoute(HttpRequest request, int requestCode) {
        routeList.add(dispatcher.newDispatchRoute(request, requestCode));
    }

    public void execute() {
        if (routeList.size() == 0)
            return ;

        try {
            DispatchRoute dispatchRoute;
            for (int i = 0, size = routeList.size(); i < size; i++) {
                dispatchRoute = routeList.get(i);
                executorService.execute(dispatchRoute);
            }
        } finally {
            // Remove all of service task
            routeList.clear();
        }
    }

    public void release() {
        executorService.shutdown();
    }

}
