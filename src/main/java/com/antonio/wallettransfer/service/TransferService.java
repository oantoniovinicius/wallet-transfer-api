package com.antonio.wallettransfer.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.antonio.wallettransfer.entity.Transaction;
import com.antonio.wallettransfer.entity.User;
import com.antonio.wallettransfer.entity.Wallet;
import com.antonio.wallettransfer.enums.UserType;
import com.antonio.wallettransfer.repository.TransactionRepository;
import com.antonio.wallettransfer.repository.UserRepository;
import com.antonio.wallettransfer.repository.WalletRepository;

import com.antonio.wallettransfer.exception.InsufficientBalanceException;
import com.antonio.wallettransfer.exception.UnauthorizedTransferException;
import com.antonio.wallettransfer.exception.UserNotFoundException;
import com.antonio.wallettransfer.integration.AuthorizerClient;
import com.antonio.wallettransfer.integration.NotificationClient;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class TransferService {
    private final AuthorizerClient authorizerClient;
    private final NotificationClient notificationClient;

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;
    private final TransactionRepository transactionRepository;

    @Transactional
    public void transfer(Long payerId, Long payeeId, BigDecimal amount) {
        User payer = userRepository.findById(payerId)
                .orElseThrow(() -> new UserNotFoundException("Payer not found"));

        User payee = userRepository.findById(payeeId)
                .orElseThrow(() -> new UserNotFoundException("Payee not found"));

        if (payer.getUserType() == UserType.MERCHANT) {
            throw new UnauthorizedTransferException("Merchants cannot initiate transfers");
        }

        Wallet payerWallet = walletRepository.findByUser(payer)
                .orElseThrow(() -> new IllegalStateException("Payer wallet not found"));

        Wallet payeeWallet = walletRepository.findByUser(payee)
                .orElseThrow(() -> new IllegalStateException("Payee wallet not found"));

        if (payerWallet.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException();
        }

        boolean authorized;
        try {
            authorized = authorizerClient.authorize();
        } catch (Exception ex) {
            throw new UnauthorizedTransferException("Authorization service unavailable");
        }

        if (!authorized) {
            throw new UnauthorizedTransferException("Transfer not authorized");
        }

        payerWallet.debit(amount);
        payeeWallet.credit(amount);

        walletRepository.save(payerWallet);
        walletRepository.save(payeeWallet);

        Transaction transaction = Transaction.success(payer, payee, amount);
        transactionRepository.save(transaction);

        try {
            notificationClient.notifyUser(payee.getId());
        } catch (Exception ex) {
        }

    }
}
