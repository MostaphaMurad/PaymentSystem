package com.paymentsystem;

import com.paymentsystem.Models.Admin;
import com.paymentsystem.Models.Course;
import com.paymentsystem.Models.Trainer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class TestPaymentSystem {
    @Autowired private TestEntityManager entityManager;
    @Test
    public void test(){}
    @Test
    public void addAdmin(){
        Admin admin=new Admin();
        admin.setFname("Mostafa");
        admin.setLname("Murad");
        admin.setEmail("mo@gmail.com");
        admin.setPassword("1234");
        entityManager.persist(admin);
    }
    @Test
    public void assignManyTrainerForOneCourse(){
        Trainer t1=entityManager.find(Trainer.class,1);
        Course c1=entityManager.find(Course.class,"java");
        Course c2=entityManager.find(Course.class,"c++");
        Set<Course> courses=new HashSet<>();
        courses.add(c1);
        courses.add(c2);
        t1.setCourses(courses);
        entityManager.persist(t1);
    }
}
