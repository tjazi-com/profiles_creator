package com.tjazi.profilescreator.service.core;

import com.tjazi.profilescreator.messages.CreateBasicProfileRequestMessage;
import com.tjazi.profilescreator.messages.CreateBasicProfileResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Created by Krzysztof Wasiak on 22/10/2015.
 */

@Service
public class ProfilesCreatorImpl implements ProfilesCreator {

    private Logger log = LoggerFactory.getLogger(ProfilesCreatorImpl.class);

    @Override
    public CreateBasicProfileResponseMessage createProfile(CreateBasicProfileRequestMessage requestMessage) {

        if (requestMessage == null) {

            String errorMessage = "requestMessage is null";

            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        return null;
    }
}
