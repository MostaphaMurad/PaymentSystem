package com.paymentsystem.Services;

import com.paymentsystem.Models.Admin;
import com.paymentsystem.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServices {
    @Autowired private UserRepository userRepository;
    public Admin getAdminByEmail(String email) {
        Admin admin=userRepository.findByEmail(email);
        if(admin!=null)return admin;
        return null;
    }
}
