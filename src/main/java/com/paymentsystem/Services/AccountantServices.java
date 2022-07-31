package com.paymentsystem.Services;

import com.paymentsystem.Models.Accountant;
import com.paymentsystem.Repositories.AccountantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountantServices {
    @Autowired private AccountantRepository accountantRepository;
    public List<Accountant> getAllAccs(String name, String branch) {
        return accountantRepository.findAllByFnameAndBranch(name,branch);
    }

    public Accountant addNewAccountant(Accountant accountant) {
        Accountant newAccountant=accountantRepository.save(accountant);
        return accountant;
    }

    public boolean deleteAccountantById(int id) {
        Accountant accountant=accountantRepository.findById(id).get();
        if(accountant!=null){
            accountantRepository.deleteById(id);
            return true;
        }
        else return false;
    }

    public Accountant getAccountantById(int id) {
        Accountant accountant=accountantRepository.findById(id).get();
        return accountant;
    }

    public List<Accountant> getAllAccountant() {
        return accountantRepository.findAll();
    }
}
