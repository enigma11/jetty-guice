package com.gdiama.server;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.servlets.MetricsServlet;
import com.gdiama.websocket.WebsocketEndpoint;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Stage;
import com.google.inject.servlet.GuiceServletContextListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;

public class ApplicationContextListener extends GuiceServletContextListener {

    private String[] packageName;
    private Injector injector;

    public ApplicationContextListener(String... packageName) {
        this.packageName = packageName;
    }

    @Override
    protected Injector getInjector() {
        if(injector == null) {
            injector = Guice.createInjector(Stage.PRODUCTION, new MainModule(packageName));
        }
        return injector;
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);

        ServletContext servletContext = servletContextEvent.getServletContext();
        Injector injector = (Injector) servletContext.getAttribute(Injector.class.getName());

        servletContext.setAttribute(
                MetricsServlet.METRICS_REGISTRY,
                injector.getInstance(MetricRegistry.class));

//        ShutdownService shutdownService = injector.getInstance(ShutdownService.class);
//        Runtime.getRuntime()
//                .addShutdownHook(
//                        new ShutdownThread(shutdownService));
    }
}
