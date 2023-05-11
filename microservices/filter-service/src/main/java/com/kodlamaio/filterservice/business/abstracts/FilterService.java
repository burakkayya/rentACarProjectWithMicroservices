package com.kodlamaio.filterservice.business.abstracts;

import com.kodlamaio.filterservice.business.dto.GetAllFiltersResponse;
import com.kodlamaio.filterservice.business.dto.GetFilterResponse;
import com.kodlamaio.filterservice.entities.Filter;

import java.util.List;
import java.util.UUID;

public interface FilterService {
    List<GetAllFiltersResponse> getAll();
    GetFilterResponse getById(UUID id);
    void add(Filter filter);
    void delete(UUID id);
    void deleteAllByBrand(UUID brandIdd);
    void deleteAllByModel(UUID modelId);

}
