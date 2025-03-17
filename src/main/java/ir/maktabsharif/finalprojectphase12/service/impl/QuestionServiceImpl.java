package ir.maktabsharif.finalprojectphase12.service.impl;

import ir.maktabsharif.finalprojectphase12.dto.question.TestQuestionDto;
import ir.maktabsharif.finalprojectphase12.entity.StudentAnswer;
import ir.maktabsharif.finalprojectphase12.entity.Test;
import ir.maktabsharif.finalprojectphase12.entity.User;
import ir.maktabsharif.finalprojectphase12.entity.question.*;
import ir.maktabsharif.finalprojectphase12.exception.BadCredentialsException;
import ir.maktabsharif.finalprojectphase12.repository.StudentAnswerRepository;
import ir.maktabsharif.finalprojectphase12.repository.TestRepository;
import ir.maktabsharif.finalprojectphase12.repository.UserRepository;
import ir.maktabsharif.finalprojectphase12.repository.question.QuestionRepository;
import ir.maktabsharif.finalprojectphase12.repository.question.TestQuestionRepository;
import ir.maktabsharif.finalprojectphase12.service.QuestionService;
import ir.maktabsharif.finalprojectphase12.service.TestService;
import ir.maktabsharif.finalprojectphase12.utility.UserContext;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
    private final QuestionRepository questionRepository;
    private final TestRepository testRepository;
    private final TestQuestionRepository testQuestionRepository;
    private final UserContext userContext;
    private final UserRepository userRepository;
    private final TestService testService;
    private final StudentAnswerRepository studentAnswerRepository;
    private EntityManager entityManager;

    @Transactional
    @Override
    public void createDescriptiveQuestion(Long testId, DescriptiveQuestion descriptiveQuestion, Long teacherId) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new BadCredentialsException("Exam not found"));
        User teacher = userRepository.findById(teacherId).get();

        DescriptiveQuestion question = DescriptiveQuestion.builder()
                .title(descriptiveQuestion.getTitle())
                .questionText(descriptiveQuestion.getQuestionText())
                .teacher(teacher)
                .course(test.getCourse())
                .build();

        questionRepository.save(question);
        linkQuestionToTest(question, test , 0.0);
    }

    @Transactional
    @Override
    public void createMultipleChoiceQuestion(Long testId, MultipleChoiceQuestion multipleChoiceQuestion, List<String> options,
                                             int correctAnswer, Long teacherId) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new BadCredentialsException("Exam not found"));
        User teacher = userRepository.findById(teacherId).get();

        MultipleChoiceQuestion question = MultipleChoiceQuestion.builder()
                .title(multipleChoiceQuestion.getTitle())
                .questionText(multipleChoiceQuestion.getQuestionText())
                .teacher(teacher)
                .course(test.getCourse())
                .options(options)
                .correctAnswer(multipleChoiceQuestion.getCorrectAnswer())
                .build();

        questionRepository.save(question);
        linkQuestionToTest(question, test , 0.0);
    }

/*    @Transactional
    @Override
    public void addQuestionFromBank(Long testId, Long questionId) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new BadCredentialsException("Exam not found"));
        if (test.getTestQuestions().stream().anyMatch(tq -> tq.getQuestion().getId().equals(questionId))) {
            throw new IllegalStateException("Question already in test");
        }
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new BadCredentialsException("Question not found"));
        TestQuestion testQuestion = new TestQuestion();
        testQuestion.setQuestion(question);
        testQuestion.setTest(test);
        test.getTestQuestions().add(testQuestion);
        testRepository.save(test);
    }*/
@Transactional
@Override
public void addQuestionFromBank(Long testId, Long questionId) {
    Test test = testRepository.findById(testId)
            .orElseThrow(() -> new BadCredentialsException("Test not found"));

    // Check for duplicate questions
    if (test.getTestQuestions().stream().anyMatch(tq -> tq.getQuestion().getId().equals(questionId))) {
        throw new IllegalStateException("Question already in test");
    }

    // Reattach the question to the current session
    Question question = entityManager.find(Question.class, questionId);
    if (question == null) {
        throw new BadCredentialsException("Question not found");
    }

    // Validate the question
    if (question.getTitle() == null || question.getTitle().trim().isEmpty() ||
            question.getQuestionText() == null || question.getQuestionText().trim().isEmpty()) {
        throw new IllegalStateException("Question title and text must not be empty");
    }

    // Validate MultipleChoiceQuestion if applicable
    if (question instanceof MultipleChoiceQuestion) {
        MultipleChoiceQuestion mcQuestion = (MultipleChoiceQuestion) question;
        if (mcQuestion.getOptions() == null || mcQuestion.getOptions().isEmpty()) {
            throw new IllegalStateException("Multiple-choice question must have options");
        }
        if (mcQuestion.getCorrectAnswer() == null || !mcQuestion.getOptions().contains(mcQuestion.getCorrectAnswer())) {
            throw new IllegalStateException("Invalid correct answer for multiple-choice question");
        }
    }

    // Create and configure TestQuestion
    TestQuestion testQuestion = new TestQuestion();
    testQuestion.setQuestion(question);
    testQuestion.setTest(test);
    testQuestion.setScore(0.0);

    // Add to the test
    test.getTestQuestions().add(testQuestion);

    // Save the test
    testRepository.save(test);
}


    @Transactional
    @Override
    public void updateQuestionScore(Long testQuestionId, Double score) {
        // Fetch the TestQuestion entity
        TestQuestion testQuestion = testQuestionRepository.findById(testQuestionId)
                .orElseThrow(() -> new RuntimeException("TestQuestion not found with ID: " + testQuestionId));

        // Debugging: Print the score being set
        System.out.println("Setting score for TestQuestion ID " + testQuestionId + " to: " + score);

        // Update the score for this specific TestQuestion
        testQuestion.setScore(score);
        testQuestionRepository.save(testQuestion);
    }



/*    @Transactional
    @Override
    public void updateDescriptiveQuestion(Long examId, TestQuestionDto testQuestionDto) {
        DescriptiveQuestion question = (DescriptiveQuestion) questionRepository.findById(testQuestionDto.getQuestionId())
                .orElseThrow(() -> new BadCredentialsException("Question not found"));
        question.setTitle(testQuestionDto.getQuestionTitle());
        question.setQuestionText(testQuestionDto.getQuestionText());
        questionRepository.save(question);
    }*/
@Transactional
@Override
public void updateDescriptiveQuestion(Long examId, TestQuestionDto testQuestionDto) {
    Question question = questionRepository.findById(testQuestionDto.getQuestionId())
            .orElseThrow(() -> new IllegalArgumentException("Question not found"));
    if (!(question instanceof DescriptiveQuestion)) {
        throw new IllegalArgumentException("Question is not a DescriptiveQuestion");
    }
    DescriptiveQuestion descriptiveQuestion = (DescriptiveQuestion) question;
    descriptiveQuestion.setTitle(testQuestionDto.getQuestionTitle());
    descriptiveQuestion.setQuestionText(testQuestionDto.getQuestionText());
    questionRepository.save(descriptiveQuestion);
}

    @Transactional
    @Override
    public void updateMultipleChoiceQuestion(Long examId, MultipleChoiceQuestion multipleChoiceQuestion) {
        MultipleChoiceQuestion existingQuestion = (MultipleChoiceQuestion) questionRepository.findById(multipleChoiceQuestion.getId())
                .orElseThrow(() -> new BadCredentialsException("Question not found"));
        if (multipleChoiceQuestion.getTitle() != null) {
            existingQuestion.setTitle(multipleChoiceQuestion.getTitle());
        }
        if (multipleChoiceQuestion.getQuestionText() != null) {
            existingQuestion.setQuestionText(multipleChoiceQuestion.getQuestionText());
        }
        if (multipleChoiceQuestion.getOptions() != null && !multipleChoiceQuestion.getOptions().isEmpty()) {
            existingQuestion.setOptions(multipleChoiceQuestion.getOptions());
        }
        if (multipleChoiceQuestion.getCorrectAnswer() != null) {
            existingQuestion.setCorrectAnswer(multipleChoiceQuestion.getCorrectAnswer());
        }
        questionRepository.save(existingQuestion);
    }

    @Transactional
    @Override
    public void deleteQuestionById(Long questionId, Long examId) {
        Test exam = testService.getTestById(examId);
        List<StudentAnswer> studentAnswers = studentAnswerRepository.findByQuestionId(questionId);
        if (!studentAnswers.isEmpty()) {
            throw new IllegalStateException("Cannot delete question: It has been answered in a submitted test.");
        }
        boolean removed = exam.getTestQuestions().removeIf(tq -> tq.getQuestion().getId().equals(questionId));
        if (!removed) {
            throw new IllegalStateException("Question " + questionId + " not found in test " + examId);
        }
        testRepository.save(exam);
    }


    @Override
    public List<Question> getQuestionsByTestId(Long testId) {
        return testQuestionRepository.findByTestId(testId)
                .stream()
                .map(TestQuestion::getQuestion)
                .collect(Collectors.toList());
    }

    @Override
    public List<Question> getQuestionsByTeacherIdAndCourseId(Long teacherId, Long courseId) {
        return questionRepository.findByTeacherIdAndCourseId(teacherId, courseId);
    }

    @Override
    public List<Question> getQuestionsByTeacherId(Long teacherId) {
        return questionRepository.findByTeacherId(teacherId);
    }

    @Override
    public Question getQuestionById(Long questionId) {
        return questionRepository.findById(questionId)
                .orElseThrow(() -> new BadCredentialsException("Question not found"));
    }

    @Override
    public TestQuestion findTestQuestionById(Long questionId) {
        return testQuestionRepository.findById(questionId).get();
    }

    private void linkQuestionToTest(Question question, Test test, Double defaultScore) {
        if (question == null || test == null) {
            throw new BadCredentialsException("Question and Test must not be null");
        }

        TestQuestion testQuestion = new TestQuestion();
        testQuestion.setQuestion(question);
        testQuestion.setTest(test);
        testQuestion.setScore(defaultScore); // Set the score explicitly
        test.getTestQuestions().add(testQuestion);
        testRepository.save(test);
    }
}



