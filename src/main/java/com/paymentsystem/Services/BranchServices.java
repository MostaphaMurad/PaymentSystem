package com.paymentsystem.Services;

import com.paymentsystem.Models.Branch;
import com.paymentsystem.Repositories.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BranchServices {
    @Autowired private BranchRepository branchRepository;
    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }
}
