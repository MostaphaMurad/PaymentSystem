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

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Controller
public class accountantController {
    @Autowired private RolesServices rolesServices;
    @Autowired private CourseServices courseServices;
    @Autowired private BranchServices branchServices;
    @Autowired private StudentServices studentServices;
    @Autowired private AccountantServices accountantServices;
    @Autowired private TrainerServices trainerServices;
    @GetMapping("/accountant")
    public String getAccountant(Model model,@AuthenticationPrincipal MyUserDetails userDetails){
       /* if(userDetails==null)return "redirect:/login";*/
        String loggedAccountantBranch=userDetails.getAccountantBranch();
        Branch acountantBranch=branchServices.getBranchByName(loggedAccountantBranch);
        List<Student> students=studentServices.getAllStudents();
        List<Course>courses=courseServices.getAllCourses();
        List<Student>stdInAccBranch=new ArrayList<>();
        for(Student s:students){
            if(s.getBranch().getName().equals(loggedAccountantBranch)){
                stdInAccBranch.add(s);
            }
        }
        List<Course>coursesInAccountantBranch=new ArrayList<>();
        for(Course course:courses){
            if(course.getBranches().contains(acountantBranch)){
                coursesInAccountantBranch.add(course);
            }
        }
       // List<Branch>branches=branchServices.getAllBranches();
        model.addAttribute("branch",acountantBranch);
        model.addAttribute("allstds",stdInAccBranch);
        model.addAttribute("courses",coursesInAccountantBranch);
        model.addAttribute("course",new Course());
        return "accountantpage";
    }
    @GetMapping("/accountant/student/register")
    public String addNewStudent(Model model,@AuthenticationPrincipal MyUserDetails userDetails){
        String loggedAccBranch=userDetails.getAccountantBranch();
        Branch accBranch=branchServices.getBranchByName(loggedAccBranch);
        Roles role=rolesServices.GetRoleByName("STUDENT");
        String roleName=role.getName();
        int roleId=role.getRoleId();
        model.addAttribute("roleName",roleName);
        model.addAttribute("roleId",roleId);
        model.addAttribute("branches",accBranch);
        model.addAttribute("student",new Student());
        return "register";
    }
    @PostMapping("/accountant/find")
    public String findMethod(@RequestParam("search")String name, @RequestParam("branch")String branch, RedirectAttributes model, @AuthenticationPrincipal MyUserDetails userDetails){

        List<Student>students=studentServices.getAllStds(name,branch);
        model.addAttribute("stds",students);
        model.addAttribute("branch",branch);
        return "redirect:/accountant/accountantpage";
    }
    @GetMapping("/accountant/accountantpage")
    public String foundAccountantOrStudent(@ModelAttribute("stds") ArrayList<Student> students,@AuthenticationPrincipal MyUserDetails userDetails, Model model, RedirectAttributes rd){
        String loggedBranch=userDetails.getAccountantBranch();
        Branch accBranch=branchServices.getBranchByName(loggedBranch);
        List<Student>gotStds = accBranch.getStudents();
        //List<Branch>branches=branchServices.getAllBranches();
        model.addAttribute("branch",accBranch);
        if(students!=null) {
            gotStds=new ArrayList<>();
            for(Student s:students)
                gotStds.add(s);
        }
        if(gotStds.size()==0){

            model.addAttribute("branch",accBranch);
            rd.addFlashAttribute("error","Invalid Student Name");
            return "accountantpage";
        }else {
            model.addAttribute("stds", gotStds);
            model.addAttribute("branch",accBranch);
            return "accountantpage";
        }
    }
    @GetMapping("/accountant/allstudent")
    public String getAllStudents(Model model,@AuthenticationPrincipal MyUserDetails userDetails){
  /*      if(userDetails==null){
            return "redirect:/login";
        }*/
        String loggedBranch=userDetails.getAccountantBranch();
        Branch branch=branchServices.getBranchByName(loggedBranch);
        List<Student> students=studentServices.getAllStudents();
        List<Course>courses=courseServices.getAllCourses();
        List<Student>stdInAccBranch=new ArrayList<>();
        for(Student s:students){
            if(s.getBranch().equals(branch)){
                stdInAccBranch.add(s);
            }
        }
        List<Course>coursesInAccountantBranch=new ArrayList<>();
        for(Course course:courses){
            if(course.getBranches().contains(branch)){
                coursesInAccountantBranch.add(course);
            }
        }
        List<Trainer>trainers=trainerServices.getAllTrainer();
        List<Trainer>trainersInAccBranch=new ArrayList<>();
        for(Trainer trainer:trainers){
            if(trainer.getBranches().contains(branch)){
                trainersInAccBranch.add(trainer);
            }
        }
        model.addAttribute("allstds",stdInAccBranch);
        model.addAttribute("branch",branch);
        model.addAttribute("courses",coursesInAccountantBranch);
        model.addAttribute("trainers",trainersInAccBranch);
        return "accountantpage";
    }
    @GetMapping("/accountant/student/edit/{stdId}")
    public String AccountantEditStudent(@PathVariable("stdId")int id,Model model,RedirectAttributes rd,@AuthenticationPrincipal MyUserDetails userDetails){
        Student student=studentServices.getStudentById(id);
        String roleName=student.getRole().getName();
        int roleId=student.getRole().getRoleId();

        String loggedBranch=userDetails.getAccountantBranch();
        Branch accBranch=branchServices.getBranchByName(loggedBranch);
        if(student!=null){
            model.addAttribute("roleName",roleName);
            model.addAttribute("roleId",roleId);
            model.addAttribute("branches",accBranch);
            model.addAttribute("student",student);
            return "register";
        }
        else{
            rd.addFlashAttribute("errorEditStudent","Invalid Student !!");
            return "redirect:/accountant";
        }
    }
    @GetMapping("/accountant/student/delete/{stdId}")
    public String deleteStudent(@PathVariable("stdId")int id,@AuthenticationPrincipal MyUserDetails userDetails,RedirectAttributes rd,Model model){
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
    public String getTrainer(Model model,@AuthenticationPrincipal MyUserDetails userDetails){
        String loggedBranch=userDetails.getAccountantBranch();
        Branch accBranch=branchServices.getBranchByName(loggedBranch);
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
        List<Course>courses=courseServices.getAllCourses();
        List<Course>coursesInThisBranch=new ArrayList<>();
        for(Course course:courses){
            if(course.getBranches().contains(accBranch)){
                coursesInThisBranch.add(course);
            }
        }
        model.addAttribute("brns",accBranch);
        model.addAttribute("trainer",new Trainer());
        model.addAttribute("roleName",roleName);
        model.addAttribute("roleId",roleId);
        model.addAttribute("courses",coursesInThisBranch);
        return "addtrainer";
    }
    @PostMapping("/accountant/savetrainer")
    public String saveTrainer(@RequestParam("branch")String bran, @RequestParam("courses") Set<Course> courseList, @ModelAttribute("trainer")Trainer trainer, RedirectAttributes rd){
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        String plainPassword=trainer.getPassword();
        String encPassword=encoder.encode(plainPassword);
        trainer.setPassword(encPassword);
        ArrayList<Branch>branches=new ArrayList<>();
        Branch branch=branchServices.getBranchByName(bran);
        branch.setTrainers(Collections.singleton(trainer));
        trainer.setBranches(Collections.singleton(branch));
        if(trainer.getMaxCourses()>=courseList.size()){
            trainer.setCourses(courseList);
            int dif=trainer.getMaxCourses()-courseList.size();
            trainer.setMaxCourses(dif);
            if(dif==0){
                trainer.setAvailable(false);
            }

        }
        else{
            rd.addFlashAttribute("TrainerAssignedCourseNotValidNumber","You Cant Assign More Courses");
            return "redirect:/accountant/addtrainer";
        }
        boolean saved=trainerServices.addNewTrainer(trainer);

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
    public String getAllTrainers(Model model,@AuthenticationPrincipal MyUserDetails userDetails){
        String loggedAccBranch=userDetails.getAccountantBranch();
        Branch accBranch=branchServices.getBranchByName(loggedAccBranch);
        List<Student>students=studentServices.getAllStudents();
        List<Branch>branches=branchServices.getAllBranches();
        List<Trainer>trainers=trainerServices.getAllTrainer();
        List<Student>stdsInAccBranch=new ArrayList<>();
        List<Trainer>trainersInAccBranch=new ArrayList<>();
        for(Student s:students){
            if(s.getBranch().equals(accBranch)){
                stdsInAccBranch.add(s);
            }
        }
        for(Trainer trainer:trainers){
            if(trainer.getBranches().contains(accBranch)){
                trainersInAccBranch.add(trainer);
            }
        }
        model.addAttribute("trainers",trainersInAccBranch);
       model.addAttribute("allstds",stdsInAccBranch);
        model.addAttribute("branch",accBranch);
        return "accountantpage";
    }
    @GetMapping("/accountant/trainer/assigncourse/{trID}")
    public String GetCoursesForTrainer(@PathVariable("trID")int id,@AuthenticationPrincipal MyUserDetails userDetails,RedirectAttributes rd,Model model){
        String loggedBranch=userDetails.getAccountantBranch();
        Branch accBranch=branchServices.getBranchByName(loggedBranch);

        Set<Course>coursesInThisBranch=accBranch.getCourses();

        model.addAttribute("courses",coursesInThisBranch);
        model.addAttribute("branches",accBranch);
        Trainer trainer=trainerServices.getTrainerById(id);
        model.addAttribute("trainer",trainer);
        return "trainercourse";
    }
    @PostMapping("/accountant/trainer/assigncourse")
    public String AssignCoursesForTrainer(@ModelAttribute("trainer")Trainer trainer,@RequestParam("courses") Set<Course> courses,Model model,RedirectAttributes rd){
      int id=trainer.getId();
      Trainer trainer1=trainerServices.getTrainerById(id);
      Set<Course>old=trainer1.getCourses();
        if(trainer1!=null){
            if(trainer.getMaxCourses()>=courses.size()+old.size()){
                int dif=trainer1.getMaxCourses()-(courses.size()+old.size());
                trainer1.setMaxCourses(dif);
                if(dif<=0)trainer1.setAvailable(false);
                for(Course course:courses){
                    old.add(course);
                }
                trainer1.setCourses(old);
                boolean newTrainerWithCourses=trainerServices.saveTrainerWithAssignedCourses(trainer1);
                if(newTrainerWithCourses){
                    rd.addFlashAttribute("AssignedCourses","Assigned Courses Successfully!!");
                    return "redirect:/accountant";
                }
                else{
                    rd.addFlashAttribute("FaildAssignedCourses","Assigned Courses Failed!!");
                    return "redirect:/accountant";
                }
            }
            else {
                rd.addFlashAttribute("FaildAssignedCourses", "Assigned Courses Failed!!");
                return "redirect:/accountant";
            }

        }
        else{
            rd.addFlashAttribute("FaildAssignedCourses","Assigned Courses Failed!!");
            return "redirect:/accountant";
        }
    }
    @GetMapping("/accountant/trainer/delete/{tID}")
    public String DeleteTrainer(@PathVariable("tID")int id,@AuthenticationPrincipal MyUserDetails userDetails,RedirectAttributes rd){
     /*  if(userDetails==null)return "redirect:/login";*/
        boolean deleteTrainer=trainerServices.DeleteTrainerById(id);
        if(deleteTrainer){
            rd.addFlashAttribute("deletedTrainer","Trainer Deleted Successfully!!");
            return "redirect:/accountant";
        }
        else{
            rd.addFlashAttribute("faildDeletedTrainer","Faild To Delete Trainer!!");
            return "redirect:/accountant";
        }
    }
    @GetMapping("/accountant/trainer/edit/{tID}")
    public String editTrainer(@PathVariable("tID")int id,Model model,@AuthenticationPrincipal MyUserDetails userDetails){
        String loggedBranch=userDetails.getAccountantBranch();
        Branch accBranch=branchServices.getBranchByName(loggedBranch);

        Set<Course>coursesInThisBranch=accBranch.getCourses();


        Trainer trainer=trainerServices.getTrainerById(id);
        if(trainer!=null){
            String roleName=trainer.getRole().getName();
            int roleId=trainer.getRole().getRoleId();
            model.addAttribute("brns",accBranch);
            model.addAttribute("courses",coursesInThisBranch);
            model.addAttribute("roleName",roleName);
            model.addAttribute("roleId",roleId);
            model.addAttribute("trainer",trainer);
            return "addtrainer";
        }
        else{
            return "redirect:/accountant";
        }
    }
    @GetMapping("/accountant/addcourse")
    public String getAddCourse(Model model,@AuthenticationPrincipal MyUserDetails userDetails){
       /* if(userDetails==null)return "redirect:/login";*/
        String loggedAccBranch=userDetails.getAccountantBranch();
        Branch accBranch=branchServices.getBranchByName(loggedAccBranch);
        model.addAttribute("course",new Course());
        model.addAttribute("branches",accBranch);
        return "addcourse";
    }
    @PostMapping("/accountant/course/addcourse")
    public String addCourse(@ModelAttribute("course")Course course,@RequestParam("branch")Set<Branch> branchsName,Model model,RedirectAttributes rd){
        course.setBranches(branchsName);
        Boolean saved=courseServices.addNewCourse(course);
        if(saved){
            rd.addFlashAttribute("savedCourse","Course Added Successfully!!");
            return "redirect:/accountant";
        }
        else {
            rd.addFlashAttribute("FailedsaveCourse","Failed To Add New Course!!!");
            return "redirect:/accountant";
        }
    }
    @GetMapping("/accountant/course/edit/{cName}")
    public String editCourse(@PathVariable("cName")String courseName,@AuthenticationPrincipal MyUserDetails userDetails,Model model){
       String loggedBranch=userDetails.getAccountantBranch();
       Branch accBranch=branchServices.getBranchByName(loggedBranch);

        Course course=courseServices.getCourseByName(courseName);

        if(course!=null){
            model.addAttribute("course",course);
            model.addAttribute("branches",accBranch);
            return "addcourse";
        }else return "redirect:/accountant";
    }
    @GetMapping("/accountant/getallcourses")
    public String getCourses(Model model,@AuthenticationPrincipal MyUserDetails userDetails){
/*        if(userDetails==null)return "redirect:/login";*/
        String loggedAccBranch=userDetails.getAccountantBranch();
        Branch accBranch=branchServices.getBranchByName(loggedAccBranch);

        List<Student>students=studentServices.getAllStudents();
        List<Branch>branches=branchServices.getAllBranches();
        List<Trainer>trainers=trainerServices.getAllTrainer();
        List<Course> courses=courseServices.getAllCourses();


        List<Student>stdsInAccBranch=new ArrayList<>();
        for(Student s:students){
            if(s.getBranch().equals(accBranch)){
                stdsInAccBranch.add(s);
            }
        }

        List<Course>coursesInAccBranch=new ArrayList<>();
        for(Course course:courses){
            if(course.getBranches().contains(accBranch)){
                coursesInAccBranch.add(course);
            }
        }

        List<Trainer>trainersInAccBranch=new ArrayList<>();
        for(Trainer trainer:trainers){
            if(trainer.getBranches().contains(accBranch))
                trainersInAccBranch.add(trainer);
        }

        model.addAttribute("trainers",trainersInAccBranch);
        model.addAttribute("allstds",stdsInAccBranch);
        model.addAttribute("branch",accBranch);
        model.addAttribute("courses",coursesInAccBranch);

        return "accountantpage";
    }
    @GetMapping("/accountant/course/delete/{cName}")
    public String deleteCourse(@PathVariable("cName")String courseName,Model model,RedirectAttributes rd){

        List<Trainer>trainers=trainerServices.getAllTrainer();
        Course course=courseServices.getCourseByName(courseName);
        List<Branch>branchList=branchServices.getAllBranches();
        List<Student>studentList=studentServices.getAllStudents();
        for(Trainer t:trainers){
            if(t.getCourses().contains(course)==true){

                course.removeTrainer(t);
            }
        }
        for(Branch b:branchList){
            if(b.getCourses().contains(course)){
                course.removeBranch(b);
            }
        }
        for(Student s:studentList){
            if(s.getCourses().contains(course)){
                double bal=s.getBalance()+course.getCourseSalary();
                s.setBalance(bal);
                Student ss=studentServices.addNewStudent(s);
                course.removeStudent(s);
            }
        }
        boolean deleted=courseServices.deleteCourseByName(courseName);
        if(deleted){
            rd.addFlashAttribute("CourseDeleted","Course Deleted Successfully!!");
            model.addAttribute("trainers",trainers);
            return "redirect:/accountant";
        }
        else{
            rd.addFlashAttribute("FaildDeleteCourse","Faild To Delete Course");
            return "redirect:/accountant";
        }
    }
}
