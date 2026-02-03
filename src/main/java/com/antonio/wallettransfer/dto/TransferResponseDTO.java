package com.antonio.wallettransfer.dto;

import java.math.BigDecimal;

public record TransferResponseDTO(
        String status,
        BigDecimal value) {
}
