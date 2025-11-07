package com.example.common.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class SearchRequest {
    private int page = 0;
    private int size = 20;
    private List<String> sort;
    private Map<String, String> filters;
}
