package com.car.parking.booking.controllers;

import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.car.parking.booking.controllers.dto.ParkingSpaceDto;
import com.car.parking.booking.controllers.dto.BookingDto;
import com.car.parking.booking.entities.ParkingSpace;
import com.car.parking.booking.entities.Booking;
import com.car.parking.booking.entities.Rating;
import com.car.parking.booking.entities.User;
import com.car.parking.booking.services.ParkingSpaceService;
import com.car.parking.booking.services.BookingService;
import com.car.parking.booking.services.DtoMapping;
import com.car.parking.booking.services.UserService;
import com.razorpay.*;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class LoggedUserController {

    @Autowired
    private ParkingSpaceService parkingSpaceService;

    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    @Autowired
    private DtoMapping converter;

    @Autowired
    private EmailController emailService;

    @GetMapping("/403")
    public String error403() {
        return "/errors/403";
    }


    //USER

    @GetMapping()
    public String authentificatedUserHomePage(Model theModel, @AuthenticationPrincipal UserDetails currentUser) {
        User user = userService.findByUsername(currentUser.getUsername());
        theModel.addAttribute("currentUser", user);

        return "/LU/optionsPage";
    }

    @GetMapping(value = "/viewProfile")
    public String viewProfile(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userService.findByUsername(name);
        model.addAttribute("user", user);


        //Tourist
        List<Booking> myBookings = bookingService.findAllByUser(user.getId());
        double totalIncome = 0;
        for (Booking b : myBookings) {
        	long beta = Integer.parseInt(b.getInTime()) - Integer.parseInt(b.getOutTime());
        	System.out.println(beta);
            long diff = b.getCheckOut().getTime() - b.getCheckIn().getTime();
            long diffDays = diff / (24 * 60 * 60 * 1000) + 1;
            int SumDays = (int) diffDays;
            totalIncome += b.getParkingSpace().getPpn() * SumDays;
        }
        int size = myBookings.size();
        model.addAttribute("size", size);
        model.addAttribute("sum", totalIncome);
        List<ParkingSpace> activeParkingSpaces = parkingSpaceService.findAllAcceptedByUser(user);
        int guestIncome = 0;
        int bookingsSize = 0;
        for (ParkingSpace a : activeParkingSpaces) {
            for (Booking b : a.getBookings()) {
            	long beta = Integer.parseInt(b.getInTime()) - Integer.parseInt(b.getOutTime());
            	System.out.println(beta);
                long diff = b.getCheckOut().getTime() - b.getCheckIn().getTime();
                long diffDays = diff / (24 * 60 * 60 * 1000) + 1;
                int SumDays = (int) diffDays;
                guestIncome += b.getParkingSpace().getPpn() * SumDays;
                bookingsSize++;
            }

        }
        
        


        model.addAttribute("activeParkingSpaces", activeParkingSpaces.size());
        model.addAttribute("guestIncome", guestIncome);
        model.addAttribute("reservations", bookingsSize);

        return "/LU/viewProfile";
    }

    //APARTMENT

    @GetMapping("/stays")
    public String ownedParkingSpaces(Model theModel) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userService.findByUsername(name);
        List<ParkingSpace> theParkingSpaces = parkingSpaceService.findByUser(user);
        if (theParkingSpaces.size() == 0)
            return "/LU/empty";
        else {
            theModel.addAttribute("parkingSpaces", theParkingSpaces);
            theModel.addAttribute("theUser", user);

            return "/LU/list-parkingSpaces";
        }
    }

    @RequestMapping(path = "/viewParkingSpace/{id}")
    public String viewParkingSpaceById(Model model, @PathVariable("id") int id) {
        ParkingSpace parkingSpace = parkingSpaceService.findById(id);
        model.addAttribute(parkingSpace);
        List<Booking> activeBookings = bookingService.findBookingByTheParkingSpace(id);
        Date actual = new Date();
        List<Booking> valid = new ArrayList<>();
        for (Booking b : activeBookings) {
            if (b.getCheckIn().after(actual))
                valid.add(b);
        }
        model.addAttribute("valid", valid);

        return "/LU/viewParkingSpace";
    }

    @RequestMapping(path = "/delete/{id}")
    public String deleteParkingSpaceById(Model model, @PathVariable("id") int id) {
        ParkingSpace parkingSpace = parkingSpaceService.findById(id);

        parkingSpaceService.delete(parkingSpace);


        return "redirect:/user/stays";
    }

	/*
	 * @GetMapping(path = "/showFormForAdd") public String showFormForAdd(Model
	 * theModel) {
	 * 
	 * ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();
	 * 
	 * 
	 * if (!theModel.containsAttribute("parkingSpace")) {
	 * theModel.addAttribute("parkingSpace", parkingSpaceDto); }
	 * 
	 * return "/LU/parkingSpace-form"; }
	 */

    @PostMapping("/save")
    public String addParkingSpace(@ModelAttribute("parkingSpace") @Valid ParkingSpaceDto parkingSpace, BindingResult result, RedirectAttributes attr) {


        if (result.hasErrors()) {

            attr.addFlashAttribute("org.springframework.validation.BindingResult.parkingSpace",
                    result);
            attr.addFlashAttribute("parkingSpace", parkingSpace);
            return "redirect:/user/showFormForAdd";
        }


        parkingSpaceService.save(parkingSpace);
        return "redirect:/user/stays";
    }


    @GetMapping(value = "/updateParkingSpace/{id}")
    public String showEditParkingSpaceForm(Model model, @PathVariable("id") int id) {
        ParkingSpace parkingSpace = parkingSpaceService.findById(id);
        ParkingSpaceDto dto = converter.getParkingSpaceDtoFromParkingSpace(parkingSpace);
        model.addAttribute("parkingSpace", dto);

        return "/LU/updateParkingSpace";
    }

    @PostMapping("edit-parkingSpace")
    public String saveEditForParkingSpace(@ModelAttribute ParkingSpaceDto dto) {
        parkingSpaceService.update(dto);

        return "redirect:/user/stays";
    }

    @RequestMapping(value = "/favorites/{id}")
    public String addToFavorites(@PathVariable("id") int id) {
        ParkingSpace fav = parkingSpaceService.findById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User currentUser = userService.findByUsername(name);
        for (int i : currentUser.getFavorites()) {
            if (fav.getId() == i) {
                return "/LU/duplicatedSave";
            }
        }
        currentUser.getFavorites().add(fav.getId());
        userService.update(currentUser);
        return "redirect:/stays";

    }

    @GetMapping(value = "/savedParkingSpaces")
    public String myFavorites(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User currentUser = userService.findByUsername(name);
        if (currentUser.getFavorites().size() == 0)
            return "/LU/empty";
        else {
            List<ParkingSpace> favorites = new ArrayList<>();
            for (int i : currentUser.getFavorites()) {
                favorites.add(parkingSpaceService.findById(i));
            }
            model.addAttribute("favorites", favorites);
            return "/LU/favoriteList";
        }
    }

    @GetMapping(value = "/savedParkingSpaces/remove/{id}")
    public String deleteFavorites(@PathVariable("id") int id) {
        ParkingSpace fav = parkingSpaceService.findById(id);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User currentUser = userService.findByUsername(name);
        int identity = fav.getId();
        for (Iterator i = currentUser.getFavorites().iterator(); i.hasNext(); ) {
            int favorite = (int) i.next();
            if (favorite == identity) {
                i.remove();
            }
        }
        userService.update(currentUser);


        return "redirect:/user/savedParkingSpaces";
    }


    //BOOKING

    @GetMapping("/myBookings")
    public String ownedBookings(Model theModel) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userService.findByUsername(name);
        List<Booking> bookings = bookingService.findAllByUser(user.getId());
        Date currentDate = new Date();
        List<Booking> viableBookings = new ArrayList<>();
        for (Booking b : bookings) {
            if (b.getCheckIn().after(currentDate))
                viableBookings.add(b);
        }
        if (viableBookings.size() == 0)
            return "/LU/empty";

        theModel.addAttribute("bookings", viableBookings);
        return "/LU/list-myBookings";

    }

    @RequestMapping("/deleteBooking/{id}")
    public String cancelBooking(Model model, @PathVariable("id") int id) {
        bookingService.delete(id);
        return "redirect:/user/myBookings";
    }

    @RequestMapping(path = "/viewParkingSpace/{id}/calendar")
    public String viewParkingSpaceBookingCalendar(Model model, @PathVariable("id") int id) {
        List<Booking> bookings = bookingService.findBookingByTheParkingSpace(id);
        ParkingSpace theParkingSpace = parkingSpaceService.findById(id);
        int theId = theParkingSpace.getId();
        if (bookings.size() == 0) {
            model.addAttribute("id", theId);

            return "/LU/emptyCalendar";

        } else {
            model.addAttribute("bookings", bookings);

            return "/LU/bookingCalendar";

        }
    }

    @GetMapping(path = "/viewParkingSpace/{id}/calendar/showFormForAdd")
    public String showFormForAddBooking(Model theModel, @PathVariable("id") int id) {
        BookingDto bookingDto = new BookingDto();
        ParkingSpace parkingSpace = parkingSpaceService.findById(id);
        theModel.addAttribute("parkingSpace", parkingSpace);
        if (!theModel.containsAttribute("booking")) {
            theModel.addAttribute("booking", bookingDto);
        }
        Double beta = parkingSpace.getPpn();
        
        theModel.addAttribute("beta", beta);

        return "/LU/bookingForm";
    }


    @PostMapping("/saveBooking/{id}")
    public String addBooking(Model model, @PathVariable("id") int id, @ModelAttribute("booking") @Valid BookingDto booking, BindingResult result, RedirectAttributes attr) {

        if (result.hasErrors()) {
            attr.addFlashAttribute("org.springframework.validation.BindingResult.booking",
                    result);
            attr.addFlashAttribute("booking", booking);
            return "redirect:/user/viewParkingSpace/" + id + "/calendar/showFormForAdd";
        }

        if (bookingService.isOverlapping(booking, id) == false) {
            model.addAttribute("id", id);
            return "/LU/errorBookingForm";

        } else {
            ParkingSpace parkingSpace = parkingSpaceService.findById(id);
            ParkingSpaceDto dto = converter.getParkingSpaceDtoFromParkingSpace(parkingSpace);
            booking.setParkingSpaceDto(dto);
            bookingService.save(booking);


            //TODO:review this tomorrow!
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String name = auth.getName();
            User user = userService.findByUsername(name);
          
            
            try {
                Thread.sleep( 1000);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
            }

            emailService.sendBookingInfoForCustomer(user.getUserName(), user.getEmail(), booking.getCheckIn().toString().substring(0, 10),
                    booking.getCheckOut().toString().substring(0, 10), parkingSpace.getParkingSpaceName(), parkingSpace.
                            getCity(), parkingSpace.getCountrey(), parkingSpace.getAdress(), booking.getInTime(),booking.getOutTime());

            emailService.sendBookingAlertToGuest(parkingSpace.getUser().getUserName(), parkingSpace.getUser().getEmail(),
                    parkingSpace.getParkingSpaceName(), booking.getCheckIn().toString().substring(0, 10), booking.getCheckOut().toString().substring(0, 10),booking.getInTime(),booking.getOutTime());


            System.out.println(booking.getInTime().toString());
            return "redirect:/user/viewParkingSpace/" + id + "/calendar";

        }
    }

    @GetMapping(value = "/pastVisits")
    public String viewPastExperiences(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String name = auth.getName();
        User user = userService.findByUsername(name);
        List<Booking> bookings = bookingService.findAllByUser(user.getId());
        List<Booking> pastBookings = new ArrayList<>();
        Date currentDate = new Date();
        for (Booking b : bookings) {
            if (b.getCheckOut().before(currentDate))
                pastBookings.add(b);
        }


        if (pastBookings.size() == 0)
            return "/LU/empty";

        model.addAttribute("bookings", pastBookings);
        model.addAttribute("rating", new Rating());

        return "/LU/pastBookings";
    }

    //RATING

    @RequestMapping(value = "/saveRating/{id}")
    public String submitRating(Model model, @ModelAttribute("rating") Rating rating, @PathVariable("id") int id) {
        Booking booking = bookingService.findById(id);
        booking.setRating(rating);
        bookingService.update(booking);

        return "redirect:/user/pastVisits";
    }
    
    
   
	/*
	 * @RequestMapping(value = "/payment") public String alpha() { return
	 * "/LU/payment";
	 * 
	 * }
	 */
    
   

}
