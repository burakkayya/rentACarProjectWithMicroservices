package com.kodlamaio.inventoryservice.business.rules;

import com.kodlamaio.commonpackage.constants.Messages;
import com.kodlamaio.inventoryservice.core.exceptions.BusinessException;
import com.kodlamaio.inventoryservice.repository.ModelRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class ModelBusinessRules {
    private final ModelRepository repository;
    public void checkIfModelExistsById(UUID id){
        if(!repository.existsById(id)){
            throw new BusinessException(Messages.Model.NotExists);
        }
    }
    public void checkIfModelExistsByName(String name){
        if(repository.existsByNameIgnoreCase(name)){
            throw new BusinessException(Messages.Model.Exists);
        }
    }
}
