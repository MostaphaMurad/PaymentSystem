package com.paymentsystem.Services;

import com.paymentsystem.Models.Course;
import com.paymentsystem.Repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServices {
    @Autowired private CourseRepository courseRepository;
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    public Boolean addNewCourse(Course course) {
        Course c=courseRepository.save(course);
        if(c!=null)return true;
        return false;
    }

    public Course getCourseByName(String courseName) {
        Course course=courseRepository.findByName(courseName);
        return course;
    }

    public Boolean deleteCourseByName(String courseName) {
        Course course=courseRepository.findByName(courseName);
        if(course!=null){
            courseRepository.deleteById(courseName);
            return true;
        }return false;
    }
}
