package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.security.Principal;
import java.util.List;


@Controller
public class UserController {

    private final UserService userService;

    private final RoleService roleService;

    @Autowired
    public UserController(UserServiceImpl userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "/admin")
    public String adminPage(ModelMap model, Principal principal) {
        User user1 = userService.findByEmail(principal.getName());
        model.addAttribute("userInfo", user1);
        model.addAttribute(userService.findAll());
        model.addAttribute("user", new User());
        List<Role> roles = roleService.findAll();
        model.addAttribute("allRoles", roles);

        return "admin";
    }

    @GetMapping(value = "/user")
    public String userPage(ModelMap model, Principal principal) {
        User user = userService.findByEmail(principal.getName());
        model.addAttribute("user", user);

        return "user";
    }

    @PostMapping("/save_user")
    public String saveUser(@ModelAttribute User user) {
        try {
            userService.save(user);
        } catch (RuntimeException ignored) {
        }
        return "redirect:/admin";
    }

    @PostMapping("/update_user")
    public String updateUser(@ModelAttribute User user) {
        try {
            userService.update(user);
        } catch (RuntimeException ignored) {
        }

        return "redirect:/admin";
    }

    @PostMapping("/delete_user")
    public String deleteUser(@ModelAttribute User user) {
        userService.deleteById(user.getId());

        return "redirect:/admin";
    }
}