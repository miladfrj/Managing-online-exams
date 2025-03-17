package ir.maktabsharif.finalprojectphase12.controller;

import ir.maktabsharif.finalprojectphase12.dto.AnswerReviewDTO;
import ir.maktabsharif.finalprojectphase12.entity.StudentTest;
import ir.maktabsharif.finalprojectphase12.dto.StudentTestOverview;
import ir.maktabsharif.finalprojectphase12.exception.BadCredentialsException;
import ir.maktabsharif.finalprojectphase12.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/teacher")
@RequiredArgsConstructor
public class TeacherController {
    private final StudentService studentService;


    @GetMapping("/test/{testId}/students")
    public String getTestStudents(@PathVariable Long testId, Model model) {
        List<StudentTestOverview> overviews = studentService.getStudentTestOverviewByTestId(testId);
        model.addAttribute("overviews", overviews);
        model.addAttribute("testId", testId);
        return "teacher/test-students";
    }


    @GetMapping("/review/{studentTestId}")
    public String reviewTest(@PathVariable Long studentTestId, Model model) {
        try {
            StudentTest studentTest = studentService.getStudentTest(studentTestId);
            List<AnswerReviewDTO> answerReviews = studentService.getAnswerReviewsForStudentTest(studentTestId);

            model.addAttribute("studentTest", studentTest);
            model.addAttribute("answerReviews", answerReviews);
            model.addAttribute("errorMessage", "");

            return "teacher/review";
        } catch (BadCredentialsException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/teacher/dashboard";
        }
    }

    @PostMapping("/review/{studentAnswerId}/score")
    public String updateScore(
            @PathVariable Long studentAnswerId,
            @RequestParam Double score,
            @RequestParam String feedback,
            Model model) {
        try {
            studentService.updateAnswerScore(studentAnswerId, score, feedback);
            return "redirect:/teacher/review/" + studentService.getStudentTestByAnswerId(studentAnswerId).getId();
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/teacher/review/" + studentService.getStudentTestByAnswerId(studentAnswerId).getId();
        }
    }





/*    @GetMapping("/review/{studentTestId}")
    public String reviewTest(@PathVariable Long studentTestId, Model model) {
        StudentTest studentTest = studentService.getStudentTest(studentTestId);
        List<StudentAnswer> answers = studentTest.getAnswers();

        List<AnswerReviewDTO> answerReviews = new ArrayList<>();
        for (int i = 0; i < answers.size(); i++) {
            StudentAnswer answer = answers.get(i);
            AnswerReviewDTO review = new AnswerReviewDTO();
            review.setAnswerId(answer.getId());
            review.setQuestionIndex(i + 1);

            // Fetch question text
            TestQuestion testQuestion = studentTest.getTest().getTestQuestions().get(i);
            review.setQuestionText(testQuestion.getQuestion().getQuestionText());

            // Set student's answer
            review.setStudentAnswer(answer.getAnswer() != null ? answer.getAnswer() : "Not answered");

            // Set score and max score
            review.setScore(answer.getScore() != null ? answer.getScore() : 0.0);
            review.setMaxScore(testQuestion.getScore()); // Max score from TestQuestion

            // Set feedback
            review.setFeedback(answer.getFeedback() != null ? answer.getFeedback() : "");

            // Set question type
            review.setDescriptiveQuestion("DESCRIPTION".equals(testQuestion.getQuestion().getQuestionType()));

            answerReviews.add(review);
        }

        model.addAttribute("studentTest", studentTest);
        model.addAttribute("answerReviews", answerReviews);
        model.addAttribute("errorMessage", "");
        return "teacher/review";
    }*/




   /* @PostMapping("/review/{studentAnswerId}/score")
    public String updateScore(
            @PathVariable Long studentAnswerId,
            @RequestParam Double score,
            @RequestParam String feedback,
            Model model) {
        try {
            // Fetch the student answer
            StudentAnswer studentAnswer = studentAnswerRepository.findById(studentAnswerId)
                    .orElseThrow(() -> new BadCredentialsException("StudentAnswer not found"));

            // Fetch the corresponding TestQuestion
            TestQuestion testQuestion = studentAnswer.getStudentTest().getTest().getTestQuestions().stream()
                    .filter(tq -> tq.getQuestion().getId().equals(studentAnswer.getQuestion().getId()))
                    .findFirst()
                    .orElseThrow(() -> new BadCredentialsException("TestQuestion not found"));



            // Update the score and feedback
            studentAnswer.setScore(score);
            studentAnswer.setFeedback(feedback);
            studentAnswerRepository.save(studentAnswer);

            // Recalculate the total score for the test
            StudentTest studentTest = studentAnswer.getStudentTest();
            double totalScore = studentTest.getAnswers().stream()
                    .mapToDouble(answer -> answer.getScore() != null ? answer.getScore() : 0.0)
                    .sum();
            studentTest.setTotalScore(totalScore);
            studentTestRepository.save(studentTest);

            return "redirect:/teacher/review/" + studentTest.getId();
        } catch (Exception e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "redirect:/teacher/review/" + studentAnswerRepository.findById(studentAnswerId)
                    .orElseThrow(() -> new BadCredentialsException("StudentAnswer not found"))
                    .getStudentTest().getId();
        }
    }*/
}
