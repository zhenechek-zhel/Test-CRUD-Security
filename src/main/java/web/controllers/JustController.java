package web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import web.model.User;
import web.service.UserService;

import java.security.Principal;

@RestController
public class JustController {

    private UserService userService;

    @Autowired
    public JustController(UserService userService) {
        this.userService = userService;
    }



    @GetMapping("/")
    public String helloPage() {
        return "this is hello page";
    }

    @GetMapping("/auth")
    public String authPage(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        return "this is part of web service " + user.getUsername() + " " + user.getEmail();
    }

    @GetMapping("/read_profile")
    public String readProFile() {
        return "thi is read pro file page";
    }

    @GetMapping("/only_for_admins")
    public String forAdminPage() {
        return "page only for admin";
    }
}
