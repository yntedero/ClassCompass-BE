package org.example.marketserver.dtos;
import lombok.Builder;

@Builder
public record MessageDTO(String to, String message, String from) { }
