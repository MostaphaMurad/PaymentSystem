package com.paymentsystem.Repositories;

import com.paymentsystem.Models.Admin;
import com.paymentsystem.Models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    Admin findByEmail(String email);
}
