package com.paymentsystem.Services;

import com.paymentsystem.Models.Trainer;
import com.paymentsystem.Repositories.TrainerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrainerServices {
    @Autowired private TrainerRepository trainerRepository;
    public boolean addNewTrainer(Trainer trainer) {
        Trainer savedTrainer=trainerRepository.save(trainer);
        if(savedTrainer!=null)return true;
        return false;
    }

    public List<Trainer> getAllTrainer() {
        return trainerRepository.findAll();
    }
}
