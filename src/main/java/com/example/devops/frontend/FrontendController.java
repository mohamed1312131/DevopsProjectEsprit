package com.example.devops.frontend;

import com.example.devops.services.foyer.IFoyerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class FrontendController {

    private final IFoyerService foyerService;

    public FrontendController(IFoyerService foyerService) {
        this.foyerService = foyerService;
    }

    @GetMapping("/")
    public String homePage(Model model) {
        model.addAttribute("message", "Welcome to the University Management System!");
        return "home";
    }

    @GetMapping("/foyer")
    public String foyersPage(Model model) {
        model.addAttribute("foyers", foyerService.findAll());
        return "foyer";
    }
}
