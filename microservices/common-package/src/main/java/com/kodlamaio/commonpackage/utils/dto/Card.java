package com.kodlamaio.commonpackage.utils.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    @NotBlank
    private String cardNumber;
    @NotBlank
    private String cardHolder;
    @Min(2023)
    private int cardExpirationYear;
    @Min(1)
    @Max(12)
    private int cardExpirationMonth;
    @Min(3)
    @Max(3)
    private String cardCvv;
}
