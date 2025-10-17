package com.example.organizationservice.controller;

import com.example.organizationservice.dto.OrganizationDto;
import com.example.organizationservice.dto.PagedResponse;
import com.example.organizationservice.model.Organization;
import com.example.organizationservice.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
@Slf4j
public class OrganizationController {

    private final OrganizationService organizationService;

    @GetMapping
    public ResponseEntity<PagedResponse<Organization>> getAllFlats(
            @RequestParam Map<String, String> allParams,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size){
        Page<Organization> organizationsPage = organizationService.getAllOrganizations(allParams, page, size);

        PagedResponse<Organization> response = PagedResponse.<Organization>builder()
                .content(organizationsPage.getContent())
                .page(organizationsPage.getNumber())
                .size(organizationsPage.getSize())
                .totalElements(organizationsPage.getTotalElements())
                .firstElement(organizationsPage.isFirst())
                .lastElement(organizationsPage.isLast())
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Organization> getOrganizationById(@PathVariable Long id){
        return organizationService.getOrganizationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Organization> createOrganization(@RequestBody OrganizationDto organization) {
        Organization createdOrg = organizationService.createOrganization(organization.toEntity());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrg);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Organization> updateOrganization(
            @PathVariable Long id,
            @Valid @RequestBody Organization organization) {
        try{
            Organization updatedOrg = organizationService.updateOrganization(id, organization);
            return ResponseEntity.ok(updatedOrg);
        }catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrganization(@PathVariable Long id){
        try{
            organizationService.deleteOrganizationById(id);
            return ResponseEntity.ok().build();
        }catch (NoSuchElementException e){
            return ResponseEntity.notFound().build();
        }
    }

}
