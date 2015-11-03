package com.tjazi.profilescreator.client;

import com.tjazi.lib.messaging.rest.RestClient;
import com.tjazi.profilescreator.messages.CreateBasicProfileRequestMessage;
import com.tjazi.profilescreator.messages.CreateBasicProfileResponseMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Krzysztof Wasiak on 02/11/2015.
 */
public class ProfilesCreatorClientImpl implements ProfilesCreatorClient {

    private RestClient restClient;

    private Logger log = LoggerFactory.getLogger(ProfilesCreatorClientImpl.class);

    public ProfilesCreatorClientImpl(RestClient restClient) {
        if (restClient == null) {
            String errorMessage = "restClient constructor parameter is null";

            log.error(errorMessage);
            throw new IllegalArgumentException(errorMessage);
        }

        this.restClient = restClient;
    }

    public CreateBasicProfileResponseMessage createBasicProfile(String userName, String userEmail, String passwordHash) {

        if (userName == null || userName.isEmpty()) {
            throw new IllegalArgumentException("userName parameter is null or empty");
        }

        if (userEmail == null || userEmail.isEmpty()) {
            throw new IllegalArgumentException("userEmail parameter is null or empty");
        }

        if (passwordHash == null || passwordHash.isEmpty()) {
            throw new IllegalArgumentException("passwordHash parameter is null or empty");
        }

        CreateBasicProfileRequestMessage requestMessage = new CreateBasicProfileRequestMessage();
        requestMessage.setUserName(userName);
        requestMessage.setUserEmail(userEmail);
        requestMessage.setPasswordHash(passwordHash);

        return (CreateBasicProfileResponseMessage) restClient.sendRequestGetResponse(requestMessage, CreateBasicProfileResponseMessage.class);
    }
}
