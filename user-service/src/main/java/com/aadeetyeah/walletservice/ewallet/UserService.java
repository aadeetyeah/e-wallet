package com.aadeetyeah.walletservice.ewallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

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

    }
}
