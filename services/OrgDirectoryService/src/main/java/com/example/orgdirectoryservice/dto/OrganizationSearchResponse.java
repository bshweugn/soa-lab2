package com.example.orgdirectoryservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrganizationSearchResponse {
    private List<OrganizationDto> organizations;
    private int page;
    private int size;
    private long totalElements;
    private boolean firstElement;
    private boolean lastElement;
}
