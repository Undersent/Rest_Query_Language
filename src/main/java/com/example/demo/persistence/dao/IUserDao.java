package com.example.demo.persistence.dao;

import com.example.demo.persistence.model.User;
import com.example.demo.web.util.SearchCriteria;

import java.util.List;

public interface IUserDao {
    List<User> searchUser(List<SearchCriteria> params);

    void save(User entity);
}
