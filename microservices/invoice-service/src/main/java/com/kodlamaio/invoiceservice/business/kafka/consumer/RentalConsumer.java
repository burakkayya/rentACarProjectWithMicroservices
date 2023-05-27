package com.kodlamaio.invoiceservice.business.kafka.consumer;

import com.kodlamaio.commonpackage.events.rental.RentalCreatedEvent;
import com.kodlamaio.invoiceservice.business.abstracts.InvoiceService;
import com.kodlamaio.invoiceservice.business.dto.CreateInvoiceRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RentalConsumer {
    private final InvoiceService service;
    private final ModelMapper mapper;

    @KafkaListener(
            topics =  "rental-created",
            groupId = "invoice-rental-create"
    )
    public void consume(RentalCreatedEvent event){

        var invoice =  mapper.map(event, CreateInvoiceRequest.class);
        service.add(invoice);
        log.info("Rental created event consumed {}", event);
    }
}
