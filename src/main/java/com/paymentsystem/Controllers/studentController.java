package com.paymentsystem.Controllers;

import com.paymentsystem.Models.Branch;
import com.paymentsystem.Models.Roles;
import com.paymentsystem.Models.Student;
import com.paymentsystem.Services.BranchServices;
import com.paymentsystem.Services.RolesServices;
import com.paymentsystem.Services.StudentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
@Controller
@RequestMapping({"/student","stud"})
public class studentController {
    @Autowired
    BranchServices branchServices;
    @Autowired private StudentServices studentServices;
    @Autowired private RolesServices rolesServices;
    @GetMapping("/register")
    public String studRegister(Model model){
        List<Branch>branches=branchServices.getAllBranches();
        model.addAttribute("student",new Student());
        model.addAttribute("branches",branches);
        List<Roles> roles=rolesServices.getAllRoles();
        String roleName="";
        int roleId=0;
        for(Roles r:roles){
            if(r.getName().equals("STUDENT")){
                roleName=r.getName();
                roleId=r.getRoleId();
                break;
            }
        }
        model.addAttribute("roleName",roleName);
        model.addAttribute("roleId",roleId);
        return "register";
    }
    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public String addStudent(@ModelAttribute("student")Student student, RedirectAttributes rd){
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        String plainPassword= student.getPassword();
        String encPassword=encoder.encode(plainPassword);
        student.setPassword(encPassword);
        Student newStud=studentServices.addNewStudent(student);
        if(newStud!=null){
            rd.addFlashAttribute("success","Successfully Registered!");
            return "redirect:/login";
        }
        rd.addFlashAttribute("error","Invalid Data!");
        return "redirect:/student/register";
    }
}
