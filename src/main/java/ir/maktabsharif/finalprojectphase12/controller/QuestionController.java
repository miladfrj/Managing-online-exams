package ir.maktabsharif.finalprojectphase12.controller;

import ir.maktabsharif.finalprojectphase12.dto.question.QuestionScoreDto;
import ir.maktabsharif.finalprojectphase12.dto.question.TestQuestionDto;
import ir.maktabsharif.finalprojectphase12.dto.test.TestDTO;
import ir.maktabsharif.finalprojectphase12.entity.Test;
import ir.maktabsharif.finalprojectphase12.entity.question.DescriptiveQuestion;
import ir.maktabsharif.finalprojectphase12.entity.question.MultipleChoiceQuestion;
import ir.maktabsharif.finalprojectphase12.entity.question.Question;
import ir.maktabsharif.finalprojectphase12.entity.question.TestQuestion;
import ir.maktabsharif.finalprojectphase12.exception.BadCredentialsException;
import ir.maktabsharif.finalprojectphase12.service.TestService;
import ir.maktabsharif.finalprojectphase12.service.QuestionService;
import ir.maktabsharif.finalprojectphase12.utility.UserContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller
@RequestMapping("/question")
@RequiredArgsConstructor
public class QuestionController {
    private final QuestionService questionService;
    private final TestService testService;
    private final UserContext userContext;

    @GetMapping("/{examId}")
    public String questionList(@PathVariable Long examId, Model model) {
        Test test = testService.getTestById(examId);
       /* User currentUser = userContext.getLoggedInUser();
        if (!test.getTeacher().getId().equals(currentUser.getId())) {
            throw new SecurityException("You are not authorized to view this exam's questions");
        }
        Long teacherId = currentUser.getId();
        Long courseId = test.getCourse().getId();*/
        model.addAttribute("questionList", test.getTestQuestions());
        model.addAttribute("examId", examId);
        return "/questions/list-question";
    }


    @GetMapping("/addD/{examId}")
    public String showCreateDescriptiveQuestionForm(@PathVariable Long examId, Model model) {
        model.addAttribute("question", new DescriptiveQuestion());
        model.addAttribute("examId", examId);
        return "questions/add-questions";
    }

    @PostMapping("/addD/{examId}")
    public String createDescriptiveQuestion(@PathVariable Long examId,
                                            @ModelAttribute("question") DescriptiveQuestion question) {
        Long teacherId = userContext.getLoggedInUserId();
        questionService.createDescriptiveQuestion(examId, question, teacherId);
        return "redirect:/question/" + examId;
    }

    @GetMapping("/addM/{examId}")
    public String showCreateMultipleChoiceQuestionForm(@PathVariable Long examId, Model model) {
        model.addAttribute("question", new MultipleChoiceQuestion());
        model.addAttribute("testId", examId);
        return "questions/add-multi-question";
    }

    @PostMapping("/addM/{examId}")
    public String createMultipleChoiceQuestion(@PathVariable Long examId,
                                               @ModelAttribute("question") MultipleChoiceQuestion question,
                                               @RequestParam List<String> options,
                                               @RequestParam int correctAnswer) {
        Long teacherId = userContext.getLoggedInUserId();
        questionService.createMultipleChoiceQuestion(examId, question, options, correctAnswer, teacherId);
        return "redirect:/question/" + examId;
    }

    @GetMapping("/addBank/{examId}")
    public String showQuestionBank(@PathVariable Long examId, Model model) {
        Test test = testService.getTestById(examId);
        Long teacherId = userContext.getLoggedInUserId();
        Long courseId = test.getCourse().getId();
        List<Question> questions = questionService.getQuestionsByTeacherIdAndCourseId(teacherId , courseId);
        model.addAttribute("questionList", questions);
        model.addAttribute("examId", examId);
        return "/questions/add-question-bank";
    }

/*
    @PostMapping("/addBank")
    public String addQuestionFromBank(@RequestParam Long examId,
                                      @RequestParam Long questionId) {
        questionService.addQuestionFromBank(examId, questionId);
        return "redirect:/question/" + examId;
    }
*/

    @PostMapping("/addBank")
    public String addQuestionFromBank(@RequestParam Long testId,
                                      @RequestParam Long questionId) {
        questionService.addQuestionFromBank(testId, questionId);
        return "redirect:/question/" + testId;
    }


    @PostMapping("/save-scores/{examId}")
    public String saveQuestionScores(@PathVariable Long examId,
                                     @ModelAttribute QuestionScoreDto questionScoreDto) {
        List<Long> testQuestionIds = questionScoreDto.getTestQuestionIds();
        List<Double> scores = questionScoreDto.getScores();

        // Debugging: Print the received data
        System.out.println("Received testQuestionIds: " + testQuestionIds);
        System.out.println("Received scores: " + scores);

        for (int i = 0; i < testQuestionIds.size(); i++) {
            Long testQuestionId = testQuestionIds.get(i);
            Double score = scores.get(i);

            // Debugging: Print the IDs and scores being processed
            System.out.println("Processing TestQuestion ID: " + testQuestionId + " with score: " + score);

            // Update the score for the specific TestQuestion
            questionService.updateQuestionScore(testQuestionId, score );
        }

        return "redirect:/question/" + examId;
    }



    /*@GetMapping("/edit")
    public String showEditQuestionForm(@RequestParam Long examId,
                                       @RequestParam Long questionId,
                                       @RequestParam String typeQuestion,
                                       Model model) {
        Question question = questionService.getQuestionById(questionId);

        if ("MULTIPLE_CHOICE".equals(typeQuestion) && question instanceof MultipleChoiceQuestion) {
            MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) question;
            model.addAttribute("question", mcq);
            model.addAttribute("options", mcq.getOptions());
        } else if ("DESCRIPTION".equals(typeQuestion) && question instanceof DescriptiveQuestion) {
            DescriptiveQuestion dq = (DescriptiveQuestion) question;
            model.addAttribute("question", dq);
        } else {
            throw new IllegalArgumentException("Invalid question type: " + typeQuestion);
        }

        model.addAttribute("typeQuestion", typeQuestion);
        model.addAttribute("examId", examId);
        return "/questions/edit-question";
    }*/
    @GetMapping("/edit")
    public String showEditQuestionForm(@RequestParam Long examId,
                                       @RequestParam Long questionId,
                                       @RequestParam String typeQuestion,
                                       Model model) {
        Question question = questionService.getQuestionById(questionId);
        if ("MULTIPLE_CHOICE".equals(typeQuestion) && question instanceof MultipleChoiceQuestion) {
            MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) question;
            model.addAttribute("question", mcq);
            model.addAttribute("options", mcq.getOptions());
        } else if ("DESCRIPTION".equals(typeQuestion) && question instanceof DescriptiveQuestion) {
            DescriptiveQuestion dq = (DescriptiveQuestion) question;
            model.addAttribute("question", dq);
        } else {
            throw new BadCredentialsException("Invalid question type: " + typeQuestion);
        }
        model.addAttribute("typeQuestion", typeQuestion);
        model.addAttribute("examId", examId);
        return "/questions/edit-question";
    }




   /* @PostMapping("/editDescriptive/{examId}")
    public String updateDescriptiveQuestion(@PathVariable Long examId,
                                            @ModelAttribute TestQuestionDto testQuestionDto) {
        questionService.updateDescriptiveQuestion(examId, testQuestionDto);
        return "redirect:/question/" + examId;
    }*/
   @PostMapping("/editDescriptive/{examId}")
   public String updateDescriptiveQuestion(@PathVariable Long examId, @ModelAttribute TestQuestionDto testQuestionDto, Model model) {
       try {
           questionService.updateDescriptiveQuestion(examId, testQuestionDto);
           return "redirect:/question/" + examId;
       } catch (Exception e) {
           model.addAttribute("errorMessage", "Failed to update question: " + e.getMessage());
           return "/questions/edit-question";
       }
   }


    @PostMapping("/editMultiple-choice/{examId}")
    public String updateMultipleChoiceQuestion(@PathVariable Long examId, @ModelAttribute MultipleChoiceQuestion multiQuestionDto, Model model) {
        try {
            System.out.println("Updating multiple-choice question: " + multiQuestionDto.getId());
            questionService.updateMultipleChoiceQuestion(examId, multiQuestionDto);
            return "redirect:/question/" + examId;
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to update question: " + e.getMessage());
            model.addAttribute("question", multiQuestionDto);
            model.addAttribute("options", multiQuestionDto.getOptions());
            model.addAttribute("typeQuestion", "MULTIPLE_CHOICE");
            model.addAttribute("examId", examId);
            return "/questions/edit-question";
        }
    }


    @GetMapping("/deleteList")
    public String deleteList(@RequestParam Long examId, @RequestParam Long questionId, TestDTO testDTO) {
        Test test = testService.getTestById(examId);
        TestQuestion question = questionService.findTestQuestionById(questionId);
        test.getTestQuestions().remove(question);
        testService.updateTest(examId, testDTO);
        return "redirect:/question/" + examId;
    }


    @Transactional
    @PostMapping("/delete")
    public String delete(@RequestParam Long examId, @RequestParam Long questionId, Model model) {
        try {
            Test test = testService.getTestById(examId);
            Long teacherId = userContext.getLoggedInUserId();
            if (!test.getTeacher().getId().equals(teacherId)) {
                throw new SecurityException("Unauthorized to delete question");
            }
            System.out.println("Deleting question " + questionId + " from exam " + examId);
            questionService.deleteQuestionById(questionId, examId);
            return "redirect:/question/" + examId;
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to delete question: " + e.getMessage());
            model.addAttribute("questionList", testService.getTestById(examId).getTestQuestions());
            return "/questions/list-question";
        }
    }

}