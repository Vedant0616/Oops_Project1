package com.car.parking.booking.entities;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import javax.persistence.*;

import java.sql.Time;
import java.util.Date;

@Entity
@Table(name = "booking")
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "booking_id")
    private int bookingId;

    @Column(name = "checkin_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date checkIn;

    @Column(name = "checkout_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date checkOut;
    
    
    @Column(name="inTime")
    private String inTime;
    
    @Column(name="outTime")
    private String outTime;
    
  


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parkingSpace_id", referencedColumnName = "id")
    private ParkingSpace parkingSpace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToOne(mappedBy = "booking", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Rating rating;

    public Booking() {
    }

    public Booking(int bookingId, Date checkIn, Date checkOut,String inTime,String outTime) {
        this.bookingId = bookingId;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.inTime=inTime;
        this.outTime=outTime;
        
    }
    

  
	public int getBookingId() {
        return bookingId;
    }

    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
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

	public void setCheckIn(Date checkIn) {
        this.checkIn = checkIn;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut = checkOut;
    }


    public ParkingSpace getParkingSpace() {
        return parkingSpace;
    }

    public void setParkingSpace(ParkingSpace parkingSpace) {
        this.parkingSpace = parkingSpace;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getCheckIn() {
        return checkIn;
    }

    public Date getCheckOut() {
        return checkOut;
    }

    public Rating getRating() {
        return rating;
    }
    

 

	public void setRating(Rating rating) {

        if (rating == null) {
            if (this.rating != null) {
                this.rating.setBooking(null);
            }
        } else {
            rating.setBooking(this);

        }
        this.rating = rating;
    }

    @Override
	public String toString() {
		return "Booking [bookingId=" + bookingId + ", checkIn=" + checkIn + ", checkOut=" + checkOut + ", inTime="
				+ inTime + ", outTime=" + outTime + "]";
	}
}
