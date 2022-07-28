package com.paymentsystem.Models;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
public class Student extends User{
    @OneToOne
    @JoinColumn(name = "role_id")
    private Roles role;
    @ManyToOne
    @JoinColumn(name = "branch_name", nullable = true)
    private Branch branch;
    @Column(length = 15)
   private String course;
    @Column(length = 15)
    private String mobile;
    @Column(name = "father_name",length = 15)
    private String fatherName;
    @Column(name = "mother_name",length = 15)
    private String motherName;
    @Column(name = "birth_date")
    private Date date;
    @Column(name = "date_joining")
    private Date joinedDate;
    private double fee;
    private double paid;
    private double balance;
    @Column(length = 100)
    private String description;
    private String trainer;

    public String getCourse() {
        return course;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getMobile() {
        return mobile;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getFatherName() {
        return fatherName;
    }

    public void setFatherName(String fatherName) {
        this.fatherName = fatherName;
    }

    public String getMotherName() {
        return motherName;
    }

    public void setMotherName(String motherName) {
        this.motherName = motherName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getJoinedDate() {
        return joinedDate;
    }

    public void setJoinedDate(Date joinedDate) {
        this.joinedDate = joinedDate;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }

    public double getPaid() {
        return paid;
    }

    public void setPaid(double paid) {
        this.paid = paid;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTrainer() {
        return trainer;
    }

    public void setTrainer(String trainer) {
        this.trainer = trainer;
    }

    @Override
    public String toString() {
        return "Student{" +
                "role=" + role +
                ", branch=" + branch +
                ", course='" + course + '\'' +
                ", mobile='" + mobile + '\'' +
                ", fatherName='" + fatherName + '\'' +
                ", motherName='" + motherName + '\'' +
                ", date=" + date +
                ", joinedDate=" + joinedDate +
                ", fee=" + fee +
                ", paid=" + paid +
                ", balance=" + balance +
                ", description='" + description + '\'' +
                ", trainer='" + trainer + '\'' +
                '}';
    }
}
