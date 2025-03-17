package ir.maktabsharif.finalprojectphase12.controller;

import ir.maktabsharif.finalprojectphase12.dto.course.CourseDTO;
import ir.maktabsharif.finalprojectphase12.entity.User;
import ir.maktabsharif.finalprojectphase12.exception.BadCredentialsException;
import ir.maktabsharif.finalprojectphase12.service.CourseService;
import ir.maktabsharif.finalprojectphase12.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/admin/courses")
public class CourseController {

    private final CourseService courseService;
    private final UserService userService;

    @Autowired
    public CourseController(CourseService courseService, UserService userService) {
        this.courseService = courseService;
        this.userService = userService;
    }

    @GetMapping
    public String viewAllCourses(Model model) {
        try {
            List<CourseDTO> courses = courseService.getAllCourses();
            model.addAttribute("courses", courses);
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An error occurred while fetching courses. Please try again.");
        }
        return "course/course-list";
    }


    @GetMapping("/new")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new CourseDTO());
        model.addAttribute("teachers", userService.getAllTeachers());
        return "course/course-form";
    }

    @PostMapping("/add")
    public String addCourse(@ModelAttribute CourseDTO courseDTO , Model model) {
        try {
            courseService.addCourse(courseDTO);
        } catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("teachers", userService.getAllTeachers());
            return "course/course-form";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An error occurred while adding the course. Please try again.");
            model.addAttribute("teachers", userService.getAllTeachers());
            return "course/course-form";
        }
        return "redirect:/admin/courses";
    }

    @GetMapping("/edit/{id}")
    public String showEditCourseForm(@PathVariable Long id, Model model) {
        try {
            CourseDTO courseDTO = courseService.getCourseById(id);
            model.addAttribute("course", courseDTO);
            model.addAttribute("teachers", userService.getAllTeachers());
        } catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/courses";
        }
        return "course/course-form";
    }

    @PostMapping("/edit")
    public String updateCourse(@ModelAttribute CourseDTO courseDTO , Model model) {
        try {
            courseService.updateCourse(courseDTO);
        } catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("teachers", userService.getAllTeachers());
            return "course/course-form";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An error occurred while updating the course. Please try again.");
            model.addAttribute("teachers", userService.getAllTeachers());
            return "course/course-form";
        }
        return "redirect:/admin/courses";
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable Long id , Model model) {
        try {
            courseService.deleteCourse(id);
        } catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", e.getMessage());
        } catch (Exception e) {
            model.addAttribute("errorMessage", "An error occurred while deleting the course. Please try again.");
        }
        return "redirect:/admin/courses";
    }

    @GetMapping("/{id}")
    public String viewCourseDetails(@PathVariable Long id, Model model) {
        try {
            CourseDTO courseDTO = courseService.getCourseById(id);
            List<User> users = courseService.getProfessorsAndStudentsInCourse(id);
            List<User> allUsers = userService.getAllUsers();
            model.addAttribute("course", courseDTO);
            model.addAttribute("users", users);
            model.addAttribute("allUsers", allUsers);
            model.addAttribute("courseId", id);
        } catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/courses";
        }
        return "course/course-details";
    }

    @PostMapping("/{courseId}/addProfessor")
    public String addProfessorToCourse(@PathVariable Long courseId, @RequestParam Long professorId , Model model) {
        try {
            courseService.addProfessorToCourse(courseId, professorId);
        } catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/courses/" + courseId;
    }

    @PostMapping("/{courseId}/addStudent")
    public String addStudentToCourse(@PathVariable Long courseId, @RequestParam Long studentId , Model model) {
        try {
            courseService.addStudentToCourse(courseId, studentId);
        } catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/courses/" + courseId;
    }

    @PostMapping("/{courseId}/removeProfessor")
    public String removeProfessorFromCourse(@PathVariable Long courseId, @RequestParam Long professorId , Model model) {
        try {
            courseService.removeProfessorFromCourse(courseId, professorId);
        } catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/courses/" + courseId;
    }

    @PostMapping("/{courseId}/removeStudent")
    public String removeStudentFromCourse(@PathVariable Long courseId, @RequestParam Long studentId , Model model) {
        try {
            courseService.removeStudentFromCourse(courseId, studentId);
        } catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/courses/" + courseId;
    }
    }





