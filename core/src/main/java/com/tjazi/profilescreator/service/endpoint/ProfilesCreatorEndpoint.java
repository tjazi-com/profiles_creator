package com.tjazi.profilescreator.service.endpoint;

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

    public void createBasicProfile(CreateBasicProfileRequestMessage createBasicProfileRequestMessage) {

        if (createBasicProfileRequestMessage == null) {
            log.error("createBasicProfileRequestMessage is null.");
            throw new IllegalArgumentException();
        }

        log.info("Requesting profile creation for user name: {} and email: {}.");

        profilesCreator.createProfile(createBasicProfileRequestMessage);
    }
}
