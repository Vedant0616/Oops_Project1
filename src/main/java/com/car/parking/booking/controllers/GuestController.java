package com.car.parking.booking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.car.parking.booking.entities.ParkingSpace;
import com.car.parking.booking.services.ParkingSpaceService;
import com.car.parking.booking.services.UserService;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GuestController {


    @Autowired
    private ParkingSpaceService parkingSpaceService;

    @Autowired
    private UserService userService;

    @GetMapping("/403")
    public String error403() {
        return "/errors/403";
    }


    @GetMapping("/")
    public String homePage(Model model) {
        if (parkingSpaceService.findAllAccepted().size() > 3) {
            List<ParkingSpace> accepted = parkingSpaceService.findAllAccepted();
            List<ParkingSpace> sortedByRating = accepted.stream()
                    .sorted(Comparator.comparing(ParkingSpace::getRatting).reversed())
                    .collect(Collectors.toList());


            int count = 0;
            for (ParkingSpace a : sortedByRating) {
                if (a.getRatting() >= 1)
                    count++;
            }

            if (count > 2) {
                ParkingSpace first = sortedByRating.get(0);
                ParkingSpace second = sortedByRating.get(1);
                ParkingSpace third = sortedByRating.get(2);
                model.addAttribute("first", first);
                model.addAttribute("second", second);
                model.addAttribute("third", third);
                return "/HOME/homePage";
            }
            else return "/HOME/default";
        }

        return "/HOME/default";

    }


    @GetMapping("/contact")
    public String contact() {

        return "/HOME/contactPage";
    }

    @GetMapping("/aboutUs")
    public String about() {

        return "/HOME/aboutPage";
    }

    @GetMapping("/login")
    public String login() {

        return "login";
    }

    @RequestMapping(path = "/viewParkingSpace/{id}")
    public String viewParkingSpaceById(Model model, @PathVariable("id") int id) {
        ParkingSpace parkingSpace = parkingSpaceService.findById(id);
        model.addAttribute(parkingSpace);

        return "/HOME/viewParkingSpace";
    }


    @GetMapping(value = "/parkingSpaces")
    public ModelAndView searchGET(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        List<ParkingSpace> parkingSpaces = parkingSpaceService.findAllAccepted();
        model.addAttribute("search", parkingSpaces);
        modelAndView.setViewName("/HOME/search");

        return modelAndView;
    }

    @GetMapping(value = "/searchParkingSpaces")
    public String searchPOST(@RequestParam("search") String search, @RequestParam("criteria") String criteria, Model model) {
        List<ParkingSpace> parkingSpaces = null;
        switch (criteria) {
            case "parkingSpacePrice": {
                parkingSpaces = parkingSpaceService.findByMaxPrice(search);
                model.addAttribute("search", parkingSpaces);
                break;
            }
            case "parkingSpaceCity": {
                parkingSpaces = parkingSpaceService.findAllByTheCity(search);
                model.addAttribute("search", parkingSpaces);
                break;
            }
            case "none": {
                parkingSpaces = parkingSpaceService.findAllAccepted();
                model.addAttribute("search", parkingSpaces);
                break;
            }
            case "parkingSpaceCountry": {
                parkingSpaces = parkingSpaceService.findAllByTheCountry(search);
                model.addAttribute("search", parkingSpaces);
                break;
            }

            default:
                break;

        }


        return "/HOME/search";

    }


    @GetMapping(value = "/stays")
    public ModelAndView searchLUGET(Model model) {
        ModelAndView modelAndView = new ModelAndView();
        List<ParkingSpace> parkingSpaces = null;
        String auth = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ParkingSpace> ownedParkingSpaces = parkingSpaceService.findByUser(userService.findByUsername(auth));
        parkingSpaces = parkingSpaceService.findAllAccepted();
        parkingSpaces.removeAll(ownedParkingSpaces);
        model.addAttribute("search", parkingSpaces);
        modelAndView.setViewName("/HOME/loggedSearch");

        return modelAndView;
    }

    @GetMapping(value = "/searchStays")
    public String searchLUPOST(@RequestParam("search") String search, @RequestParam("criteria") String criteria, Model model) {

        List<ParkingSpace> parkingSpaces = null;
        String auth = SecurityContextHolder.getContext().getAuthentication().getName();
        List<ParkingSpace> ownedParkingSpaces = parkingSpaceService.findByUser(userService.findByUsername(auth));
        switch (criteria) {
            case "parkingSpacePrice": {

                parkingSpaces = parkingSpaceService.findByMaxPrice(search);
                parkingSpaces.removeAll(ownedParkingSpaces);
                model.addAttribute("search", parkingSpaces);
                break;
            }
            case "parkingSpaceCity": {
                parkingSpaces = parkingSpaceService.findAllByTheCity(search);
                parkingSpaces.removeAll(ownedParkingSpaces);
                model.addAttribute("search", parkingSpaces);
                break;
            }
            case "none": {
                parkingSpaces = parkingSpaceService.findAllAccepted();
                parkingSpaces.removeAll(ownedParkingSpaces);
                model.addAttribute("search", parkingSpaces);
                break;
            }
            case "parkingSpaceCountry": {
                parkingSpaces = parkingSpaceService.findAllByTheCountry(search);
                parkingSpaces.removeAll(ownedParkingSpaces);
                model.addAttribute("search", parkingSpaces);
                break;
            }

            default:
                break;

        }
        return "/HOME/loggedSearch";

    }


}
