package com.aadeetyeah.walletservice.ewallet;

import org.apache.commons.logging.Log;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class WalletService {

    private static  Logger logger=Logger.getLogger(String.valueOf(WalletService.class));


    private final static String USER_CREATE_TOPIC=CommonConstants.USER_CREATE_TOPIC;

    @Autowired
    WalletRepository walletRepository;

    @Value("${amount.wallet.create}")
    private Double amount;

    @KafkaListener(topics = {USER_CREATE_TOPIC},groupId = "ewallet_grp")
    public void createWallet(String msg){
        //This method will be invoked everytime we create a new user or
        //We can consider this as a consumer of user_create topic
        JSONObject jsonObject=null;
        try {
            JSONParser jsonParser = new JSONParser();
             jsonObject = (JSONObject) jsonParser.parse(msg);
        }catch (ParseException parseException){
            logger.warning("Parsing of user object failed.");
        }
        String email=(String)jsonObject.get(CommonConstants.EMAIL_ATTRIBUTE);
        String phoneNo=(String) jsonObject.get(CommonConstants.PHONE_ATTRIBUTE);
        Wallet wallet=Wallet.builder().email(email).phone(phoneNo).build();
        wallet.setBalance(amount);
        walletRepository.save(wallet);
        logger.info("Wallet created successfully!!");
    }
}
