package com.kodlamaio.maintenanceservice.business.dto.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CreateMaintenanceRequest {
    @NotBlank
    private UUID carId;
    @NotBlank
    @Length(min = 2 , max = 100)
    private String information;
}
