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

        this.assertInput(requestMessage);

        UUID newProfileUuid = this.createNewProfileUuid();

        // register profile, but skip name and surname
        // those are not needed for basic profile
        boolean profileRegistrationResult = profilesClient.registerNewProfile(
                newProfileUuid, requestMessage.getUserName(), requestMessage.getUserEmail(), null, null);

        if (!profileRegistrationResult) {
            log.error("Profile UUID: {}, got 'false' when calling profilesClient::registerNewProfile()", newProfileUuid);

            return;
        }

        // second call - register profile with user name and password
        if (!securityClient.registerNewUserCredentials(newProfileUuid, requestMessage.getPasswordHash())) {
            log.error("Profile UUID: {}, got 'false' when calling securityClient::registerNewUserCredentials()");

            /*
                TODO: At this stage we have user profile, which doesn't have any security profile. This has to be fixed somehow...
            */
        }

        log.info("Profile UUID: {}, got 'true' from profiles and security. PROFILE REGISTRATION COMPLETE.");
    }

    private UUID createNewProfileUuid() {
        return UUID.randomUUID();
    }

    private void assertInput(CreateBasicProfileRequestMessage requestInput) {

        if (requestInput == null) {

            String errorMessage = "requestMessage is null";

            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        if (requestInput.getUserName() == null || requestInput.getUserName().isEmpty()) {

            String errorMessage = "requestMessage::UserName is null or empty";

            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        if (requestInput.getUserEmail() == null || requestInput.getUserEmail().isEmpty()) {

            String errorMessage = "requestMessage::UserEmail is null or empty";

            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        if (requestInput.getPasswordHash() == null || requestInput.getPasswordHash().isEmpty()) {

            String errorMessage = "requestMessage::PasswordHash is null or empty";

            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
