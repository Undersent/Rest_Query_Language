package com.example.demo.persistence.dao;

import com.example.demo.config.PersistenceConfig;
import com.example.demo.persistence.model.User;
import com.example.demo.web.util.SearchCriteria;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsIn.isIn;
import static org.hamcrest.core.IsNot.not;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceConfig.class })
@Transactional
@TransactionConfiguration
public class UserDaoTest {

    @Autowired
    private IUserDao userApi;

    private User userKajko;

    private User userRomek;

    @Before
    public void init() {
        userKajko = new User();
        userKajko.setFirstName("Kajko");
        userKajko.setLastName("Kokosz");
        userKajko.setEmail("Kajko@Kokosz.com");
        userKajko.setAge(22);
        userApi.save(userKajko);

        userRomek = new User();
        userRomek.setFirstName("Romek");
        userRomek.setLastName("Kokosz");
        userRomek.setEmail("Romek@Atomek.com");
        userRomek.setAge(26);
        userApi.save(userRomek);
    }

    @Test
    public void givenFirstAndLastName_whenGettingListOfUsers_thenCorrect() {
        List<SearchCriteria> params = new ArrayList<SearchCriteria>();
        params.add(new SearchCriteria("firstName", ":", "Kajko"));
        params.add(new SearchCriteria("lastName", ":", "Kokosz"));

        List<User> results = userApi.searchUser(params);

        assertThat(userKajko, isIn(results));
        assertThat(userRomek, not(isIn(results)));
    }

    @Test
    public void givenLast_whenGettingListOfUsers_thenCorrect() {
        List<SearchCriteria> params = new ArrayList<SearchCriteria>();
        params.add(new SearchCriteria("lastName", ":", "Kokosz"));

        List<User> results = userApi.searchUser(params);
        assertThat(userKajko, isIn(results));
        assertThat(userRomek, isIn(results));
    }

    @Test
    public void givenLastAndAge_whenGettingListOfUsers_thenCorrect() {
        List<SearchCriteria> params = new ArrayList<SearchCriteria>();
        params.add(new SearchCriteria("lastName", ":", "Kokosz"));
        params.add(new SearchCriteria("age", "<", "24"));

        List<User> results = userApi.searchUser(params);

        assertThat(userKajko, isIn(results));
        assertThat(userRomek, not(isIn(results)));
    }

    @Test
    public void givenWrongFirstAndLast_whenGettingListOfUsers_thenCorrect() {
        List<SearchCriteria> params = new ArrayList<SearchCriteria>();
        params.add(new SearchCriteria("firstName", ":", "Bolek"));
        params.add(new SearchCriteria("lastName", ":", "Lolek"));

        List<User> results = userApi.searchUser(params);
        assertThat(userKajko, not(isIn(results)));
        assertThat(userRomek, not(isIn(results)));
    }

    @Test
    public void givenPartialFirst_whenGettingListOfUsers_thenCorrect() {
        List<SearchCriteria> params = new ArrayList<SearchCriteria>();
        params.add(new SearchCriteria("firstName", ":", "Ka"));

        List<User> results = userApi.searchUser(params);

        assertThat(userKajko, isIn(results));
        assertThat(userRomek, not(isIn(results)));
    }
}