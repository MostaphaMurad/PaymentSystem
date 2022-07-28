package com.paymentsystem.Repositories;

import com.paymentsystem.Models.Accountant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountantRepository extends JpaRepository<Accountant,Integer> {
    Accountant findByEmail(String email);
}
