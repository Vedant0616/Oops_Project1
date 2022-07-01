package com.car.parking.booking.services;

import org.springframework.web.multipart.MultipartFile;

import com.car.parking.booking.controllers.dto.ParkingSpaceDto;
import com.car.parking.booking.entities.ParkingSpace;
import com.car.parking.booking.entities.User;

import java.util.List;

public interface ParkingSpaceService {

    List<ParkingSpace> findAllParkingSpacesByStatus(String status);

    ParkingSpace findById(int theId);

    List<ParkingSpace> findByUser(User user);

    List<ParkingSpace> findAllAccepted();

    List<ParkingSpace> findAllAcceptedByUser(User user);

    List<ParkingSpace> findByMaxPrice(String price);

    ParkingSpace update(ParkingSpaceDto parkingSpaceDto);

    //AICI
    ParkingSpace save(ParkingSpaceDto theParkingSpace);

    ParkingSpace updateStatus(ParkingSpaceDto parkingSpaceDto);

    void delete(int theId);

    void delete(ParkingSpace parkingSpace);

    List<ParkingSpace> findAllByTheCity(String city);

    List<ParkingSpace> findAllByTheCountry(String country);


}
