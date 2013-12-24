package com.gdiama.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(value = Config.LoadType.FIRST)
@Config.Sources({
        "file:${application.configurationFile}"
})
public interface AppConfig extends Config {
}
