package com.example.organizationservice.controller;

import com.example.common.dto.OrganizationDto;
import com.example.common.dto.PagedResponse;
import com.example.common.dto.SearchRequest;
import com.example.common.model.Organization;
import com.example.organizationservice.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/organizations")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping("/search")
    public ResponseEntity<PagedResponse<Organization>> searchOrganizations(
            @RequestBody SearchRequest searchRequest){
        Map<String, String> filters = searchRequest.getFilters();
        String sortParam = null;
        if(searchRequest.getSort() != null && !searchRequest.getSort().isEmpty()){
            sortParam = String.join(",", searchRequest.getSort());
        }
        filters.put("sort", sortParam);

        Page<Organization> organizationsPage = organizationService.getAllOrganizations(
                filters,
                searchRequest.getPage(),
                searchRequest.getSize()
        );

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
    public ResponseEntity<Organization> createOrganization(@Valid @RequestBody OrganizationDto organization) {
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

    @DeleteMapping("/by/fullname")
    public ResponseEntity<Void> deleteByFullName(@RequestParam String fullName){
        boolean deletedOrg = organizationService.deleteByFullName(fullName);
        if(deletedOrg){
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/by/min-employees")
    public ResponseEntity<Organization> getByMinEmployees(){
        try{
            Organization org = organizationService.getByMinEmployees();
            return ResponseEntity.ok(org);
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/by/max-fullname")
    public ResponseEntity<Organization> getByMaxFullName(){
        try{
            Organization org = organizationService.getByMaxFullName();
            return ResponseEntity.ok(org);
        }catch (NoSuchElementException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
