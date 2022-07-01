package com.car.parking.booking.entities;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parkingSpace")
public class ParkingSpace {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "parkingSpace_name")
    private String parkingSpaceName;

    @Column(name = "countrey")
    private String countrey;

    @Column(name = "city")
    private String city;
    
    @Column(name="service")
    private String service;

    @Column(name = "adress")
    private String adress;

    @Column(name = " price_per_night")
    private double ppn;

    @Column(name = "parkingSpace_image")
    private byte[] parkingSpaceImage;

    @Column(name = "status")
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "parkingSpace",
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Booking> bookings = new ArrayList<>();


    public ParkingSpace() {

    }

    public ParkingSpace(int id, String parkingSpaceName, String countrey, String city, String adress, double ppn, byte[] parkingSpaceImage, String status,String service, User user) {
        this.id = id;
        this.parkingSpaceName = parkingSpaceName;
        this.countrey = countrey;
        this.city = city;
        this.service=service;
        this.adress = adress;
        this.ppn = ppn;
        this.parkingSpaceImage = parkingSpaceImage;
        this.status = status;
        this.user = user;
    }


    public ParkingSpace(String parkingSpaceName, String countrey, String city, String adress, double ppn, byte[] parkingSpaceImage,String service, String status, User user) {
        this.parkingSpaceName = parkingSpaceName;
        this.countrey = countrey;
        this.city = city;
        this.adress = adress;
        this.ppn = ppn;
        this.service=service;
        this.parkingSpaceImage = parkingSpaceImage;
        this.status = status;
        this.user = user;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getParkingSpaceName() {
        return parkingSpaceName;
    }

    public void setParkingSpaceName(String parkingSpaceName) {
        this.parkingSpaceName = parkingSpaceName;
    }

    public String getCountrey() {
        return countrey;
    }

    public void setCountrey(String countrey) {
        this.countrey = countrey;
    }
    

    public String getService() {
		return service;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public double getPpn() {
        return ppn;
    }

    public void setPpn(double ppn) {
        this.ppn = ppn;
    }


    public byte[] getParkingSpaceImage() {
        return parkingSpaceImage;
    }

    public void setParkingSpaceImage(byte[] parkingSpaceImage) {
        this.parkingSpaceImage = parkingSpaceImage;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
    }

    public double getRatting() {
        List<Booking> allBookings = this.getBookings();
        List<Booking> ratedBooking = new ArrayList<>();
        for (Booking b : allBookings) {
            if (b.getRating() != null)
                ratedBooking.add(b);
        }
        double sum = 0;
        for (Booking b : ratedBooking) {
            if(b.getRating().getStars()>0)
                sum += b.getRating().getStars();
        }
        String result;
        if(sum>0) {
            result = Double.toString(sum / ratedBooking.size());
            String string = result.substring(0, 3);
            Double end = Double.parseDouble(string);
            return end;
        }

        else return 0;

    }


    @Override
	public String toString() {
		return "ParkingSpace [id=" + id + ", parkingSpaceName=" + parkingSpaceName + ", countrey=" + countrey + ", city=" + city
				+ ", service=" + service + ", adress=" + adress + ", ppn=" + ppn + "]";
	}
}
