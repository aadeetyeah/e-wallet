package com.aadeetyeah.walletservice.ewallet;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import static com.aadeetyeah.walletservice.ewallet.CommonConstants.TRANSACTION_CREATE_TOPIC;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    KafkaTemplate<String,String> kafkaTemplate;

    public String doTransaction(TransactionRequest transactionRequest){
        Transaction transaction=Transaction.builder().transactionId(UUID.randomUUID().toString())
                .transactionStatus(TransactionStatus.PENDING).amount(transactionRequest.getAmount())
                .purpose(transactionRequest.getPurpose()).sender(transactionRequest.getSender())
                .receiver(transactionRequest.getReceiver()).build();


        transactionRepository.save(transaction);

        JSONObject jsonObject=new JSONObject();
        jsonObject.put(CommonConstants.SENDER_ATTRIBUTE,transaction.getSender());
        jsonObject.put(CommonConstants.RECEIVER_ATTRIBUTE,transaction.getReceiver());
        jsonObject.put(CommonConstants.AMOUNT_ATTRIBUTE,transaction.getAmount());
        jsonObject.put(CommonConstants.TRANSACTION_ID_ATTRIBUTE,transaction.getTransactionId());

        kafkaTemplate.send(TRANSACTION_CREATE_TOPIC,jsonObject.toString());
        return transaction.getTransactionId();
    }
}
