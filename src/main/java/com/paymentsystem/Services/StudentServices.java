package com.paymentsystem.Services;

import com.paymentsystem.Models.Student;
import com.paymentsystem.Repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServices {
    @Autowired private StudentRepository studentRepository;
    public Student addNewStudent(Student student) {
        Student newStd=studentRepository.save(student);
        if(newStd!=null)return newStd;
        else return null;
    }

    public List<Student> getAllStds(String name, String branch) {
       List<Student>students= studentRepository.findAllByFnameAndBranchName(name,branch);
       return students;
    }

    public boolean deleteStudentById(int id) {
        Student student=studentRepository.findById(id).get();
        if(student!=null){
            studentRepository.deleteById(id);
            return true;
        }
        else return false;
    }

    public Student getStudentById(int id) {
        Student student=studentRepository.findById(id).get();
        return student;
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    public Student getStudentByEmail(String logedEmail) {
        Student student=studentRepository.findByEmail(logedEmail);
        return student;
    }

    public boolean saveStudentWithEnrolledCourses(Student logedStudent) {
        Student student=studentRepository.save(logedStudent);
        if(student!=null)return true;
        return false;
    }
}
