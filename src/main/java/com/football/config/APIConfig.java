package com.football.config;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.ConfigCache;

/**
 * Main API config which loads properties / env vars from different sources.
 */
@Config.LoadPolicy(Config.LoadType.MERGE)
public interface APIConfig extends Config {

    APIConfig CONFIG = ConfigCache.getOrCreate(APIConfig.class, System.getenv(), System.getProperties());

    @DefaultValue("http://3.68.165.45")
    String apiUrl();

    @DefaultValue("6")
    int randomCharactersAmount();

    @DefaultValue("17")
    int minAge();

    @DefaultValue("59")
    int maxAge();
}
