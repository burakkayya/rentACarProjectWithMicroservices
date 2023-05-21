package com.kodlamaio.rentalservice.api.clients;

import com.kodlamaio.commonpackage.utils.dto.ClientResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
@Slf4j
@Component
public class PaymentClientFallback implements PaymentClient{
    @Override
    public ClientResponse checkIfPaymentCompleted() {
        log.info("PAYMENT SERVICE IS DOWN");
        throw new RuntimeException("PAYMENT SERVICE IS DOWN");
    }
}
