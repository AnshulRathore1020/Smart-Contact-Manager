package com.smart.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class HomeController {

    @Autowired
    private UserRepository userRepository;
    
    
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/")
    public String home(Model m) {
        m.addAttribute("tittle", "Home - Smart Contact Manager");
        return "home";
    }

    @GetMapping("/about")
    public String about(Model m) {
        m.addAttribute("tittle", "about - Smart Contact Manager");
        return "about";
    }

    @GetMapping("/signup")
    public String signup(Model m, HttpSession session) {
        m.addAttribute("tittle", "Register - Smart Contact Manager");
        m.addAttribute("user", new User());

        // ✅ Check and pass session message to model
        Message msg = (Message) session.getAttribute("message");
        if (msg != null) {
            m.addAttribute("message", msg);
            session.removeAttribute("message"); // remove after showing
        }

        return "signup";
    }

    @PostMapping("/do_register")
    public String signuser(@Valid @ModelAttribute("user") User user,
    		               BindingResult error,
                           @RequestParam(value = "agreement", defaultValue = "false") boolean agreement,
                           Model model,
                           
                           HttpSession session  
                           ){
        try {
            if (!agreement) {
                System.out.println("You have not agreed the terms and conditions");
                throw new Exception("You have not agreed the terms and conditions");
            }
            
            if(error.hasErrors()) {
            	System.out.println("ERROR: " + error.toString());

            	model.addAttribute("user",user);
            	
            	return "signup";
            }
            
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            
            
            user.setRole("ROLE_USER");
            user.setEnabled(true);
            user.setImageUrl("default.png");

            System.out.println("Agreement: " + agreement);
            System.out.println("User: " + user);

            User result = this.userRepository.save(user);
            model.addAttribute("user", new User());

            session.setAttribute("message", new Message("Successfully Signed Up!", "alert-success"));
            return "redirect:/signup";

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("user", user);
            session.setAttribute("message", new Message("Something went wrong !! " + e.getMessage(), "alert-danger"));
            return "redirect:/signup";
        }
    }
    
    @GetMapping("/signin")
    public String CustomLogin(Model model) {
    	
    	model.addAttribute("tittle","Login page");
    	
    	return "login";
    }
    
    @GetMapping("/login-fail")
    public String loginfail() {
    	return "login-fail";
    }

}
