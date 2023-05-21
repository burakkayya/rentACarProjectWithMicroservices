package com.kodlamaio.paymentservice.business.rules;

import com.kodlamaio.commonpackage.events.rental.RentalPaymentCreatedEvent;
import com.kodlamaio.commonpackage.utils.constants.Messages;
import com.kodlamaio.commonpackage.utils.exceptions.BusinessException;
import com.kodlamaio.paymentservice.business.dto.requests.CreatePaymentRequest;
import com.kodlamaio.paymentservice.repository.PaymentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@AllArgsConstructor
@Service
public class PaymentBusinessRules {
    private final PaymentRepository repository;
    public void checkIfPaymentExists(UUID id){
        if(!repository.existsById(id)){
            throw new BusinessException(Messages.Payment.NotFound);
        }
    }
    public void checkIfCardExists(CreatePaymentRequest request) {
        if(repository.existsByCardNumber(request.getCardNumber())){
            throw new BusinessException(Messages.Payment.CardNumberAlreadyExists);
        }
    }

    public void checkIfPaymentIsValid(RentalPaymentCreatedEvent event) {
        if(!repository.existsByCardNumberAndCardHolderAndCardExpirationYearAndCardExpirationMonthAndCardCvv(
                event.getCard().getCardNumber(),
                event.getCard().getCardHolder(),
                event.getCard().getCardExpirationYear(),
                event.getCard().getCardExpirationMonth(),
                event.getCard().getCardCvv()
        )){
            throw new BusinessException(Messages.Payment.NotAValidPayment);
        }
    }

    public void checkIfBalanceIsEnough(double price, double balance){
        if(balance<price){
            throw new BusinessException(Messages.Payment.NotEnoughMoney);
        }
    }
}