package com.aadeetyeah.walletservice.ewallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @PostMapping("/transaction")
    public String doTransaction(@RequestBody TransactionRequest transactionRequest){
        //Sender
        //Receiver
        //text msg(Purpose)
        //amount

        if(!transactionRequest.validate()){
            throw new RuntimeException("Request is not valid");
        }
        return   transactionService.doTransaction(transactionRequest);
    }
}
