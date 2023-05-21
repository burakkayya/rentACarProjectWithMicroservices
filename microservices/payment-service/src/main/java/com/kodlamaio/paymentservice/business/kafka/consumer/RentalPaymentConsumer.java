package com.kodlamaio.paymentservice.business.kafka.consumer;

import com.kodlamaio.commonpackage.events.rental.RentalPaymentCreatedEvent;
import com.kodlamaio.paymentservice.business.abstracts.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class RentalPaymentConsumer {
    PaymentService service;
    @KafkaListener(
            topics =  "rental-payment-created",
            groupId = "rental-payment-create"
    )
    public void consume(RentalPaymentCreatedEvent event){
        service.processRentalPayment(event);
        log.info("Rental Payment created event consumed {}", event);
    }
}
