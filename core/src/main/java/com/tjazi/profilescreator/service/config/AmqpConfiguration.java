package com.tjazi.profilescreator.service.config;

import com.tjazi.profilescreator.service.endpoints.queuehandlers.ProfilesCreatorEndpoint;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.support.converter.JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Krzysztof Wasiak on 21/12/2015.
 */

@Configuration
public class AmqpConfiguration {

    @Autowired
    private ProfilesCreatorEndpoint profilesCreatorEndpoint;

    @Value("${profilescreator.inputqueuename}")
    private String queueName;

    @Value("${profilescreator.exchangename}")
    private String exchangeName;

    @Bean
    Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(exchangeName);
    }

    @Bean
    Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(queueName);
    }

    @Bean
    MessageConverter messageConverter() {
        return new JsonMessageConverter();
    }

    @Bean
    SimpleMessageListenerContainer container(
            ConnectionFactory connectionFactory, MessageConverter messageConverter, ProfilesCreatorEndpoint profilesCreatorEndpoint) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();

        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);

        MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(profilesCreatorEndpoint, messageConverter);
        messageListenerAdapter.setDefaultListenerMethod("createBasicProfileRequestHandler");
        container.setMessageListener(messageListenerAdapter);

        return container;
    }

    @Bean
    ProfilesCreatorEndpoint profilesCreatorEndpoint() {
        return new ProfilesCreatorEndpoint();
    }

    @Bean
    MessageListenerAdapter listenerAdapter() {
        return new MessageListenerAdapter(profilesCreatorEndpoint, "createBasicProfile");
    }
}
