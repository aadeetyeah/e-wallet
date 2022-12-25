package com.aadeetyeah.walletservice.ewallet;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    private static final String USER_CREATE_TOPIC=CommonConstants.USER_CREATE_TOPIC;

    @Autowired
    UserRepository userRepository;

    public User getUser(int id){
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByMail(String email){
        return userRepository.findByEmail(email);
    }

    public void createUser(User user){
        userRepository.save(user);

        //Publish to Kafka Event so that wallet service can consume and add some
        //default money to user's account
        JSONObject jsonObject=new JSONObject();
        jsonObject.put(CommonConstants.EMAIL_ATTRIBUTE,user.getEmail());
        jsonObject.put(CommonConstants.PHONE_ATTRIBUTE,user.getPhoneNo());
        kafkaTemplate.send(USER_CREATE_TOPIC,jsonObject.toString());
    }
}
