package com.example.videocaminfo.dto;

import lombok.Data;

@Data
public class TokenDTO {
    private String value;
    private int ttl;
}
