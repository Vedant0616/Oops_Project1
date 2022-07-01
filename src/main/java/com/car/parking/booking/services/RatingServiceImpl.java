package com.car.parking.booking.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.car.parking.booking.entities.Rating;
import com.car.parking.booking.repositories.RatingRepository;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingRepository ratingRepository;


    @Override
    public Rating save(Rating rating) {
        return ratingRepository.saveAndFlush(rating);
    }


}
