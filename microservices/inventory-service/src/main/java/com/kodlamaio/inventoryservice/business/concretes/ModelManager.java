package com.kodlamaio.inventoryservice.business.concretes;

import com.kodlamaio.commonpackage.utils.mappers.ModelMapperService;
import com.kodlamaio.inventoryservice.business.abstracts.ModelService;
import com.kodlamaio.inventoryservice.business.dto.requests.create.CreateModelRequest;
import com.kodlamaio.inventoryservice.business.dto.requests.update.UpdateModelRequest;
import com.kodlamaio.inventoryservice.business.dto.responses.create.CreateModelResponse;
import com.kodlamaio.inventoryservice.business.dto.responses.get.GetAllModelsResponse;
import com.kodlamaio.inventoryservice.business.dto.responses.get.GetModelResponse;
import com.kodlamaio.inventoryservice.business.dto.responses.update.UpdateModelResponse;
import com.kodlamaio.inventoryservice.business.rules.ModelBusinessRules;
import com.kodlamaio.inventoryservice.entities.Model;
import com.kodlamaio.inventoryservice.repository.ModelRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ModelManager implements ModelService {

    private  final ModelRepository repository;
    private final ModelMapperService mapper;
    private final ModelBusinessRules rules;

    @Override
    public List<GetAllModelsResponse> getAll() {
        List<Model> models = repository.findAll();
        List<GetAllModelsResponse> responses= models
                .stream()
                .map(model -> mapper.forResponse().map(model, GetAllModelsResponse.class))
                .toList();

        return responses;
    }

    @Override
    public GetModelResponse getById(UUID id) {
        rules.checkIfModelExistsById(id);
        Model model = repository.findById(id).orElseThrow();
        GetModelResponse response = mapper.forResponse().map(model, GetModelResponse.class);
        return response;
    }

    @Override
    public CreateModelResponse add(CreateModelRequest request) {
        rules.checkIfModelExistsByName(request.getName());
        Model model = mapper.forRequest().map(request, Model.class);
        model.setId(null);
        Model createdModel = repository.save(model);
        CreateModelResponse response = mapper.forResponse().map(createdModel, CreateModelResponse.class);

        return response;
    }

    @Override
    public UpdateModelResponse update(UUID id, UpdateModelRequest request) {
        rules.checkIfModelExistsById(id);
        Model model = mapper.forRequest().map(request,Model.class);
        model.setId(id);
        repository.save(model);
        UpdateModelResponse response = mapper.forResponse().map(model,UpdateModelResponse.class);

        return response;
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

}
