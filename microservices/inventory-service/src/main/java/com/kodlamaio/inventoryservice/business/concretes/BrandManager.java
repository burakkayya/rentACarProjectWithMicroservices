package com.kodlamaio.inventoryservice.business.concretes;

import com.kodlamaio.commonpackage.events.inventory.BrandDeletedEvent;
import com.kodlamaio.commonpackage.utils.kafka.producer.KafkaProducer;
import com.kodlamaio.commonpackage.utils.mappers.ModelMapperService;
import com.kodlamaio.inventoryservice.business.abstracts.BrandService;
import com.kodlamaio.inventoryservice.business.dto.requests.create.CreateBrandRequest;
import com.kodlamaio.inventoryservice.business.dto.requests.update.UpdateBrandRequest;
import com.kodlamaio.inventoryservice.business.dto.responses.create.CreateBrandResponse;
import com.kodlamaio.inventoryservice.business.dto.responses.get.GetAllBrandsResponse;
import com.kodlamaio.inventoryservice.business.dto.responses.get.GetBrandResponse;
import com.kodlamaio.inventoryservice.business.dto.responses.update.UpdateBrandResponse;
import com.kodlamaio.inventoryservice.business.rules.BrandBusinessRules;
import com.kodlamaio.inventoryservice.entities.Brand;
import com.kodlamaio.inventoryservice.repository.BrandRepository;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class BrandManager implements BrandService {

    private final BrandRepository repository;
    private final ModelMapperService mapper;
    private final BrandBusinessRules rules;
    private final KafkaProducer producer;
    @Override
    @Cacheable(value = "brand_list")
    public List<GetAllBrandsResponse> getAll() {
        List<Brand> brands = repository.findAll();
        List<GetAllBrandsResponse> responses = brands
                .stream()
                .map(brand -> mapper.forResponse().map(brand, GetAllBrandsResponse.class))
                .toList();
        return responses;
    }

    @Override
    public GetBrandResponse getById(UUID id) {
        rules.checkIfBrandExistsById(id);
        Brand brand = repository.findById(id).orElseThrow();
        GetBrandResponse response = mapper.forResponse().map(brand, GetBrandResponse.class);
        return response;
    }

    @Override
    @CacheEvict(value = "brand_list", allEntries = true)
    public CreateBrandResponse add(CreateBrandRequest request) {

        rules.checkIfBrandExistByName(request.getName());
        Brand brand= mapper.forRequest().map(request,Brand.class);
        brand.setId(null);
        Brand createdBrand = repository.save(brand);
        CreateBrandResponse response = mapper.forResponse().map(createdBrand,CreateBrandResponse.class);
        return response;
    }

    @Override
    @CacheEvict(value = "brand_list", allEntries = true)
    public UpdateBrandResponse update(UUID id, UpdateBrandRequest request) {
        rules.checkIfBrandExistsById(id);
        Brand brand = mapper.forRequest().map(request,Brand.class);
        brand.setId(id);
        Brand updatedBrand = repository.save(brand);
        UpdateBrandResponse response = mapper.forResponse().map(updatedBrand,UpdateBrandResponse.class);
        return response;
    }

    @Override
    public void delete(UUID id) {
        rules.checkIfBrandExistsById(id);
        repository.deleteById(id);
        sendKafkaBrandDeletedEvent(id);
    }

    private void validateBrand(Brand brand){
        checkIfNameLengthValid(brand);
        //other validation functions
    }

    private void checkIfNameLengthValid(Brand brand) {
        if(brand.getName().length()<3 || brand.getName().length()>20) throw new IllegalArgumentException("Name lenght must be between 3 and 20 character.");
    }

    private void sendKafkaBrandDeletedEvent(UUID id) {
        producer.sendMessage(new BrandDeletedEvent(id), "brand-deleted");
    }

}
