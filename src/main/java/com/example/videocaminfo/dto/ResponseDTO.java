package com.example.videocaminfo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class ResponseDTO {
    private List<AggregationCamDTO> resultList;
    private String errorMessage;

    public ResponseDTO(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ResponseDTO(List<AggregationCamDTO> resultList) {
        this.resultList = resultList;
    }
}
