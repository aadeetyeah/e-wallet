package com.aadeetyeah.walletservice.ewallet;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import java.util.logging.Logger;

import static com.aadeetyeah.walletservice.ewallet.CommonConstants.*;

@Service
public class TransactionService {

    private static Logger logger    =   Logger.getLogger(String.valueOf(TransactionService.class));
    private static Date   date      =   new Date();
    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    @Autowired
    ObjectMapper objectMapper;

    public String doTransaction(TransactionRequest transactionRequest){

        Transaction transaction     =   Transaction.builder().transactionId(UUID.randomUUID().toString())
                                        .transactionStatus(TransactionStatus.PENDING).amount(transactionRequest.getAmount())
                                        .purpose(transactionRequest.getPurpose()).sender(transactionRequest.getSender())
                                        .receiver(transactionRequest.getReceiver()).build();


        transactionRepository.save(transaction);

        JSONObject jsonObject       =   new JSONObject();
        jsonObject.put(CommonConstants.SENDER_ATTRIBUTE,transaction.getSender());
        jsonObject.put(CommonConstants.RECEIVER_ATTRIBUTE,transaction.getReceiver());
        jsonObject.put(CommonConstants.AMOUNT_ATTRIBUTE,transaction.getAmount());
        jsonObject.put(CommonConstants.TRANSACTION_ID_ATTRIBUTE,transaction.getTransactionId());
        kafkaTemplate.send(TRANSACTION_CREATE_TOPIC,jsonObject.toString());

        return transaction.getTransactionId();
    }

    @KafkaListener(topics = {CommonConstants.WALLET_UPDATE_TOPIC},groupId = "ewallet_grp")
    public void updateTransaction(String msg) throws JsonProcessingException {

        JSONObject walletUpdate   =   null;

        try {
            walletUpdate          =   (JSONObject) new JSONParser().parse(msg);
        } catch (ParseException e) {
            logger.warning("Empty or null msg from wallet topic cannot update Transaction Status.");
        }

        String trxId              =    (String) walletUpdate.get(CommonConstants.TRANSACTION_ID_ATTRIBUTE);
        String trxStatus          =    (String) walletUpdate.get(CommonConstants.WALLET_UPDATE_STATUS_ATTRIBUTE);
        String sender             =    (String) walletUpdate.get(CommonConstants.SENDER_ATTRIBUTE);
        String receiver           =    (String) walletUpdate.get(CommonConstants.RECEIVER_ATTRIBUTE);
        Double amount             =    (Double) walletUpdate.get(AMOUNT_ATTRIBUTE);


        //Send Notification to users of credit and debit
        JSONObject senderNotification   =   new JSONObject();
        senderNotification.put(EMAIL_ATTRIBUTE,sender);
        senderNotification.put(IS_SENDER,true);
        senderNotification.put(AMOUNT_ATTRIBUTE,amount);
        senderNotification.put(TRANSACTION_ID_ATTRIBUTE,trxId);
        senderNotification.put(TRANSACTION_STATUS_ATTRIBUTE,TRANSACTION_SUCCESS_STATUS);
        senderNotification.put(TRANSACTION_TIME_ATTRIBUTE,date.toString());

        if(WALLET_UPDATE_SUCCESS_STATUS.equals(trxStatus)){

            transactionRepository.updateTrx(trxId,TransactionStatus.SUCCESS);

            JSONObject receiverNotification    =   new JSONObject();
            receiverNotification.put(EMAIL_ATTRIBUTE,receiver);
            receiverNotification.put(AMOUNT_ATTRIBUTE,amount);
            receiverNotification.put(IS_SENDER,false);
            receiverNotification.put(TRANSACTION_ID_ATTRIBUTE,trxId);
            receiverNotification.put(TRANSACTION_STATUS_ATTRIBUTE, TRANSACTION_SUCCESS_STATUS);
            receiverNotification.put(TRANSACTION_TIME_ATTRIBUTE,date.toString());

            //notify receiver
            kafkaTemplate.send(TRANSACTION_COMPLETE_TOPIC,receiverNotification.toString());

        }else{

            transactionRepository.updateTrx(trxId,TransactionStatus.FAILED);
            senderNotification.put(TRANSACTION_STATUS_ATTRIBUTE,TRANSACTION_FAILED_STATUS);
        }

        //Failed TRX notify to sender
        //Success TRX notify to sender and receiver

        //notify sender

        kafkaTemplate.send(TRANSACTION_COMPLETE_TOPIC,senderNotification.toString());
    }
}
