package com.kodlamaio.inventoryservice.business.dto.requests.update;

import com.kodlamaio.commonpackage.constants.Regex;
import com.kodlamaio.commonpackage.utils.annotations.NotFutureYear;
import com.kodlamaio.inventoryservice.entities.enums.State;
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
public class UpdateCarRequest {
    @NotBlank
    @NotNull
    private UUID modelId;
    @NotFutureYear
    @Min(value = 2000)
    private int modelYear;
    @NotBlank
    @NotNull
    @Pattern(regexp = Regex.Plate)
    private String plate;
    @NotBlank
    @NotNull
    private State state;
    @Min(value = 1)
    private double dailyPrice;
}
