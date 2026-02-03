package com.antonio.wallettransfer.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.antonio.wallettransfer.entity.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

}
