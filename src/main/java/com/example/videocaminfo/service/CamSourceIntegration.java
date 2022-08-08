package com.example.videocaminfo.service;

import com.example.videocaminfo.dto.AvailableCamDTO;
import com.example.videocaminfo.dto.SourceCamDTO;
import com.example.videocaminfo.dto.TokenDTO;
import com.example.videocaminfo.exception.IntegrationException;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface CamSourceIntegration {
    List<AvailableCamDTO> getAvailableCams() throws IntegrationException;
    CompletableFuture<SourceCamDTO> getSourceCamData(String sourceDataUrl) throws IntegrationException;
    CompletableFuture<TokenDTO> getTokenCamData(String tokenDataUrl) throws IntegrationException;
}
