package com.kodlamaio.rentalservice.api.clients;

import com.kodlamaio.commonpackage.utils.dto.ClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "payment-service", fallback = CarClientFallback.class)
public interface PaymentClient {
    @GetMapping(value = "/api/payments/check-payment-completed")
    ClientResponse checkIfPaymentCompleted();
}
