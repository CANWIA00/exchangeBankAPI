package com.canwia.BankExchange.config;



import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;



@Configuration
public class RabbitMqConfig {

    @Value("${sample.rabbitmq.exchange}")
    String exchange;
    @Value("firstStepQueue")
    String queueName;
    @Value("sample.routingKey")
    String routingKey;


    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Queue firstStepQueue() {
        return new Queue(queueName, false);
    }

    @Bean
    public Queue secondStepQueue() {
        return new Queue("secondStepQueue", true);
    }
    @Bean
    public Queue thirdStepQueue() {
        return new Queue("thirdStepQueue", true);
    }


    @Bean
    Binding firstStepBinding(Queue firstStepQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(firstStepQueue).to(directExchange).with(routingKey);
    }

    @Bean
    Binding secondStepBinding(Queue secondStepQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(secondStepQueue).to(directExchange).with("secondRoute");
    }

    @Bean
    Binding thirdStepBinding(Queue thirdStepQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(thirdStepQueue).to(directExchange).with("thirdRoute");
    }


    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
