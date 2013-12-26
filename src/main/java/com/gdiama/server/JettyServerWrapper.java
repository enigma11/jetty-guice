package com.gdiama.server;

import com.google.inject.servlet.GuiceServletContextListener;

public class JettyServerWrapper {

    private static final String[] PACKAGE_NAMES = {"com.gdiama.resources"};
    private final GuiceServletContextListener guiceConfig;
    private final int port;

    private JettyServer instance;

    public JettyServerWrapper(int port) {
        this.port = port;
        this.guiceConfig = new ApplicationContextListener(PACKAGE_NAMES);
    }

    public void start() throws Exception {
        instance = new JettyServer(port, guiceConfig);
        instance.start();
    }

    public void join() throws InterruptedException {
        if (instance == null) {
            throw new IllegalArgumentException("Cannot join before starting server. Call start first!");
        }
        instance.join();
    }

    public void stop() throws Exception {
        if (instance != null) {
            instance.shutdown();
        }
    }
}
