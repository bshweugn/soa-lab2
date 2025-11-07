package com.example.organizationservice.spec;

import com.example.common.model.Organization;
import com.example.common.model.Address;
import com.example.common.model.Location;
import com.example.common.model.OrganizationType;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class OrganizationSpecification {

    private static final Set<String> ALLOWED_KEYS = Set.of(
            "name", "fullName", "type",
            "annualTurnoverMin", "annualTurnoverMax",
            "employeesCountMin", "employeesCountMax",
            "coordinatesXMin", "coordinatesXMax",
            "coordinatesYMin", "coordinatesYMax",
            "zipCode", "townName"

    );

    public static Specification<Organization> filter(Map<String, String> filters) {
        return (root, query, criteriaBuilder) -> {

            if(filters == null || filters.isEmpty()){
                return criteriaBuilder.conjunction();
            }



            for(String rawKey : filters.keySet()) {
               if(rawKey == null){
                   return criteriaBuilder.disjunction();
               }

               if("sort".equalsIgnoreCase(rawKey)){
                   continue;
               }

               if(!ALLOWED_KEYS.contains(rawKey)){
                   return criteriaBuilder.disjunction();
               }
            }

            boolean allBlank = filters.values().stream().allMatch(v -> v == null || v.isBlank());
            if(allBlank){
                return criteriaBuilder.conjunction();
            }

            List<Predicate> predicateList = new ArrayList<>();

            if(hasText(filters.get("name"))){
                predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + filters.get("name").toLowerCase() + "%"));
            }

            if (hasText(filters.get("fullName"))) {
                predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), "%" + filters.get("fullName").toLowerCase() + "%"));
            }

            if (hasText(filters.get("type"))) {
                try {
                    OrganizationType type = OrganizationType.valueOf(filters.get("type"));
                    predicateList.add(criteriaBuilder.equal(root.get("type"), type));
                } catch (IllegalArgumentException ex) {
                    return criteriaBuilder.disjunction();
                }
            }

            if (hasText(filters.get("annualTurnoverMin"))) {
                try {
                    long min = Long.parseLong(filters.get("annualTurnoverMin"));
                    predicateList.add(criteriaBuilder.ge(root.get("annualTurnover"), min));
                } catch (NumberFormatException ex) {
                    return criteriaBuilder.disjunction();
                }
            }

            if (hasText(filters.get("annualTurnoverMax"))) {
                try {
                    long max = Long.parseLong(filters.get("annualTurnoverMax"));
                    predicateList.add(criteriaBuilder.le(root.get("annualTurnover"), max));
                } catch (NumberFormatException ex) {
                    return criteriaBuilder.disjunction();
                }
            }

            if (hasText(filters.get("employeesCountMin"))) {
                try {
                    int min = Integer.parseInt(filters.get("employeesCountMin"));
                    predicateList.add(criteriaBuilder.ge(root.get("employeesCount"), min));
                } catch (NumberFormatException ex) {
                    return criteriaBuilder.disjunction();
                }
            }

            if (hasText(filters.get("employeesCountMax"))) {
                try {
                    int max = Integer.parseInt(filters.get("employeesCountMax"));
                    predicateList.add(criteriaBuilder.le(root.get("employeesCount"), max));
                } catch (NumberFormatException ex) {
                    return criteriaBuilder.disjunction();
                }
            }

            if (hasText(filters.get("coordinatesXMin"))) {
                try {
                    double min = Double.parseDouble(filters.get("coordinatesXMin"));
                    predicateList.add(criteriaBuilder.ge(root.get("coordinates").get("x"), min));
                } catch (NumberFormatException ex) {
                    return criteriaBuilder.disjunction();
                }
            }

            if (hasText(filters.get("coordinatesXMax"))) {
                try {
                    double max = Double.parseDouble(filters.get("coordinatesXMax"));
                    predicateList.add(criteriaBuilder.le(root.get("coordinates").get("x"), max));
                } catch (NumberFormatException ex) {
                    return criteriaBuilder.disjunction();
                }
            }

            if (hasText(filters.get("coordinatesYMin"))) {
                try {
                    double min = Double.parseDouble(filters.get("coordinatesYMin"));
                    predicateList.add(criteriaBuilder.ge(root.get("coordinates").get("y"), min));
                } catch (NumberFormatException ex) {
                    return criteriaBuilder.disjunction();
                }
            }

            if (hasText(filters.get("coordinatesYMax"))) {
                try {
                    double max = Double.parseDouble(filters.get("coordinatesYMax"));
                    predicateList.add(criteriaBuilder.le(root.get("coordinates").get("y"), max));
                } catch (NumberFormatException ex) {
                    return criteriaBuilder.disjunction();
                }
            }

            if (hasText(filters.get("zipCode"))) {
                Join<Organization, Address> addressJoin = root.join("officialAddress", JoinType.LEFT);
                predicateList.add(criteriaBuilder.equal(addressJoin.get("zipCode"), filters.get("zipCode")));
            }

            if (hasText(filters.get("townName"))) {
                Join<Organization, Address> addressJoin = root.join("officialAddress", JoinType.LEFT);
                Join<Address, Location> townJoin = addressJoin.join("town", JoinType.LEFT);
                predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(townJoin.get("name")), "%" + filters.get("townName").toLowerCase() + "%"));
            }

            if(predicateList.isEmpty()){
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };
    }

    private static boolean hasText(String s){
        return s != null && !s.isBlank();
    }
}
