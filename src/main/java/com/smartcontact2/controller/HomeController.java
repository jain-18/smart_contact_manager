package com.smartcontact2.controller;

import com.smartcontact2.dao.UserRepository;
import com.smartcontact2.entities.User;
import com.smartcontact2.helper.Message;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
public class HomeController {
    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public HomeController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping("/")
    public String home(Model model,HttpSession session,Principal principal){
//        model.addAttribute("title","Home -Smart Contact Manager");
//        return "home";
        // Check if the user is logged in
        if (principal != null) {
            // If user is logged in, redirect to the dashboard page
            return "redirect:/user/index"; // Assuming the dashboard page exists
        } else {
            // If user is not logged in, allow access to the home page
            model.addAttribute("title", "Home - Smart Contact Manager");
            return "home";
        }
    }

    //handler for custom login
    @GetMapping("/signin")
    public String customLogin(Model model,Principal principal){
        model.addAttribute("title","Login Page -Smart Contact Manager");
        return "login";
    }

    @RequestMapping("/about")
    public String about(Model model,Principal principal){
//        model.addAttribute("title","About -Smart Contact Manager");
//        return "about";
        if (principal != null) {
            // If user is logged in, redirect to the dashboard page
            return "redirect:/user/index"; // Assuming the dashboard page exists
        } else {
            // If user is not logged in, allow access to the home page
            model.addAttribute("title", "Home - Smart Contact Manager");
            return "about";
        }
    }
    @RequestMapping("/signup")
    public String signup(Model model,Principal principal){
//        model.addAttribute("title","Register -Smart Contact Manager");
//
//        return "signup";
        if (principal != null) {
            // If user is logged in, redirect to the dashboard page
            return "redirect:/user/index"; // Assuming the dashboard page exists
        } else {
            // If user is not logged in, allow access to the home page
            model.addAttribute("user",new User());
            model.addAttribute("title", "Home - Smart Contact Manager");
            return "signup";
        }
    }

    //handler for register user
    @PostMapping("/do_register")
    public String registerUser(@Valid @ModelAttribute("user") User user,BindingResult result, @RequestParam(value = "agreement",defaultValue = "false") boolean agreement, Model model, HttpSession session){
        try{
            if(!agreement) {
                System.out.println("You have not agreed the terms and condition");
                model.addAttribute("user",user);
                session.setAttribute("message", new Message("You must agree to the terms and conditions", "alert-danger"));
//                throw new Exception("You have not agreed the terms and condition");
                return "signup";
            }
            if (result.hasErrors()) {
                    System.out.println("Error" + result.toString());
                    model.addAttribute("user", user);
                    return "signup";
                }else {


                    user.setRole("ROLE_USER");
                    user.setEnabled(true);
                    user.setImageUrl("default.png");
                    user.setPassword(passwordEncoder.encode(user.getPassword()));

                    User result1 = this.userRepository.save(user);
                    model.addAttribute("user", new User());
                    session.setAttribute("message", new Message("Successfully Registered!!", "alert-success"));
                }


        } catch (DataIntegrityViolationException e) {
            // Handle duplicate username or other data integrity issues
            model.addAttribute("error", "Username or email already exists");
            session.setAttribute("message",new Message("Username or email already exists","alert-danger"));
            return "signup";
        }catch (Exception e){
            e.printStackTrace();
            model.addAttribute("user",user);
            session.setAttribute("message",new Message("SomeThing Went Wrong!!"+e.getMessage(),"alert-danger"));
            return "signup";
        }
        return "signup";
    }


}
