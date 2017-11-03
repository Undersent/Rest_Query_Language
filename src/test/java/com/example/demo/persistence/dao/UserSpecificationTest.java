package com.example.demo.persistence.dao;

import com.example.demo.config.PersistenceConfig;
import com.example.demo.persistence.dao.repository.UserRepository;
import com.example.demo.persistence.model.User;
import com.example.demo.web.util.SearchCriteria;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

import javax.transaction.Transactional;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.collection.IsIn.isIn;
import static org.hamcrest.core.IsNot.not;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { PersistenceConfig.class})
@Transactional
@Rollback
public class UserSpecificationTest {

    @Autowired
    private UserRepository repository;

    private User userJohn;
    private User userTom;

    @Before
    public void setUp() throws Exception {
        userJohn = new User();
        userJohn.setFirstName("John");
        userJohn.setLastName("Doe");
        userJohn.setEmail("john@doe.com");
        userJohn.setAge(22);
        repository.save(userJohn);

        userTom = new User();
        userTom.setFirstName("Tom");
        userTom.setLastName("Doe");
        userTom.setEmail("tom@doe.com");
        userTom.setAge(26);
        repository.save(userTom);
    }

    @Test
    public void givenLast_whenGettingListOfUsers_thenCorrect() {
        UserSpecification spec =
                new UserSpecification(new SearchCriteria("lastName", ":", "Doe"));

        List<User> results = repository.findAll(spec);

        assertThat(userJohn, isIn(results));
        assertThat(userTom, isIn(results));
    }

    @Test
    public void givenFirstAndLastName_whenGettingListOfUsers_thenCorrect() {
        UserSpecification spec1 =
                new UserSpecification(new SearchCriteria("firstName", ":", "John"));
       UserSpecification spec2 =
                new UserSpecification(new SearchCriteria("lastName", ":", "Doe"));

        List<User> results = repository.findAll(Specifications
                .where(spec1)
                .and(spec2));

        assertThat(userJohn, isIn(results));
        assertThat(userTom, not(isIn(results)));
    }

    @Test
    public void givenLastAndAge_whenGettingListOfUsers_thenCorrect() {
        UserSpecification spec1 =
                new UserSpecification(new SearchCriteria("age", ">", "25"));
        UserSpecification spec2 =
                new UserSpecification(new SearchCriteria("lastName", ":", "Doe"));

        List<User> results =
                repository.findAll(Specifications
                        .where(spec1)
                        .and(spec2));

        assertThat(userTom, isIn(results));
        assertThat(userJohn, not(isIn(results)));
    }

    @Test
    public void givenWrongFirstAndLast_whenGettingListOfUsers_thenCorrect() {
        UserSpecification spec1 =
                new UserSpecification(new SearchCriteria("firstName", ":", "Adam"));
        UserSpecification spec2 =
                new UserSpecification(new SearchCriteria("lastName", ":", "Fox"));

        List<User> results =
                repository.findAll(Specifications
                        .where(spec1)
                        .and(spec2));

        assertThat(userJohn, not(isIn(results)));
        assertThat(userTom, not(isIn(results)));
    }

    @Test
    public void givenPartialFirst_whenGettingListOfUsers_thenCorrect() {
        UserSpecification spec =
                new UserSpecification(new SearchCriteria("firstName", ":", "Jo"));

        List<User> results = repository.findAll(spec);

        assertThat(userJohn, isIn(results));
        assertThat(userTom, not(isIn(results)));
    }

}