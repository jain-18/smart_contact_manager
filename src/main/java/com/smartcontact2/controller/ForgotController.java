package com.smartcontact2.controller;

import com.smartcontact2.dao.UserRepository;
import com.smartcontact2.entities.User;
import com.smartcontact2.helper.Message;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import services.EmailService;

import java.util.Optional;
import java.util.Random;

@Controller
@ComponentScan(basePackages = "services")
public class ForgotController {
    Random random = new Random();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    //email id form open Handler
    @RequestMapping("/forgot")
    public String openEmailForm(){

        return "forgot_email_form";
    }
    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam("email") String email,Model model, HttpSession session){

        model.addAttribute("title","Email");
        //generating otp of 4 digit
        int otp = random.nextInt(9999999);
        System.out.println(email);
        System.out.println(otp);

        //write a code for send otp to email
        String subject = "OTP for Password Change from Smart Contact Manager";
        String message = "" +
                "<div style='border:1px solid #e2e2e2: padding:20px'>" +
                "<h1>" +
                "OTP is " +
                "<b>" + otp +
                "</n>" +
                "</h1>" +
                "</div>";
        String to = email;

        boolean flag = this.emailService.sendEmail(subject,message,to);
        System.out.println(flag);
        if(flag){

            session.setAttribute("myotp",otp);
            session.setAttribute("email",email);
            session.setAttribute("message",new Message("We have sent OTP to your email.."," alert-success "));

            return "verify_otp";
        }
        else{
            session.setAttribute("message",new Message("Enter your correct email-Id!!"," alert-danger "));
            return "forgot_email_form";
        }
//        return "verify_otp";
    }

    //verify-otp
    @PostMapping("/verify-otp")
    public String verifyOtp(@RequestParam("otp") String ootp,HttpSession session,Model model) {
        model.addAttribute("title", "Verifying OTP");
        int myOtp = (int) session.getAttribute("myotp");
        String email = (String) session.getAttribute("email");
        System.out.println(ootp);
        System.out.println(myOtp);
        String numericRegex = "\\d+";
        if (ootp.matches(numericRegex)) {
            int otp = Integer.parseInt(ootp);
            if (myOtp == otp) {
                //password change form
                Optional<User> user = Optional.ofNullable(this.userRepository.getUserByUserName(email));

                if (user.isPresent()) {
                    // User exists, proceed to password change form
                    return "password_change_form";
                } else {
                    // User does not exist, send error message and redirect back to the forgot email form
                    session.setAttribute("message", new Message("User with this email-Id does not exist!!", "alert-danger"));
                    System.out.println("no user");
                    return "forgot_email_form";
                }
            }
            else {
                session.setAttribute("message", new Message("You have entered wrong OTP!!", " alert-danger "));
                System.out.println("wrong");
                return "verify_otp";
            }

        } else {
            session.setAttribute("message", new Message("You have entered wrong OTP!!", " alert-danger "));
            System.out.println("wrong");
            return "verify_otp";
        }
//        return "signin";
    }


    //change password
    @PostMapping("/change-password")
    public String changePassword(@RequestParam("newpassword") String newpassword, Model m, HttpSession session){
        String email = (String)session.getAttribute("email");
        User user = this.userRepository.getUserByUserName(email);
        user.setPassword(this.bCryptPasswordEncoder.encode(newpassword));
        this.userRepository.save(user);
        session.setAttribute("message", new Message("Your password has been changed!!", "alert-success"));
        return "redirect:/signin";
    }
}
