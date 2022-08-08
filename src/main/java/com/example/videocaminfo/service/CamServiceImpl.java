package com.example.videocaminfo.service;

import com.example.videocaminfo.dto.AggregationCamDTO;
import com.example.videocaminfo.dto.AvailableCamDTO;
import com.example.videocaminfo.dto.ResponseDTO;
import com.example.videocaminfo.dto.SourceCamDTO;
import com.example.videocaminfo.exception.IntegrationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class CamServiceImpl implements CamService {
    private final CamSourceIntegration camSourceIntegration;

    @Override
    public ResponseDTO collectCamInfo() throws IntegrationException {
        List<AvailableCamDTO> availableCams = camSourceIntegration.getAvailableCams();
        List<CompletableFuture<AggregationCamDTO>> completableFutures = new ArrayList<>();
        for (AvailableCamDTO availableCam : availableCams) {
            completableFutures.add(aggregateCamInfo(availableCam));
        }
        List<AggregationCamDTO> result = completableFutures.stream()
                .map(CompletableFuture::join)
                .collect(Collectors.toList());
        return new ResponseDTO(result);
    }

    private CompletableFuture<AggregationCamDTO> aggregateCamInfo(AvailableCamDTO availableVideoCamDto) throws IntegrationException {
        CompletableFuture<SourceCamDTO> sourceCamData = camSourceIntegration.getSourceCamData(availableVideoCamDto.getSourceDataUrl());
        return sourceCamData.thenCombine(camSourceIntegration.getTokenCamData(availableVideoCamDto.getTokenDataUrl()),
                (a, b) -> new AggregationCamDTO(availableVideoCamDto.getId(), a.getUrlType(), a.getVideoUrl(), b.getValue(), b.getTtl()));
    }
}
