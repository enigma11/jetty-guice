package com.gdiama.server;

import com.codahale.metrics.JmxReporter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Slf4jReporter;
import com.codahale.metrics.servlets.MetricsServlet;
import com.gdiama.config.AppConfig;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import org.aeonbits.owner.ConfigFactory;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class MainModule extends JerseyServletModule {

    private final String packageName;

    public MainModule(String packageName) {
        this.packageName = packageName;
    }

    @Override
    protected void configureServlets() {
        bind(MetricsServlet.class).asEagerSingleton();

        Map<String, String> parameters = new HashMap<>();
        parameters.put(JSONConfiguration.FEATURE_POJO_MAPPING, "true");
        parameters.put("com.sun.jersey.config.property.resourceConfigClass", "com.sun.jersey.api.core.PackagesResourceConfig");
        parameters.put("com.sun.jersey.config.property.packages", packageName);
        parameters.put("jersey.config.beanValidation.enableOutputValidationErrorEntity.server", "true");
        parameters.put("com.sun.jersey.api.json.POJOMappingFeature", "true");

        serve("/metrics").with(MetricsServlet.class);
        serve("/*").with(GuiceContainer.class, parameters);
    }

    @Provides
    @Singleton
    AppConfig appConfig() {
        return ConfigFactory.create(AppConfig.class);
    }

    @Provides
    @Singleton
    MetricRegistry metricRegistry() {
        return new MetricRegistry();
    }

    @Provides
    @Singleton
    public JmxReporter jmxReporter(MetricRegistry metricRegistry) {
        JmxReporter jmxReporter = JmxReporter.forRegistry(metricRegistry).build();
        jmxReporter.start();
        return jmxReporter;
    }

    @Provides
    @Singleton
    public Slf4jReporter slf4jReporter(MetricRegistry registry) {
        final Slf4jReporter reporter = Slf4jReporter.forRegistry(registry)
                .outputTo(LoggerFactory.getLogger("metrics"))
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(1, TimeUnit.MINUTES);
        return reporter;
    }
}