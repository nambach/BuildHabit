package io.nambm.buildhabit.controller.impl;

import io.nambm.buildhabit.controller.HomeController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class HomeControllerImpl implements HomeController {

    @GetMapping("/")
    public ModelAndView home() {
        return new ModelAndView("login/login");
    }

    @GetMapping("/demo-geo")
    public ModelAndView demoGeo() {
        return new ModelAndView("demo/geolocationDemo");
    }

    @GetMapping("/demo-store")
    public ModelAndView demoStore() {
        return new ModelAndView("demo/storageDemo");
    }

    @GetMapping("/demo-cache")
    public ModelAndView demoCache() {
        return new ModelAndView("demo/html_manifest");
    }

}
