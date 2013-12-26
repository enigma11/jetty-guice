package com.gdiama.server;

import com.gdiama.websocket.WebsocketEndpoint;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceServletContextListener;
import com.sun.jersey.spi.container.servlet.ServletContainer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.websocket.jsr356.server.ServerContainer;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

public class JettyServer {

    private static Server server;
    private int port;
    private GuiceServletContextListener guiceConfig;

    public JettyServer(int port, GuiceServletContextListener guiceConfig) {
        this.port = port;
        this.guiceConfig = guiceConfig;
    }

    public boolean shutdown() throws Exception {
        try {
            server.stop();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void start() throws Exception {
        server = new Server(port);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
        context.addFilter(GuiceFilter.class, "/*", null);
        context.addEventListener(guiceConfig);

//        ServletHolder servletHolder = new ServletHolder(ServletContainer.class);
//        servletHolder.setInitParameter("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
//        servletHolder.setInitParameter("com.sun.jersey.config.property.packages", "com.gdiama");
//        servletHolder.setInitParameter("com.sun.jersey.config.feature.Debug", "true");
//        servletHolder.setInitParameter("com.sun.jersey.config.feature.Trace", "true");
//        servletHolder.setInitParameter("com.sun.jersey.spi.container.ContainerRequestFilters", "com.sun.jersey.api.container.filter.LoggingFilter");
//        servletHolder.setInitParameter("com.sun.jersey.spi.container.ContainerResponseFilters", "com.sun.jersey.api.container.filter.LoggingFilter");
//        context.addServlet(servletHolder, "/rest/*");


        context.setServer(server);
        ServerContainer wscontainer = WebSocketServerContainerInitializer.configureContext(context);
        wscontainer.addEndpoint(WebsocketEndpoint.class);

        server.setHandler(context);
        server.start();
    }

    public void join() throws InterruptedException {
        server.join();
    }
}