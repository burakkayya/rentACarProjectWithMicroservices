package com.kodlamaio.inventoryservice.business.concretes;

import com.kodlamaio.commonpackage.events.inventory.CarCreatedEvent;
import com.kodlamaio.commonpackage.events.inventory.CarDeletedEvent;
import com.kodlamaio.commonpackage.utils.dto.ClientResponse;
import com.kodlamaio.commonpackage.utils.kafka.producer.KafkaProducer;
import com.kodlamaio.commonpackage.utils.mappers.ModelMapperService;
import com.kodlamaio.inventoryservice.business.abstracts.CarService;
import com.kodlamaio.inventoryservice.business.dto.requests.create.CreateCarRequest;
import com.kodlamaio.inventoryservice.business.dto.requests.update.UpdateCarRequest;
import com.kodlamaio.inventoryservice.business.dto.responses.create.CreateCarResponse;
import com.kodlamaio.inventoryservice.business.dto.responses.get.GetAllCarsResponse;
import com.kodlamaio.inventoryservice.business.dto.responses.get.GetCarResponse;
import com.kodlamaio.inventoryservice.business.dto.responses.update.UpdateCarResponse;
import com.kodlamaio.inventoryservice.business.rules.CarBusinessRules;
import com.kodlamaio.inventoryservice.entities.Car;
import com.kodlamaio.inventoryservice.entities.enums.State;
import com.kodlamaio.inventoryservice.repository.CarRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CarManager implements CarService {
    private final CarRepository repository;
    private final ModelMapperService mapper;
    private final CarBusinessRules rules;
    private final KafkaProducer producer;
    @Override
    public List<GetAllCarsResponse> getAll() {

        List<Car> cars = repository.findAll();
        List<GetAllCarsResponse> response = cars
                .stream()
                .map(car -> mapper.forResponse().map(car, GetAllCarsResponse.class))
                .toList();

        return response;
    }

    @Override
    public GetCarResponse getById(UUID id) {
        rules.checkIfCarExistsById(id);
        Car car = repository.findById(id).orElseThrow();
        GetCarResponse response = mapper.forResponse().map(car,GetCarResponse.class);
        return response;
    }

    @Override
    public CreateCarResponse add(CreateCarRequest request) {
        Car car = mapper.forRequest().map(request,Car.class);
        car.setId(UUID.randomUUID());
        car.setState(State.Available);
        var createdCar = repository.save(car);

        sendKafkaCarCreatedEvent(createdCar);

        CreateCarResponse response = mapper.forResponse().map(createdCar,CreateCarResponse.class);
        return response;
    }

    @Override
    public UpdateCarResponse update(UUID id, UpdateCarRequest request) {
        rules.checkIfCarExistsById(id);
        Car car = mapper.forRequest().map(request,Car.class);
        car.setId(id);
        repository.save(car);

        UpdateCarResponse response = mapper.forResponse().map(car,UpdateCarResponse.class);
        return response;
    }

    @Override
    public void delete(UUID id) {
        rules.checkIfCarExistsById(id);
        repository.deleteById(id);
        sendKafkaCarDeletedEvent(id);
    }

    @Override
    public void changeState(UUID carId, State state) {
        var response = new ClientResponse();
        changeCarState(state, carId, response);
    }

    @Override
    public ClientResponse checkIfCarAvailable(UUID id) {
        var response = new ClientResponse();
        validateCarAvailability(id, response);

        return response;
    }

    private void sendKafkaCarCreatedEvent(Car createdCar) {
        //CarCreatedEvent
        var event = mapper.forResponse().map(createdCar, CarCreatedEvent.class);
        producer.sendMessage(event, "car-created");
    }

    private void sendKafkaCarDeletedEvent(UUID id) {
        producer.sendMessage(new CarDeletedEvent(id), "car-deleted");
    }

    private void validateCarAvailability(UUID id, ClientResponse response) {
        try{
            rules.checkIfCarExistsById(id);
            rules.checkCarAvailability(id);
            response.setSuccess(true);
        } catch (Exception e){
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
    }

    private void changeCarState(State state, UUID carId, ClientResponse response) {
        try {
            rules.checkIfCarExistsById(carId);
            repository.changeStateByCarId(state, carId);
            response.setSuccess(true);
        }catch (Exception e) {
            response.setSuccess(false);
            response.setMessage(e.getMessage());
        }
    }
}
