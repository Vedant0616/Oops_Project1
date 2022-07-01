package com.car.parking.booking.controllers.dto;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;

import java.sql.Time;
import java.util.Date;

public class BookingDto {

    @NotNull
    private int bookingId;

    @NotNull(message = "Check In must not be null.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "Check Out must be a future date.")
    private Date checkIn;

    @NotNull(message = "Check Out must not be null.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Future(message = "Check Out must be a future date.")
    private Date checkOut;

    private String inTime;
    
    private String outTime;
    
    private ParkingSpaceDto parkingSpaceDto;

    public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    
    public String getInTime() {
		return inTime;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }
    
  

	public ParkingSpaceDto getParkingSpaceDto() {
        return parkingSpaceDto;
    }

    public void setParkingSpaceDto(ParkingSpaceDto parkingSpaceDto) {
        this.parkingSpaceDto = parkingSpaceDto;
    }
}
