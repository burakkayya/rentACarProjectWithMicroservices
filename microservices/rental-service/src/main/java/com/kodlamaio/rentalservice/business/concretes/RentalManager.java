package com.kodlamaio.rentalservice.business.concretes;

import com.kodlamaio.commonpackage.events.rental.RentalCreatedEvent;
import com.kodlamaio.commonpackage.utils.mappers.ModelMapperService;
import com.kodlamaio.rentalservice.api.clients.CarClient;
import com.kodlamaio.rentalservice.business.abstracts.RentalService;
import com.kodlamaio.rentalservice.business.dto.requests.CreateRentalRequest;
import com.kodlamaio.rentalservice.business.dto.requests.UpdateRentalRequest;
import com.kodlamaio.rentalservice.business.dto.responses.CreateRentalResponse;
import com.kodlamaio.rentalservice.business.dto.responses.GetAllRentalsResponse;
import com.kodlamaio.rentalservice.business.dto.responses.GetRentalResponse;
import com.kodlamaio.rentalservice.business.dto.responses.UpdateRentalResponse;
import com.kodlamaio.rentalservice.business.kafka.producer.RentalProducer;
import com.kodlamaio.rentalservice.business.rules.RentalBusinessRules;
import com.kodlamaio.rentalservice.entities.Rental;
import com.kodlamaio.rentalservice.repository.RentalRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class RentalManager implements RentalService {

    private  final RentalRepository repository;
    private final ModelMapperService mapper;
    private final RentalBusinessRules rules;
    private final CarClient carClient;
    private final RentalProducer producer;

    @Override
    public List<GetAllRentalsResponse> getAll() {
        List<Rental> rentals = repository.findAll();
        List<GetAllRentalsResponse> responses= rentals
                .stream()
                .map(rental -> mapper.forResponse().map(rental, GetAllRentalsResponse.class))
                .toList();

        return responses;
    }

    @Override
    public GetRentalResponse getById(UUID id) {
        rules.checkIfRentalExistsById(id);
        Rental rental = repository.findById(id).orElseThrow();
        GetRentalResponse response = mapper.forResponse().map(rental, GetRentalResponse.class);
        return response;
    }

    @Override
    public CreateRentalResponse add(CreateRentalRequest request) {
        carClient.checkIfCarAvailable(request.getCarId());
        Rental rental = mapper.forRequest().map(request, Rental.class);
        rental.setId(null);
        rental.setTotalPrice(getTotalPrice(rental));
        rental.setRentedAt(LocalDateTime.now());
        Rental createdRental = repository.save(rental);
        sendKafkaRentalCreatedEvent(request.getCarId());
        CreateRentalResponse response = mapper.forResponse().map(createdRental, CreateRentalResponse.class);

        return response;
    }

    @Override
    public UpdateRentalResponse update(UUID id, UpdateRentalRequest request) {
        rules.checkIfRentalExistsById(id);
        Rental rental = mapper.forRequest().map(request,Rental.class);
        rental.setId(id);
        repository.save(rental);
        UpdateRentalResponse response = mapper.forResponse().map(rental, UpdateRentalResponse.class);

        return response;
    }

    @Override
    public void delete(UUID id) {
        repository.deleteById(id);
    }

    private double getTotalPrice(Rental rental){
        return rental.getDailyPrice() * rental.getRentedForDays();
    }
    private void sendKafkaRentalCreatedEvent(UUID carId) {
        producer.sendMessage(new RentalCreatedEvent(carId));
    }
}