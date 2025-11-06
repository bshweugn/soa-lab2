package com.example.orgdirectoryservice.service;

import com.example.orgdirectoryservice.config.OrganizationClient;
import com.example.orgdirectoryservice.dto.MinMaxTurnoverDto;
import com.example.orgdirectoryservice.dto.OrganizationDto;
import com.example.orgdirectoryservice.dto.TypeDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrgDirectoryService {

    private final OrganizationClient client;

    public List<OrganizationDto> findByType(TypeDto type){
        Map<String, String> filters = Map.of("type", type.getType());
        var request = Map.of("filters", filters, "page", 0, "size", 100);
        return client.searchOrganizations(request);
    }

    public List<OrganizationDto> findByTurnover(MinMaxTurnoverDto turnoverDto){
        Map<String, String> filters = Map.of(
                "annualTurnoverMin", String.valueOf(turnoverDto.getMinTurnover()),
                "annualTurnoverMax", String.valueOf(turnoverDto.getMaxTurnover())
        );
        var request = Map.of("filters", filters, "page", 0, "size", 100);
        return client.searchOrganizations(request);
    }
}
