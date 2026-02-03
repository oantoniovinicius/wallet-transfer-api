package com.antonio.wallettransfer.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record TransferRequestDTO(

        @NotNull Long payer,

        @NotNull Long payee,

        @NotNull @Positive BigDecimal value) {
}