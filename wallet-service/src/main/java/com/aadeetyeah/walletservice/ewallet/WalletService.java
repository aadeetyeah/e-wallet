package com.aadeetyeah.walletservice.ewallet;

import org.apache.commons.logging.Log;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
public class WalletService {

    private static  Logger logger=Logger.getLogger(String.valueOf(WalletService.class));

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

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

    @KafkaListener(topics = {CommonConstants.TRANSACTION_CREATE_TOPIC},groupId="ewallet_grp")
    public void updateWallet(String msg){
        JSONParser jsonParser           =   new JSONParser();
        JSONObject updateWalletRequest  =   null;

        try {
            updateWalletRequest = (JSONObject) jsonParser.parse(msg);
        }catch (ParseException parseException){
            logger.warning("Empty or null transaction cannot update wallet.");
        }

        String trxId    = (String) updateWalletRequest.get(CommonConstants.TRANSACTION_ID_ATTRIBUTE);
        String sender   = (String) updateWalletRequest.get(CommonConstants.SENDER_ATTRIBUTE);
        String receiver = (String) updateWalletRequest.get(CommonConstants.RECEIVER_ATTRIBUTE);
        Double amount   = (Double) updateWalletRequest.get(CommonConstants.AMOUNT_ATTRIBUTE);

        Wallet senderWallet     =   walletRepository.findByEmail(sender);

        JSONObject trxStatusObj =   new JSONObject();

        trxStatusObj.put(CommonConstants.TRANSACTION_ID_ATTRIBUTE,trxId);
        trxStatusObj.put(CommonConstants.WALLET_UPDATE_STATUS_ATTRIBUTE,CommonConstants.WALLET_UPDATE_FAILED_STATUS);
        trxStatusObj.put(CommonConstants.SENDER_ATTRIBUTE,sender);
        trxStatusObj.put(CommonConstants.RECEIVER_ATTRIBUTE,receiver);
        trxStatusObj.put(CommonConstants.AMOUNT_ATTRIBUTE,amount);

        if(senderWallet!=null && senderWallet.getBalance()-amount>=0){
            walletRepository.updateWallet(receiver,amount);
            walletRepository.updateWallet(sender,0-amount);
            trxStatusObj.put(CommonConstants.WALLET_UPDATE_STATUS_ATTRIBUTE,CommonConstants.WALLET_UPDATE_SUCCESS_STATUS);
        }

        kafkaTemplate.send(CommonConstants.WALLET_UPDATE_TOPIC,trxStatusObj.toString());

    }
}
