package com.example.videocaminfo.web;

import com.example.videocaminfo.dto.ResponseDTO;
import com.example.videocaminfo.exception.IntegrationException;
import com.example.videocaminfo.service.CamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CamController {
    private final CamService camService;

    @GetMapping("/")
    public ResponseEntity<ResponseDTO> getCamInfo() throws IntegrationException {
        return ResponseEntity.ok(camService.collectCamInfo());
    }
}
