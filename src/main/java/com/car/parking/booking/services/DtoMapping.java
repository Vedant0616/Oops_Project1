package com.car.parking.booking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.car.parking.booking.controllers.dto.ParkingSpaceDto;
import com.car.parking.booking.controllers.dto.BookingDto;
import com.car.parking.booking.controllers.dto.UserRegistrationDto;
import com.car.parking.booking.entities.ParkingSpace;
import com.car.parking.booking.entities.Booking;
import com.car.parking.booking.entities.Role;
import com.car.parking.booking.entities.User;

import java.util.Arrays;

@Component
public class DtoMapping {

    @Autowired
    private ParkingSpaceService parkingSpaceService;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    //AICI
    public ParkingSpace getParkingSpaceFromParkingSpaceDto(ParkingSpaceDto parkingSpaceDto)
    {
        ParkingSpace parkingSpace = new ParkingSpace();
        parkingSpace.setId(parkingSpaceDto.getParkingSpaceID());
        parkingSpace.setParkingSpaceName(parkingSpaceDto.getParkingSpaceName());
        parkingSpace.setCountrey(parkingSpaceDto.getCountrey());
        parkingSpace.setAdress(parkingSpaceDto.getAdress());
        parkingSpace.setCity(parkingSpaceDto.getCity());
        parkingSpace.setStatus(parkingSpaceDto.getStatus());
        parkingSpace.setParkingSpaceImage(null);
        parkingSpace.setPpn(parkingSpaceDto.getPpn());
        parkingSpace.setService(parkingSpaceDto.getService());
        parkingSpace.setUser(userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));

        return parkingSpace;
    }

    //AICI
    public ParkingSpaceDto getParkingSpaceDtoFromParkingSpace(ParkingSpace parkingSpace)
    {
        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
        parkingSpaceDto.setParkingSpaceID(parkingSpace.getId());
        parkingSpaceDto.setParkingSpaceName(parkingSpace.getParkingSpaceName());
        parkingSpaceDto.setCountrey(parkingSpace.getCountrey());
        parkingSpaceDto.setCity(parkingSpace.getCity());
        parkingSpaceDto.setAdress(parkingSpace.getAdress());
        parkingSpaceDto.setService(parkingSpace.getService());
        parkingSpaceDto.setPpn(parkingSpace.getPpn());
        parkingSpaceDto.setStatus(parkingSpace.getStatus());
        parkingSpaceDto.setUserName(parkingSpace.getUser().getUserName());

        return  parkingSpaceDto;
    }

    //AICI
    public ParkingSpace updateParkingSpaceFromParkingSpaceDto(ParkingSpaceDto parkingSpaceDto)
    {
        ParkingSpace parkingSpace = parkingSpaceService.findById(parkingSpaceDto.getParkingSpaceID());
        parkingSpace.setParkingSpaceName(parkingSpaceDto.getParkingSpaceName());
        parkingSpace.setPpn(parkingSpaceDto.getPpn());
        return parkingSpace;
    }



    public ParkingSpace updateParkingSpaceStatusFromParkingSpaceDto(ParkingSpaceDto parkingSpaceDto)
    {
        ParkingSpace parkingSpace = parkingSpaceService.findById(parkingSpaceDto.getParkingSpaceID());
        parkingSpace.setStatus(parkingSpaceDto.getStatus());
        return parkingSpace;
    }

    public User getUserFromUserDto(UserRegistrationDto registration)
    {
        User user = new User();
        user.setId(registration.getId());
        user.setUserName(registration.getUserName());
        user.setEmail(registration.getEmail());
        user.setPassword(passwordEncoder.encode(registration.getPassword()));
        user.setParkingSpaces(null);
        user.setRoles(Arrays.asList(new Role("ROLE_USER")));
        user.setPhone(registration.getPhone());
        user.setCarReg(registration.getCarReg());

        return user;
    }

    public UserRegistrationDto getUserDtoFromUser(User user)
    {
        UserRegistrationDto dto = new UserRegistrationDto();

        dto.setId(user.getId());

        dto.setUserName(user.getUserName());

        dto.setEmail(user.getEmail());

        dto.setPassword(user.getPassword());
        
        dto.setPhone(user.getPhone());

        dto.setCarReg(user.getCarReg());
        return dto;
    }

    public Booking getBookingFromBookingDto(BookingDto bookingDto)
    {
        Booking booking = new Booking();
        booking.setBookingId(bookingDto.getBookingId());
        booking.setCheckIn(bookingDto.getCheckIn());
        booking.setCheckOut(bookingDto.getCheckOut());
        booking.setInTime(bookingDto.getInTime());
        booking.setOutTime(bookingDto.getOutTime());
        booking.setParkingSpace(getParkingSpaceFromParkingSpaceDto(bookingDto.getParkingSpaceDto()));
        booking.setUser(userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));

        return booking;

    }

    public BookingDto getBookingDtoFromBooking(Booking booking)
    {
        BookingDto bookingDto = new BookingDto();
        bookingDto.setBookingId(booking.getBookingId());
        bookingDto.setCheckIn(booking.getCheckIn());
        bookingDto.setCheckOut(booking.getCheckOut());
        bookingDto.setInTime(booking.getInTime());
        bookingDto.setOutTime(booking.getOutTime());
        bookingDto.setParkingSpaceDto(getParkingSpaceDtoFromParkingSpace(booking.getParkingSpace()));

        return bookingDto;
    }


}
