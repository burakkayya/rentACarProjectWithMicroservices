package com.kodlamaio.filterservice.business.abstracts;

import com.kodlamaio.filterservice.business.dto.GetAllFiltersResponse;
import com.kodlamaio.filterservice.business.dto.GetFilterResponse;
import com.kodlamaio.filterservice.entities.Filter;

import java.util.List;
import java.util.UUID;

public interface FilterService {
    List<GetAllFiltersResponse> getAll();
    GetFilterResponse getById(String id);
    void add(Filter filter);
    void deleteAllByBrandId(UUID brandId);
    void deleteAllByModelId(UUID modelId);
    void deleteByCarId(UUID id);
    Filter getByCarId(UUID id);

}
