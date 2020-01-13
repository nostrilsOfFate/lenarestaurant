package ru.lena.restaurant.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RootController {

    @GetMapping("/")
    public String root() {
        return "redirect:restaurants";
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/users")
    public String getUsers() {
        return "users";
    }

    @GetMapping(value = "/login")
    public String login() {
        return "login";
    }

//    @PostMapping("/users")
//    public String setUser(HttpServletRequest request) {
//        long userId = Long.parseLong(request.getParameter("userId"));
//        SecurityUtil.setAuthUserId(userId);
//        return "redirect:restaurants";
//    }

    @GetMapping("/dishes")
    public String getDishes() {
        return "dishes";
    }

    @GetMapping("/restaurants")
    public String getRestaurants() {
        return "restaurants";
    }

    @GetMapping("/history")
    public String getVoteHistory() {
        return "history";
    }


}
