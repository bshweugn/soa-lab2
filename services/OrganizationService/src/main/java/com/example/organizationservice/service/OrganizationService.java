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

import java.util.*;

@Service
@RequiredArgsConstructor
public class OrganizationService {

    private final OrganizationRepository organizationRepository;

    // Create (Предлагаю переделать)
    /*public Organization create(OrganizationInput input) {
        validateBusinessRules(input);
        Organization o = new Organization();
        long id = idGen.getAndIncrement();
        o.setId(id);
        o.setName(input.getName());
        o.setCoordinates(input.getCoordinates());
        o.setCreationDate(LocalDateTime.now());
        o.setAnnualTurnover(input.getAnnualTurnover());
        o.setFullName(input.getFullName());
        o.setEmployeesCount(input.getEmployeesCount());
        o.setType(input.getType());
        o.setOfficialAddress(input.getOfficialAddress());
        store.put(id, o);
        return o;
    }*/

    // Read (Предлагаю переделать)
   /* public Organization getById(long id) {
        Organization o = store.get(id);
        if (o == null) throw new NoSuchElementException("Organization not found with id=" + id);
        return o;
    }*/

    // Update (replace fields except id and creationDate) (Предлагаю переделать)
    /*public Organization update(long id, OrganizationInput input) {
        validateBusinessRules(input);
        Organization existing = getById(id);
        existing.setName(input.getName());
        existing.setCoordinates(input.getCoordinates());
        existing.setAnnualTurnover(input.getAnnualTurnover());
        existing.setFullName(input.getFullName());
        existing.setEmployeesCount(input.getEmployeesCount());
        existing.setType(input.getType());
        existing.setOfficialAddress(input.getOfficialAddress());
        store.put(id, existing);
        return existing;
    }*/

    // Delete by id
    /*public void deleteById(long id) {
        Organization removed = store.remove(id);
        if (removed == null) throw new NoSuchElementException("Organization not found with id=" + id);
    }*/

    @Transactional
    public Organization createOrganization(Organization organization){
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
    public Organization updateOrganization(Long id, Organization orgDetails){
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

    // Delete one by fullName (any)
    /*public boolean deleteOneByFullName(String fullName) {
        Optional<Long> key = store.entrySet().stream()
                .filter(e -> Objects.equals(e.getValue().getFullName(), fullName))
                .map(Map.Entry::getKey)
                .findFirst();
        key.ifPresent(store::remove);
        return key.isPresent();
    }*/

    // Get one with minimal employeesCount (any)
    /*public Organization getOneWithMinEmployees() {
        return store.values().stream()
                .min(Comparator.comparingInt(Organization::getEmployeesCount))
                .orElseThrow(NoSuchElementException::new);
    }*/

    // Get one with max fullName (lexicographically)
    /*public Organization getOneWithMaxFullName() {
        return store.values().stream()
                .max(Comparator.comparing(Organization::getFullName, Comparator.nullsFirst(String::compareTo)))
                .orElseThrow(NoSuchElementException::new);
    }*/

    // Filter & sort & paginate
    /*public List<Organization> listFiltered(
            String name,
            String fullName,
            OrganizationType type,
            Long annualTurnoverMin,
            Long annualTurnoverMax,
            Integer employeesCountMin,
            Integer employeesCountMax,
            Double coordinatesXMin,
            Double coordinatesXMax,
            Double coordinatesYMin,
            Double coordinatesYMax,
            String zipCode,
            String townName,
            String sort // e.g. "name,-annualTurnover"
    ) {
        Stream<Organization> s = store.values().stream();

        if (name != null && !name.isEmpty())
            s = s.filter(o -> o.getName() != null && o.getName().contains(name));
        if (fullName != null && !fullName.isEmpty())
            s = s.filter(o -> o.getFullName() != null && o.getFullName().contains(fullName));
        if (type != null)
            s = s.filter(o -> o.getType() == type);
        if (annualTurnoverMin != null)
            s = s.filter(o -> o.getAnnualTurnover() >= annualTurnoverMin);
        if (annualTurnoverMax != null)
            s = s.filter(o -> o.getAnnualTurnover() <= annualTurnoverMax);
        if (employeesCountMin != null)
            s = s.filter(o -> o.getEmployeesCount() >= employeesCountMin);
        if (employeesCountMax != null)
            s = s.filter(o -> o.getEmployeesCount() <= employeesCountMax);
        if (coordinatesXMin != null)
            s = s.filter(o -> o.getCoordinates()!=null && o.getCoordinates().getX()!=null && o.getCoordinates().getX() >= coordinatesXMin);
        if (coordinatesXMax != null)
            s = s.filter(o -> o.getCoordinates()!=null && o.getCoordinates().getX()!=null && o.getCoordinates().getX() <= coordinatesXMax);
        if (coordinatesYMin != null)
            s = s.filter(o -> o.getCoordinates()!=null && o.getCoordinates().getY()!=null && o.getCoordinates().getY() >= coordinatesYMin);
        if (coordinatesYMax != null)
            s = s.filter(o -> o.getCoordinates()!=null && o.getCoordinates().getY()!=null && o.getCoordinates().getY() <= coordinatesYMax);
        if (zipCode != null && !zipCode.isEmpty())
            s = s.filter(o -> o.getOfficialAddress()!=null && zipCode.equals(o.getOfficialAddress().getZipCode()));
        if (townName != null && !townName.isEmpty())
            s = s.filter(o -> o.getOfficialAddress()!=null && o.getOfficialAddress().getTown()!=null &&
                    o.getOfficialAddress().getTown().getName()!=null &&
                    o.getOfficialAddress().getTown().getName().contains(townName));

        List<Organization> list = s.collect(Collectors.toList());

        // sorting
        if (sort != null && !sort.isEmpty()) {
            Comparator<Organization> cmp = buildComparator(sort);
            if (cmp != null) {
                list.sort(cmp);
            }
        }

        return list;
    }*/

    // return a page sublist
    /*public List<Organization> toPage(List<Organization> items, int page, int size) {
        int start = page * size;
        if (start >= items.size()) return Collections.emptyList();
        int end = Math.min(start + size, items.size());
        return items.subList(start, end);
    }*/

   /* public long countAll() {
        return store.size();
    }

    public int totalPages(int totalElements, int size) {
        if (size <= 0) return 0;
        return (int) Math.ceil((double) totalElements / size);
    }

    // Filter by turnover (for /orgdirectory)
    public List<Organization> filterByTurnover(long min, long max) {
        return store.values().stream()
                .filter(o -> o.getAnnualTurnover() >= min && o.getAnnualTurnover() <= max)
                .collect(Collectors.toList());
    }*/

    // Filter by type
    /*public List<Organization> filterByType(OrganizationType type) {
        return store.values().stream()
                .filter(o -> o.getType() == type)
                .collect(Collectors.toList());
    }

    // Basic business validation beyond bean validation (if needed)
    private void validateBusinessRules(OrganizationInput input) {
        if (input.getAnnualTurnover() <= 0) throw new IllegalArgumentException("annualTurnover must be > 0");
        if (input.getEmployeesCount() <= 0) throw new IllegalArgumentException("employeesCount must be > 0");
        // coordinates checked by bean validation
    }

    // build comparator for supported fields
    private Comparator<Organization> buildComparator(String sortParam) {
        String[] parts = sortParam.split(",");
        Comparator<Organization> combined = null;
        for (String p : parts) {
            boolean desc = p.startsWith("-");
            String key = desc ? p.substring(1) : p;
            Comparator<Organization> cmp = comparatorForKey(key);
            if (cmp == null) continue;
            if (desc) cmp = cmp.reversed();
            combined = combined == null ? cmp : combined.thenComparing(cmp);
        }
        return combined;
    }

    private Comparator<Organization> comparatorForKey(String key) {
        switch (key) {
            case "id":
                return Comparator.comparingLong(Organization::getId);
            case "name":
                return Comparator.comparing(o -> o.getName() == null ? "" : o.getName());
            case "annualTurnover":
                return Comparator.comparingLong(Organization::getAnnualTurnover);
            case "fullName":
                return Comparator.comparing(o -> o.getFullName() == null ? "" : o.getFullName());
            case "employeesCount":
                return Comparator.comparingInt(Organization::getEmployeesCount);
            case "creationDate":
                return Comparator.comparing(o -> o.getCreationDate() == null ? LocalDateTime.MIN : o.getCreationDate());
            case "type":
                return Comparator.comparing(o -> o.getType() == null ? "" : o.getType().name());
            case "coordinatesX":
                return Comparator.comparingDouble(o -> o.getCoordinates()!=null && o.getCoordinates().getX()!=null ? o.getCoordinates().getX() : Double.NaN);
            case "coordinatesY":
                return Comparator.comparingDouble(o -> o.getCoordinates()!=null && o.getCoordinates().getY()!=null ? o.getCoordinates().getY() : Double.NaN);
            case "zipCode":
                return Comparator.comparing(o -> o.getOfficialAddress()!=null && o.getOfficialAddress().getZipCode()!=null ? o.getOfficialAddress().getZipCode() : "");
            case "townName":
                return Comparator.comparing(o -> {
                    if (o.getOfficialAddress()==null || o.getOfficialAddress().getTown()==null || o.getOfficialAddress().getTown().getName()==null) return "";
                    return o.getOfficialAddress().getTown().getName();
                });
            default:
                return null;
        }
    }*/
}