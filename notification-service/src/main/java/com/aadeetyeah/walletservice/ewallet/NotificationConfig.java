package com.aadeetyeah.walletservice.ewallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class NotificationConfig {
    Properties getKafkaProps(){
        Properties properties       =   new Properties();

        //Consumer Properties
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,StringSerializer.class);
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,StringSerializer.class);
        return properties;
    }



    ConsumerFactory<String,String> getConsumerFactory(){
        return new DefaultKafkaConsumerFactory(getKafkaProps());
    }

    @Bean
    ConcurrentKafkaListenerContainerFactory<String,String> getConcurrentKafkaListenerFactory(){
        ConcurrentKafkaListenerContainerFactory concurrentKafkaListenerContainerFactory     =
                new ConcurrentKafkaListenerContainerFactory();

        concurrentKafkaListenerContainerFactory.setConsumerFactory(getConsumerFactory());

        return concurrentKafkaListenerContainerFactory;
    }

    @Bean
    SimpleMailMessage getMailMessage(){
        return new SimpleMailMessage();
    }

    @Bean
    JavaMailSender  getMailSender(){
        //mail server : smtp host(Simple mail transfer protocol
        JavaMailSenderImpl javaMailSender=new JavaMailSenderImpl();
        javaMailSender.setHost("smtp.gmail.com");
        javaMailSender.setPort(587);
        javaMailSender.setUsername("---your gmail address---");
        javaMailSender.setPassword("---your gmail password---");

        Properties properties=javaMailSender.getJavaMailProperties();
        properties.put("mail.smtp.starttls.enable",true);
        properties.put("mail.debug",true);

        return javaMailSender;
    }

    @Bean
    ObjectMapper getObjectMapper(){
        return  new ObjectMapper();
    }
}
