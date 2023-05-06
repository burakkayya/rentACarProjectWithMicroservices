package com.kodlamaio.inventoryservice.business.rules;

import com.kodlamaio.commonpackage.constants.Messages;
import com.kodlamaio.inventoryservice.core.exceptions.BusinessException;
import com.kodlamaio.inventoryservice.repository.BrandRepository;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class BrandBusinessRules {
    BrandRepository repository;
    public void checkIfBrandExistsById(UUID id){
        if(!repository.existsById(id)) throw new BusinessException(Messages.Brand.NotExists);
    }
    public void checkIfBrandExistByName(String name){
        if(repository.existsByNameIgnoreCase(name)) throw new BusinessException(Messages.Brand.Exists);
    }
}
