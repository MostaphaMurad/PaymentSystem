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
        if(userDetails==null)return "redirect:/login";
        List<Student> students=studentServices.getAllStudents();
        List<Branch>branches=branchServices.getAllBranches();
        model.addAttribute("branches",branches);
        List<Course>courses=courseServices.getAllCourses();
        model.addAttribute("courses",courses);
        model.addAttribute("course",new Course());
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
    public String getAllStudents(Model model,@AuthenticationPrincipal MyUserDetails userDetails){
        if(userDetails==null){
            return "redirect:/login";
        }
        List<Student>students=studentServices.getAllStudents();
        List<Branch>branches=branchServices.getAllBranches();
        List<Trainer>trainers=trainerServices.getAllTrainer();
        model.addAttribute("allstds",students);
        model.addAttribute("branches",branches);
        model.addAttribute("trainers",trainers);
        return "accountantpage";
    }
    @GetMapping("/accountant/student/edit/{stdId}")
    public String AccountantEditStudent(@PathVariable("stdId")int id,Model model,RedirectAttributes rd,@AuthenticationPrincipal MyUserDetails userDetails){
        if(userDetails==null){
            return "redirect:/login";
        }
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
    public String deleteStudent(@PathVariable("stdId")int id,@AuthenticationPrincipal MyUserDetails userDetails,RedirectAttributes rd,Model model){
        if(userDetails==null)return "redirect:/login";
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
        if(userDetails==null)return "redirect:/login";
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
        List<Course>courses=courseServices.getAllCourses();
        model.addAttribute("brns",branches);
        model.addAttribute("trainer",new Trainer());
        model.addAttribute("roleName",roleName);
        model.addAttribute("roleId",roleId);
        model.addAttribute("courses",courses);
        return "addtrainer";
    }
    @PostMapping("/accountant/savetrainer")
    public String saveTrainer(@RequestParam("branch")String bran, @RequestParam("courses") Set<Course> courseList, @ModelAttribute("trainer")Trainer trainer, RedirectAttributes rd){
        BCryptPasswordEncoder encoder=new BCryptPasswordEncoder();
        String plainPassword=trainer.getPassword();
        String encPassword=encoder.encode(plainPassword);
        trainer.setPassword(encPassword);
        ArrayList<Branch>branches=new ArrayList<>();
        Branch b=new Branch();
        b.setName(bran);
        b.setTrainers(Collections.singleton(trainer));
        trainer.setBranches(Collections.singleton(b));
        trainer.setCourses(courseList);
        boolean saved=trainerServices.addNewTrainer(trainer);
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
    public String getAllTrainers(Model model,@AuthenticationPrincipal MyUserDetails userDetails){
        if(userDetails==null)return "redirect:/login";
        List<Student>students=studentServices.getAllStudents();
        List<Branch>branches=branchServices.getAllBranches();
        List<Trainer>trainers=trainerServices.getAllTrainer();
        model.addAttribute("trainers",trainers);
       model.addAttribute("allstds",students);
        model.addAttribute("branches",branches);
        return "accountantpage";
    }
    @GetMapping("/accountant/trainer/assigncourse/{trID}")
    public String GetCoursesForTrainer(@PathVariable("trID")int id,@AuthenticationPrincipal MyUserDetails userDetails,RedirectAttributes rd,Model model){
       if(userDetails==null)return "redirect:/login";
        List<Course>courses=courseServices.getAllCourses();
        model.addAttribute("courses",courses);
        List<Student>students=studentServices.getAllStudents();
        List<Branch>branches=branchServices.getAllBranches();
        List<Trainer>trainers=trainerServices.getAllTrainer();
        model.addAttribute("trainers",trainers);
        model.addAttribute("allstds",students);
        model.addAttribute("branches",branches);
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
            if(trainer.getMaxCourses()<=courses.size()+old.size()){
                int dif=trainer1.getMaxCourses()-courses.size();
                trainer1.setMaxCourses(dif);
                if(dif==0)trainer1.setAvailable(false);
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
                    return "trainercourse";
                }
            }
            else {
                rd.addFlashAttribute("FaildAssignedCourses", "Assigned Courses Failed!!");
                return "trainercourse";
            }

        }
        else{
            rd.addFlashAttribute("FaildAssignedCourses","Assigned Courses Failed!!");
            return "trainercourse";
        }
    }
    @GetMapping("/accountant/trainer/delete/{tID}")
    public String DeleteTrainer(@PathVariable("tID")int id,@AuthenticationPrincipal MyUserDetails userDetails,RedirectAttributes rd){
       if(userDetails==null)return "redirect:/login";
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
       if(userDetails==null)return "redirect:/login";
        Trainer trainer=trainerServices.getTrainerById(id);
        if(trainer!=null){
            String roleName=trainer.getRole().getName();
            int roleId=trainer.getRole().getRoleId();
            List<Branch>branches=branchServices.getAllBranches();
            List<Course>courses=courseServices.getAllCourses();
            model.addAttribute("brns",branches);
            model.addAttribute("courses",courses);
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
        if(userDetails==null)return "redirect:/login";
        model.addAttribute("course",new Course());
        List<Branch>branches=branchServices.getAllBranches();
        model.addAttribute("branches",branches);
        return "addcourse";
    }
    @PostMapping("/accountant/course/addcourse")
    public String addCourse(@ModelAttribute("course")Course course,@RequestParam("branch")Set<Branch> branchsName,Model model,RedirectAttributes rd){
        System.out.println("HERE DOOOOG!!"+branchsName);
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
       if(userDetails==null)return "redirect:/login";
        Course course=courseServices.getCourseByName(courseName);
        List<Branch>branches=branchServices.getAllBranches();
        if(course!=null){
            model.addAttribute("course",course);
            model.addAttribute("branches",branches);
            return "addcourse";
        }else return "redirect:/accountant";
    }
    @GetMapping("/accountant/getallcourses")
    public String getCourses(Model model,@AuthenticationPrincipal MyUserDetails userDetails){
        if(userDetails==null)return "redirect:/login";
        List<Student>students=studentServices.getAllStudents();
        List<Branch>branches=branchServices.getAllBranches();
        List<Trainer>trainers=trainerServices.getAllTrainer();
        model.addAttribute("trainers",trainers);
        model.addAttribute("allstds",students);
        model.addAttribute("branches",branches);
        List<Course> courses=courseServices.getAllCourses();
        model.addAttribute("courses",courses);

        return "accountantpage";
    }
    @GetMapping("/accountant/course/delete/{cName}")
    public String deleteCourse(@PathVariable("cName")String courseName,@AuthenticationPrincipal MyUserDetails userDetails,Model model,RedirectAttributes rd){
       if(userDetails==null)return "redirect:/login";
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
