package com.antonio.wallettransfer.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.antonio.wallettransfer.entity.User;
import com.antonio.wallettransfer.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Optional<Wallet> findByUser(User user);
}
