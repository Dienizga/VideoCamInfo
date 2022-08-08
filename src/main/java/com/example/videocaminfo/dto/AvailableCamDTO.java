package com.example.videocaminfo.dto;

import lombok.Data;

@Data
public class AvailableCamDTO {
    private Long id;
    private String sourceDataUrl;
    private String tokenDataUrl;
}
