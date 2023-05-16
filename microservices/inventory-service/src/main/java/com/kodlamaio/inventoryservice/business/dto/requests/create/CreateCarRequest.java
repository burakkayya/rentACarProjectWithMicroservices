package com.kodlamaio.inventoryservice.business.dto.requests.create;

import com.kodlamaio.commonpackage.constants.Regex;
import com.kodlamaio.commonpackage.utils.annotations.NotFutureYear;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCarRequest {
    @NotNull
    private UUID modelId;
    @NotFutureYear
    @Min(value = 2000)
    private int modelYear;
    @NotBlank
    @Pattern(regexp = Regex.Plate)
    private String plate;
    @Min(value = 1)
    private double dailyPrice;
}
