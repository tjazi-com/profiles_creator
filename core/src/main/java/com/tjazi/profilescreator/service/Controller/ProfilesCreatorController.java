package com.tjazi.profilescreator.service.controller;

import com.tjazi.profilescreator.messages.CreateBasicProfileRequestMessage;
import com.tjazi.profilescreator.messages.CreateBasicProfileResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Krzysztof Wasiak on 22/10/2015.
 */

@RestController
@RequestMapping(value = "/profilescreator")
public class ProfilesCreatorController {

    private Logger log = LoggerFactory.getLogger(ProfilesCreatorController.class);

    @RequestMapping(value = "/createprofile", method = RequestMethod.POST)
    public CreateBasicProfileResponseMessage createBasicProfile(
            CreateBasicProfileRequestMessage createBasicProfileRequestMessage) {

        if (createBasicProfileRequestMessage == null) {
            log.error("createBasicProfileRequestMessage is null.");
            throw new IllegalArgumentException();
        }

        log.info("Requesting profile creation for user name: {} and email: {}.");

        return null;
    }
}
