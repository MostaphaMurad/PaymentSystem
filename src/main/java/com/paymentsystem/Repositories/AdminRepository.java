package com.paymentsystem.Repositories;

import com.paymentsystem.Models.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin,Integer> {
    Admin findByEmail(String email);
}
