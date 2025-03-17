package ir.maktabsharif.finalprojectphase12.controller;

import ir.maktabsharif.finalprojectphase12.dto.course.CourseDTO;
import ir.maktabsharif.finalprojectphase12.dto.test.TestDTO;
import ir.maktabsharif.finalprojectphase12.entity.Test;
import ir.maktabsharif.finalprojectphase12.exception.BadCredentialsException;
import ir.maktabsharif.finalprojectphase12.service.CourseService;
import ir.maktabsharif.finalprojectphase12.service.TestService;
import ir.maktabsharif.finalprojectphase12.utility.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TestController {
    private final TestService testService;
    private final CourseService courseService;
    private final UserContext userContext;


    @GetMapping("/course/{courseId}")
    public String getTestsByCourse(@PathVariable Long courseId, Model model) {
        Long teacherId = userContext.getLoggedInUserId();
        try {
            List<Test> tests = testService.getTestByCourseIdAndTeacherId(courseId,teacherId);
            model.addAttribute("tests", tests);
            model.addAttribute("courseId", courseId);
        } catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/teacher/dashboard";
        }
        return "test/test-list";
    }

    @GetMapping("/dashboard")
    public String showTeacherDashboard(Model model) {
        try {
            Long teacherId = userContext.getLoggedInUserId();
            List<CourseDTO> courses = courseService.getCoursesByInstructorId(teacherId);
            model.addAttribute("courses", courses);
        } catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "teacher/dashboard";
    }


    @GetMapping("/course/{courseId}/new")
    public String showCreateTestForm(@PathVariable Long courseId, Model model) {
        TestDTO testDTO = new TestDTO();
        testDTO.setCourseId(courseId);
        model.addAttribute("test", testDTO);
        return "test/test-form";
    }

    @PostMapping("/create")
    public String createTest(@ModelAttribute TestDTO testDTO, Model model) {
        try {
            Long teacherId = userContext.getLoggedInUserId();
            testDTO.setTeacherId(teacherId);
            testService.createTest(testDTO);
        } catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "test/test-form"; // Return to form with error message
        }
        return "redirect:/teacher/course/" + testDTO.getCourseId();
    }

    @PostMapping("/update/{testId}")
    public String updateTest(@PathVariable Long testId, @ModelAttribute TestDTO testDTO, Model model) {
        Long teacherId = userContext.getLoggedInUserId();
        testDTO.setTeacherId(teacherId);
        try {
            testService.updateTest(testId, testDTO);
        } catch (BadCredentialsException | IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "test/test-form";
        }
        return "redirect:/teacher/course/" + testDTO.getCourseId();
    }

/*    @PostMapping("/create")
    public String createTest(@Valid @ModelAttribute TestDTO testDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // If there are validation errors, return to the form with error messages
            model.addAttribute("errorMessage", "Validation failed. Please check the form.");
            return "test/test-form";
        }
        try {
            Long teacherId = userContext.getLoggedInUserId();
            testDTO.setTeacherId(teacherId);
            testService.createTest(testDTO);
        } catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "test/test-form"; // Return to form with error message
        }
        return "redirect:/teacher/course/" + testDTO.getCourseId();
    }

    @PostMapping("/update/{testId}")
    public String updateTest(@PathVariable Long testId, @Valid @ModelAttribute TestDTO testDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // If there are validation errors, return to the form with error messages
            model.addAttribute("errorMessage", "Validation failed. Please check the form.");
            return "test/test-form";
        }
        Long teacherId = userContext.getLoggedInUserId();
        testDTO.setTeacherId(teacherId);
        try {
            testService.updateTest(testId, testDTO);
        } catch (BadCredentialsException | IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "test/test-form";
        }
        return "redirect:/teacher/course/" + testDTO.getCourseId();
    }*/

    @GetMapping("/edit/{testId}")
    public String showEditTestForm(@PathVariable Long testId, Model model) {
        try {
            TestDTO testDTO = testService.getTestsById(testId);
            model.addAttribute("testId", testId);
            // Ensure courseId is never null
            if (testDTO.getCourseId() == null) {
                throw new BadCredentialsException("Course ID is missing for this test.");
            }

            model.addAttribute("test", testDTO);
        } catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/teacher/dashboard";
        }
        return "test/test-form";
    }


    @GetMapping("/delete/{testId}")
    public String deleteTest(@PathVariable Long testId, @RequestParam Long courseId, Model model) {
        try {
            testService.deleteTest(testId);
        } catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/teacher/course/" + courseId;
    }

    @GetMapping("/course/{courseId}/tests")
    public String viewTests(@PathVariable Long courseId, Model model) {
        List<TestDTO> tests = testService.getTestsByCourseId(courseId);
        model.addAttribute("tests", tests);
        return "student/tests"; // Thymeleaf template for displaying tests
    }
}