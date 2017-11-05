package com.example.demo.web.controller;

import com.example.demo.persistence.dao.IUserDao;
import com.example.demo.persistence.dao.MyUserPredicateBuilder;
import com.example.demo.persistence.dao.UserSpecificationBuilder;
import com.example.demo.persistence.dao.repository.MyUserRepository;
import com.example.demo.persistence.dao.repository.UserRepository;
import com.example.demo.persistence.model.MyUser;
import com.example.demo.persistence.model.User;
import com.example.demo.web.util.SearchCriteria;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.querydsl.binding.QuerydslPredicate;
import org.springframework.web.bind.annotation.*;
import com.querydsl.core.types.Predicate;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class UserController {

    private final IUserDao api;
    private final UserRepository repo;
    private final MyUserRepository myUserRepository;
    @Autowired
    public UserController(IUserDao api, UserRepository repo, MyUserRepository myUserRepository) {
        this.api = api;
        this.repo = repo;
        this.myUserRepository = myUserRepository;
    }

    @GetMapping("/users") //users?search=lastName:Bozenka,age>25
    public List<User> findAll(@RequestParam(value = "search", required = false)String search){
        List<SearchCriteria> params = new ArrayList<SearchCriteria>();
        if(search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)([:<>])(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while(matcher.find()) {
                params.add(new SearchCriteria(matcher.group(1),
                        matcher.group(2), matcher.group(3)));
            }
        }
        return api.searchUser(params);
    }

    @GetMapping("/users/specs") //users/specs?search=lastName:Bozenka,age>25
    public List<User> search(@RequestParam(value = "search") String search) {
        UserSpecificationBuilder builder = new UserSpecificationBuilder();
        Pattern pattern = Pattern.compile("(\\w+?)([:<>])(\\w+?),");
        Matcher matcher = pattern.matcher(search + ",");
        while (matcher.find()) {
            builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
        }

        Specification<User> spec = builder.build();
        return repo.findAll(spec);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/myusers")
    @ResponseBody//http://localhost:8080/users?search=lastName:doe,age>25
    public Iterable<MyUser> findAllByQuerydsl(@RequestParam(value = "search") String search) {
        MyUserPredicateBuilder builder = new MyUserPredicateBuilder();
        if (search != null) {
            Pattern pattern = Pattern.compile("(\\w+?)([:<>])(\\w+?),");
            Matcher matcher = pattern.matcher(search + ",");
            while (matcher.find()) {
                builder.with(matcher.group(1), matcher.group(2), matcher.group(3));
            }
        }
        BooleanExpression exp = builder.build();
        return myUserRepository.findAll(exp);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/api/myusers")
    @ResponseBody
    public Iterable<MyUser> findAllByWebQuerydsl(@QuerydslPredicate(root = MyUser.class) Predicate predicate) {
        return myUserRepository.findAll(predicate);
    }
}
