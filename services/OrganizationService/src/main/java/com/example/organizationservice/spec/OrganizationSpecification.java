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

    public static Specification<Organization> filter(Map<String, String> filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicateList = new ArrayList<>();

            if (filters.containsKey("name")) {
                String name = filters.get("name");
                if (name != null && !name.isBlank()) {
                    predicateList.add(criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("name")),
                            "%" + name.toLowerCase() + "%"
                    ));
                }
            }

            if (filters.containsKey("fullName")) {
                String fullName = filters.get("fullName");
                if (fullName != null && !fullName.isBlank()) {
                    predicateList.add(criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("fullName")),
                            "%" + fullName.toLowerCase() + "%"
                    ));
                }
            }

            if (filters.containsKey("type")) {
                String typeValue = filters.get("type");
                if (typeValue != null && !typeValue.isBlank()) {
                    try {
                        OrganizationType type = OrganizationType.valueOf(typeValue);
                        predicateList.add(criteriaBuilder.equal(root.get("type"), type));
                    } catch (IllegalArgumentException e) {
                        // Если значение не соответствует enum — просто игнорируем
                    }
                }
            }

            // Числовые фильтры — проверяем, что значение не пустое
            if (filters.containsKey("annualTurnoverMin")) {
                String val = filters.get("annualTurnoverMin");
                if (val != null && !val.isBlank()) {
                    long min = Long.parseLong(val);
                    predicateList.add(criteriaBuilder.ge(root.get("annualTurnover"), min));
                }
            }

            if (filters.containsKey("annualTurnoverMax")) {
                String val = filters.get("annualTurnoverMax");
                if (val != null && !val.isBlank()) {
                    long max = Long.parseLong(val);
                    predicateList.add(criteriaBuilder.le(root.get("annualTurnover"), max));
                }
            }

            if (filters.containsKey("employeesCountMin")) {
                String val = filters.get("employeesCountMin");
                if (val != null && !val.isBlank()) {
                    int min = Integer.parseInt(val);
                    predicateList.add(criteriaBuilder.ge(root.get("employeesCount"), min));
                }
            }

            if (filters.containsKey("employeesCountMax")) {
                String val = filters.get("employeesCountMax");
                if (val != null && !val.isBlank()) {
                    int max = Integer.parseInt(val);
                    predicateList.add(criteriaBuilder.le(root.get("employeesCount"), max));
                }
            }

            if (filters.containsKey("coordinatesXMin")) {
                String val = filters.get("coordinatesXMin");
                if (val != null && !val.isBlank()) {
                    double min = Double.parseDouble(val);
                    predicateList.add(criteriaBuilder.ge(root.get("coordinates").get("x"), min));
                }
            }

            if (filters.containsKey("coordinatesXMax")) {
                String val = filters.get("coordinatesXMax");
                if (val != null && !val.isBlank()) {
                    double max = Double.parseDouble(val);
                    predicateList.add(criteriaBuilder.le(root.get("coordinates").get("x"), max));
                }
            }

            if (filters.containsKey("coordinatesYMin")) {
                String val = filters.get("coordinatesYMin");
                if (val != null && !val.isBlank()) {
                    double min = Double.parseDouble(val);
                    predicateList.add(criteriaBuilder.ge(root.get("coordinates").get("y"), min));
                }
            }

            if (filters.containsKey("coordinatesYMax")) {
                String val = filters.get("coordinatesYMax");
                if (val != null && !val.isBlank()) {
                    double max = Double.parseDouble(val);
                    predicateList.add(criteriaBuilder.le(root.get("coordinates").get("y"), max));
                }
            }

            if (filters.containsKey("zipCode")) {
                String zip = filters.get("zipCode");
                if (zip != null && !zip.isBlank()) {
                    Join<Organization, Address> addressJoin = root.join("officialAddress", JoinType.LEFT);
                    predicateList.add(criteriaBuilder.equal(addressJoin.get("zipCode"), zip));
                }
            }

            if (filters.containsKey("townName")) {
                String townName = filters.get("townName");
                if (townName != null && !townName.isBlank()) {
                    Join<Organization, Address> addressJoin = root.join("officialAddress", JoinType.LEFT);
                    Join<Address, Location> townJoin = addressJoin.join("town", JoinType.LEFT);
                    predicateList.add(criteriaBuilder.like(
                            criteriaBuilder.lower(townJoin.get("name")),
                            "%" + townName.toLowerCase() + "%"
                    ));
                }
            }

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };
    }
}
