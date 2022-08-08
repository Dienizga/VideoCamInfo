package com.example.videocaminfo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AggregationCamDTO {
    private Long id;
    private UrlType urlType;
    private String videoUrl;
    private String value;
    private int ttl;
}
