package io.nambm.buildhabit.controller.impl;

import io.nambm.buildhabit.controller.HomeController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeControllerImpl implements HomeController {

    @GetMapping("/")
    public ModelAndView home() {
        return new ModelAndView("home/index");
    }

}
