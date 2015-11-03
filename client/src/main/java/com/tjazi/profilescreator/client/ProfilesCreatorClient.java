package com.tjazi.profilescreator.client;

import com.tjazi.profilescreator.messages.CreateBasicProfileResponseMessage;

/**
 * Created by Krzysztof Wasiak on 02/11/2015.
 */
public interface ProfilesCreatorClient {

    CreateBasicProfileResponseMessage createBasicProfile(String userName, String userEmail, String passwordHash);
}
