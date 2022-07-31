package com.paymentsystem.Controllers;

import com.paymentsystem.Models.Accountant;
import com.paymentsystem.Models.Branch;
import com.paymentsystem.Models.Roles;
import com.paymentsystem.Models.Student;
import com.paymentsystem.Services.AccountantServices;
import com.paymentsystem.Services.BranchServices;
import com.paymentsystem.Services.RolesServices;
import com.paymentsystem.Services.StudentServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping({"/","/admin"})
public class adminController {
    @Autowired private BranchServices branchServices;
    @Autowired private StudentServices studentServices;
    @Autowired private AccountantServices accountantServices;
    @Autowired private RolesServices rolesServices;
    @GetMapping("/admin")
    public String adminHome(Model model){
        List<Branch>branches=branchServices.getAllBranches();
        model.addAttribute("branches",branches);
        return "adminpage";
    }
    @PostMapping("/admin/find")
    public View findMethod(@RequestParam("search")String name,@RequestParam("branch")String branch, RedirectAttributes model){
        List<Student>students=studentServices.getAllStds(name,branch);
        List<Accountant>accountants=accountantServices.getAllAccs(name,branch);
        model.addAttribute("stds",students);
        model.addAttribute("accs",accountants);
        model.addAttribute("branch",branch);
        return new RedirectView( "adminpage");
    }
    @GetMapping("/admin/adminpage")
    public String foundAccountantOrStudent(@ModelAttribute("stds")ArrayList<Student>students,@ModelAttribute("accs")ArrayList<Accountant>accountants,Model model,RedirectAttributes rd){
        List<Student>gotStds = null;
        List<Accountant>GotAccs=null;
        List<Branch>branches=branchServices.getAllBranches();
        model.addAttribute("branches",branches);
        if(students!=null) {gotStds=new ArrayList<>();for(Student s:students)gotStds.add(s);}
        if(accountants!=null){
            GotAccs=new ArrayList<>();
            for(Accountant accountant:accountants){
                GotAccs.add(accountant);
            }
        }
        if(gotStds.size()==0&&GotAccs.size()==0){
            rd.addFlashAttribute("error","Invalid Student name and Accountant Name");
            return "redirect:/admin";
        }else if(gotStds.size()!=0&&GotAccs.size()==0)  {
            model.addAttribute("stds", gotStds);
            return "adminpage";
        }
        else if(GotAccs.size()!=0&&gotStds.size()==0){
            model.addAttribute("accs",GotAccs);
            return "adminpage";
        }
        else{
            model.addAttribute("accs",GotAccs);
            model.addAttribute("stds",gotStds);
            return "adminpage";
        }
    }
    @GetMapping("/createaccountant")
    public String showCreateNewAccountantForm(Model model){
        List<Branch>branches=branchServices.getAllBranches();
        List<Roles>roles=rolesServices.getAllRoles();
        String AccountantRoleName="";
        int AccountantRoleId=0;
        for(Roles role:roles){
            if(role.getName().equals("ACCOUNTANT")){
                AccountantRoleName=role.getName();
                AccountantRoleId=role.getRoleId();
                break;
            }
        }
        model.addAttribute("accountant",new Accountant());
        model.addAttribute("branches",branches);
        model.addAttribute("roleId",AccountantRoleId);
        model.addAttribute("roleName",AccountantRoleName);
        return "addaccountant";
    }
    @PostMapping("/saveaccountant")
    public String saveAccountant(@ModelAttribute("accountant")Accountant accountant,RedirectAttributes rd){
        String plainPassword=accountant.getPassword();
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        String encPassword=encoder.encode(plainPassword);
        accountant.setPassword(encPassword);
        Accountant newAccountant=accountantServices.addNewAccountant(accountant);
        if(newAccountant==null){
            rd.addFlashAttribute("errorAccountant","Invalid Accountant Data");
            System.out.println("from error doooooooooooooooooooooog");
            return "redirect:/createaccountant";
        }
        else{
            rd.addFlashAttribute("successAccountant","New Accountant Successfully Added!!!");
            System.out.println("from successfull doooooooooooooooooooooog");
            return "redirect:/admin/createaccountant";
        }
    }
    @GetMapping("/student/delete/{stdId}")
    public String deleteStudent(@PathVariable("stdId")int id,RedirectAttributes rd){
        boolean deleted=studentServices.deleteStudentById(id);
        if (deleted){
            rd.addFlashAttribute("deletedStudent","Student Deleted Successfully!!");
            return "redirect:/admin";
        }
        else{
            rd.addFlashAttribute("faildDeletingStudent","Failed to Delete Student!!");
            return "redirect:/admin";
        }
    }
    @GetMapping("/student/edit/{stdId}")
    public String editStudnt(@PathVariable("stdId")int id, RedirectAttributes rd,Model model){
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
            return "redirect:/admin";
        }
    }
    @GetMapping("/accountant/delete/{acId}")
    public String deleteAccountant(@PathVariable("acId") int id,RedirectAttributes rd){
        boolean deleted=accountantServices.deleteAccountantById(id);
        if(deleted){
            rd.addFlashAttribute("deletedAccountant","Accountant Deleted Successfully!!");
            return "redirect:/admin";
        }
        else{
            rd.addFlashAttribute("faildDeletingAccountant","Failed to Delete Accountant!!");
            return "redirect:/admin";
        }
    }
    @GetMapping("/accountant/edit/{acId}")
    public String editAccountant(@PathVariable("acId")int id,Model model,RedirectAttributes rd){
        Accountant accountant=accountantServices.getAccountantById(id);
        List<Branch>branches=branchServices.getAllBranches();
        if(accountant!=null){
            String roleName=accountant.getRole().getName();
            int roleId=accountant.getRole().getRoleId();
            model.addAttribute("accountant",accountant);
            model.addAttribute("branches",branches);
            model.addAttribute("roleName",roleName);
            model.addAttribute("roleId",roleId);
            return "addaccountant";
        }
        else{
            rd.addFlashAttribute("errorEditAccountant","Invalid Accountant");
            return "redirect:/admin";
        }
    }
    @GetMapping("/allstudent")
    public String getAllStudents(Model model){
        List<Student>students=studentServices.getAllStudents();
        List<Branch>branches=branchServices.getAllBranches();
        model.addAttribute("allstds",students);
        model.addAttribute("branches",branches);
        return "adminpage";
    }
    @GetMapping("/allaccountant")
    public String getAllAccountant(Model model){
        List<Accountant> accountants=accountantServices.getAllAccountant();
        List<Branch>branches=branchServices.getAllBranches();
        model.addAttribute("allaccs",accountants);
        model.addAttribute("branches",branches);
        return "adminpage";    }
}
