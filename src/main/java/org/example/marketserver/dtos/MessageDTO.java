package org.example.marketserver.dtos;
import lombok.Builder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@Builder
public record MessageDTO(String to, String message, String from, String timestamp) {

}
