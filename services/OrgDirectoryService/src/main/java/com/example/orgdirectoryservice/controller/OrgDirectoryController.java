package com.example.orgdirectoryservice.controller;

import com.example.orgdirectoryservice.dto.MinMaxTurnoverDto;
import com.example.orgdirectoryservice.dto.OrganizationDto;
import com.example.orgdirectoryservice.dto.TypeDto;
import com.example.orgdirectoryservice.service.OrgDirectoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrgDirectoryController {

    private final OrgDirectoryService orgDirectoryService;

    @PostMapping("/find-by/type")
    public ResponseEntity<List<OrganizationDto>> findByType(@RequestBody TypeDto typeDto){
        List<OrganizationDto> organizations = orgDirectoryService.findByType(typeDto);
        return ResponseEntity.ok(organizations);
    }

    @PostMapping("/find-by/min-and-max-turnover")
    public ResponseEntity<List<OrganizationDto>> findByTurnoverRange(
            @RequestBody MinMaxTurnoverDto minMaxTurnoverDto){
        List<OrganizationDto> organizations = orgDirectoryService.findByTurnover(minMaxTurnoverDto);
        return ResponseEntity.ok(organizations);
    }
}
