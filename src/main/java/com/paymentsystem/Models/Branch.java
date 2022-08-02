package com.paymentsystem.Models;

import javax.persistence.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Branch {
    @Id
    private String name;
    @OneToMany(mappedBy = "branch",fetch = FetchType.EAGER,cascade = CascadeType.DETACH)
    private List<Student>students;
    @ManyToMany(mappedBy = "branches")
    private Set<Trainer> trainers=new HashSet<>();
    @ManyToMany(mappedBy = "branches")
    Set<Course>courses=new HashSet<>();
    public Set<Trainer> getTrainers() {
        return trainers;
    }

    public Set<Course> getCourses() {
        return courses;
    }

    public void setCourses(Set<Course> courses) {
        this.courses = courses;
    }

    public void setTrainers(Set<Trainer> trainers) {
        this.trainers = trainers;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    @Override
    public String toString() {
        return "Branch{" +
                "name='" + name + '\'' +
                ", students=" + students +
                '}';
    }
}
