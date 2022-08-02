package com.paymentsystem.Repositories;

import com.paymentsystem.Models.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course,String> {
    Course findByName(String courseName);
}
