package com.tjazi.profilescreator.service.config;

import com.tjazi.lib.messaging.rest.RestClient;
import com.tjazi.lib.messaging.rest.RestClientImpl;
import com.tjazi.profiles.client.ProfilesClient;
import com.tjazi.profiles.client.ProfilesClientImpl;
import com.tjazi.security.client.SecurityClient;
import com.tjazi.security.client.SecurityClientImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.env.Environment;

/**
 * Created by Krzysztof Wasiak on 12/11/2015.
 */

@EnableBinding(Sink.class)
@Configuration
public class ExternalServiceWiringConfig {

    @Autowired
    private Environment environment;

    @Bean(name = "profilesClient")
    @Scope(value = "prototype")
    public ProfilesClient getProfilesClientFactory() {
        String profilesServiceUrl = environment.getProperty("com.tjazi.profiles.service.rooturl");

        return new ProfilesClientImpl();
    }

    @Bean(name = "securityClient")
    @Scope(value = "prototype")
    public SecurityClient getSecurityClientFactory() {
        String securityServiceUrl = environment.getProperty("com.tjazi.security.service.rooturl");

        return new SecurityClientImpl();
    }
}
