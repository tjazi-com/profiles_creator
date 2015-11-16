package com.tjazi.profilescreator.service.core;

import com.tjazi.profiles.client.ProfilesClient;
import com.tjazi.profiles.messages.RegisterNewProfileResponseMessage;
import com.tjazi.profiles.messages.RegisterNewProfileResponseStatus;
import com.tjazi.profilescreator.messages.CreateBasicProfileRequestMessage;
import com.tjazi.profilescreator.messages.CreateBasicProfileResponseMessage;
import com.tjazi.profilescreator.messages.CreateBasicProfileResponseStatus;
import com.tjazi.security.client.SecurityClient;
import com.tjazi.security.messages.RegisterNewUserCredentialsResponseMessage;
import com.tjazi.security.messages.RegisterNewUserCredentialsResponseStatus;
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
    public CreateBasicProfileResponseMessage createProfile(CreateBasicProfileRequestMessage requestMessage) {

        if (requestMessage == null) {

            String errorMessage = "requestMessage is null";

            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        // register profile, but skip name and surname
        // those are not needed for basic profile
        RegisterNewProfileResponseMessage registerNewProfileResponseMessage =
                profilesClient.registerNewProfile(requestMessage.getUserName(), requestMessage.getUserEmail(), null, null);

        CreateBasicProfileResponseMessage profilesResponseMessage =
                this.findProfileRegistrationResponseErrors(registerNewProfileResponseMessage);

        if (profilesResponseMessage != null) {
            return profilesResponseMessage;
        }

        final UUID newUserProfileUuid = registerNewProfileResponseMessage.getNewProfileUuid();

        RegisterNewUserCredentialsResponseMessage securityRegistrationResponseMessage =
                securityClient.registerNewUserCredentials(newUserProfileUuid, requestMessage.getPasswordHash());

        CreateBasicProfileResponseMessage securityResponseMessage =
                this.findSecurityProfileRegistrationErrors(securityRegistrationResponseMessage, newUserProfileUuid);

        if (securityResponseMessage != null) {
            return securityResponseMessage;
        }

        CreateBasicProfileResponseMessage responseMessage = new CreateBasicProfileResponseMessage();
        responseMessage.setResponseStatus(CreateBasicProfileResponseStatus.OK);
        responseMessage.setCreatedProfileUuid(newUserProfileUuid);

        return responseMessage;
    }

    private CreateBasicProfileResponseMessage findProfileRegistrationResponseErrors(
            RegisterNewProfileResponseMessage registerNewProfileResponseMessage) {

        CreateBasicProfileResponseMessage responseMessage = new CreateBasicProfileResponseMessage();

        if (registerNewProfileResponseMessage == null) {
            responseMessage.setResponseStatus(CreateBasicProfileResponseStatus.GENERAL_ERROR);
            return responseMessage;
        }

        RegisterNewProfileResponseStatus registrationResponseStatus = registerNewProfileResponseMessage.getRegisterNewProfileResponseStatus();

        if (registrationResponseStatus == RegisterNewProfileResponseStatus.USER_EMAIL_ALREADY_REGISTERED_WITH_DIFFERENT_USER) {
            responseMessage.setResponseStatus(CreateBasicProfileResponseStatus.USER_EMAIL_ALREADY_REGISTERED);
            return responseMessage;
        }

        if (registrationResponseStatus == RegisterNewProfileResponseStatus.USER_NAME_ALREADY_REGISTERED) {
            responseMessage.setResponseStatus(CreateBasicProfileResponseStatus.USER_NAME_ALREADY_REGISTERED);
            return responseMessage;
        }

        if (registrationResponseStatus == RegisterNewProfileResponseStatus.GENERAL_REGISTRATION_ERROR) {
            responseMessage.setResponseStatus(CreateBasicProfileResponseStatus.GENERAL_ERROR);
            return responseMessage;
        }

        // all good, no error to return...
        return null;
    }

     private CreateBasicProfileResponseMessage findSecurityProfileRegistrationErrors(
             RegisterNewUserCredentialsResponseMessage securityRegistrationResponseMessage, UUID newUserProfileUuid) {

         CreateBasicProfileResponseMessage responseMessage = new CreateBasicProfileResponseMessage();

         // at this stage we have profile UUID, because it was created earlier so we can do some decoration
         // of all outgoing messages
         responseMessage.setCreatedProfileUuid(newUserProfileUuid);

         if (securityRegistrationResponseMessage == null) {
             responseMessage.setResponseStatus(CreateBasicProfileResponseStatus.GENERAL_ERROR);
             return responseMessage;
         }

         if (securityRegistrationResponseMessage.getRegistrationStatus() == RegisterNewUserCredentialsResponseStatus.GENERAL_ERROR) {
             responseMessage.setResponseStatus(CreateBasicProfileResponseStatus.GENERAL_ERROR);
             return responseMessage;
         }

         // all good, no error to return...
         return null;
     }
}
