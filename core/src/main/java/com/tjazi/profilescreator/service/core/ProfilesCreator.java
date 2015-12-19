package com.tjazi.profilescreator.service.core;

import com.tjazi.profilescreator.messages.CreateBasicProfileRequestMessage;
import com.tjazi.profilescreator.messages.CreateBasicProfileResponseMessage;

/**
 * Created by kr329462 on 22/10/2015.
 */
public interface ProfilesCreator {

    void createProfile(CreateBasicProfileRequestMessage requestMessage);
}
