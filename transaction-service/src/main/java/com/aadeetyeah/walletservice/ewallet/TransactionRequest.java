package com.aadeetyeah.walletservice.ewallet;

import lombok.*;

import javax.persistence.Entity;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {

    private String sender;
    private String receiver;

    private Double amount;
    private String purpose;

    public boolean validate(){
        if(this.amount==null || this.amount==0 || this.sender==null ||
                this.sender.isEmpty() || this.receiver==null || this.receiver.isEmpty()){
            return false;
        }
        return true;
    }
}
