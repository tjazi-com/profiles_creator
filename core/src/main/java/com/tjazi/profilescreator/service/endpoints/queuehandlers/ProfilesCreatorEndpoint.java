package com.tjazi.profilescreator.service.endpoints.queuehandlers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.tjazi.profilescreator.messages.CreateBasicProfileRequestMessage;
import com.tjazi.profilescreator.service.core.ProfilesCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by Krzysztof Wasiak on 22/10/2015.
 */

@Service
public class ProfilesCreatorEndpoint {

    private Logger log = LoggerFactory.getLogger(ProfilesCreatorEndpoint.class);

    @Autowired
    private ProfilesCreator profilesCreator;

    @HystrixCommand
    public void createBasicProfileRequestHandler(CreateBasicProfileRequestMessage createBasicProfileRequestMessage) {
        try
        {
            if (createBasicProfileRequestMessage == null) {
                log.error("createBasicProfileRequestMessage is null.");
                throw new IllegalArgumentException();
            }

            log.info("Requesting profile creation for user name: {} and email: {}.");

            profilesCreator.createProfile(createBasicProfileRequestMessage);
        }
        catch (Exception ex) {
            log.error("Exception when registering new user profile.\n" + ex.toString());
            throw ex;
        }
    }
}
