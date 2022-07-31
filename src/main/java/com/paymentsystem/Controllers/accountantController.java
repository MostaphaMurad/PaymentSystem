package com.paymentsystem.Controllers;

import com.paymentsystem.Models.*;
import com.paymentsystem.Security.MyUserDetails;
import com.paymentsystem.Services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class accountantController {
    @Autowired private RolesServices rolesServices;
    @Autowired private BranchServices branchServices;
    @Autowired private StudentServices studentServices;
    @Autowired private AccountantServices accountantServices;
    @Autowired private TrainerServices trainerServices;
    @GetMapping("/accountant")
    public String getAccountant(Model model){
        List<Student> students=studentServices.getAllStudents();
        List<Branch>branches=branchServices.getAllBranches();
        model.addAttribute("branches",branches);
        return "accountantpage";
    }

    @PostMapping("/accountant/find")
    public View findMethod(@RequestParam("search")String name, @RequestParam("branch")String branch, RedirectAttributes model, @AuthenticationPrincipal MyUserDetails userDetails){
        List<Student>students=studentServices.getAllStds(name,branch);
        model.addAttribute("stds",students);
        model.addAttribute("branch",branch);
        System.out.println(userDetails.getAccountantBranch());
        return new RedirectView( "accountantpage");
    }
    @GetMapping("/accountant/accountantpage")
    public String foundAccountantOrStudent(@ModelAttribute("stds") ArrayList<Student> students, Model model, RedirectAttributes rd){
        List<Student>gotStds = null;
        List<Branch>branches=branchServices.getAllBranches();
        model.addAttribute("branches",branches);
        if(students!=null) {
            System.out.println("here dooog not null student get method ");
            gotStds=new ArrayList<>();
            for(Student s:students)
                gotStds.add(s);
        }
        if(gotStds.size()==0){

            rd.addFlashAttribute("error","Invalid Student Name");
            return "redirect:/accountant";
        }else {
            model.addAttribute("stds", gotStds);
            return "accountantpage";
        }
    }
    @GetMapping("/accountant/allstudent")
    public String getAllStudents(Model model){
        List<Student>students=studentServices.getAllStudents();
        List<Branch>branches=branchServices.getAllBranches();
        model.addAttribute("allstds",students);
        model.addAttribute("branches",branches);
        return "accountantpage";
    }
    @GetMapping("/accountant/student/edit/{stdId}")
    public String AccountantEditStudent(@PathVariable("stdId")int id,Model model,RedirectAttributes rd){
        Student student=studentServices.getStudentById(id);
        String roleName=student.getRole().getName();
        int roleId=student.getRole().getRoleId();
        List<Branch>branches=branchServices.getAllBranches();
        if(student!=null){
            model.addAttribute("roleName",roleName);
            model.addAttribute("roleId",roleId);
            model.addAttribute("branches",branches);
            model.addAttribute("student",student);
            return "register";
        }
        else{
            rd.addFlashAttribute("errorEditStudent","Invalid Student !!");
            return "redirect:/accountant";
        }
    }
    @GetMapping("/accountant/student/delete/{stdId}")
    public String deleteStudent(@PathVariable("stdId")int id,RedirectAttributes rd,Model model){
        boolean deleted=studentServices.deleteStudentById(id);
        List<Student>students=studentServices.getAllStudents();
        model.addAttribute("allstds",students);
        if (deleted){
            rd.addFlashAttribute("deletedStudent","Student Deleted Successfully!!");
            return "redirect:/accountant/allstudent";
        }
        else{
            rd.addFlashAttribute("faildDeletingStudent","Failed to Delete Student!!");
            return "redirect:/accountant/allstudent";
        }
    }
    @GetMapping("/accountant/addtrainer")
    public String getTrainer(Model model){
        String roleName="";
        int roleId=0;
        List<Roles>roles=rolesServices.getAllRoles();
        for(Roles r:roles){
            if(r.getName().equals("TRAINER")){
                roleName=r.getName();
                roleId=r.getRoleId();
                break;
            }
        }
        List<Branch>branches=branchServices.getAllBranches();
        model.addAttribute("brns",branches);
        model.addAttribute("trainer",new Trainer());
        model.addAttribute("roleName",roleName);
        model.addAttribute("roleId",roleId);
        return "addtrainer";
    }
    @PostMapping("/accountant/savetrainer")
    public String saveTrainer(@RequestParam("branch")String bran,@ModelAttribute("trainer")Trainer trainer,RedirectAttributes rd){
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        String plainPassword=trainer.getPassword();
        String encPassword=encoder.encode(plainPassword);
        trainer.setPassword(encPassword);
        List<Branch>branches=new ArrayList<>();
        Branch b=new Branch();
        b.setName(bran);
        b.setTrainers(Collections.singleton(trainer));
        trainer.setBranches(Collections.singleton(b));
        boolean saved=trainerServices.addNewTrainer(trainer);
        System.out.println("Trainer here dooog!!"+trainer);
        System.out.println(bran);

        if(saved){
            rd.addFlashAttribute("TrainedAdded","Trainer Added Successfully!!");
            return "redirect:/accountant/addtrainer";
        }
        else{
            rd.addFlashAttribute("ErrorAddingTrainer","Invalid Trainer Data!!");
            return "redirect:/accountant/addtrainer";
        }
    }
    @GetMapping("/accountant/alltrainer")
    public String getAllTrainers(Model model){
        List<Student>students=studentServices.getAllStudents();
        List<Branch>branches=branchServices.getAllBranches();
        List<Trainer>trainers=trainerServices.getAllTrainer();
        model.addAttribute("trainers",trainers);
        model.addAttribute("allstds",students);
        model.addAttribute("branches",branches);
        return "accountantpage";
    }
}
