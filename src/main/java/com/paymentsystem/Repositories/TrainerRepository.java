package com.paymentsystem.Repositories;

import com.paymentsystem.Models.Trainer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TrainerRepository extends JpaRepository<Trainer ,Integer> {
}
