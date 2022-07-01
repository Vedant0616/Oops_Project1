package com.car.parking.booking.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.mail.internet.MimeMessage;

@Controller
public class EmailController {

    @Autowired
    private TemplateEngine templateEngine;

    @Autowired
    private JavaMailSender javaMailSender;

    @PostMapping("/send-contact-email")
    public String sendContactEmail(@RequestParam("name") String name, @RequestParam("email") String email, @RequestParam("subject") String subject, @RequestParam("message") String message, Model m) {
        try {
            sendContactFormEmail(name, email, subject, message);
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            m.addAttribute("exception", e);
            return "/errorRegistration";
        }
    }
    public void sendOtp(String to, String username,String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("vedantmathur16@gmail.com");
        message.setTo(to);
        message.setSubject("Hello customer");
        message.setText("Welcome " + username + "Your Otp is " + otp);
        javaMailSender.send(message);
    }
    

    public void sendHelloEmail(String to, String username) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("vedantmathur16@gmail.com");
        message.setTo(to);
        message.setSubject("Hello customer");
        message.setText("Welcome " + username + "\n\n ,you are now a Smart Car Parker!");
        javaMailSender.send(message);
    }

    public void sendBookingInfoForCustomer(String userName,String email, String checkin,String checkout,String parkingSpaceName,String parkingSpaceCity,String parkingSpaceCountry,String street,String inTime,String outTime){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("vedantmathur16@gmail.com");
        message.setTo(email);
        String capitalizaedUserName = userName.substring(0, 1).toUpperCase()+userName.substring(1);
        message.setSubject("Reservation made successfully!!");
        message.setText("Welcome " + capitalizaedUserName + "\nYour reservation has been registered succesfully between "+checkin
                +" and "+checkout+ "at time"+ inTime+ "from" + outTime+ ".\nThe parking space name is "+parkingSpaceName+" from "+parkingSpaceCity +","+parkingSpaceCountry+" situated on "+street+
                "\nYour vehicle is in safe hand so just relax and enjoy the service !\nHave a nice day!");
        javaMailSender.send(message);

    }

    public void sendBookingAlertToGuest(String userName,String email,String parkingSpaceName,String checkin,String checkout,String inTime,String outTime)
    {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("vedantmathur16@gmail.com");
        message.setTo(email);
        String capitalizaedUserName = userName.substring(0, 1).toUpperCase()+userName.substring(1);
        message.setSubject("New reservation on one of your parkingSpaces!!");
        message.setText("Hello"+capitalizaedUserName+"!\nNew reservation on "+parkingSpaceName+" from date"+checkin+" to " + "and time " + inTime+ "to " +outTime+checkout);
        javaMailSender.send(message);

    }

    private void sendContactFormEmail(String name, String email, String subject, String message) {

        final Context context = new Context();
        context.setVariable("name", name);
        context.setVariable("email", email);
        context.setVariable("subject", subject);
        context.setVariable("message", message);

        String body = templateEngine.process("contact-email", context);
        String sendTo = "vedantmathur16@gmail.com";
        String emailSubject = "New message from Smart Parker Customer";

        sendPreparedMail(body, sendTo, emailSubject);
    }

    private void sendPreparedMail(String body, String sendTo, String subject) {
        try {
            MimeMessage mail = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setTo(sendTo);
            helper.setSubject(subject);
            helper.setText(body, true);
            javaMailSender.send(mail);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
