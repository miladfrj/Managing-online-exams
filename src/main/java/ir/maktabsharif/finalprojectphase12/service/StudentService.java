package ir.maktabsharif.finalprojectphase12.service;

import ir.maktabsharif.finalprojectphase12.dto.AnswerReviewDTO;
import ir.maktabsharif.finalprojectphase12.dto.course.CourseDTO;
import ir.maktabsharif.finalprojectphase12.dto.test.TestDTO;
import ir.maktabsharif.finalprojectphase12.entity.StudentTest;
import ir.maktabsharif.finalprojectphase12.dto.StudentTestOverview;
import jakarta.transaction.Transactional;

import java.util.List;

public interface StudentService {

    List<CourseDTO> getRegisteredCourses(Long studentId);

    List<TestDTO> getTestsForCourse(Long courseId, Long studentId);


    StudentTest startOrResumeTest(Long testId, Long studentId);

    void saveAnswer(Long studentTestId, Long questionId, String response);

    void submitTestOnTimeout(Long studentTestId);

    StudentTest getStudentTest(Long studentTestId);

    void submitTest(Long studentTestId);

    void updateAnswerScore(Long studentAnswerId, Double score, String feedback);

    void updateRemainingTime(Long studentTestId, Integer remainingTime);

    StudentTest getStudentTestByAnswerId(Long studentAnswerId);

    @Transactional
    List<AnswerReviewDTO> getAnswerReviewsForStudentTest(Long studentTestId);

    List<StudentTestOverview> getStudentTestOverviewByTestId(Long testId);
}
