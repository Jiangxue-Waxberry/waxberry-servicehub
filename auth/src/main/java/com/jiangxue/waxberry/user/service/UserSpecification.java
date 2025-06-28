package com.jiangxue.waxberry.user.service;

import com.jiangxue.waxberry.user.entity.User;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> hasUsername(String username) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (username == null || username.trim().isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(root.get("username"), "%" + username + "%");
        };
    }

    public static Specification<User> hasRole(String role) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (role == null || role.trim().isEmpty()) {
                return null;
            }
            return criteriaBuilder.equal(root.get("role"), role);
        };
    }

    public static Specification<User> hasMobile(String mobile) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (mobile == null || mobile.trim().isEmpty()) {
                return null;
            }
            return criteriaBuilder.like(root.get("mobile"), "%" + mobile + "%");
        };
    }

    public static Specification<User> compositeSpec(String username, String role, String mobile, User.UserStatus status, LocalDate startDate, LocalDate endDate) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (username != null && !username.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("username"), "%" + username + "%"));
            }
            if (role != null && !role.trim().isEmpty()) {
                predicates.add(criteriaBuilder.equal(root.get("role"), role));
            }
            if (mobile != null && !mobile.trim().isEmpty()) {
                predicates.add(criteriaBuilder.like(root.get("mobile"), "%" + mobile + "%"));
            }
            if (status != null) {
                predicates.add(criteriaBuilder.equal(root.get("status"), status));

            }

            if (startDate != null && endDate != null) {
                LocalDateTime startDateTime = startDate.atStartOfDay();
                LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
                predicates.add(criteriaBuilder.between(root.get("createdAt"), startDateTime, endDateTime));
            } else if (startDate != null) {
                LocalDateTime startDateTime = startDate.atStartOfDay();
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdAt"), startDateTime));
            } else if (endDate != null) {
                LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX);
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdAt"), endDateTime));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
