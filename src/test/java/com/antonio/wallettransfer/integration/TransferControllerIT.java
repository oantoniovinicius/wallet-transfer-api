package com.antonio.wallettransfer.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import com.antonio.wallettransfer.dto.TransferResponseDTO;
import com.antonio.wallettransfer.entity.User;
import com.antonio.wallettransfer.entity.Wallet;
import com.antonio.wallettransfer.enums.UserType;
import com.antonio.wallettransfer.repository.UserRepository;
import com.antonio.wallettransfer.repository.WalletRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TransferControllerIT {
    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WalletRepository walletRepository;

    @BeforeEach
    void setUp() {
        walletRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    public void shouldTransferSuccesfully() {
        // GIVEN
        User payer = userRepository
                .save(User.create("Antonio Payer", "payer@email.com", "12345678901", UserType.COMMON));
        User payee = userRepository
                .save(User.create("Lojista", "merchant@email.com", "98765432100", UserType.MERCHANT));

        walletRepository.save(Wallet.create(payer, new BigDecimal("100.00")));
        walletRepository.save(Wallet.create(payee, BigDecimal.ZERO));

        Map<String, Object> request = new HashMap<>();
        request.put("payer", payer.getId());
        request.put("payee", payee.getId());
        request.put("value", 50.00);

        // WHEN
        ResponseEntity<TransferResponseDTO> response = restTemplate.postForEntity("/transfers", request,
                TransferResponseDTO.class);

        // THEN
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("SUCCESS", response.getBody().status());
    }
}
