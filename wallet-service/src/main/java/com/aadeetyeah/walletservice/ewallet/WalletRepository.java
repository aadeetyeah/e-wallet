package com.aadeetyeah.walletservice.ewallet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface WalletRepository extends JpaRepository<Wallet,Integer> {


    /* Issue : QueryExecutionRequestException: Not supported for DML operations
    https://stackoverflow.com/questions/44022076/jparepository-not-supported-for-dml-operations-delete-query*/

    @Transactional
    @Modifying
    @Query("update Wallet w set w.balance= w.balance+ :amount where w.email= :userEmail")
    void updateWallet(String userEmail, Double amount);

    Wallet findByEmail(String email);

}
