package com.car.parking.booking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.car.parking.booking.controllers.dto.ParkingSpaceDto;
import com.car.parking.booking.entities.ParkingSpace;
import com.car.parking.booking.entities.User;
import com.car.parking.booking.repositories.ParkingSpaceRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ParkingSpaceServiceImpl implements ParkingSpaceService {

    private ParkingSpaceRepository parkingSpaceRepository;

    @Autowired
    public ParkingSpaceServiceImpl(ParkingSpaceRepository theParkingSpaceRepository) {
        parkingSpaceRepository = theParkingSpaceRepository;
    }


    @Autowired
    DtoMapping converter;

    @Override
    public List<ParkingSpace> findAllParkingSpacesByStatus(String status) {
        return parkingSpaceRepository.findAllByStatus(status);
    }

    @Override
    public ParkingSpace findById(int theId) {
        Optional<ParkingSpace> result = parkingSpaceRepository.findById(theId);

        ParkingSpace theParkingSpace = null;

        if(result.isPresent()){
            theParkingSpace = result.get();
        }
        else {
            throw new RuntimeException("Did not find paking space id - " + theId);
        }

        return theParkingSpace;
    }

    @Override
    public List<ParkingSpace> findByUser(User user) {
        return parkingSpaceRepository.findByUser(user);
    }

    @Override
    public List<ParkingSpace> findAllAccepted() {
        return parkingSpaceRepository.findAllByStatus("accepted");
    }

    @Override
    public List<ParkingSpace> findAllAcceptedByUser(User user) {
        return parkingSpaceRepository.findAllByUserAndStatus(user,"accepted");
    }

    @Override
    public List<ParkingSpace> findByMaxPrice(String price) {
        if(price=="")
            price="0";
        return parkingSpaceRepository.findAllByStatusLikeAndPpnLessThan("accepted",Double.parseDouble(price));
    }




    //AICI
    @Override
    public ParkingSpace save(ParkingSpaceDto addParkingSpace) {
        ParkingSpace parkingSpace = converter.getParkingSpaceFromParkingSpaceDto(addParkingSpace);
        parkingSpace.setStatus("pending");
        return parkingSpaceRepository.saveAndFlush(parkingSpace);
    }

    //AICI
    @Override
    public ParkingSpace update(ParkingSpaceDto addParkingSpace) {
        ParkingSpace parkingSpace = converter.updateParkingSpaceFromParkingSpaceDto(addParkingSpace);
        return parkingSpaceRepository.saveAndFlush(parkingSpace);
    }

    //different cuz admin side
    //AICI
    @Override
    public ParkingSpace updateStatus(ParkingSpaceDto parkingSpaceDto) {
        ParkingSpace parkingSpace = converter.updateParkingSpaceStatusFromParkingSpaceDto(parkingSpaceDto);
        return parkingSpaceRepository.saveAndFlush(parkingSpace);
    }



    @Override
    public void delete(int theId) {

        parkingSpaceRepository.deleteById(theId);

    }

    @Override
    public void delete(ParkingSpace parkingSpace) {
        parkingSpaceRepository.delete(parkingSpace);
    }

    @Override
    public List<ParkingSpace> findAllByTheCity(String city) {
        return parkingSpaceRepository.findAllByCityContainsAndStatusLike(city,"accepted");
    }

    @Override
    public List<ParkingSpace> findAllByTheCountry(String country) {
        return parkingSpaceRepository.findAllByCountreyContainsAndStatusLike(country,"accepted");
    }


}
