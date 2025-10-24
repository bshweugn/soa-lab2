package com.example.orgdirectoryservice.controller;

import com.example.orgdirectoryservice.dto.MinMaxTurnoverDto;
import com.example.orgdirectoryservice.dto.TypeDto;
import com.example.orgdirectoryservice.model.Organization;
import com.example.orgdirectoryservice.service.OrgDirectoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrgDirectoryController {

    private final OrgDirectoryService orgDirectoryService;

    @PostMapping("/find-by/type")
    public ResponseEntity<List<Organization>> findByType(@RequestBody TypeDto typeDto){
        List<Organization> organizations = orgDirectoryService.findByType(typeDto);
        return ResponseEntity.ok(organizations);
    }

    @PostMapping("/find-by/min-and-max-turnover")
    public ResponseEntity<List<Organization>> findByTurnoverRange(
            @RequestBody MinMaxTurnoverDto minMaxTurnoverDto){
        List<Organization> organizations = orgDirectoryService.findByTurnover(minMaxTurnoverDto);
        return ResponseEntity.ok(organizations);
    }
}
