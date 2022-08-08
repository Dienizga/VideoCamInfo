package com.example.videocaminfo.service;

import com.example.videocaminfo.dto.*;
import com.example.videocaminfo.exception.IntegrationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {CamServiceImpl.class})
public class CamServiceImplTest {
    @Autowired private CamService subj;
    @MockBean private CamSourceIntegration camSourceIntegration;
    @Autowired private ResourceLoader resourceLoader;
    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void collectCamInfo() throws IntegrationException, IOException {
        List<AvailableCamDTO> availableCamDTOS = loadResource("io-main.json", new TypeReference<List<AvailableCamDTO>>() {
        });
        SourceCamDTO source = loadResource("source.json", new TypeReference<SourceCamDTO>() {
        });
        TokenDTO token = loadResource("token.json", new TypeReference<TokenDTO>() {
        });
        List<AggregationCamDTO> result = loadResource("right-answer.json", new TypeReference<List<AggregationCamDTO>>() {
        });

        when(camSourceIntegration.getAvailableCams()).thenReturn(availableCamDTOS);
        when(camSourceIntegration.getSourceCamData(anyString())).thenReturn(CompletableFuture.completedFuture(source));
        when(camSourceIntegration.getTokenCamData(anyString())).thenReturn(CompletableFuture.completedFuture(token));

        ResponseDTO responseDto = subj.collectCamInfo();
        assertEquals(result, responseDto.getResultList());
    }

    @Test(expected = IntegrationException.class)
    public void collectCamInfo_WithException() throws IntegrationException, IOException {
        List<AvailableCamDTO> cams = loadResource("io-main.json", new TypeReference<List<AvailableCamDTO>>() {
        });

        when(camSourceIntegration.getAvailableCams()).thenReturn(cams);
        when(camSourceIntegration.getSourceCamData(anyString())).thenThrow(IntegrationException.class);

        ResponseDTO responseDTO = subj.collectCamInfo();
        assertNotNull(responseDTO);
    }

    private <T> T loadResource(String resourceName, TypeReference<T> obj) throws IOException {
        Resource resource = resourceLoader.getResource("classpath:" + resourceName);
        StringBuilder stringBuilder = new StringBuilder();
        Files.lines(Paths.get(resource.getURI())).forEach(stringBuilder::append);
        return mapper.readValue(stringBuilder.toString(), obj);
    }
}