package com.example.videocaminfo.service;

import com.example.videocaminfo.dto.AvailableCamDTO;
import com.example.videocaminfo.dto.SourceCamDTO;
import com.example.videocaminfo.dto.TokenDTO;
import com.example.videocaminfo.exception.IntegrationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class CamSourceIntegrationImpl implements CamSourceIntegration {
    private final ObjectMapper mapper;

    private final RestTemplate restTemplate;

    @Value("${mock-cams-uri}")
    private String mockAvailableCamsUrl;

    @Override
    public List<AvailableCamDTO> getAvailableCams() throws IntegrationException {
        return proceedRequest(new TypeReference<List<AvailableCamDTO>>() {
        }, mockAvailableCamsUrl);
    }

    @Override
    @Async
    public CompletableFuture<SourceCamDTO> getSourceCamData(String sourceDataUrl) throws IntegrationException {
        return CompletableFuture.completedFuture(proceedRequest(new TypeReference<SourceCamDTO>() {
        }, sourceDataUrl));
    }

    @Override
    @Async
    public CompletableFuture<TokenDTO> getTokenCamData(String tokenDataUrl) throws IntegrationException {
        return CompletableFuture.completedFuture(proceedRequest(new TypeReference<TokenDTO>() {
        }, tokenDataUrl));
    }

    private <T> T proceedRequest(TypeReference<T> responseClassTypeReference, String url) throws IntegrationException {
        UriComponentsBuilder builder = UriComponentsBuilder
                .fromHttpUrl(url);
        log.info("Request to {}", builder.toUriString());
        HttpEntity<?> entity = new HttpEntity<>(null);
        T responseDto;
        try {
            String body = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, String.class).getBody();
            log.info("Response {}", body);
            responseDto = mapper.readValue(body, responseClassTypeReference);
        } catch (HttpStatusCodeException e) {
            throw new IntegrationException(e.getResponseBodyAsString());
        } catch (IOException e1) {
            throw new IntegrationException("Can not deserialize response to " + responseClassTypeReference.toString());
        }
        return responseDto;
    }
}
