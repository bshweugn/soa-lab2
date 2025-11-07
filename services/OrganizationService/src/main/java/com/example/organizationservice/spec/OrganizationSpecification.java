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
import java.util.Objects;

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

            if (filters == null || filters.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            for (String rawKey : filters.keySet()) {
                if (rawKey == null) {
                    return criteriaBuilder.disjunction();
                }
                if ("sort".equalsIgnoreCase(rawKey)) {
                    continue;
                }
                if (!ALLOWED_KEYS.contains(rawKey)) {
                    return criteriaBuilder.disjunction();
                }
            }

            boolean allNull = filters.values().stream().allMatch(Objects::isNull);
            if (allNull) {
                return criteriaBuilder.conjunction();
            }

            List<Predicate> predicateList = new ArrayList<>();

            if (filters.get("name") != null) {
                String raw = filters.get("name");
                String pattern;
                if (raw.trim().isEmpty() && !raw.isEmpty()) {
                    pattern = "%" + raw + "%";
                    predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern));
                } else {
                    String norm = raw.trim().toLowerCase();
                    if (!norm.isEmpty()) {
                        pattern = "%" + norm + "%";
                        predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), pattern));
                    }
                }
            }

            if (filters.get("fullName") != null) {
                String raw = filters.get("fullName");
                String pattern;
                if (raw.trim().isEmpty() && !raw.isEmpty()) {
                    pattern = "%" + raw + "%";
                    predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), pattern));
                } else {
                    String norm = raw.trim().toLowerCase();
                    if (!norm.isEmpty()) {
                        pattern = "%" + norm + "%";
                        predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")), pattern));
                    }
                }
            }

            if (hasNonBlank(filters.get("type"))) {
                try {
                    OrganizationType type = OrganizationType.valueOf(filters.get("type").trim());
                    predicateList.add(criteriaBuilder.equal(root.get("type"), type));
                } catch (IllegalArgumentException ex) {
                    return criteriaBuilder.disjunction();
                }
            }

            if (hasNonBlank(filters.get("annualTurnoverMin"))) {
                try {
                    long min = Long.parseLong(filters.get("annualTurnoverMin").trim());
                    predicateList.add(criteriaBuilder.ge(root.get("annualTurnover"), min));
                } catch (NumberFormatException ex) {
                    return criteriaBuilder.disjunction();
                }
            }

            if (hasNonBlank(filters.get("annualTurnoverMax"))) {
                try {
                    long max = Long.parseLong(filters.get("annualTurnoverMax").trim());
                    predicateList.add(criteriaBuilder.le(root.get("annualTurnover"), max));
                } catch (NumberFormatException ex) {
                    return criteriaBuilder.disjunction();
                }
            }

            if (hasNonBlank(filters.get("employeesCountMin"))) {
                try {
                    int min = Integer.parseInt(filters.get("employeesCountMin").trim());
                    predicateList.add(criteriaBuilder.ge(root.get("employeesCount"), min));
                } catch (NumberFormatException ex) {
                    return criteriaBuilder.disjunction();
                }
            }

            if (hasNonBlank(filters.get("employeesCountMax"))) {
                try {
                    int max = Integer.parseInt(filters.get("employeesCountMax").trim());
                    predicateList.add(criteriaBuilder.le(root.get("employeesCount"), max));
                } catch (NumberFormatException ex) {
                    return criteriaBuilder.disjunction();
                }
            }

            if (hasNonBlank(filters.get("coordinatesXMin"))) {
                try {
                    double min = Double.parseDouble(filters.get("coordinatesXMin").trim());
                    predicateList.add(criteriaBuilder.ge(root.get("coordinates").get("x"), min));
                } catch (NumberFormatException ex) {
                    return criteriaBuilder.disjunction();
                }
            }

            if (hasNonBlank(filters.get("coordinatesXMax"))) {
                try {
                    double max = Double.parseDouble(filters.get("coordinatesXMax").trim());
                    predicateList.add(criteriaBuilder.le(root.get("coordinates").get("x"), max));
                } catch (NumberFormatException ex) {
                    return criteriaBuilder.disjunction();
                }
            }

            if (hasNonBlank(filters.get("coordinatesYMin"))) {
                try {
                    double min = Double.parseDouble(filters.get("coordinatesYMin").trim());
                    predicateList.add(criteriaBuilder.ge(root.get("coordinates").get("y"), min));
                } catch (NumberFormatException ex) {
                    return criteriaBuilder.disjunction();
                }
            }

            if (hasNonBlank(filters.get("coordinatesYMax"))) {
                try {
                    double max = Double.parseDouble(filters.get("coordinatesYMax").trim());
                    predicateList.add(criteriaBuilder.le(root.get("coordinates").get("y"), max));
                } catch (NumberFormatException ex) {
                    return criteriaBuilder.disjunction();
                }
            }

            if (hasNonBlank(filters.get("zipCode"))) {
                Join<Organization, Address> addressJoin = root.join("officialAddress", JoinType.LEFT);
                predicateList.add(criteriaBuilder.equal(addressJoin.get("zipCode"), filters.get("zipCode").trim()));
            }

            if (filters.get("townName") != null) {
                String raw = filters.get("townName");
                String pattern;
                Join<Organization, Address> addressJoin = root.join("officialAddress", JoinType.LEFT);
                Join<Address, Location> townJoin = addressJoin.join("town", JoinType.LEFT);

                if (raw.trim().isEmpty() && !raw.isEmpty()) {
                    pattern = "%" + raw + "%";
                    predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(townJoin.get("name")), pattern));
                } else {
                    String norm = raw.trim().toLowerCase();
                    if (!norm.isEmpty()) {
                        pattern = "%" + norm + "%";
                        predicateList.add(criteriaBuilder.like(criteriaBuilder.lower(townJoin.get("name")), pattern));
                    }
                }
            }

            if (predicateList.isEmpty()) {
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.and(predicateList.toArray(new Predicate[0]));
        };
    }

    private static boolean hasNonBlank(String s) {
        return s != null && !s.isBlank();
    }
}
