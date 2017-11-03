package com.example.demo.persistence.dao;

import com.example.demo.persistence.model.User;
import com.example.demo.web.util.SearchCriteria;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;


@Service
public class UserDao implements IUserDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<User> searchUser(List<SearchCriteria> params) {

        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<User> query = builder.createQuery(User.class);
        final Root<User> r = query.from(User.class);

        Predicate predicate = builder.conjunction();

        for (SearchCriteria param : params) {
            if (param.getOperation().equalsIgnoreCase(">")) {
                predicate = builder.and(predicate,
                        builder.greaterThanOrEqualTo(r.get(param.getKey()),
                                param.getValue().toString()));
            } else if (param.getOperation().equalsIgnoreCase("<")) {
                predicate = builder.and(predicate,
                        builder.lessThanOrEqualTo(r.get(param.getKey()),
                                param.getValue().toString()));
            } else if (param.getOperation().equalsIgnoreCase(":")) {
                if (r.get(param.getKey()).getJavaType() == String.class) {
                    predicate = builder.and(predicate,
                            builder.like(r.get(param.getKey()),
                                    "%" + param.getValue() + "%"));
                } else {
                    predicate = builder.and(predicate,
                            builder.equal(r.get(param.getKey()), param.getValue()));
                }
            }
        }
        query.where(predicate);

        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public void save(User entity) {
        entityManager.persist(entity);
    }

}
