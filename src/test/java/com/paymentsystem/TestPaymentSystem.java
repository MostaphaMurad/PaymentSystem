package com.paymentsystem;

import com.paymentsystem.Models.Admin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

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
}
