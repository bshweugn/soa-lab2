package com.example.organizationservice.service;

import com.example.organizationservice.model.Organization;
import com.example.organizationservice.repository.OrganizationRepository;
import com.example.organizationservice.spec.OrganizationSpecification;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.Valid;
import java.util.*;

@Service
@Validated
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Transactional
    public Organization createOrganization(@Valid Organization organization){
        return organizationRepository.save(organization);
    }

    public Page<Organization> getAllOrganizations(Map<String, String> filters, int page, int size){
        Specification<Organization> spec = OrganizationSpecification.filter(filters);
        Pageable pageable = PageRequest.of(page, size, buildSort(filters.get("sort")));
        return organizationRepository.findAll(spec, pageable);
    }

    public Optional<Organization> getOrganizationById(Long id){
        return organizationRepository.findById(id);
    }

    @Transactional
    public Organization updateOrganization(Long id, @Valid Organization orgDetails){
        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Organization not found by id: " + id));

        organization.setName(orgDetails.getName());
        organization.setCoordinates(orgDetails.getCoordinates());
        organization.setAnnualTurnover(orgDetails.getAnnualTurnover());
        organization.setFullName(orgDetails.getFullName());
        organization.setEmployeesCount(orgDetails.getEmployeesCount());
        organization.setType(orgDetails.getType());
        organization.setOfficialAddress(orgDetails.getOfficialAddress());


        return organizationRepository.save(organization);
    }

    @Transactional
    public void deleteOrganizationById(Long id){
        if(!organizationRepository.existsById(id)){
            throw new NoSuchElementException("Organization not found by id: " + id);
        }
        organizationRepository.deleteById(id);
    }

    @Transactional
    public boolean deleteByFullName(String fullName){
        Optional<Organization> orgWithFullName = organizationRepository.findAll().stream()
                .filter(o -> o.getFullName() != null && o.getFullName().equals(fullName))
                .findFirst();

        orgWithFullName.ifPresent(organizationRepository::delete);
        return orgWithFullName.isPresent();
    }

    public Organization getByMinEmployees() {
        return organizationRepository.findAll().stream()
                .min(Comparator.comparingInt(Organization::getEmployeesCount))
                .orElseThrow(() -> new NoSuchElementException("No organizations found"));
    }

    public Organization getByMaxFullName(){
        return organizationRepository.findAll().stream()
                .max(Comparator.comparing(
                        Organization::getFullName,
                        Comparator.nullsFirst(String::compareToIgnoreCase)))
                .orElseThrow(() -> new NoSuchElementException("No organization found"));
    }

    private Sort buildSort(String sortParam){
        if (sortParam == null || sortParam.isBlank()){
            return Sort.unsorted();
        }

        List<Sort.Order> orderList = Arrays.stream(sortParam.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(s -> s.startsWith("-")
                    ? new Sort.Order(Sort.Direction.DESC, s.substring(1))
                    : new Sort.Order(Sort.Direction.ASC, s.startsWith("+") ? s.substring(1) : s))
                .toList();
        return Sort.by(orderList);
    }

}