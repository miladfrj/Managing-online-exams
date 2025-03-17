package ir.maktabsharif.finalprojectphase12.service;

import ir.maktabsharif.finalprojectphase12.dto.test.TestDTO;
import ir.maktabsharif.finalprojectphase12.entity.Test;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface TestService {

    void createTest(TestDTO testDTO);

    void updateTest(Long testId, TestDTO testDTO);

    void deleteTest(Long testId);

    List<TestDTO> getTestsByCourseId(Long courseId);

    List<TestDTO> getTestsByTeacherId(Long teacherId);

    TestDTO getTestsById(Long testId);

    List<Test> getTestByCourseIdAndTeacherId(Long courseId, Long teacherId);

    void updateBankQuestion(Long examId, Test test);

    Test getTestById(Long testId);

    List<TestDTO> getAvailableTestsForStudent(Long courseId, Long studentId);
}
