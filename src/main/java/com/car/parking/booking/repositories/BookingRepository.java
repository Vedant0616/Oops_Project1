package com.car.parking.booking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.car.parking.booking.entities.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,Integer> {

    List<Booking> findAllByUserId(int id);

    List<Booking> findAllByParkingSpaceId(int id);

}
