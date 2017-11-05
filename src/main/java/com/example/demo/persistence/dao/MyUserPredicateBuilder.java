package com.example.demo.persistence.dao;

import com.example.demo.web.util.SearchCriteria;
import com.querydsl.core.types.dsl.BooleanExpression;

import java.util.ArrayList;
import java.util.List;

public final class MyUserPredicateBuilder {
    private final List<SearchCriteria> params;

    public MyUserPredicateBuilder() {
        params = new ArrayList<>();
    }

    public MyUserPredicateBuilder with(final String key, final String operation, final Object value) {
        params.add(new SearchCriteria(key, operation, value));
        return this;
    }

    public BooleanExpression build() {
        if (params.size() == 0) {
            return null;
        }

        final List<BooleanExpression> predicates = new ArrayList<>();
        MyUserPredicate predicate;
        for (final SearchCriteria param : params) {
            predicate = new MyUserPredicate(param);
            final BooleanExpression exp = predicate.getPredicate();
            if (exp != null) {
                predicates.add(exp);
            }
        }

        BooleanExpression result = predicates.get(0);
        for (int i = 1; i < predicates.size(); i++) {
            result = result.and(predicates.get(i));
        }
        return result;
    }
}
