package com.aadeetyeah.walletservice.ewallet;

public class CommonConstants {

    //kafka topics
    public static final String USER_CREATE_TOPIC                =   "user_create";
    public static final String TRANSACTION_CREATE_TOPIC         =   "transaction_create";
    public static final String WALLET_UPDATE_TOPIC              =   "wallet_update";
    public static final String TRANSACTION_COMPLETE_TOPIC       =   "transaction_complete";


    public static final String EMAIL_ATTRIBUTE                  =   "email";
    public static final String PHONE_ATTRIBUTE                  =   "phone";
    public static final String TRANSACTION_ID_ATTRIBUTE         =   "transaction_id";
    public static final String SENDER_ATTRIBUTE                 =   "sender";
    public static final String RECEIVER_ATTRIBUTE               =   "receiver";
    public static final String AMOUNT_ATTRIBUTE                 =   "amount";

    public static final String WALLET_UPDATE_STATUS_ATTRIBUTE   =   "wallet_update_status";
    public static final String WALLET_UPDATE_SUCCESS_STATUS     =   "SUCCESS";
    public static final String WALLET_UPDATE_FAILED_STATUS      =   "FAILED";

    public static final String TRANSACTION_STATUS_ATTRIBUTE     =   "transaction_status";
    public static final String TRANSACTION_TIME_ATTRIBUTE       =   "transaction_time";
    public static final String TRANSACTION_SUCCESS_STATUS     =   "SUCCESS";
    public static final String TRANSACTION_FAILED_STATUS      =   "FAILED";

    public static final String EMAIL_MESSAGE_ATTRIBUTE          =   "email_message";

    public static final String IS_SENDER                        =   "is_sender";
}
//java.sql.SQLIntegrityConstraintViolationException: Duplicate entry 'parthy@google.com' for key 'user.UK_ob8kqyqqgmefl0aco34akdtpe'
