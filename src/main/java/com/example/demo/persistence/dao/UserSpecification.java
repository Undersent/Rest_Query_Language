package com.example.demo.persistence.dao;

import com.example.demo.persistence.model.User;
import com.example.demo.web.util.SearchCriteria;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Getter
@AllArgsConstructor
public class UserSpecification implements Specification<User> {

    private SearchCriteria criteria;

    @Override
    public Predicate toPredicate(Root<User> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
        if (criteria.getOperation().equalsIgnoreCase(">")) {
            return criteriaBuilder.greaterThanOrEqualTo(
                    root.get(criteria.getKey()), criteria.getValue().toString()
            );
        } else if (criteria.getOperation().equalsIgnoreCase("<")) {
            return criteriaBuilder.lessThanOrEqualTo(
                    root.get(criteria.getKey()), criteria.getValue().toString()
            );
        } else if (criteria.getOperation().equalsIgnoreCase(":")) {
            if (root.get(criteria.getKey()).getJavaType() == String.class) {
                return criteriaBuilder.like(
                        root.get(criteria.getKey()), "%" + criteria.getValue() + "%"
                );
            } else {
                return criteriaBuilder.equal(root.get(criteria.getKey()), criteria.getValue());
            }
        }
        return null;
    }
}
