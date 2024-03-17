package com.empbulletin.bootcampersbulletin.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ApplicationController {
    @GetMapping("/index")
    public String gondex() {
        return "index";
    }

    @GetMapping("/Home")
    public String goHome() {
        return "index";
    }

    @GetMapping("/Admin")
    public String goadmin() {
        return "AdminLogin";
    }

    @GetMapping("/Employee")
    public String goemployee() {
        return "EmployeeLogin";
    }

    @GetMapping("/About")
    public String goabout() {
        return "About";
    }

    @GetMapping("/Contact")
    public String gocontact() {
        return "Contact";
    }

    @GetMapping("/EmployeeDashboard")
    public String goemployeedashboard() {
        return "EmployeeDashboard";
    }

    @GetMapping("/AdminDashboard")
    public String goadmindashboard() {
        return "AdminDashboard";
    }

    @GetMapping("/EmployeeViewDashboard")
    public String goemployeeviewdashboard() {
        return "EmployeeViewDashboard";
    }

}