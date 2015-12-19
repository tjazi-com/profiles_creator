package com.tjazi.profilescreator.client;

import com.tjazi.profilescreator.messages.CreateBasicProfileRequestMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Krzysztof Wasiak on 02/11/2015.
 */

@Service
@EnableBinding(Source.class)
public class ProfilesCreatorClientImpl implements ProfilesCreatorClient {

    private Logger log = LoggerFactory.getLogger(ProfilesCreatorClientImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    @Output(Source.OUTPUT)
    private MessageChannel messageChannel;

    /**
     * Create basic profile
     * @param userName User name
     * @param userEmail User email
     * @param passwordHash Password MD5 hash
     */
    public void createBasicProfile(String userName, String userEmail, String passwordHash) {

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

        messageChannel.send(MessageBuilder.withPayload(requestMessage).build());
    }
}
