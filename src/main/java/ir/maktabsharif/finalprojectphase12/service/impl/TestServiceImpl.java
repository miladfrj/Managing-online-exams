package ir.maktabsharif.finalprojectphase12.service.impl;

import ir.maktabsharif.finalprojectphase12.dto.test.TestDTO;
import ir.maktabsharif.finalprojectphase12.entity.Test;
import ir.maktabsharif.finalprojectphase12.exception.BadCredentialsException;
import ir.maktabsharif.finalprojectphase12.repository.CourseRepository;
import ir.maktabsharif.finalprojectphase12.repository.TestRepository;
import ir.maktabsharif.finalprojectphase12.repository.UserRepository;
import ir.maktabsharif.finalprojectphase12.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {
    private final TestRepository testRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Override
    public void createTest(TestDTO testDTO) {
        if (testDTO.getCourseId() == null) {
            throw new BadCredentialsException("Course ID cannot be null");
        }
        if (testDTO.getTeacherId() == null) {
            throw new BadCredentialsException("Teacher ID cannot be null");
        }

        Test test = Test.builder()
                .title(testDTO.getTitle())
                .description(testDTO.getDescription())
                .time(testDTO.getTime())
                .course(courseRepository.findById(testDTO.getCourseId())
                        .orElseThrow(() -> new BadCredentialsException("Course not found")))
                .teacher(userRepository.findById(testDTO.getTeacherId())
                        .orElseThrow(() -> new BadCredentialsException("Teacher not found")))
                .build();

        testRepository.save(test);
    }

    @Override
    public void updateTest(Long testId, TestDTO testDTO) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new BadCredentialsException("Test not found"));

        test.setTitle(testDTO.getTitle());
        test.setDescription(testDTO.getDescription());
        test.setTime(testDTO.getTime());
        test.setCourse(courseRepository.findById(testDTO.getCourseId())
                .orElseThrow(() -> new BadCredentialsException("Course not found")));
        test.setTeacher(userRepository.findById(testDTO.getTeacherId())
                .orElseThrow(() -> new BadCredentialsException("Teacher not found")));

        testRepository.save(test);
    }

    @Override
    public void deleteTest(Long testId) {
        if (!testRepository.existsById(testId)) {
            throw new BadCredentialsException("Test not found");
        }
        testRepository.deleteById(testId);
    }

    @Override
    public List<TestDTO> getTestsByCourseId(Long courseId) {
        return testRepository.findByCourseId(courseId).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<TestDTO> getTestsByTeacherId(Long teacherId) {
        return testRepository.findByTeacherId(teacherId).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public TestDTO getTestsById(Long testId) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new BadCredentialsException("Test not found"));
        return mapToDTO(test);
    }

    @Override
    public List<Test> getTestByCourseIdAndTeacherId(Long courseId, Long teacherId) {
        return testRepository.findByCourseIdAndTeacherId(courseId, teacherId);
    }

    @Override
    public void updateBankQuestion(Long examId, Test test) {
        testRepository.save(test);
    }

    @Override
    public Test getTestById(Long testId) {
        return testRepository.findById(testId).get();
    }

    @Override
    public List<TestDTO> getAvailableTestsForStudent(Long courseId, Long studentId) {
        List<Test> tests = testRepository.findAvailableTestsForStudent(courseId,studentId);
        return tests.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private TestDTO mapToDTO(Test test) {
        return TestDTO.builder()
               .id(test.getId())
                .title(test.getTitle())
                .description(test.getDescription())
                .time(test.getTime())
                .courseId(test.getCourse().getId())
                .teacherId(test.getTeacher().getId())
                .testQuestions(test.getTestQuestions())
                .build();
    }
}