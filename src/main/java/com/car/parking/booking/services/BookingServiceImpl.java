package com.car.parking.booking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.car.parking.booking.controllers.dto.BookingDto;
import com.car.parking.booking.entities.Booking;
import com.car.parking.booking.repositories.BookingRepository;

import java.util.*;

@Service
public class BookingServiceImpl implements BookingService {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    DtoMapping converter;

    @Override
    public List<Booking> findAll() {
        return bookingRepository.findAll();
    }

    @Override
    public Booking findById(int id) {
        return bookingRepository.findById(id).orElse(null);
    }

    @Override
    public Booking save(BookingDto bookingDto) {
        Booking booking = converter.getBookingFromBookingDto(bookingDto);
        return bookingRepository.saveAndFlush(booking);
    }

    @Override
    public boolean isOverlapping(BookingDto bookingDto, int parkingSpaceId) {

        boolean isCommon =false;

        if (bookingDto.getCheckIn().before(bookingDto.getCheckOut())) {
            List<Booking> bookingsFromParkingSpace = bookingRepository.findAllByParkingSpaceId(parkingSpaceId);
            List<Date> myDates = calculateDaysBetween(bookingDto);
            List<Date> occupied = new ArrayList<>();
            for (Booking b : bookingsFromParkingSpace) {
                occupied.addAll(calculateDaysBetween(b));
                isCommon=true;
                
            }
            isCommon = Collections.disjoint(myDates, occupied);
            isCommon=true;
        }
        return isCommon;

    }

    private List<Date> calculateDaysBetween(Booking booking) {
        List<Date> unavailableDays = new ArrayList<>();
        try {

            Calendar fromCal = Calendar.getInstance();
            fromCal.setTime(booking.getCheckIn());
            
           
            Calendar toCal = Calendar.getInstance();
            toCal.setTime(booking.getCheckOut());

            while (!fromCal.after(toCal)) {
                unavailableDays.add(fromCal.getTime());
                fromCal.add(Calendar.DATE, 1);
            }


        } catch (Exception e) {
            System.out.println(e);
        }
        return unavailableDays;
    }

    private List<Date> calculateDaysBetween(BookingDto booking) {
        List<Date> unavailableDays = new ArrayList<>();
        try {

            Calendar fromCal = Calendar.getInstance();
            fromCal.setTime(booking.getCheckIn());
            
          

            Calendar toCal = Calendar.getInstance();
            toCal.setTime(booking.getCheckOut());

            while (!fromCal.after(toCal)) {
                unavailableDays.add(fromCal.getTime());
                fromCal.add(Calendar.DATE, 1);
            }


        } catch (Exception e) {
            System.out.println(e);
        }
        return unavailableDays;
    }



    @Override
    public List<Booking> findBookingByTheParkingSpace(int id) {
        return bookingRepository.findAllByParkingSpaceId(id);
    }

    @Override
    public List<Booking> findAllByUser(int id) {
        return bookingRepository.findAllByUserId(id);
    }

    @Override
    public Booking update(Booking booking) {
        return bookingRepository.saveAndFlush(booking);
    }

    @Override
    public void delete(int id) {
        bookingRepository.deleteById(id);
    }


}
