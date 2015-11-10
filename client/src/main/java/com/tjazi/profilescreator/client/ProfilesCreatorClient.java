package com.tjazi.profilescreator.client;

import com.tjazi.profilescreator.messages.CreateBasicProfileResponseMessage;

/**
 * Created by Krzysztof Wasiak on 02/11/2015.
 */
public interface ProfilesCreatorClient {

    /**
     * Create basic profile
     * @param userName User name
     * @param userEmail User email
     * @param passwordHash Password MD5 hash
     * @return Profile creation status
     */
    CreateBasicProfileResponseMessage createBasicProfile(String userName, String userEmail, String passwordHash);
}
