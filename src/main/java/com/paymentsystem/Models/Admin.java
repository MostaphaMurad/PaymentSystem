package com.paymentsystem.Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
@Entity
public class Admin extends User {
    @Column(unique = true,nullable = false,length = 45)
    private String email;
    @OneToOne
    @JoinColumn(name = "role_id")
    Roles roles;
    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }

    public Roles getRoles() {
        return roles;
    }

    public void setRoles(Roles roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "email='" + email + '\'' +
                ", roles=" + roles +
                '}';
    }
}
