package com.car.parking.booking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.car.parking.booking.entities.ParkingSpace;
import com.car.parking.booking.entities.User;

import java.util.List;

@Repository
public interface ParkingSpaceRepository extends JpaRepository<ParkingSpace,Integer> {


    List<ParkingSpace> findAllByStatus(String status);

    List<ParkingSpace> findByUser(User user);

    List<ParkingSpace> findAllByStatusLikeAndPpnLessThan(String status,double ppn);

    List<ParkingSpace> findAllByCityContainsAndStatusLike(String city,String status);

    List<ParkingSpace> findAllByCountreyContainsAndStatusLike(String country, String status);

    List<ParkingSpace> findAllByUserAndStatus(User user,String string);

}
