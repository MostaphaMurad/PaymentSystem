package com.paymentsystem.Repositories;

import com.paymentsystem.Models.Accountant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AccountantRepository extends JpaRepository<Accountant,Integer> {
    Accountant findByEmail(String email);
    @Query("SELECT ac FROM Accountant ac WHERE ac.fname=?1 and ac.branch.name=?2")
    List<Accountant> findAllByFnameAndBranch(String name,String branch);
}
