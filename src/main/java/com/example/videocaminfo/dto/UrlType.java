package com.example.videocaminfo.dto;

import lombok.Getter;

public enum UrlType {
    LIVE("LIVE"),
    ARCHIVE("ARCHIVE");
    @Getter
    private final String type;

    UrlType(String type) {
        this.type = type;
    }
}
