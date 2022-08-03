package com.paymentsystem.Controllers;

import com.paymentsystem.Models.*;
import com.paymentsystem.Security.MyUserDetails;
import com.paymentsystem.Services.*;
import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Controller
public class studentController {
    @Autowired private BranchServices branchServices;
    @Autowired private StudentServices studentServices;
    @Autowired private RolesServices rolesServices;
    @Autowired private CourseServices courseServices;
    @Autowired private TrainerServices trainerServices;
    @GetMapping({"/student"})
    public String Home(Model model, @AuthenticationPrincipal MyUserDetails userDetails,RedirectAttributes rd ){

        String LogedEmail=userDetails.getEmailLoggedUser();
        Student LogedStudent=studentServices.getStudentByEmail(LogedEmail);
        Branch StudentBranch=LogedStudent.getBranch();
        List<Course> courses=courseServices.getAllCourses();
        Set<Course>Enrolled=LogedStudent.getCourses();
        List<Course>found=new ArrayList<>();
        for(Course course:courses){
            if(course.getBranches().contains(StudentBranch)){
                found.add(course);
            }
        }
        model.addAttribute("foundCourses",found);
        model.addAttribute("enrolled",Enrolled);
        model.addAttribute("std",LogedStudent);
        return "studenthome";
    }

    @PostMapping("/student/enroll")
    public String EnrollStudentInCourses(@RequestParam(value = "courses",required = false)Set<Course>courses,RedirectAttributes rd,@AuthenticationPrincipal MyUserDetails userDetails)
    {
        //System.out.println("from post doooog!! "+courses);
        if(courses==null){
            rd.addFlashAttribute("CantBeEmpty","select course please");
            return "redirect:/student";
        }
        String LogedEmail=userDetails.getEmailLoggedUser();
        Student LogedStudent=studentServices.getStudentByEmail(LogedEmail);
        Set<Course>alreadyHave= LogedStudent.getCourses();
        for(Course c:courses){
            if(alreadyHave.contains(c)==false){
                if(c.getCourseSalary()<=LogedStudent.getBalance()) {
                    alreadyHave.add(c);
                    double newBalance=LogedStudent.getBalance()-c.getCourseSalary();
                    LogedStudent.setBalance(newBalance);
                }
                else{
                    rd.addFlashAttribute("NoBalance","Sorry No Enought Balance");
                    return "redirect:/student";
                }
            }
        }
        LogedStudent.setCourses(alreadyHave);
        boolean StudendEnrolled=studentServices.saveStudentWithEnrolledCourses(LogedStudent);
        if(StudendEnrolled){
            rd.addFlashAttribute("Enrolled","You Have Enrolled Successfully !!");
            return "redirect:/student";
        }
        else{
            rd.addFlashAttribute("FaildEnroll","You Faild To Enroll !!");
            return "redirect:/student";
        }
    }
    @GetMapping("/student/removecourse/{courseName}")
    public String removeCourseFromStudent(@PathVariable("courseName")String courseName,RedirectAttributes rd,@AuthenticationPrincipal MyUserDetails userDetails){
       Course course=courseServices.getCourseByName(courseName);
       double coursePrice=course.getCourseSalary();
        String LogedEmail=userDetails.getEmailLoggedUser();
        Student LogedStudent=studentServices.getStudentByEmail(LogedEmail);
        Set<Course> LogedCourses=LogedStudent.getCourses();
        LogedCourses.remove(course);
        LogedStudent.setCourses(LogedCourses);
        LogedStudent.setBalance(LogedStudent.getBalance()+coursePrice);
        boolean done=studentServices.saveStudentWithEnrolledCourses(LogedStudent);
        if(done){
            rd.addFlashAttribute("CourseRemoved","You Have Removed From Course");
            return "redirect:/student";
        }
        else{
            rd.addFlashAttribute("CourseRemovedFaild","You Have Faild To Cancle Joining Course");
            return "redirect:/student";
        }
    }
    @GetMapping("/student/register")
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
    @RequestMapping(value = "/student/register",method = RequestMethod.POST)
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
