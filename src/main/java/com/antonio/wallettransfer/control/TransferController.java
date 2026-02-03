package com.antonio.wallettransfer.control;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.antonio.wallettransfer.dto.TransferRequestDTO;
import com.antonio.wallettransfer.dto.TransferResponseDTO;
import com.antonio.wallettransfer.service.TransferService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/transfers")
@RequiredArgsConstructor
public class TransferController {
    private final TransferService transferService;

    @PostMapping
    public ResponseEntity<TransferResponseDTO> transfer(@RequestBody @Valid TransferRequestDTO request) {
        transferService.transfer(request.payer(), request.payee(), request.value());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new TransferResponseDTO("SUCCESS", request.value()));
    }

}
