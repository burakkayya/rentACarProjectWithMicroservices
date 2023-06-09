package com.kodlamaio.filterservice.kafka.consumer;

import com.kodlamaio.commonpackage.events.inventory.BrandDeletedEvent;
import com.kodlamaio.commonpackage.events.inventory.CarCreatedEvent;
import com.kodlamaio.commonpackage.events.inventory.CarDeletedEvent;
import com.kodlamaio.commonpackage.utils.mappers.ModelMapperService;
import com.kodlamaio.filterservice.business.abstracts.FilterService;
import com.kodlamaio.filterservice.entities.Filter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class InventoryConsumer {
    private final FilterService service;
    private final ModelMapperService mapper;
    @KafkaListener(
            topics =  "car-created",
            groupId = "car-create"
    )
    public void consume(CarCreatedEvent event){
        var filter = mapper.forRequest().map(event, Filter.class);
        service.add(filter);
        log.info("Car created event consumed {}", event);
    }

    @KafkaListener(
            topics =  "car-deleted",
            groupId = "car-delete"
    )
    public void consume(CarDeletedEvent event){
        var filter = mapper.forRequest().map(event, Filter.class);
        service.delete(event.getCarId());
        log.info("Car deleted event consumed {}", event);
    }

    @KafkaListener(
            topics =  "brand-deleted",
            groupId = "brand-delete"
    )
    public void consume(BrandDeletedEvent event){
        var filter = mapper.forRequest().map(event, Filter.class);
        service.deleteAllByBrandId(event.getCarId());
        log.info("Brand deleted event consumed {}", event);
    }
}
