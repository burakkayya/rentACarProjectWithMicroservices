package com.kodlamaio.inventoryservice.business.dto.requests.update;

import com.kodlamaio.inventoryservice.entities.enums.State;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
    // TODO: NotFuture custom annotation
    @Min(value = 2000)
    private int modelYear;
    @NotBlank
    @NotNull
    // TODO: Regex
    private String plate;
    @NotBlank
    @NotNull
    private State state;
    @Min(value = 1)
    private double dailyPrice;
}
