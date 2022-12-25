package com.aadeetyeah.walletservice.ewallet;

import lombok.Getter;

@Getter
public enum TransactionStatus {
    PENDING,
    SUCCESS,
    FAILED
    /*Enum's value can't be changed that is why we only have getters for Enum and no Setters.*/
}
