package com.example.orgdirectoryservice.service;

import com.example.orgdirectoryservice.dto.MinMaxTurnoverDto;
import com.example.orgdirectoryservice.dto.TypeDto;
import com.example.orgdirectoryservice.model.Organization;
import com.example.orgdirectoryservice.model.OrganizationType;
import com.example.orgdirectoryservice.repository.OrganizationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrgDirectoryService {
    private final OrganizationRepository organizationRepository;

    public List<Organization> findByType(TypeDto typeDto){
        if(typeDto.getType() == null || typeDto.getType().isBlank()){
            throw new IllegalArgumentException("Type cannot be null");
        }

        OrganizationType type = OrganizationType.valueOf(typeDto.getType().toUpperCase());
        return organizationRepository.findAll()
                .stream()
                .filter(o -> o.getType() == type)
                .toList();
    }

    public List<Organization> findByTurnover(MinMaxTurnoverDto turnoverDto){
        long min = turnoverDto.getMinTurnover();
        long max = turnoverDto.getMaxTurnover();

        if(min > max){
            throw new IllegalArgumentException("error turnover values");
        }

        return organizationRepository.findAll()
                .stream()
                .filter(o -> o.getAnnualTurnover() >= min && o.getAnnualTurnover() <= max)
                .toList();
    }
}
