package com.antonio.wallettransfer.dto;

import java.time.Instant;

public record ErrorResponseDTO(
        String message,
        Instant timestamp) {
}
