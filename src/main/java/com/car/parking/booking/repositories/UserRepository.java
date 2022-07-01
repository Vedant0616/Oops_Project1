package com.car.parking.booking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.car.parking.booking.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {

    User findByUserName(String userName);

	User findByRollno(String rollno);

}
