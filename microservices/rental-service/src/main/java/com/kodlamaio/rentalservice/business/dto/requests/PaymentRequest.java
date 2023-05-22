package com.kodlamaio.rentalservice.business.dto.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequest {// base class
@NotBlank(message="Kart numarası alanı boş bırakılamaz.")
@Length(min =16,max=16,message = "Kart numarası 16 haneden oluşmalıdır.")
private String cardNumber;

@NotBlank(message = "Kart sahibi boş bırakılamaz")
@Length(min=5,message = "Kart sahibi bilgisi en az 5 haneden oluşmalı")
private String cardHolder;

@Min(value = 2023,message = "Kart son kullanma yılı geçersiz")
private int cardExpirationYear;
@Min(value = 1)
@Max(value = 12)
private int cardExpirationMonth;
@NotBlank(message = "Kart cvv boş bırakılamaz")
@Length(min=3,max=3)
private String cardCvv;

}