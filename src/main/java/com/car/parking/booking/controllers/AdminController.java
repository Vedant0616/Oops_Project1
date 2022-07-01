package com.car.parking.booking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.car.parking.booking.controllers.dto.ParkingSpaceDto;
import com.car.parking.booking.controllers.dto.UserRegistrationDto;
import com.car.parking.booking.entities.ParkingSpace;
import com.car.parking.booking.entities.Booking;
import com.car.parking.booking.entities.User;
import com.car.parking.booking.services.ParkingSpaceService;
import com.car.parking.booking.services.BookingService;
import com.car.parking.booking.services.DtoMapping;
import com.car.parking.booking.services.UserService;

import java.io.IOException;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import javax.validation.Valid;

@Controller()
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ParkingSpaceService parkingSpaceService;

    @Autowired
    private UserService userService;

    @Autowired
    DtoMapping converter;

    @Autowired
    private BookingService bookingService;

    @GetMapping()
    public String adminHomePage() {

        return "/ADM/optionsPage";
    }

    @GetMapping("/403")
    public String error403() {
        return "/errors/403";
    }



    //USER

    @GetMapping("/users")
    public String userList(Model theModel) {
        List<User> theUsers = userService.findAllUsers();
        User admin = userService.findByUsername("admin");
        User worker1=userService.findByUsername("worker1");
        User worker2=userService.findByUsername("worker2");
        User worker3=userService.findByUsername("worker3");
        theUsers.remove(userService.findByRollno("worker"));
        theUsers.remove(admin);
        theUsers.remove(worker1);
        theUsers.remove(worker2);
        theUsers.remove(worker3);
        

        if(theUsers.size()==0)
            return "/ADM/empty";

        theModel.addAttribute("users", theUsers);

        return "/ADM/userList";
    }
    
	@PostMapping("/save")
	public String addParkingSpace(@ModelAttribute("parkingSpace") @Valid ParkingSpaceDto parkingSpace, BindingResult result,
			RedirectAttributes attr) {

		if (result.hasErrors()) {

			attr.addFlashAttribute("org.springframework.validation.BindingResult.parkingSpace", result);
			attr.addFlashAttribute("parkingSpace", parkingSpace);
			return "redirect:/admin/showFormForAdd";
		}
		
		parkingSpace.setStatus("accepted");

		parkingSpaceService.save(parkingSpace);
			
		return "redirect:/admin/pendingParkingSpaces";
	}
    @GetMapping("/workers")
    public String workerList(Model theModel) {
        List<User> theWorkers = userService.findAllUsers();
        theWorkers.remove(userService.findByRollno("user"));
        User admin = userService.findByUsername("admin");
        User user1= userService.findByUsername("luv");
        User user2= userService.findByUsername("vedant");
        User user3= userService.findByUsername("aryan");
        User user4 = userService.findByUsername("nikhil");
        theWorkers.remove(admin);
        theWorkers.remove(user1);
        theWorkers.remove(user2);
        theWorkers.remove(user3);
        theWorkers.remove(user4);
 
        if(theWorkers.size()==0)
            return "/ADM/empty";

        theModel.addAttribute("workers", theWorkers);

        return "/ADM/workerList";
    }

    @RequestMapping(path = "/delete/{id}")
    public String deleteUserById(@PathVariable("id") int id) {
        userService.deleteUserById(id);

        return "redirect:/admin/workers";
    }

    @RequestMapping(path = "/viewUser/{id}")
    public String viewUserById(Model model, @PathVariable("id") int id) {
        User user = userService.findById(id);
        List<ParkingSpace> parkingSpaces = parkingSpaceService.findByUser(user);
        List<String> apartNames = new ArrayList<String>();
        for (int i = 0; i < parkingSpaces.size(); i++) {
            apartNames.add(parkingSpaces.get(i).getParkingSpaceName());
        }
        model.addAttribute("user", user);
        model.addAttribute("apartNames", apartNames);

        return "/ADM/viewUser";
    }


    //APARTMENT

    @RequestMapping(path = "/checkParkingSpace/{id}")
    public String reviewParkingSpace(Model model, @PathVariable("id") int id) throws IOException {
        ParkingSpace parkingSpace = parkingSpaceService.findById(id);
        String acceptedStatus = "accepted";
        String declinedStatus = "declined";
        model.addAttribute("parkingSpace", parkingSpace);
        model.addAttribute("accepted", acceptedStatus);
        model.addAttribute("declined", declinedStatus);

        return "/ADM/reviewParkingSpace";
    }
    
    @RequestMapping(path = "/editParkingSpace/{id}")
    public String editParkingSpace(Model model, @PathVariable("id") int id) throws IOException {
        ParkingSpace parkingSpace = parkingSpaceService.findById(id);
        String acceptedStatus = "accepted";
        String declinedStatus = "declined";
        model.addAttribute("parkingSpace", parkingSpace);
        model.addAttribute("accepted", acceptedStatus);
        model.addAttribute("declined", declinedStatus);

        return "/ADM/editParkingSpace";
    }

    @GetMapping("/pendingParkingSpaces")
    public String listPendingParkingSpaces(Model theModel) {
        List<ParkingSpace> theParkingSpaces = parkingSpaceService.findAllParkingSpacesByStatus("pending");

        if(theParkingSpaces.size()==0)
            return "/ADM/empty";

        theModel.addAttribute("parkingSpaces", theParkingSpaces);

        return "/ADM/pendingParkingSpaces";


    }
    
    @GetMapping(path = "/showFormForAdd")
    public String showFormForAdd(Model theModel) {

        ParkingSpaceDto parkingSpaceDto = new ParkingSpaceDto();


        if (!theModel.containsAttribute("parkingSpace")) {
            theModel.addAttribute("parkingSpace", parkingSpaceDto);
        }

        return "/ADM/parkingSpace-form";
    }

    
    
    @GetMapping("/viewAll")
    public String listViewAll(Model theModel) {
    	List<ParkingSpace> theParkingSpaces = parkingSpaceService.findAllAccepted();
    	if (theParkingSpaces.size()==0) {
			return "/ADM/empty";
		}
    	theModel.addAttribute("parkingSpaces", theParkingSpaces);
    	return "/ADM/viewAll";
    }

    @PostMapping("/saveStatus")
    public String updateParkingSpaceStatus(@ModelAttribute("parkingSpaceId") int parkingSpaceId, @ModelAttribute("parkingSpaceStatus") String status) {
        ParkingSpace parkingSpace = parkingSpaceService.findById(parkingSpaceId);
        ParkingSpaceDto dto = converter.getParkingSpaceDtoFromParkingSpace(parkingSpace);
        dto.setStatus(status);
        parkingSpaceService.updateStatus(dto);

        return "redirect:/admin/pendingParkingSpaces";
    }

    @GetMapping("/workersAdd")
    public String showRegistrationFormforworker(Model theModel) {

        UserRegistrationDto usertDto = new UserRegistrationDto();
        if (!theModel.containsAttribute("user")) {
            theModel.addAttribute("user", usertDto);
        }
        return "/ADM/registerworker";
    }


    //STATISTICS

    @RequestMapping(path = "/statistics")
    public String statistics(Model model) {
        List<Booking> bookings = bookingService.findAll();
        int numberOfBookings = bookings.size();
        int totalIncome = 0;
        for (Booking b : bookings) {
            long diff = b.getCheckOut().getTime() - b.getCheckIn().getTime() ;
            long diffDays = diff / ( 60 * 60 * 1000) + 1  +Integer.parseInt(b.getOutTime()) -Integer.parseInt(b.getInTime())  ; 
            int SumDays = (int) diffDays;
            totalIncome += b.getParkingSpace().getPpn() * SumDays ;
        }
        model.addAttribute("bookingsNumber", numberOfBookings);
        model.addAttribute("totalIncome", totalIncome);

        return "/ADM/statistics";
    }

	/*
	 * @RequestMapping("/chart") public String generateGraph(Model model) {
	 * Map<String, Integer> techMap = getTechnologyMap();
	 * model.addAttribute("techMap", techMap);
	 * 
	 * return "/ADM/chart"; }
	 */


	/*
	 * @RequestMapping("/pie") public String generatePieChart(Model model) {
	 * 
	 * List<Booking> bookings = bookingService.findAll();
	 * 
	 * int sumIan = 0, sumFeb = 0, sumMar = 0, sumApr = 0, sumMay = 0, sumIun = 0,
	 * sumIul = 0, sumAug = 0, sumSep = 0, sumOct = 0, sumNov = 0, sumDec = 0; for
	 * (Booking b : bookings) { switch
	 * (b.getCheckIn().toInstant().atZone(ZoneId.systemDefault()).toLocalDate().
	 * getMonthValue()) { case 1: sumIan += getBookingPrice(b); break; case 2:
	 * sumFeb += getBookingPrice(b); break; case 3: sumMar += getBookingPrice(b);
	 * break; case 4: sumApr += getBookingPrice(b); break; case 5: sumMay +=
	 * getBookingPrice(b); break; case 6: sumIun += getBookingPrice(b); break; case
	 * 7: sumIul += getBookingPrice(b); break; case 8: sumAug += getBookingPrice(b);
	 * break; case 9: sumSep += getBookingPrice(b); break; case 10: sumOct +=
	 * getBookingPrice(b); break; case 11: sumNov += getBookingPrice(b); break; case
	 * 12: sumDec += getBookingPrice(b); break; default: break; }
	 * 
	 * 
	 * }
	 * 
	 * 
	 * model.addAttribute("January", sumIan); model.addAttribute("February",
	 * sumFeb); model.addAttribute("March", sumMar); model.addAttribute("April",
	 * sumApr); model.addAttribute("May", sumMay); model.addAttribute("June",
	 * sumIun); model.addAttribute("July", sumIul); model.addAttribute("August",
	 * sumAug); model.addAttribute("September", sumSep);
	 * model.addAttribute("October", sumOct); model.addAttribute("November",
	 * sumNov); model.addAttribute("December", sumDec); return "/ADM/pie";
	 * 
	 * }
	 */

    private double getBookingPrice(Booking booking) {
        double sum = 0;
        long diff = booking.getCheckOut().getTime() - booking.getCheckIn().getTime();
        long diffDays = diff / (24 * 60 * 60 * 1000) + 1;
        int SumDays = (int) diffDays;
        sum = booking.getParkingSpace().getPpn() * SumDays;
        return sum;
    }

	/*
	 * private Map<String, Integer> getTechnologyMap() { List<Booking> bookings =
	 * bookingService.findAll(); List<Date> checkInDates = new ArrayList<>(); for
	 * (Booking b : bookings) { checkInDates.add(b.getCheckIn()); }
	 * 
	 * List<Integer> months = new ArrayList<>(); for (Date d : checkInDates) {
	 * months.add(d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().
	 * getMonthValue()); }
	 * 
	 * int januaryNr = 0, februaryNr = 0, marchNr = 0, aprilNr = 0, mayNr = 0,
	 * juneNr = 0, julyNr = 0, augustNr = 0, septemberNr = 0, octoberNr = 0,
	 * novemberNr = 0, decemberNr = 0;
	 * 
	 * for (int i = 0; i < months.size(); i++) { switch (months.get(i)) { case 1:
	 * januaryNr++; break; case 2: februaryNr++; break; case 3: marchNr++; break;
	 * case 4: aprilNr++; break; case 5: mayNr++; break; case 6: juneNr++; break;
	 * case 7: julyNr++; break; case 8: augustNr++; break; case 9: septemberNr++;
	 * break; case 10: octoberNr++; break; case 11: novemberNr++; break; case 12:
	 * decemberNr++; break;
	 * 
	 * default: break; } } Map<String, Integer> techMap = new ConcurrentHashMap<>();
	 * techMap.put("January", januaryNr); techMap.put("February", februaryNr);
	 * techMap.put("March", marchNr); techMap.put("April", aprilNr);
	 * techMap.put("May", mayNr); techMap.put("June", juneNr); techMap.put("July",
	 * julyNr); techMap.put("August", augustNr); techMap.put("September",
	 * septemberNr); techMap.put("Octomber", octoberNr); techMap.put("November",
	 * novemberNr); techMap.put("December", decemberNr); return techMap; }
	 */


}
