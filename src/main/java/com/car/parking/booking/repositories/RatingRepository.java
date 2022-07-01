package com.car.parking.booking.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.car.parking.booking.entities.Rating;

public interface RatingRepository extends JpaRepository<Rating,Integer> {

}
