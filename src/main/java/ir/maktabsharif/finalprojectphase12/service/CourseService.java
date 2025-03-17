package ir.maktabsharif.finalprojectphase12.service;

import ir.maktabsharif.finalprojectphase12.dto.course.CourseDTO;
import ir.maktabsharif.finalprojectphase12.entity.User;

import java.util.List;

public interface CourseService {
    List<CourseDTO> getAllCourses();
    void addCourse(CourseDTO courseDTO);
    void updateCourse(CourseDTO courseDTO);
    void deleteCourse(Long courseId);
    void addProfessorToCourse(Long courseId, Long professorId);
    void addStudentToCourse(Long courseId, Long studentId);
    List<User> getProfessorsAndStudentsInCourse(Long courseId);
    CourseDTO getCourseById(Long id);
    void removeProfessorFromCourse(Long courseId, Long professorId);
    void removeStudentFromCourse(Long courseId, Long studentId);
    List<CourseDTO> getCoursesByInstructorId(Long instructorId);

    List<CourseDTO> getStudentCourses(Long studentId);
}
