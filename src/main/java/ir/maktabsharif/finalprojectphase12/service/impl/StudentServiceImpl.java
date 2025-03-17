/*
package ir.maktabsharif.finalprojectphase12.service.impl;

import ir.maktabsharif.finalprojectphase12.dto.course.CourseDTO;
import ir.maktabsharif.finalprojectphase12.dto.test.TestDTO;
import ir.maktabsharif.finalprojectphase12.entity.*;
import ir.maktabsharif.finalprojectphase12.entity.question.MultipleChoiceQuestion;
import ir.maktabsharif.finalprojectphase12.entity.question.Question;
import ir.maktabsharif.finalprojectphase12.entity.question.TestQuestion;
import ir.maktabsharif.finalprojectphase12.exception.BadCredentialsException;
import ir.maktabsharif.finalprojectphase12.repository.*;
import ir.maktabsharif.finalprojectphase12.repository.question.QuestionRepository;
import ir.maktabsharif.finalprojectphase12.service.StudentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final CourseRepository courseRepository;
    private final TestRepository testRepository;
    private final StudentTestRepository studentTestRepository;
    private final StudentAnswerRepository studentAnswerRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    private CourseDTO mapToCourseDTO(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(course.getId());
        courseDTO.setCourseCode(course.getCourseCode());
        courseDTO.setTitle(course.getTitle());
        courseDTO.setStartDate(course.getStartDate() != null ? LocalDate.parse(course.getStartDate().toString()) : null);
        courseDTO.setEndDate(course.getEndDate() != null ? LocalDate.parse(course.getEndDate().toString()) : null);
        courseDTO.setTeacherId(course.getTeacher() != null ? course.getTeacher().getId() : null);
        courseDTO.setStudentIds(course.getStudents() != null
                ? course.getStudents().stream().map(User::getId).collect(Collectors.toList())
                : null);
        return courseDTO;
    }

    // Manual mapping method: Test to TestDTO
    private TestDTO mapToTestDTO(Test test) {
        TestDTO testDTO = new TestDTO();
        testDTO.setId(test.getId());
        testDTO.setTitle(test.getTitle());
        testDTO.setDescription(test.getDescription());
        testDTO.setTime(test.getTime());
        testDTO.setCourseId(test.getCourse() != null ? test.getCourse().getId() : null);
        testDTO.setTeacherId(test.getTeacher() != null ? test.getTeacher().getId() : null);
        testDTO.setTestQuestions(test.getTestQuestions());
        return testDTO;
    }

    @Override
    @Transactional
    public List<CourseDTO> getRegisteredCourses(Long studentId) {
        List<Course> courses = courseRepository.findByStudentsId(studentId);
        return courses.stream().map(this::mapToCourseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<TestDTO> getTestsForCourse(Long courseId, Long studentId) {
        List<Test> availableTests = testRepository.findAvailableTestsForStudent(courseId, studentId);
        return availableTests.stream().map(this::mapToTestDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<TestDTO> getAllTestsForCourse(Long courseId) {
        List<Test> tests = testRepository.findByCourseId(courseId);
        return tests.stream().map(this::mapToTestDTO).collect(Collectors.toList());
    }

    @Transactional
    @Override
    public StudentTest startOrResumeTest(Long testId, Long studentId) {
        Optional<StudentTest> existingTest = studentTestRepository.findByStudentIdAndTestId(testId, studentId);
        if (existingTest.isPresent()) {
            throw new IllegalStateException("You have already taken or started this test.");
        }
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new BadCredentialsException("Test not found with ID: " + testId));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new BadCredentialsException("Student not found with ID: " + studentId));
        StudentTest studentTest = new StudentTest();
        studentTest.setStudent(student);
        studentTest.setTest(test);
        studentTest.setRemainingTime(test.getTime() * 60);
        studentTest.setCompleted(false);
        studentTest.setTotalScore(0.0);
        return studentTestRepository.save(studentTest);
    }

    @Override
    @Transactional
    public void saveAnswer(Long studentTestId, Long questionId, String response) {
        StudentTest studentTest = studentTestRepository.findById(studentTestId)
                .orElseThrow(() -> new BadCredentialsException("StudentTest not found with ID: " + studentTestId));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new BadCredentialsException("Question not found with ID: " + questionId));
        TestQuestion testQuestion = studentTest.getTest().getTestQuestions().stream()
                .filter(tq -> tq.getQuestion().getId().equals(questionId))
                .findFirst()
                .orElseThrow(() -> new BadCredentialsException("TestQuestion not found with question ID: " + questionId));

        Optional<StudentAnswer> existingAnswer = studentTest.getAnswers().stream()
                .filter(answer -> answer.getQuestion().getId().equals(questionId))
                .findFirst();

        StudentAnswer studentAnswer;
        //Updates the existing answer if found.
        if (existingAnswer.isPresent()) {
            studentAnswer = existingAnswer.get();
            studentAnswer.setAnswer(response);
        } else {
            //Creates a new answer if none exists.
            studentAnswer = new StudentAnswer();
            studentAnswer.setStudentTest(studentTest);
            studentAnswer.setQuestion(question);
            studentAnswer.setAnswer(response);
            studentTest.getAnswers().add(studentAnswer);
        }
        studentAnswer.setDescriptiveQuestion("DESCRIPTION".equals(testQuestion.getQuestion().getQuestionType()));
        studentAnswerRepository.save(studentAnswer);
        studentTestRepository.save(studentTest);
    }

    @Override
    public StudentTest getStudentTest(Long studentTestId) {
        return studentTestRepository.findById(studentTestId)
                .orElseThrow(() -> new BadCredentialsException("StudentTest not found with ID: " + studentTestId));
    }

    @Override
    @Transactional
    public void submitTest(Long studentTestId) {
        StudentTest studentTest = studentTestRepository.findById(studentTestId)
                .orElseThrow(() -> new BadCredentialsException("StudentTest not found with ID: " + studentTestId));

        if (studentTest.isCompleted()) {
            throw new BadCredentialsException("Test has already been submitted.");
        }

        List<TestQuestion> testQuestions = studentTest.getTest().getTestQuestions();
        for (TestQuestion testQuestion : testQuestions) {
            boolean hasAnswer = studentTest.getAnswers().stream()
                    .anyMatch(answer -> answer.getQuestion().getId().equals(testQuestion.getQuestion().getId()));
            if (!hasAnswer) {
                StudentAnswer studentAnswer = new StudentAnswer();
                studentAnswer.setStudentTest(studentTest);
                studentAnswer.setQuestion(testQuestion.getQuestion());
                studentAnswer.setAnswer(null);
                studentAnswer.setScore(0.0);
                studentAnswer.setDescriptiveQuestion("DESCRIPTION".equals(testQuestion.getQuestion().getQuestionType()));
                studentTest.getAnswers().add(studentAnswer);
                studentAnswerRepository.save(studentAnswer);
            }
        }

        double totalScore = 0.0;
        for (StudentAnswer answer : studentTest.getAnswers()) {
            TestQuestion testQuestion = testQuestions.stream()
                    .filter(tq -> tq.getQuestion().getId().equals(answer.getQuestion().getId()))
                    .findFirst()
                    .orElse(null);
            if (testQuestion != null) {
                String questionType = testQuestion.getQuestion().getQuestionType();
                System.out.println("Processing answer for question ID " + answer.getQuestion().getId() + ", type: " + questionType + ", answer: " + answer.getAnswer());
                if ("MULTIPLE_CHOICE".equalsIgnoreCase(questionType)) {
                    try {
                        MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) testQuestion.getQuestion();
                        String correctAnswer = mcq.getCorrectAnswer();
                        if (answer.getAnswer() != null && answer.getAnswer().trim().equalsIgnoreCase(correctAnswer != null ? correctAnswer.trim() : "")) {
                            answer.setScore(testQuestion.getScore());
                            System.out.println("MCQ correct for question ID " + answer.getQuestion().getId() + ", score: " + testQuestion.getScore());
                        } else {
                            answer.setScore(0.0);
                            System.out.println("MCQ incorrect for question ID " + answer.getQuestion().getId() + ", score: 0.0, correct: " + correctAnswer + ", given: " + answer.getAnswer());
                        }
                    } catch (ClassCastException e) {
                        answer.setScore(0.0);
                        System.out.println("ClassCastException for MCQ question ID " + answer.getQuestion().getId() + ": " + e.getMessage());
                    }
                } else {
                    answer.setScore(0.0);
                    System.out.println("Descriptive question ID " + answer.getQuestion().getId() + ", score set to 0.0");
                }
                totalScore += answer.getScore();
                studentAnswerRepository.save(answer);
            }
        }

        studentTest.setTotalScore(totalScore);
        studentTest.setCompleted(true);
        studentTestRepository.save(studentTest);
    }

    @Override
    @Transactional
    public void submitTestOnTimeout(Long studentTestId) {
        StudentTest studentTest = studentTestRepository.findById(studentTestId)
                .orElseThrow(() -> new IllegalArgumentException("StudentTest not found with ID: " + studentTestId));

        if (studentTest.isCompleted()) {
            return;
        }

        // Record unanswered questions
        List<TestQuestion> testQuestions = studentTest.getTest().getTestQuestions();
        for (TestQuestion testQuestion : testQuestions) {
            boolean hasAnswer = studentTest.getAnswers().stream()
                    .anyMatch(answer -> answer.getQuestion().getId().equals(testQuestion.getQuestion().getId()));
            if (!hasAnswer) {
                StudentAnswer studentAnswer = new StudentAnswer();
                studentAnswer.setStudentTest(studentTest);
                studentAnswer.setQuestion(testQuestion.getQuestion());
                studentAnswer.setAnswer(null);
                studentAnswer.setScore(0.0);
                studentAnswer.setDescriptiveQuestion("DESCRIPTION".equals(testQuestion.getQuestion().getQuestionType()));
                studentTest.getAnswers().add(studentAnswer);
                studentAnswerRepository.save(studentAnswer);
            }
        }

        // Score answered questions
        double totalScore = 0.0;
        for (StudentAnswer answer : studentTest.getAnswers()) {
            TestQuestion testQuestion = testQuestions.stream()
                    .filter(tq -> tq.getQuestion().getId().equals(answer.getQuestion().getId()))
                    .findFirst()
                    .orElse(null);
            if (testQuestion != null) {
                String questionType = testQuestion.getQuestion().getQuestionType();
                if ("MULTIPLE_CHOICE".equals(questionType)) {
                    MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) testQuestion.getQuestion();
                    String correctAnswer = mcq.getCorrectAnswer();
                    if (answer.getAnswer() != null && answer.getAnswer().equals(correctAnswer)) {
                        answer.setScore(testQuestion.getScore());
                    } else {
                        answer.setScore(0.0);
                    }
                } else {
                    answer.setScore(0.0);
                }
                totalScore += answer.getScore();
                studentAnswerRepository.save(answer);
            }
        }

        studentTest.setTotalScore(totalScore);
        studentTest.setCompleted(true);
        studentTestRepository.save(studentTest);
    }


    @Override
    @Transactional
    public void updateRemainingTime(Long studentTestId, Integer remainingTime) {
        StudentTest studentTest = studentTestRepository.findById(studentTestId)
                .orElseThrow(() -> new IllegalStateException("Test not found"));
        studentTest.setRemainingTime(remainingTime);
        studentTestRepository.save(studentTest);
    }

    @Transactional
    @Override
    public void updateAnswerScore(Long studentAnswerId, Double score, String feedback) {
        StudentAnswer studentAnswer = studentAnswerRepository.findById(studentAnswerId)
                .orElseThrow(() -> new BadCredentialsException("StudentAnswer not found with ID: " + studentAnswerId));
        TestQuestion testQuestion = studentAnswer.getStudentTest().getTest().getTestQuestions().stream()
                .filter(tq -> tq.getQuestion().getId().equals(studentAnswer.getQuestion().getId()))
                .findFirst()
                .orElseThrow(() -> new BadCredentialsException("TestQuestion not found"));
        Double maxScore = testQuestion.getScore() != null ? testQuestion.getScore() : 10.0;
        if (score > maxScore) {
            throw new IllegalArgumentException("Score cannot exceed the maximum score of " + maxScore);
        }
        studentAnswer.setScore(score);
        studentAnswer.setFeedback(feedback);
        studentAnswerRepository.save(studentAnswer);

        StudentTest studentTest = studentAnswer.getStudentTest();
        double totalScore = studentTest.getAnswers().stream()
                .mapToDouble(StudentAnswer::getScore)
                .sum();
        studentTest.setTotalScore(totalScore);
        studentTestRepository.save(studentTest);
    }

    @Override
    public List<StudentTestOverview> getStudentTestOverviewByTestId(Long testId) {
        List<StudentTest> studentTests = studentTestRepository.findAllByTestId(testId);
        List<StudentTestOverview> overviews = new ArrayList<>();
        for (StudentTest st : studentTests) {
            StudentTestOverview overview = new StudentTestOverview();
            overview.setStudentTestId(st.getId());
            overview.setStudentName(st.getStudent().getUsername());
            overview.setCompleted(st.isCompleted());
            overview.setTotalScore(st.getTotalScore());
            overviews.add(overview);
        }
        return overviews;
    }
}*/


package ir.maktabsharif.finalprojectphase12.service.impl;

import ir.maktabsharif.finalprojectphase12.dto.AnswerReviewDTO;
import ir.maktabsharif.finalprojectphase12.dto.StudentTestOverview;
import ir.maktabsharif.finalprojectphase12.dto.course.CourseDTO;
import ir.maktabsharif.finalprojectphase12.dto.test.TestDTO;
import ir.maktabsharif.finalprojectphase12.entity.*;
import ir.maktabsharif.finalprojectphase12.entity.question.MultipleChoiceQuestion;
import ir.maktabsharif.finalprojectphase12.entity.question.Question;
import ir.maktabsharif.finalprojectphase12.entity.question.TestQuestion;
import ir.maktabsharif.finalprojectphase12.exception.BadCredentialsException;
import ir.maktabsharif.finalprojectphase12.repository.*;
import ir.maktabsharif.finalprojectphase12.repository.question.QuestionRepository;
import ir.maktabsharif.finalprojectphase12.service.StudentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final CourseRepository courseRepository;
    private final TestRepository testRepository;
    private final StudentTestRepository studentTestRepository;
    private final StudentAnswerRepository studentAnswerRepository;
    private final UserRepository userRepository;
    private final QuestionRepository questionRepository;

    private CourseDTO mapToCourseDTO(Course course) {
        CourseDTO courseDTO = new CourseDTO();
        courseDTO.setId(course.getId());
        courseDTO.setCourseCode(course.getCourseCode());
        courseDTO.setTitle(course.getTitle());
        courseDTO.setStartDate(course.getStartDate() != null ? LocalDate.parse(course.getStartDate().toString()) : null);
        courseDTO.setEndDate(course.getEndDate() != null ? LocalDate.parse(course.getEndDate().toString()) : null);
        courseDTO.setTeacherId(course.getTeacher() != null ? course.getTeacher().getId() : null);
        courseDTO.setStudentIds(course.getStudents() != null
                ? course.getStudents().stream().map(User::getId).collect(Collectors.toList())
                : null);
        return courseDTO;
    }

    private TestDTO mapToTestDTO(Test test) {
        TestDTO testDTO = new TestDTO();
        testDTO.setId(test.getId());
        testDTO.setTitle(test.getTitle());
        testDTO.setDescription(test.getDescription());
        testDTO.setTime(test.getTime());
        testDTO.setCourseId(test.getCourse() != null ? test.getCourse().getId() : null);
        testDTO.setTeacherId(test.getTeacher() != null ? test.getTeacher().getId() : null);
        testDTO.setTestQuestions(test.getTestQuestions());
        return testDTO;
    }

    @Override
    @Transactional
    public List<CourseDTO> getRegisteredCourses(Long studentId) {
        List<Course> courses = courseRepository.findByStudentsId(studentId);
        return courses.stream().map(this::mapToCourseDTO).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<TestDTO> getTestsForCourse(Long courseId, Long studentId) {
        List<Test> availableTests = testRepository.findAvailableTestsForStudent(courseId, studentId);
        return availableTests.stream().map(this::mapToTestDTO).collect(Collectors.toList());
    }



    @Transactional
    @Override
    public StudentTest startOrResumeTest(Long testId, Long studentId) {

        Optional<StudentTest> existingTest = studentTestRepository.findByStudentIdAndTestId(testId, studentId);
        if (existingTest.isPresent()) {
            throw new BadCredentialsException("You have already taken or started this test.");
        }

        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new BadCredentialsException("Test not found"));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new BadCredentialsException("Student not found"));
        StudentTest studentTest = new StudentTest();
        studentTest.setStudent(student);
        studentTest.setTest(test);
        studentTest.setRemainingTime(test.getTime() * 60);
        studentTest.setCompleted(false);
        studentTest.setTotalScore(0.0);
        return studentTestRepository.save(studentTest);
    }

    @Override
    @Transactional
    public void submitTest(Long studentTestId) {
        StudentTest studentTest = studentTestRepository.findById(studentTestId)
                .orElseThrow(() -> new BadCredentialsException("StudentTest not found"));

        if (studentTest.isCompleted()) {
            throw new BadCredentialsException("Test has already been submitted.");
        }

        List<TestQuestion> testQuestions = studentTest.getTest().getTestQuestions();
        // Create default answers for unanswered questions
        for (TestQuestion testQuestion : testQuestions) {
            boolean hasAnswer = studentTest.getAnswers().stream()
                    .anyMatch(answer -> answer.getQuestion().getId().equals(testQuestion.getQuestion().getId()));
            if (!hasAnswer) {
                StudentAnswer studentAnswer = new StudentAnswer();
                studentAnswer.setStudentTest(studentTest);
                studentAnswer.setQuestion(testQuestion.getQuestion());
                studentAnswer.setAnswer(null);
                studentAnswer.setDescriptiveQuestion("DESCRIPTION".equals(testQuestion.getQuestion().getQuestionType()));
                studentTest.getAnswers().add(studentAnswer);
                studentAnswerRepository.save(studentAnswer);
            }
        }

        double totalScore = 0.0;
        for (StudentAnswer answer : studentTest.getAnswers()) {
            TestQuestion testQuestion = testQuestions.stream()
                    .filter(tq -> tq.getQuestion().getId().equals(answer.getQuestion().getId()))
                    .findFirst()
                    .orElse(null);
            if (testQuestion != null) {
                String questionType = testQuestion.getQuestion().getQuestionType();
                if ("MULTIPLE_CHOICE".equals(questionType)) {
                    MultipleChoiceQuestion mcq = (MultipleChoiceQuestion) testQuestion.getQuestion();
                    String correctAnswer = mcq.getCorrectAnswer();
                    List<String> options = mcq.getOptions();
                    String correct = options.get(Integer.parseInt(correctAnswer));
                    if (answer.getAnswer() != null && answer.getAnswer().equals(correct)) {
                        answer.setScore(testQuestion.getScore());
                    } else {
                        answer.setScore(0.0);
                    }
                } else {
                    answer.setScore(0.0);
                }
                if (answer.getScore() != null) {
                    totalScore += answer.getScore();
                }
                studentAnswerRepository.save(answer);
            }
        }

        studentTest.setTotalScore(totalScore);
        studentTest.setCompleted(true);
        studentTestRepository.save(studentTest);
    }


    @Override
    @Transactional
    public void saveAnswer(Long studentTestId, Long questionId, String response) {
        StudentTest studentTest = studentTestRepository.findById(studentTestId)
                .orElseThrow(() -> new BadCredentialsException("StudentTest not found"));
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new BadCredentialsException("Question not found"));

        Optional<StudentAnswer> existingAnswer = studentTest.getAnswers().stream()
                .filter(answer -> answer.getQuestion().getId().equals(questionId))
                .findFirst();

        StudentAnswer studentAnswer;
        if (existingAnswer.isPresent()) {
            studentAnswer = existingAnswer.get();
            studentAnswer.setAnswer(response);
        } else {
            studentAnswer = new StudentAnswer();
            studentAnswer.setStudentTest(studentTest);
            studentAnswer.setQuestion(question);
            studentAnswer.setAnswer(response);
            studentTest.getAnswers().add(studentAnswer);
        }
        studentAnswer.setDescriptiveQuestion("DESCRIPTION".equals(question.getQuestionType()));
        studentAnswerRepository.save(studentAnswer);
        studentTestRepository.save(studentTest);
    }

    @Override
    public StudentTest getStudentTest(Long studentTestId) {
        return studentTestRepository.findById(studentTestId)
                .orElseThrow(() -> new BadCredentialsException("StudentTest not found"));
    }


    @Override
    @Transactional
    public void submitTestOnTimeout(Long studentTestId) {
        StudentTest studentTest = studentTestRepository.findById(studentTestId)
                .orElseThrow(() -> new BadCredentialsException("StudentTest not found"));

        if (studentTest.isCompleted()) {
            return;
        }

        submitTest(studentTestId);

    }

    @Override
    @Transactional
    public void updateRemainingTime(Long studentTestId, Integer remainingTime) {
        StudentTest studentTest = studentTestRepository.findById(studentTestId)
                .orElseThrow(() -> new BadCredentialsException("Test not found"));
        studentTest.setRemainingTime(remainingTime);
        studentTestRepository.save(studentTest);
    }

    @Override
    @Transactional
    public void updateAnswerScore(Long studentAnswerId, Double score, String feedback) {
        StudentAnswer studentAnswer = studentAnswerRepository.findById(studentAnswerId)
                .orElseThrow(() -> new BadCredentialsException("StudentAnswer not found"));

        TestQuestion testQuestion = studentAnswer.getStudentTest().getTest().getTestQuestions().stream()
                .filter(tq -> tq.getQuestion().getId().equals(studentAnswer.getQuestion().getId()))
                .findFirst()
                .orElseThrow(() -> new BadCredentialsException("TestQuestion not found"));

        if (score < 0 || score > testQuestion.getScore()) {
            throw new BadCredentialsException("Score must be between 0 and " + testQuestion.getScore());
        }

        studentAnswer.setScore(score);
        studentAnswer.setFeedback(feedback);
        studentAnswerRepository.save(studentAnswer);

        StudentTest studentTest = studentAnswer.getStudentTest();
        double totalScore = studentTest.getAnswers().stream()
                .mapToDouble(answer -> answer.getScore() != null ? answer.getScore() : 0.0)
                .sum();
        studentTest.setTotalScore(totalScore);
        studentTestRepository.save(studentTest);
    }


    @Override
    public StudentTest getStudentTestByAnswerId(Long studentAnswerId) {
        return studentAnswerRepository.findById(studentAnswerId)
                .orElseThrow(() -> new BadCredentialsException("StudentAnswer not found"))
                .getStudentTest();
    }

    @Transactional
    @Override
    public List<AnswerReviewDTO> getAnswerReviewsForStudentTest(Long studentTestId) {
        StudentTest studentTest = studentTestRepository.findById(studentTestId)
                .orElseThrow(() -> new BadCredentialsException("StudentTest not found"));

        List<StudentAnswer> answers = studentTest.getAnswers();
        List<AnswerReviewDTO> answerReviews = new ArrayList<>();

        for (int i = 0; i < answers.size(); i++) {
            StudentAnswer answer = answers.get(i);
            AnswerReviewDTO review = new AnswerReviewDTO();
            review.setAnswerId(answer.getId());
            review.setQuestionIndex(i + 1);

            TestQuestion testQuestion = studentTest.getTest().getTestQuestions().get(i);
            review.setQuestionText(testQuestion.getQuestion().getQuestionText());
            review.setStudentAnswer(answer.getAnswer() != null ? answer.getAnswer() : "Not answered");
            //set score field .
            review.setScore(answer.getScore() != null ? answer.getScore() : 0.0);
            review.setMaxScore(testQuestion.getScore());
            review.setFeedback(answer.getFeedback() != null ? answer.getFeedback() : "");
            review.setDescriptiveQuestion("DESCRIPTION".equals(testQuestion.getQuestion().getQuestionType()));

            answerReviews.add(review);
        }

        return answerReviews;
    }

    @Override
    public List<StudentTestOverview> getStudentTestOverviewByTestId(Long testId) {
        List<StudentTest> studentTests = studentTestRepository.findAllByTestId(testId);
        List<StudentTestOverview> overviews = new ArrayList<>();
        for (StudentTest st : studentTests) {
            StudentTestOverview overview = new StudentTestOverview();
            overview.setStudentTestId(st.getId());
            overview.setStudentName(st.getStudent().getUsername());
            overview.setCompleted(st.isCompleted());
            overview.setTotalScore(st.getTotalScore());
            overviews.add(overview);
        }
        return overviews;
    }
}


