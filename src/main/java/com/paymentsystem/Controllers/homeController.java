package com.paymentsystem.Controllers;

import com.paymentsystem.Models.Admin;
import com.paymentsystem.Models.Branch;
import com.paymentsystem.Security.MyUserDetails;
import com.paymentsystem.Services.BranchServices;
import com.paymentsystem.Services.UserServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping({"/home","/"})
public class homeController {
    @Autowired private BranchServices branchServices;
    @Autowired
    UserServices userServices;
    @RequestMapping("/")
    public String home(Model model){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        if(authentication==null||authentication instanceof AnonymousAuthenticationToken)return "redirect:/login";
        else{
            return "index";
        }
    }
    @GetMapping({"/login"})
    public String login(Model model){

        List<Branch>branches=branchServices.getAllBranches();
        model.addAttribute("branches",branches);
        return "login";
    }
}
