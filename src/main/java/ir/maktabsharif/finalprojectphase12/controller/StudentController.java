/*
package ir.maktabsharif.finalprojectphase12.controller;

import ir.maktabsharif.finalprojectphase12.dto.course.CourseDTO;
import ir.maktabsharif.finalprojectphase12.dto.test.TestDTO;
import ir.maktabsharif.finalprojectphase12.entity.StudentAnswer;
import ir.maktabsharif.finalprojectphase12.entity.StudentTest;
import ir.maktabsharif.finalprojectphase12.entity.question.TestQuestion;
import ir.maktabsharif.finalprojectphase12.exception.BadCredentialsException;
import ir.maktabsharif.finalprojectphase12.service.StudentService;
import ir.maktabsharif.finalprojectphase12.utility.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final UserContext userContext;

    @GetMapping("/dashboard")
    public String showStudentDashboard(Model model) {
        try {
            Long studentId = userContext.getLoggedInUserId();
            List<CourseDTO> courses = studentService.getRegisteredCourses(studentId);
            model.addAttribute("courses", courses);
        } catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "student/dashboard";
    }

    @GetMapping("/course/{courseId}/tests")
    public String showTestsForCourse(@PathVariable Long courseId, Model model) {
        try {
            Long studentId = userContext.getLoggedInUserId();
            List<TestDTO> tests = studentService.getTestsForCourse(courseId, studentId);
            model.addAttribute("tests", tests);
            model.addAttribute("courseId", courseId);
        } catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/student/dashboard";
        }
        return "student/tests";
    }

    @GetMapping("/test/{testId}/start")
    public String startTest(@PathVariable Long testId, Model model) {
        try {
            Long studentId = userContext.getLoggedInUserId();
            StudentTest studentTest = studentService.startOrResumeTest(testId, studentId);
            List<TestQuestion> questions = studentTest.getTest().getTestQuestions();
            Map<Long, String> questionTypes = new HashMap<>();
            for (TestQuestion question : questions) {
                String type = question.getQuestion().getQuestionType();
                questionTypes.put(question.getQuestion().getId(), type);
            }
            model.addAttribute("studentTest", studentTest);
            model.addAttribute("questions", questions);
            model.addAttribute("questionTypes", questionTypes);
            model.addAttribute("currentQuestionIndex", 0);
            model.addAttribute("remainingTime", studentTest.getRemainingTime());
            TestQuestion currentQuestion = questions.get(0);
            Optional<StudentAnswer> existingAnswer = studentTest.getAnswers().stream()
                    .filter(answer -> answer.getQuestion().getId().equals(currentQuestion.getQuestion().getId()))
                    .findFirst();
            model.addAttribute("existingAnswer", existingAnswer.isPresent() ? existingAnswer.get().getAnswer() : null);
        } catch (BadCredentialsException | IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/student/dashboard";
        }
        return "student/take-test";
    }

    @PostMapping("/test/{studentTestId}/answer")
    public String answerQuestion(
            @PathVariable Long studentTestId,
            @RequestParam Long questionId,
            @RequestParam(required = false) String response,
            @RequestParam Integer currentQuestionIndex,
            @RequestParam String action,
            Model model) {
        try {
            System.out.println("=== Handling Answer Submission ===");
            System.out.println("studentTestId: " + studentTestId);
            System.out.println("questionId: " + questionId);
            System.out.println("response: " + response);
            System.out.println("currentQuestionIndex: " + currentQuestionIndex);
            System.out.println("action: " + action);

            if (!"autosave".equalsIgnoreCase(action)) {
                studentService.saveAnswer(studentTestId, questionId, response != null ? response.trim() : "");
                System.out.println("Answer saved successfully for questionId: " + questionId);
            } else {
                studentService.saveAnswer(studentTestId, questionId, response != null ? response.trim() : "");
                return null; // Prevent page reload for autosave
            }

            StudentTest studentTest = studentService.getStudentTest(studentTestId);
            if (studentTest == null) {
                throw new BadCredentialsException("StudentTest not found for ID: " + studentTestId);
            }
            List<TestQuestion> questions = studentTest.getTest().getTestQuestions().stream()
                    .sorted(Comparator.comparing(TestQuestion::getId))
                    .collect(Collectors.toList());
            System.out.println("Number of questions loaded: " + questions.size());
            questions.forEach(q -> System.out.println("Loaded Question ID: " + q.getQuestion().getId() +
                    ", Type: " + q.getQuestion().getQuestionType() + ", TestQuestion ID: " + q.getId()));

            if (questions == null || questions.isEmpty()) {
                throw new BadCredentialsException("No questions available for this test");
            }

            int newIndex = currentQuestionIndex;
            if ("next".equalsIgnoreCase(action)) {
                newIndex = currentQuestionIndex + 1;
            } else if ("previous".equalsIgnoreCase(action)) {
                newIndex = currentQuestionIndex - 1;
            }
            System.out.println("Before capping - newIndex: " + newIndex + ", Questions size: " + questions.size());
            newIndex = Math.max(0, Math.min(newIndex, questions.size() - 1));
            System.out.println("After capping - Calculated newIndex: " + newIndex + ", Questions size: " + questions.size());

            if ("submit".equalsIgnoreCase(action)) {
                studentService.submitTest(studentTestId);
                System.out.println("Test submitted, redirecting to dashboard");
                return "redirect:/student/dashboard";
            }

            model.addAttribute("studentTest", studentTest);
            model.addAttribute("questions", questions);
            model.addAttribute("currentQuestionIndex", newIndex);
            model.addAttribute("remainingTime", studentTest.getRemainingTime());
            TestQuestion currentQuestion = questions.get(newIndex);
            Optional<StudentAnswer> existingAnswer = studentTest.getAnswers().stream()
                    .filter(answer -> answer.getQuestion().getId().equals(currentQuestion.getQuestion().getId()))
                    .findFirst();
            model.addAttribute("existingAnswer", existingAnswer.isPresent() ? existingAnswer.get().getAnswer() : null);

            System.out.println("Returning to take-test view with newIndex: " + newIndex);
            return "student/take-test";
        } catch (Exception e) {
            System.out.println("Exception caught in answerQuestion: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("errorMessage", "An error occurred while processing your request: " + e.getMessage());
            return "redirect:/student/dashboard";
        }
    }

    @PostMapping("/test/{studentTestId}/submit")
    public String submitTest(@PathVariable Long studentTestId, Model model) {
        try {
            studentService.submitTest(studentTestId);
            // Redirect to the course tests page after submission
            return "redirect:/student/course/" + studentTestId + "/tests";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/student/dashboard";
        }
    }

    @PostMapping("/test/{studentTestId}/timeout")
    @ResponseBody
    public Map<String, String> handleTimeout(@PathVariable Long studentTestId) {
        studentService.submitTestOnTimeout(studentTestId);
        return Map.of("status", "submitted");
    }


    @PostMapping("/test/{studentTestId}/time")
    @ResponseBody
    public void updateRemainingTime(@PathVariable Long studentTestId, @RequestParam Integer remainingTime) {
        studentService.updateRemainingTime(studentTestId, remainingTime);
    }

}*/

package ir.maktabsharif.finalprojectphase12.controller;

import ir.maktabsharif.finalprojectphase12.dto.course.CourseDTO;
import ir.maktabsharif.finalprojectphase12.dto.test.TestDTO;
import ir.maktabsharif.finalprojectphase12.entity.StudentAnswer;
import ir.maktabsharif.finalprojectphase12.entity.StudentTest;
import ir.maktabsharif.finalprojectphase12.entity.question.TestQuestion;
import ir.maktabsharif.finalprojectphase12.exception.BadCredentialsException;
import ir.maktabsharif.finalprojectphase12.service.StudentService;
import ir.maktabsharif.finalprojectphase12.utility.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {
    private final StudentService studentService;
    private final UserContext userContext;

    @GetMapping("/dashboard")
    public String showStudentDashboard(Model model) {
        try {
            Long studentId = userContext.getLoggedInUserId();
            List<CourseDTO> courses = studentService.getRegisteredCourses(studentId);
            model.addAttribute("courses", courses);
        } catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "student/dashboard";
    }

    @GetMapping("/course/{courseId}/tests")
    public String showTestsForCourse(@PathVariable Long courseId, Model model) {
        try {
            Long studentId = userContext.getLoggedInUserId();
            List<TestDTO> tests = studentService.getTestsForCourse(courseId, studentId);
            model.addAttribute("tests", tests);
            model.addAttribute("courseId", courseId);
        } catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/student/dashboard";
        }
        return "student/tests";
    }

    @GetMapping("/test/{testId}/start")
    public String startTest(@PathVariable Long testId, Model model) {
        try {
            Long studentId = userContext.getLoggedInUserId();
            StudentTest studentTest = studentService.startOrResumeTest(testId, studentId);
            List<TestQuestion> questions = studentTest.getTest().getTestQuestions();
          /*  Map<Long, String> questionTypes = new HashMap<>();
            for (TestQuestion tq : questions) {
                questionTypes.put(tq.getQuestion().getId(), tq.getQuestion().getQuestionType());
            }*/
            //map where each question id is relatd with its question type
            Map<Long, String> questionTypes = questions.stream()
                    .collect(Collectors.toMap(
                            tq -> tq.getQuestion().getId(),
                            tq -> tq.getQuestion().getQuestionType()
                    ));
            model.addAttribute("studentTest", studentTest);
            model.addAttribute("questions", questions);
            model.addAttribute("questionTypes", questionTypes);
            model.addAttribute("currentQuestionIndex", 0);
            model.addAttribute("remainingTime", studentTest.getRemainingTime());
            TestQuestion currentQuestion = questions.get(0);
            Optional<StudentAnswer> existingAnswer = studentTest.getAnswers().stream()
                    .filter(answer -> answer.getQuestion().getId().equals(currentQuestion.getQuestion().getId()))
                    //return first match answer
                    .findFirst();
            model.addAttribute("existingAnswer", existingAnswer.isPresent() ? existingAnswer.get().getAnswer() : null);
        } catch (BadCredentialsException | IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/student/dashboard";
        }
        return "student/take-test";
    }

    @PostMapping("/test/{studentTestId}/answer")
    public String answerQuestion(
            @PathVariable Long studentTestId,
            @RequestParam Long questionId,
            @RequestParam(required = false) String response,
            @RequestParam Integer currentQuestionIndex,
            @RequestParam String action,
            Model model) {
        try {
            if (!"autosave".equalsIgnoreCase(action)) {
                studentService.saveAnswer(studentTestId, questionId, response != null ? response.trim() : "");
            } else {
                studentService.saveAnswer(studentTestId, questionId, response != null ? response.trim() : "");
                return null; // Prevent page reload for autosave
            }

            StudentTest studentTest = studentService.getStudentTest(studentTestId);
            List<TestQuestion> questions = studentTest.getTest().getTestQuestions().stream()
                    //Sorts the questions by their id in ascending order.
                    .sorted(Comparator.comparing(TestQuestion::getId))
                    .collect(Collectors.toList());
            questions.forEach(q -> System.out.println("Loaded Question ID: " + q.getQuestion().getId() +
                    ", Type: " + q.getQuestion().getQuestionType() + ", TestQuestion ID: " + q.getId()));

            int newIndex = currentQuestionIndex;
            if ("next".equalsIgnoreCase(action)) {
                newIndex = currentQuestionIndex + 1;
            } else if ("previous".equalsIgnoreCase(action)) {
                newIndex = currentQuestionIndex - 1;
            }
            newIndex = Math.max(0, Math.min(newIndex, questions.size() - 1));

            if ("submit".equalsIgnoreCase(action)) {
                studentService.submitTest(studentTestId);
                return "redirect:/student/dashboard";
            }

            model.addAttribute("studentTest", studentTest);
            model.addAttribute("questions", questions);
            model.addAttribute("currentQuestionIndex", newIndex);
            model.addAttribute("remainingTime", studentTest.getRemainingTime());
            TestQuestion currentQuestion = questions.get(newIndex);
            Optional<StudentAnswer> existingAnswer = studentTest.getAnswers().stream()
                    .filter(answer -> answer.getQuestion().getId().equals(currentQuestion.getQuestion().getId()))
                    .findFirst();
            model.addAttribute("existingAnswer", existingAnswer.isPresent() ? existingAnswer.get().getAnswer() : null);

            return "student/take-test";
        } catch (BadCredentialsException e) {
            model.addAttribute("errorMessage" + e.getMessage());
            return "redirect:/student/dashboard";
        }
    }

    @PostMapping("/test/{studentTestId}/submit")
    public String submitTest(@PathVariable Long studentTestId, Model model) {
        try {
            studentService.submitTest(studentTestId);
            return "redirect:/student/dashboard";
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/student/dashboard";
        }
    }

    @PostMapping("/test/{studentTestId}/timeout")
    @ResponseBody
    public Map<String, String> handleTimeout(@PathVariable Long studentTestId) {
        studentService.submitTestOnTimeout(studentTestId);
        return Map.of("status", "submitted");
    }

    @PostMapping("/test/{studentTestId}/time")
    @ResponseBody
    public void updateRemainingTime(@PathVariable Long studentTestId, @RequestParam Integer remainingTime) {
        studentService.updateRemainingTime(studentTestId, remainingTime);
    }
}
