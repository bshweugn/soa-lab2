package com.example.organizationservice.spec;

import com.example.organizationservice.model.Address;
import com.example.organizationservice.model.Location;
import com.example.organizationservice.model.Organization;
import com.example.organizationservice.model.OrganizationType;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrganizationSpecification {

    public static Specification<Organization> filter(Map<String, String> filters){
        return ((root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            if(filters.containsKey("name")){
                predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                        "%" + filters.get("name").toLowerCase() + "%"));
            }

            if (filters.containsKey("fullName")){
                String fullName = filters.get("fullName").toLowerCase();
                predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), "%" + fullName + "%"));
            }

            if(filters.containsKey("type")){
                OrganizationType type = OrganizationType.valueOf(filters.get("type"));
                predicateList.add(criteriaBuilder.equal(root.get("type"), type));
            }


            if(filters.containsKey("annualTurnoverMin")){
                long min = Long.parseLong(filters.get("annualTurnoverMin"));
                predicateList.add(criteriaBuilder.ge(root.get("annualTurnover"), min));
            }

            if(filters.containsKey("annualTurnoverMax")){
                long max = Long.parseLong(filters.get("annualTurnoverMax"));
                predicateList.add(criteriaBuilder.le(root.get("annualTurnover"), max));
            }

            if(filters.containsKey("employeesCountMin")){
                int min = Integer.parseInt(filters.get("employeesCountMin"));
                predicateList.add(criteriaBuilder.ge(root.get("employeesCount"), min));
            }

            if(filters.containsKey("employeesCountMax")){
                int max = Integer.parseInt(filters.get("employeesCountMax"));
                predicateList.add(criteriaBuilder.le(root.get("employeesCount"), max));
            }

            if(filters.containsKey("coordinatesXMin")){
                double min = Double.parseDouble(filters.get("coordinatesXMin"));
                predicateList.add(criteriaBuilder.ge(root.get("coordinates").get("x"), min));
            }

            if(filters.containsKey("coordinatesXMax")){
                double max = Double.parseDouble(filters.get("coordinatesXMax"));
                predicateList.add(criteriaBuilder.le(root.get("coordinates").get("x"), max));
            }

            if(filters.containsKey("coordinatesYMin")){
                double min = Double.parseDouble(filters.get("coordinatesYMin"));
                predicateList.add(criteriaBuilder.ge(root.get("coordinates").get("y"), min));
            }

            if(filters.containsKey("coordinatesYMax")){
                double max = Double.parseDouble(filters.get("coordinatesYMax"));
                predicateList.add(criteriaBuilder.le(root.get("coordinates").get("y"), max));
            }

            if(filters.containsKey("zipCode")){
                Join<Organization, Address> addressJoin = root.join("officialAddress");
                predicateList.add(criteriaBuilder.equal(addressJoin.get("zipCode"), filters.get("zipCode")));
            }

            if(filters.containsKey("townName")){
                Join<Organization, Address> addressJoin = root.join("officialAddress", JoinType.LEFT);
                Join<Address, Location> townJoin = addressJoin.join("town", JoinType.LEFT);
                String townName = filters.get("townName").toLowerCase();
                predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(townJoin.get("name")), "%" + townName + "%"));
            }

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        });
    }
}