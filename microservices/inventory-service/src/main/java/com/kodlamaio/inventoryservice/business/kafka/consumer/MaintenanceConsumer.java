package com.kodlamaio.inventoryservice.business.kafka.consumer;

import com.kodlamaio.commonpackage.events.maintenance.MaintenanceCreatedEvent;
import com.kodlamaio.commonpackage.events.maintenance.MaintenanceDeletedEvent;
import com.kodlamaio.commonpackage.events.maintenance.ReturnCarFromMaintenanceEvent;
import com.kodlamaio.inventoryservice.business.abstracts.CarService;
import com.kodlamaio.inventoryservice.entities.enums.State;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MaintenanceConsumer {
    private final CarService service;

    @KafkaListener(
            topics =  "maintenance-created",
            groupId = "inventory-maintenance-create"
    )
    public void consume(MaintenanceCreatedEvent event){
        service.changeState(event.getCarId(), State.Maintenance);
        log.info("Maintenance created event consumed {}", event);
    }

    @KafkaListener(
            topics =  "maintenance-deleted",
            groupId = "inventory-maintenance-delete"
    )
    public void consume(MaintenanceDeletedEvent event){
        service.changeState(event.getCarId(), State.Available);
        log.info("Maintenance deleted event consumed {}", event);
    }

    @KafkaListener(
            topics =  "maintenance-finished",
            groupId = "inventory-maintenance-finished"
    )
    public void consume(ReturnCarFromMaintenanceEvent event){
        service.changeState(event.getCarId(), State.Available);
        log.info("Return car from maintenance event consumed {}", event);
    }
}
