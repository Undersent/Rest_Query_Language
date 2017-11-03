package com.example.demo.web.controller;

import com.example.demo.persistence.dao.IUserDao;
import com.example.demo.persistence.model.User;
import com.example.demo.web.util.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
public class UserController {

    @Autowired
    private IUserDao api;

    @GetMapping("/users")
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
}
