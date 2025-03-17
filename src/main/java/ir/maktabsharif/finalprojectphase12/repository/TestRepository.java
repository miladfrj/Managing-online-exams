package ir.maktabsharif.finalprojectphase12.repository;

import ir.maktabsharif.finalprojectphase12.entity.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    List<Test> findByCourseId(Long courseId);
    List<Test> findByTeacherId(Long teacherId);
    List<Test> findByCourseIdAndTeacherId(Long courseId, Long teacherId);

    @Query("SELECT t FROM Test t WHERE t.course.id = :courseId " +
            "AND NOT EXISTS (SELECT ts FROM StudentTest ts WHERE ts.student.id = :studentId AND ts.test.id = t.id AND ts.completed = true )")
    List<Test> findAvailableTestsForStudent(@Param("courseId") Long courseId, @Param("studentId") Long studentId);
}
