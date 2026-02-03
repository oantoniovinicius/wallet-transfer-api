package com.antonio.wallettransfer.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.antonio.wallettransfer.entity.User;
import com.antonio.wallettransfer.entity.Wallet;
import com.antonio.wallettransfer.enums.UserType;
import com.antonio.wallettransfer.exception.InsufficientBalanceException;
import com.antonio.wallettransfer.exception.UnauthorizedTransferException;
import com.antonio.wallettransfer.exception.UserNotFoundException;
import com.antonio.wallettransfer.integration.AuthorizerClient;
import com.antonio.wallettransfer.integration.NotificationClient;
import com.antonio.wallettransfer.repository.TransactionRepository;
import com.antonio.wallettransfer.repository.UserRepository;
import com.antonio.wallettransfer.repository.WalletRepository;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private AuthorizerClient authorizerClient;

    @Mock
    private NotificationClient notificationClient;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WalletRepository walletRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @InjectMocks
    private TransferService transferService;

    private User payer;
    private User payee;
    private Wallet payerWallet;
    private Wallet payeeWallet;

    @BeforeEach
    void setup() {
        payer = mock(User.class);
        payee = mock(User.class);

        payerWallet = mock(Wallet.class);
        payeeWallet = mock(Wallet.class);
    }

    @Test
    void shouldThrowExceptionWhenMerchantTriesToTransfer() {
        // GIVEN
        when(payer.getUserType()).thenReturn(UserType.MERCHANT);

        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(payee));

        // WHEN + THEN
        assertThrows(
                UnauthorizedTransferException.class,
                () -> transferService.transfer(1L, 2L, BigDecimal.TEN));

        // THEN
        verifyNoInteractions(walletRepository, transactionRepository, notificationClient, authorizerClient);

    }

    @Test
    void shouldThrowExceptionWhenBalanceIsInsufficient() {
        // GIVEN
        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(payee));

        when(walletRepository.findByUser(payer)).thenReturn(Optional.of(payerWallet));
        when(walletRepository.findByUser(payee)).thenReturn(Optional.of(payeeWallet));

        when(payerWallet.getBalance()).thenReturn(BigDecimal.ONE);

        // WHEN + THEN
        assertThrows(
                InsufficientBalanceException.class,
                () -> transferService.transfer(1L, 2L, BigDecimal.TEN));

        // THEN
        verify(authorizerClient, never()).authorize();
        verify(transactionRepository, never()).save(any());
        verify(notificationClient, never()).notifyUser(anyLong());
    }

    @Test
    void shouldThrowExceptionWhenAuthorizerDeniesTransfer() {
        // GIVEN
        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(payee));

        when(walletRepository.findByUser(payer)).thenReturn(Optional.of(payerWallet));
        when(walletRepository.findByUser(payee)).thenReturn(Optional.of(payeeWallet));

        when(payerWallet.getBalance()).thenReturn(BigDecimal.TEN);
        when(authorizerClient.authorize()).thenReturn(false);

        // WHEN + THEN
        assertThrows(
                UnauthorizedTransferException.class,
                () -> transferService.transfer(1L, 2L, BigDecimal.ONE));

        verify(transactionRepository, never()).save(any());
        verify(notificationClient, never()).notifyUser(anyLong());
    }

    @Test
    void shouldTransferSuccessfullyWhenAllValidationsPass() {
        // GIVEN
        when(payee.getId()).thenReturn(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(payee));

        when(walletRepository.findByUser(payer)).thenReturn(Optional.of(payerWallet));
        when(walletRepository.findByUser(payee)).thenReturn(Optional.of(payeeWallet));

        when(payerWallet.getBalance()).thenReturn(BigDecimal.TEN);
        when(authorizerClient.authorize()).thenReturn(true);

        // WHEN
        transferService.transfer(1L, 2L, BigDecimal.ONE);

        // THEN
        verify(payerWallet).debit(BigDecimal.ONE);
        verify(payeeWallet).credit(BigDecimal.ONE);

        verify(transactionRepository).save(any());
        verify(notificationClient).notifyUser(2L);
    }

    @Test
    void shouldNotRollbackWhenNotificationFails() {
        // GIVEN
        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(2L)).thenReturn(Optional.of(payee));

        when(walletRepository.findByUser(payer)).thenReturn(Optional.of(payerWallet));
        when(walletRepository.findByUser(payee)).thenReturn(Optional.of(payeeWallet));

        when(payerWallet.getBalance()).thenReturn(BigDecimal.TEN);
        when(authorizerClient.authorize()).thenReturn(true);

        // WHEN + THEN
        doThrow(new RuntimeException("Notification service down"))
                .when(notificationClient).notifyUser(anyLong());

        assertDoesNotThrow(() -> transferService.transfer(1L, 2L, BigDecimal.ONE));

        // THEN
        verify(transactionRepository).save(any());
        verify(payerWallet).debit(BigDecimal.ONE);
        verify(payeeWallet).credit(BigDecimal.ONE);
    }

    @Test // payer not found
    void shouldThrowExceptionWhenPayerNotFound() {
        // GIVEN
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // WHEN + THEN
        assertThrows(
                UserNotFoundException.class,
                () -> transferService.transfer(1L, 2L, BigDecimal.ONE));

        // THEN
        verifyNoInteractions(
                walletRepository,
                transactionRepository,
                notificationClient,
                authorizerClient);
    }

    @Test // payee not found
    void shouldThrowExceptionWhenPayeeNotFound() {
        // GIVEN
        when(userRepository.findById(1L)).thenReturn(Optional.of(payer));
        when(userRepository.findById(2L)).thenReturn(Optional.empty());

        // WHEN + THEN
        assertThrows(
                UserNotFoundException.class,
                () -> transferService.transfer(1L, 2L, BigDecimal.ONE));

        // THEN
        verifyNoInteractions(
                walletRepository,
                transactionRepository,
                notificationClient,
                authorizerClient);
    }

}