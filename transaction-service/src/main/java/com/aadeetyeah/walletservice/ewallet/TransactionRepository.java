package com.aadeetyeah.walletservice.ewallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface TransactionRepository extends JpaRepository<Transaction,Integer> {

    @Query("select t from Transaction t where t.transactionId=?1")
    Transaction getById(String trxId);

    @Modifying
    @Transactional
    @Query("update Transaction t set t.transactionStatus=?2 where t.transactionId=?1")
    void updateTrx(String trxId, TransactionStatus transactionStatus);

}
