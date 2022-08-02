package com.paymentsystem.Models;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Course {
    @Id
    private String name;
    private double courseSalary;
    private String courseDuration;
    @ManyToMany(mappedBy = "courses")
    private Set<Trainer> trainers=new HashSet<>();
    @ManyToMany(fetch = FetchType.EAGER,cascade = CascadeType.DETACH)
    @JoinTable(name = "branch_courses",joinColumns = {@JoinColumn(name = "course_name")},inverseJoinColumns = {@JoinColumn(name = "branch_name")})
    Set<Branch>branches=new HashSet<>();
    @ManyToMany(mappedBy = "courses")
    Set<Student>students=new HashSet<>();
    public Set<Branch> getBranches() {
        return branches;
    }

    public Set<Student> getStudents() {
        return students;
    }
    public void setStudents(Set<Student> students) {
        this.students = students;
    }
    public void setBranches(Set<Branch> branches) {
        this.branches = branches;
    }
    public Course(){}
    public Course(Set<Trainer> trainers) {
        this.trainers = trainers;
    }
    public void removeTrainer(Trainer trainer){
        this.trainers.remove(trainer);
        trainer.getCourses().remove(this);
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public double getCourseSalary() {
        return courseSalary;
    }
    public void setCourseSalary(double courseSalary) {
        this.courseSalary = courseSalary;
    }

    public String getCourseDuration() {
        return courseDuration;
    }

    public void setCourseDuration(String courseDuration) {
        this.courseDuration = courseDuration;
    }

    public Set<Trainer> getTrainers() {
        return trainers;
    }

    public void setTrainers(Set<Trainer> trainers) {
        this.trainers = trainers;
    }

    @Override
    public String toString() {
        return "Course{" +
                "name='" + name + '\'' +
                ", courseSalary=" + courseSalary +
                ", courseDuration='" + courseDuration + '\'' +
                ", trainers=" + trainers +
                ", branches=" + branches +
                ", students=" + students +
                '}';
    }
}
