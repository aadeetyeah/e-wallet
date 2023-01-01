package com.aadeetyeah.walletservice.ewallet;

import com.aadeetyeah.walletservice.ewallet.CommonConstants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.logging.Logger;

@Service
public class NotificationService {

    private static Logger logger    =   Logger.getLogger(String.valueOf(NotificationService.class));

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    SimpleMailMessage simpleMailMessage;

    @Autowired
    ObjectMapper objectMapper;

    @KafkaListener(topics = {CommonConstants.TRANSACTION_COMPLETE_TOPIC},groupId = "ewallet_grp")
    public void publishEmail(String msg) throws Exception {

        JSONObject  trxCompleteObj  =   null;
        try{
            trxCompleteObj  =   (JSONObject) new JSONParser().parse(msg);
        }catch (ParseException parseException){
            logger.warning("Check JSON payload, not able to parse.");
        }


        String email        = (String) trxCompleteObj.get(CommonConstants.EMAIL_ATTRIBUTE);
        boolean isSender    = (boolean) trxCompleteObj.get(CommonConstants.IS_SENDER);
        Double amount       = (Double)  trxCompleteObj.get(CommonConstants.AMOUNT_ATTRIBUTE);
        String trxId        = (String) trxCompleteObj.get(CommonConstants.TRANSACTION_ID_ATTRIBUTE);
        String status       = (String) trxCompleteObj.get(CommonConstants.TRANSACTION_STATUS_ATTRIBUTE);
        String   date       =  (String) trxCompleteObj.get(CommonConstants.TRANSACTION_TIME_ATTRIBUTE);



        String emailMsg  =   null;
        if(isSender){
            //Sender
            if(status.equals(CommonConstants.TRANSACTION_SUCCESS_STATUS)){
                emailMsg="Transaction with Id: " + trxId + " has been completed at " + date.toString() +
                        " your account has been debited by amount: " + amount;
            }else if(status.equals(CommonConstants.TRANSACTION_FAILED_STATUS)){
                emailMsg="Transaction with Id: "+ trxId + "has failed at " + date.toString() + ", Please retry again.";
            }else{
                throw new Exception("Invalid Status found!");
            }
        }else{
            //Receiver
            emailMsg=" Your Account has been credited with amount: "+ amount + " on " + date.toString();
        }

        //send mail
        simpleMailMessage.setText(emailMsg);
        simpleMailMessage.setTo("adityaanerao@yahoo.in");
        simpleMailMessage.setFrom("aditz1227@gmail.com");
        simpleMailMessage.setSubject("E-Wallet Project Testing");

        javaMailSender.send(simpleMailMessage);
        logger.info("Mail sent to both sender and receiver.");
    }
}
