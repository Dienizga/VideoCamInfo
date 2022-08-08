package com.example.videocaminfo.service;

import com.example.videocaminfo.dto.ResponseDTO;
import com.example.videocaminfo.exception.IntegrationException;

public interface CamService {
    ResponseDTO collectCamInfo() throws IntegrationException;
}
