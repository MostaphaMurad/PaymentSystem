package com.paymentsystem.Models;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class Accountant extends User {
    private double salary;
    @OneToOne
    @JoinColumn(name = "branch_name")
    Branch branch;
    @OneToOne
    @JoinColumn(name = "role_id")
    Roles role;
    public double getSalary() {
        return salary;
    }


    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Branch getBranch() {
        return branch;
    }

    public void setBranch(Branch branch) {
        this.branch = branch;
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return "Accountant{" +
                "salary=" + salary +
                ", branch=" + branch +
                ", role=" + role +
                '}';
    }
}
