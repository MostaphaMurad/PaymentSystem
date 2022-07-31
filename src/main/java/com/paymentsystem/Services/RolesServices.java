package com.paymentsystem.Services;

import com.paymentsystem.Models.Roles;
import com.paymentsystem.Repositories.RolesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolesServices {
    @Autowired private RolesRepository rolesRepository;

    public List<Roles> getAllRoles() {
        return rolesRepository.findAll();
    }
}
