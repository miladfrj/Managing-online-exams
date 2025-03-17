package ir.maktabsharif.finalprojectphase12.service.impl;

import ir.maktabsharif.finalprojectphase12.dto.course.CourseDTO;
import ir.maktabsharif.finalprojectphase12.entity.Course;
import ir.maktabsharif.finalprojectphase12.entity.User;
import ir.maktabsharif.finalprojectphase12.exception.BadCredentialsException;
import ir.maktabsharif.finalprojectphase12.repository.CourseRepository;
import ir.maktabsharif.finalprojectphase12.repository.UserRepository;
import ir.maktabsharif.finalprojectphase12.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    @Override
    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void addCourse(CourseDTO courseDTO) {
        User teacher = userRepository.findById(courseDTO.getTeacherId())
                .orElseThrow(() -> new BadCredentialsException("Teacher not found"));

        Course course = Course.builder()
//                .courseCode(courseDTO.getCourseCode())
                .title(courseDTO.getTitle())
                .startDate((courseDTO.getStartDate()))
                .endDate((courseDTO.getEndDate()))
                .teacher(teacher) // Ensure the teacher is set
                .build();

        Course savedCourse = courseRepository.save(course);
        mapToDTO(savedCourse);
    }

    @Override
    public void updateCourse(CourseDTO courseDTO) {
        Course course = courseRepository.findById(courseDTO.getId())
                .orElseThrow(() -> new BadCredentialsException("Course not found"));

        User teacher = userRepository.findById(courseDTO.getTeacherId())
                .orElseThrow(() -> new BadCredentialsException("Teacher not found"));

        course.setTitle(courseDTO.getTitle());
        course.setStartDate((courseDTO.getStartDate()));
        course.setEndDate((courseDTO.getEndDate()));
        course.setTeacher(teacher);

        courseRepository.save(course);
    }

    @Override
    public void deleteCourse(Long courseId) {
       if (!courseRepository.existsById(courseId)) {
           throw new BadCredentialsException("Course not found");
       }
           courseRepository.deleteById(courseId);
       }


    @Override
    public void addProfessorToCourse(Long courseId, Long professorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new BadCredentialsException("Course not found"));
        User professor = userRepository.findById(professorId)
                .orElseThrow(() -> new BadCredentialsException("Professor not found"));
        course.setTeacher(professor);
        courseRepository.save(course);
    }

    @Override
    public void addStudentToCourse(Long courseId, Long studentId) {
        /*Optional<Course> optionalCourse = courseRepository.findById(courseId);
        Optional<User> optionalStudent = userRepository.findById(studentId);
        if (optionalCourse.isPresent() && optionalStudent.isPresent()) {
            Course course = optionalCourse.get();
            User student = optionalStudent.get();
            course.getStudents().add(student);
            Course updatedCourse = courseRepository.save(course);
            mapToDTO(updatedCourse);
        } else {
            throw new BadCredentialsException("Course or Student not found");
        }*/
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new BadCredentialsException("Course not found"));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new BadCredentialsException("Student not found"));
        course.getStudents().add(student);
        courseRepository.save(course);
    }

    @Override
    public List<User> getProfessorsAndStudentsInCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new BadCredentialsException("Course not found"));
        List<User> users = new ArrayList<>(course.getStudents());
        users.add(course.getTeacher());
        return users;
    }

    @Override
    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new BadCredentialsException("Course not found"));
        return mapToDTO(course);
    }

    @Override
    public void removeProfessorFromCourse(Long courseId, Long professorId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new BadCredentialsException("Course not found"));
        course.setTeacher(null);
        courseRepository.save(course);
    }

    @Override
    public void removeStudentFromCourse(Long courseId, Long studentId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new BadCredentialsException("Course not found"));
        User student = userRepository.findById(studentId)
                .orElseThrow(() -> new BadCredentialsException("Student not found"));
        course.getStudents().remove(student);
        courseRepository.save(course);

    }

    @Override
    public List<CourseDTO> getCoursesByInstructorId(Long instructorId) {
            return courseRepository.findByTeacherId(instructorId).stream()
                    .map(this::mapToDTO)
                    .collect(Collectors.toList());
        }

    @Override
    public List<CourseDTO> getStudentCourses(Long studentId) {
        List<Course> courses = courseRepository.findByStudentsId(studentId);
        return courses.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }


    private CourseDTO mapToDTO(Course course) {
        return CourseDTO.builder()
                .id(course.getId())
                .courseCode(course.getCourseCode())
                .title(course.getTitle())
                .startDate(course.getStartDate())
                .endDate(course.getEndDate())
                .teacherId(course.getTeacher().getId())
                .build();
    }
}


