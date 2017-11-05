package com.example.demo.persistence.dao;

import com.example.demo.config.PersistenceConfig;
import com.example.demo.persistence.dao.repository.MyUserRepository;
import com.example.demo.persistence.model.MyUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsEmptyIterable.emptyIterable;
import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.IsNot.not;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceConfig.class })
@Transactional
@Rollback
public class MyUserPredicateTest {

    @Autowired
    private MyUserRepository repo;

    private MyUser userJohn;
    private MyUser userTom;

    @Before
    public void setUp() throws Exception {
        userJohn = new MyUser();
        userJohn.setFirstName("John");
        userJohn.setLastName("Doe");
        userJohn.setEmail("john@doe.com");
        userJohn.setAge(22);
        repo.save(userJohn);

        userTom = new MyUser();
        userTom.setFirstName("Tom");
        userTom.setLastName("Doe");
        userTom.setEmail("tom@doe.com");
        userTom.setAge(26);
        repo.save(userTom);

    }

    @Test
    public void givenLast_whenGettingListOfUsers_thenCorrect() {
        final MyUserPredicateBuilder builder = new MyUserPredicateBuilder().with("lastName", ":", "Doe");

        final Iterable<MyUser> results = repo.findAll(builder.build());
        assertThat(results, containsInAnyOrder(userJohn, userTom));
    }

    @Test
    public void givenFirstAndLastName_whenGettingListOfUsers_thenCorrect() {
        final MyUserPredicateBuilder builder = new MyUserPredicateBuilder().with("firstName", ":", "john").with("lastName", ":", "doe");

        final Iterable<MyUser> results = repo.findAll(builder.build());

        assertThat(results, contains(userJohn));
        assertThat(results, not(contains(userTom)));
    }

    @Test
    public void givenWrongFirstAndLast_whenGettingListOfUsers_thenCorrect() {
        final MyUserPredicateBuilder builder = new MyUserPredicateBuilder().with("firstName", ":", "adam").with("lastName", ":", "fox");

        final Iterable<MyUser> results = repo.findAll(builder.build());
        assertThat(results, emptyIterable());
    }
}