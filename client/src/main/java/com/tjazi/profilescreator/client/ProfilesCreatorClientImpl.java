package com.tjazi.profilescreator.client;

import com.tjazi.profilescreator.messages.CreateBasicProfileRequestMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by Krzysztof Wasiak on 02/11/2015.
 */

@Service
public class ProfilesCreatorClientImpl implements ProfilesCreatorClient {

    private Logger log = LoggerFactory.getLogger(ProfilesCreatorClientImpl.class);

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Value("${profilescreator.inputqueuename}")
    private String queueName;

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

        rabbitTemplate.convertAndSend(queueName, requestMessage);
    }
}
