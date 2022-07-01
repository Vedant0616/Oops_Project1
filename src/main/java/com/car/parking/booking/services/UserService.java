package com.car.parking.booking.services;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.car.parking.booking.controllers.dto.UserRegistrationDto;
import com.car.parking.booking.entities.User;

import java.util.List;

public interface UserService extends UserDetailsService {

    User findByUsername(String username);
    
    User findByRollno(String rollno);

    List<User> findAllUsers();

    User findById(int id);

    User save(UserRegistrationDto registration);

    void update(User user);

    void deleteUserById(int id);


}
