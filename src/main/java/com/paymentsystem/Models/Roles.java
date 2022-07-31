package com.paymentsystem.Models;

import javax.persistence.*;

@Entity
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roleId;
    @Column(nullable = true,length = 15,name = "role_name")
    private String name;

    @Override
    public String toString() {
        return "Roles{" +
                "roleId=" + roleId +
                ", name='" + name + '\'' +
                '}';
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
