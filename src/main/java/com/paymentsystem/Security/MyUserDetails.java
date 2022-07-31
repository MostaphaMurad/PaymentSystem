package com.paymentsystem.Security;

import com.paymentsystem.Models.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MyUserDetails implements UserDetails {
    private Admin admin=new Admin();
    private Accountant accountant=new Accountant();
    private User user=new User();
    private Student student=new Student();
    public String getEmailLoggedUser(){
        if(admin.getEmail()!=null)
            return admin.getEmail();
        else if(student.getEmail()!=null)
            return student.getEmail();
        else return accountant.getEmail();
    }
    public String getRoleName(){
        if(student.getRole()!=null)return student.getRole().getName();
        else if(admin.getRoles()!=null)return admin.getRoles().getName();
        else if(accountant.getRole()!=null)return accountant.getRole().getName();
        else return null;
    }
    public String getFnameLoggedUser(){
        if(admin.getFname()!=null)
            return admin.getFname();
        else if(student.getFname()!=null)
            return student.getFname();
        else return accountant.getFname();
    }
    public MyUserDetails(User user){
        this.user=user;
    }
    public MyUserDetails(Admin admin){
        this.admin=admin;
    }
    public MyUserDetails(Student student){
        this.student=student;
    }
    public MyUserDetails(Accountant accountant){
        this.accountant=accountant;
    }
    public MyUserDetails(Admin admin, Accountant accountant, Student student) {
        this.admin = admin;
        this.accountant = accountant;
        this.student = student;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<Roles>roles=new ArrayList<>();
        if(admin.getRoles()!=null) roles.add(admin.getRoles());

        if(student.getRole()!=null)
            roles.add(student.getRole());
        if(accountant.getRole()!=null)
            roles.add(accountant.getRole());
        List<SimpleGrantedAuthority> authorities=new ArrayList<>();
        for(Roles role:roles){
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        /*    System.out.println(role.getName());
            System.out.println(role.getId());*/
        }
        return authorities;
    }

    @Override
    public String getPassword() {
        if(student.getPassword()!=null)return student.getPassword();
        else if(admin.getPassword()!=null)return admin.getPassword();
        else return accountant.getPassword();
    }

    @Override
    public String getUsername() {
        if(student.getEmail()!=null)return student.getEmail();
        else if(admin.getEmail()!=null)return admin.getEmail();
        else return accountant.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
