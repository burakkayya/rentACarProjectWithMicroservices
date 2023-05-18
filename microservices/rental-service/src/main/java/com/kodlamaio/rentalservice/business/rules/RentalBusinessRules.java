package com.kodlamaio.rentalservice.business.rules;

import com.kodlamaio.commonpackage.utils.constants.Messages;
import com.kodlamaio.commonpackage.utils.exceptions.BusinessException;
import com.kodlamaio.rentalservice.repository.RentalRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class RentalBusinessRules {
    private final RentalRepository repository;
    public void checkIfRentalExistsById(UUID id){
        if(!repository.existsById(id)){
            throw new BusinessException(Messages.Rental.NotExists);
        }
    }
}