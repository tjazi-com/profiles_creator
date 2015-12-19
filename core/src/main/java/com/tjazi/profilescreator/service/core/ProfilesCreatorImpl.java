package com.tjazi.profilescreator.service.core;

import com.tjazi.profiles.client.ProfilesClient;
import com.tjazi.profilescreator.messages.CreateBasicProfileRequestMessage;
import com.tjazi.security.client.SecurityClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by Krzysztof Wasiak on 22/10/2015.
 */

@Service
public class ProfilesCreatorImpl implements ProfilesCreator {

    private Logger log = LoggerFactory.getLogger(ProfilesCreatorImpl.class);

    @Autowired
    private SecurityClient securityClient;

    @Autowired
    private ProfilesClient profilesClient;

    @Override
    public void createProfile(CreateBasicProfileRequestMessage requestMessage) {

        if (requestMessage == null) {

            String errorMessage = "requestMessage is null";

            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        UUID newProfileUuid = this.createNewProfileUuid();

        // register profile, but skip name and surname
        // those are not needed for basic profile
        profilesClient.registerNewProfile(
                newProfileUuid, requestMessage.getUserName(), requestMessage.getUserEmail(), null, null);

        // second call - register profile with user name and password
        securityClient.registerNewUserCredentials(newProfileUuid, requestMessage.getPasswordHash());
    }

    private UUID createNewProfileUuid() {
        return UUID.randomUUID();
    }
}
