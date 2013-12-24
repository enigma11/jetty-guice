package com.gdiama.server;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;

public class AppExec {

    private static Logger LOGGER = LoggerFactory.getLogger(AppExec.class);

    public static void main(String[] args) throws Exception {
        Integer port = getPort();

        LOGGER.info(formatStartupMessage(args, port));

        JettyServerWrapper server = new JettyServerWrapper(port);
        server.start();
        server.join();
    }

    private static String formatStartupMessage(String[] args, Integer port) {
        String startupMessage = getStartupMessage(args, port);
        String division = StringUtils.repeat("*", startupMessage.length());

        StringBuilder message = new StringBuilder();
        message.append(System.lineSeparator());
        message.append(division);
        message.append(System.lineSeparator());
        message.append(startupMessage);
        message.append(System.lineSeparator());
        message.append(division);
        message.append(System.lineSeparator());

        return message.toString();
    }

    private static String getStartupMessage(String[] args, Integer port) {
        return format("Starting jetty on port=%s, args=%s", port, formatArguments(args));
    }

    private static String formatArguments(String[] args) {
        if (args.length > 0) {
            return StringUtils.join(args, ",");
        }
        return "[]";
    }

    private static Integer getPort() {
        String port = System.getenv("PORT");
        if (port == null || port.isEmpty()) {
            port = "9991";
        }

        LOGGER.info("Listening on port {}", port);
        return Integer.valueOf(port);
    }


}