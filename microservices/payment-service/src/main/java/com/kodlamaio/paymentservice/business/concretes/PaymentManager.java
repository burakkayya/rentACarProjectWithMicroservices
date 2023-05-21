package com.kodlamaio.paymentservice.business.concretes;

import com.kodlamaio.commonpackage.events.rental.RentalPaymentCreatedEvent;
import com.kodlamaio.commonpackage.utils.dto.ClientResponse;
import com.kodlamaio.commonpackage.utils.kafka.producer.KafkaProducer;
import com.kodlamaio.paymentservice.business.abstracts.PaymentService;
import com.kodlamaio.paymentservice.business.abstracts.PosService;
import com.kodlamaio.paymentservice.business.dto.requests.CreatePaymentRequest;
import com.kodlamaio.paymentservice.business.dto.requests.UpdatePaymentRequest;
import com.kodlamaio.paymentservice.business.dto.responses.CreatePaymentResponse;
import com.kodlamaio.paymentservice.business.dto.responses.GetAllPaymentsResponse;
import com.kodlamaio.paymentservice.business.dto.responses.GetPaymentResponse;
import com.kodlamaio.paymentservice.business.dto.responses.UpdatePaymentResponse;
import com.kodlamaio.paymentservice.business.rules.PaymentBusinessRules;
import com.kodlamaio.paymentservice.entities.Payment;
import com.kodlamaio.paymentservice.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class PaymentManager implements PaymentService {
    private final PaymentRepository repository;
    private final ModelMapper mapper;
    private final PosService posService;
    private final PaymentBusinessRules rules;
    private final KafkaProducer producer;

    @Override
    public List<GetAllPaymentsResponse> getAll() {
        List<Payment> payments = repository.findAll();
        List<GetAllPaymentsResponse> responses = payments
                .stream()
                .map(payment -> mapper.map(payment, GetAllPaymentsResponse.class))
                .toList();
        return responses;
    }

    @Override
    public GetPaymentResponse getById(UUID id) {
        rules.checkIfPaymentExists(id);
        Payment payment = repository.findById(id).orElseThrow();
        GetPaymentResponse response = mapper.map(payment, GetPaymentResponse.class);
        return response;
    }

    @Override
    public CreatePaymentResponse add(CreatePaymentRequest request) {
        rules.checkIfCardExists(request);
        Payment payment= mapper.map(request,Payment.class);
        payment.setId(UUID.randomUUID());
        Payment createdPayment = repository.save(payment);
        CreatePaymentResponse response = mapper.map(createdPayment,CreatePaymentResponse.class);
        return response;
    }

    @Override
    public UpdatePaymentResponse update(UUID id, UpdatePaymentRequest request) {
        rules.checkIfPaymentExists(id);
        Payment payment = mapper.map(request,Payment.class);
        payment.setId(id);
        Payment updatedPayment = repository.save(payment);
        UpdatePaymentResponse response = mapper.map(updatedPayment,UpdatePaymentResponse.class);
        return response;
    }

    @Override
    public void delete(UUID id) {
        rules.checkIfPaymentExists(id);
        repository.deleteById(id);
    }

    @Override
    public void processRentalPayment(RentalPaymentCreatedEvent event) {
            rules.checkIfPaymentIsValid(event);
            Payment payment = repository.findByCardNumber(event.getCard().getCardNumber());
            rules.checkIfBalanceIsEnough(event.getTotalPrice(),payment.getBalance());
            posService.pay();
            payment.setBalance(payment.getBalance() - event.getTotalPrice());
            repository.save(payment);
    }

    @Override
    public void checkPaymentCompleted() {
        var response = new ClientResponse();
        validatePaymentCompleted();

        return response;
    }
    private void validatePaymentCompleted() {
        try{
            rules.checkIfCarExistsById(id);
            rules.checkCarAvailability(id);
            response.setSuccess(true);
        } catch (Exception e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
    }


}
