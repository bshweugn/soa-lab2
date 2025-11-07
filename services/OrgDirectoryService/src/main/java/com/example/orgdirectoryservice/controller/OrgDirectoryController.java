package com.example.orgdirectoryservice.controller;

import com.example.common.dto.MinMaxTurnoverDto;
import com.example.common.dto.OrganizationDto;
import com.example.common.dto.TypeDto;
import com.example.orgdirectoryservice.service.OrgDirectoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrgDirectoryController {

    private final OrgDirectoryService orgDirectoryService;

    @PostMapping("/find-by/type")
    public ResponseEntity<List<OrganizationDto>> findByType(@Valid @RequestBody TypeDto typeDto){
        List<OrganizationDto> organizations = orgDirectoryService.findByType(typeDto);
        return ResponseEntity.ok(organizations);
    }

    @PostMapping("/find-by/min-and-max-turnover")
    public ResponseEntity<List<OrganizationDto>> findByTurnoverRange(
            @Valid @RequestBody MinMaxTurnoverDto minMaxTurnoverDto){
        List<OrganizationDto> organizations = orgDirectoryService.findByTurnover(minMaxTurnoverDto);
        return ResponseEntity.ok(organizations);
    }
}
