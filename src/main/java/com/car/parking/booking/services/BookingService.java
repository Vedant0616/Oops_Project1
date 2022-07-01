package com.car.parking.booking.services;

import org.apache.catalina.User;

import com.car.parking.booking.controllers.dto.BookingDto;
import com.car.parking.booking.entities.ParkingSpace;
import com.car.parking.booking.entities.Booking;

import java.util.Date;
import java.util.List;

public interface BookingService {

    List<Booking> findAll();

    Booking findById(int id);
   
    Booking save(BookingDto bookingDto);

    boolean isOverlapping(BookingDto bookingDto,int parkingSpaceId);

    List<Booking> findBookingByTheParkingSpace(int id);

    List<Booking> findAllByUser(int id);

    Booking update(Booking booking);

    void delete(int id);


}
