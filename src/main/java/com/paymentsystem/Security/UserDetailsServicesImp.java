package com.paymentsystem.Security;

import com.paymentsystem.Models.Accountant;
import com.paymentsystem.Models.Admin;
import com.paymentsystem.Models.Student;
import com.paymentsystem.Repositories.AccountantRepository;
import com.paymentsystem.Repositories.AdminRepository;
import com.paymentsystem.Repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServicesImp implements UserDetailsService {
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    AccountantRepository accountantRepository;
    @Autowired
    AdminRepository adminRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin=adminRepository.findByEmail(email);
        Accountant accountant=accountantRepository.findByEmail(email);
        Student student=studentRepository.findByEmail(email);
        if(admin!=null){
            return new MyUserDetails(admin);
        }
        else if(student!=null)return new MyUserDetails(student);
        else if(accountant!=null)return new MyUserDetails(accountant);
        else  throw new UsernameNotFoundException("Invalid Email");
    }
}
