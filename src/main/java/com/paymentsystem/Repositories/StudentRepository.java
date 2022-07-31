package com.paymentsystem.Repositories;

import com.paymentsystem.Models.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student,Integer> {
    Student findByEmail(String email);
    @Query("SELECT u FROM Student u WHERE u.fname = ?1 and u.branch.name = ?2")
    List<Student> findAllByFnameAndBranchName(String name,String branch);
}
